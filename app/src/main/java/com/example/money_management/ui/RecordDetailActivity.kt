package com.example.money_management.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.money_management.R
import com.example.money_management.db.DatabaseBuilder
import com.example.money_management.db.RecordDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RecordDetailActivity : AppCompatActivity() {
    private lateinit var recordDao: RecordDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_detail)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        recordDao = DatabaseBuilder.getInstance(this@RecordDetailActivity).recordDao()


        val recordId = intent.getIntExtra("record_id", -1)


        CoroutineScope(Dispatchers.IO).launch {
            val record = recordDao.getRecordById(recordId)
            runOnUiThread {

                findViewById<TextView>(R.id.type).text = record.type
                findViewById<TextView>(R.id.spend_type).text = record.spendType
                findViewById<TextView>(R.id.money).text = String.format("%.2f", record.money)
                findViewById<TextView>(R.id.date).text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(record.date)
                findViewById<TextView>(R.id.note).text = record.note
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
