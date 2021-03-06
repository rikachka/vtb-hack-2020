package com.hfad.vtb_hack_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_car_to_buy.*
import kotlinx.android.synthetic.main.activity_credit_money_calculation.*

class CreditMoneyCalculationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_money_calculation)

        buttonCreditMoneyContinue.setOnClickListener(){
            val intent: Intent = Intent(this, CreditRequestFilledActivity::class.java)
            startActivity(intent)
        }

        setSupportActionBar(creditMoneyToolbar)

        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }
}