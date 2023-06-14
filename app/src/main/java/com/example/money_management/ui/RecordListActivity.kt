package com.example.money_management.ui

import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.money_management.R
import com.example.money_management.db.DatabaseBuilder
import com.example.money_management.db.Record
import com.example.money_management.db.RecordDao
import kotlinx.android.synthetic.main.activity_financial_management.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RecordListActivity : AppCompatActivity() {
    private lateinit var recordDao: RecordDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_list)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        recordDao = DatabaseBuilder.getInstance(this@RecordListActivity).recordDao()

        
        val recyclerView = findViewById<RecyclerView>(R.id.record_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            val adapter = RecordListAdapter(this@RecordListActivity, recordDao.getAllRecords())
            recyclerView.adapter = adapter
        }

    }


    fun deleteRecord(record: Record) {
        CoroutineScope(Dispatchers.IO).launch {
            recordDao.deleteRecord(record)
            val allRecords = recordDao.getAllRecords()
            runOnUiThread {

                val recyclerView = findViewById<RecyclerView>(R.id.record_list)
                val adapter = recyclerView.adapter as RecordListAdapter
                adapter.updateList(allRecords)
            }
        }
    }


    fun viewRecordDetail(record: Record) {

        val intent = Intent(this, RecordDetailActivity::class.java)
        intent.putExtra("record_id", record.id)
        startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_export -> {
                exportRecordsToPdf()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun exportRecordsToPdf() {
        CoroutineScope(Dispatchers.IO).launch {
            val records: List<Record> =
                recordDao.getAllRecords()
            runOnUiThread{

                val pdfDocument = PdfDocument()


                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1)
                    .create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas


                val startY = 50
                val rowHeight = 30
                val columnWidth = pageInfo.pageWidth / 5
                val paint = Paint()


                paint.textSize = 16f
                canvas.drawText("Type", columnWidth.toFloat(), startY.toFloat(), paint)
                canvas.drawText("Spend Type", columnWidth * 2.toFloat(), startY.toFloat(), paint)
                canvas.drawText("Money", columnWidth * 3.toFloat(), startY.toFloat(), paint)
                canvas.drawText("Date", columnWidth * 4.toFloat(), startY.toFloat(), paint)
                canvas.drawText("Note", columnWidth * 5.toFloat(), startY.toFloat(), paint)


                paint.textSize = 14f
                records.forEachIndexed { index, record ->
                    val rowY = startY + (index + 1) * rowHeight
                    canvas.drawText(record.type, columnWidth.toFloat(), rowY.toFloat(), paint)
                    canvas.drawText(record.spendType, columnWidth * 2.toFloat(), rowY.toFloat(), paint)
                    canvas.drawText(
                        record.money.toString(),
                        columnWidth * 3.toFloat(),
                        rowY.toFloat(),
                        paint
                    )
                    canvas.drawText(
                        record.date.toString(),
                        columnWidth * 4.toFloat(),
                        rowY.toFloat(),
                        paint
                    )
                    canvas.drawText(record.note, columnWidth * 5.toFloat(), rowY.toFloat(), paint)
                }


                pdfDocument.finishPage(page)


                val directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(directory, "expense_records.pdf")

                try {
                    val outputStream = FileOutputStream(file)
                    pdfDocument.writeTo(outputStream)
                    outputStream.close()
                    pdfDocument.close()
                    Toast.makeText(this@RecordListActivity, "PDF exported successfully", Toast.LENGTH_SHORT).show()


                    val uri =
                        FileProvider.getUriForFile(this@RecordListActivity, "com.example.money_management.fileprovider", file)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(uri, "application/pdf")
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    startActivity(intent)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@RecordListActivity, "Failed to export PDF", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.ss, menu)
        return true
    }




}
