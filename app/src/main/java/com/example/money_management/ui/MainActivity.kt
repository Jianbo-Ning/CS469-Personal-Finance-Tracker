package com.example.money_management.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.money_management.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tilExpense: EditText
    private lateinit var btnEnter: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        tilExpense = findViewById(R.id.et_expense)
        btnEnter = findViewById(R.id.btn_enter)

        my.setOnClickListener {
            startActivity(Intent(this,RecordListActivity::class.java))
        }

        btnEnter.setOnClickListener {
            with(Bundle()) {
                val intent = Intent(this@MainActivity, FinancialManagementActivity::class.java)
                putDouble("money", tilExpense.text.toString().toDouble())
                intent.putExtras(this)
                startActivity(intent)
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                true
//            }
//            R.id.weather -> {
//                startActivity(Intent(this, WeatherActivity::class.java))
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}
