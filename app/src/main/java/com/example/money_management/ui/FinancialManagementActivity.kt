package  com.example.money_management.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import com.example.money_management.R
import com.example.money_management.db.AppDatabase
import com.example.money_management.db.DatabaseBuilder
import com.example.money_management.db.Record
import kotlinx.android.synthetic.main.activity_financial_management.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class FinancialManagementActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var gridCategory: GridLayout

    private  var spend: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_financial_management)

        initViews()
    }

    private fun initViews() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        gridCategory = findViewById(R.id.grid_category)
        setupGridLayout()
    }

    private fun setupGridLayout() {
        spend = intent.getDoubleExtra("money", 0.0)

        CoroutineScope(Dispatchers.IO).launch {
            val total =
                DatabaseBuilder.getInstance(this@FinancialManagementActivity).recordDao().getTotal()
            tx_total.text = "Total:  ${total + spend}"
        }

        val categories = mutableListOf(
            "Dining",
            "Shopping",
            "Transportation",
            "Entertainment",
            "Housing",
            "Others"
        )
        for (category in categories) {
            val button = MaterialButton(this).apply {
                text = category
                setOnClickListener(this@FinancialManagementActivity)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8) // Set margins for the button
                }
            }
            gridCategory.addView(button)
        }
    }

    override fun onClick(view: View) {
        if (view is MaterialButton) {
            val selectedCategory = view.text.toString()
            Log.e("TAG", "onClick: ${selectedCategory}")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("expend note")
            val input = EditText(this)
            input.setLines(5)
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            builder.setView(input)
            var note = ""
            builder.setPositiveButton("confirm") { _, _ ->
                note = input.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    val recordDao =
                        DatabaseBuilder.getInstance(this@FinancialManagementActivity).recordDao()
                    recordDao.addRecord(Record(null, "spend", selectedCategory, spend, Date(),note))
                    startActivity(Intent(this@FinancialManagementActivity,AddSuccessActivity::class.java))
                }
            }
            builder.setNegativeButton("cancel") { dialog, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    val recordDao =
                        DatabaseBuilder.getInstance(this@FinancialManagementActivity).recordDao()
                    recordDao.addRecord(Record(null, "spend", selectedCategory, spend, Date(),""))
                    dialog.cancel()


                    startActivity(Intent(this@FinancialManagementActivity,AddSuccessActivity::class.java))

                }

            }

            val dialog = builder.create()
            dialog.show()

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
