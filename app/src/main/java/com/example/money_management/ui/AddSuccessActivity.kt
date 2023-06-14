package com.example.money_management.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.money_management.R
import kotlinx.android.synthetic.main.activity_add_success.*

class AddSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_success)
        done.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}