package com.example.pda_project.adapters.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pda_project.R
import com.example.pda_project.models.manager.ErrorReport

class ErrorReportAdapter(
    private val errorReportList: List<ErrorReport>,
    private val onItemClick: (ErrorReport) -> Unit
) : RecyclerView.Adapter<ErrorReportAdapter.ErrorReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_error_report, parent, false)
        return ErrorReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ErrorReportViewHolder, position: Int) {
        val errorReport = errorReportList[position]
        holder.button.text = errorReport.title
        holder.button.setOnClickListener {
            onItemClick(errorReport)
        }
    }

    override fun getItemCount() = errorReportList.size

    class ErrorReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.errorReportButton)
    }
}
