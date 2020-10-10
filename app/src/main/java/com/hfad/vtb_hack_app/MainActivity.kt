package com.hfad.vtb_hack_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import khttp.post
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.InputStream


//функция отправки запроса
fun getRecognizedCarTEXT(photo: String) : String {
    val url = "https://gw.hackathon.vtb.ru/vtb/hackathon/car-recognize"
    val paramsMap: Map<String, String> = mapOf("content" to photo)
    val response = post(url, params = paramsMap)
    val obj : String = response.text
    return obj
}



class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonIdentifyCar.setOnClickListener(){
            dispatchTakePictureIntent()
        }

        buttonAboutCar.setOnClickListener(){
            val intent:Intent = Intent(this, AboutCarActivity::class.java)
            startActivity(intent)
        }

//        TODO: Return "CarNotFoundActivity"
        buttonNotFound.setOnClickListener(){
            val intent:Intent = Intent(this, CarToBuyActivity::class.java)
            startActivity(intent)
        }

        buttonFromGallery.setOnClickListener(){
            dispatchTakePictureFromGalleryIntent()
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //обработка событий меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                intent = Intent(this, PropertiesActivity::class.java)
                startActivity(intent)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    //Приняли фотку и обрабатываем ее, переводя в строку и отправляя запрос на сервер
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (resultCode == RESULT_OK && data!=null) {
            when (requestCode){

                REQUEST_GALLERY -> {
                    val selectedImage: Uri = data?.data!!

                    val imageStream: InputStream? = contentResolver.openInputStream(selectedImage)
                    val imageBitmap = BitmapFactory.decodeStream(imageStream)
                    val resized= getResizedBitmap(imageBitmap, 200)//maxSize варьируется



                    val byteArrayOutputStream = ByteArrayOutputStream()
                    resized!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    val photoString = android.util.Base64.encodeToString(
                        byteArray,
                        android.util.Base64.DEFAULT
                    )

                    //ставит фотку во весь экран
                    imageView.setImageBitmap(resized)
                    textView.text = photoString
                    //аналогичный перевод в base64 как при фотографировании выкидывает outOfMemory

                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap

                    //ставим картиночку в imageView, по сути не нужно
                    imageView.setImageBitmap(imageBitmap)

                    //перевод фотки в строку
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    val photoString = android.util.Base64.encodeToString(
                        byteArray,
                        android.util.Base64.DEFAULT
                    )
                    textView.text = photoString
                }


            }





////////////////////////////////////
            /*
            val imageBitmap = data?.extras?.get("data") as Bitmap

            //ставим картиночку в imageView, по сути не нужно
            imageView.setImageBitmap(imageBitmap)

            //перевод фотки в строку
            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            val photoString = android.util.Base64.encodeToString(byteArray ,android.util.Base64.DEFAULT)
            */

//////////////////////////
            //неработающая мазафака
                // GlobalScope.launch(Dispatchers.IO) {
                 //var recognisedCarData = getRecognizedCarTEXT(photoString)
            //}


            //textView.text=photoString
        }
    }


    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    //Запускаем камеру и принимаем фотку в MainActivity
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun dispatchTakePictureFromGalleryIntent() {
        val takePictureIntent = Intent(Intent.ACTION_PICK)
        takePictureIntent.type = "image/*"
        takePictureIntent.action=Intent.ACTION_GET_CONTENT
        try {
            startActivityForResult(
                Intent.createChooser(takePictureIntent, "Pick an image"),
                REQUEST_GALLERY
            )
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }


    //я вообще не помню откуда это взялось
    private fun takeResponseByBase64(bmpString: String){

    }






}

