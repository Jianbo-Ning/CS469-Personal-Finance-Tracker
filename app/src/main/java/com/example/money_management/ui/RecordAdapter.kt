package com.example.money_management.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.money_management.R
import com.example.money_management.db.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RecordListAdapter(private val activity: RecordListActivity, private var records: List<Record>) :
    RecyclerView.Adapter<RecordListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeView: TextView = view.findViewById(R.id.type)
        val spendTypeView: TextView = view.findViewById(R.id.spend_type)
        val moneyView: TextView = view.findViewById(R.id.money)
        val dateView: TextView = view.findViewById(R.id.date)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)

        init {

            view.setOnClickListener {
                activity.viewRecordDetail(records[adapterPosition])
            }
            deleteButton.setOnClickListener {
                    activity.deleteRecord(records[adapterPosition])
            }
        }
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = records[position]
            holder.spendTypeView.text = record.spendType
            holder.moneyView.text = String.format("%.2f", record.money)
            holder.dateView.text =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(record.date)
        }

        override fun getItemCount() = records.size


        @SuppressLint("NotifyDataSetChanged")
        fun updateList(newRecords: List<Record>) {
            records = newRecords
            notifyDataSetChanged()
        }
    }