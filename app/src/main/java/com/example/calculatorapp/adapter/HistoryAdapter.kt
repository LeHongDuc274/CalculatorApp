package com.example.calculatorapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculatorapp.R
import com.example.calculatorapp.data.History
import com.example.calculatorapp.databinding.ActivityMainBinding.inflate

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    var listData = mutableListOf<History>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curData = listData[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_expression).text = curData.expression
            findViewById<TextView>(R.id.tv_result).text = "=" + curData.result
        }
    }

    override fun getItemCount(): Int = listData.size

    fun setData(list: List<History>) {
        listData = list as MutableList<History>
        notifyDataSetChanged()
    }
}