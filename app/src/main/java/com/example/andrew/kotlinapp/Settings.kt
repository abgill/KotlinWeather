package com.example.andrew.kotlinapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher



class Settings : AppCompatActivity() {

    var zip: Int = 0
    var lastUpdated:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        zip = intent.getIntExtra("zip",3)
        lastUpdated = intent.getLongExtra("lastUpdated",4)

        val zipView: EditText = findViewById(R.id.zip_code) as EditText
        zipView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty() && s.toString().length == 5) {
                    zip = s.toString().toInt()
                }
            }
        });

        zipView.setText(zip.toString())


    }

    override fun onBackPressed() {
        val returnIntent:Intent = Intent()
        returnIntent.putExtra("zip",zip)
        setResult(3,returnIntent)
        finish()
    }

}
