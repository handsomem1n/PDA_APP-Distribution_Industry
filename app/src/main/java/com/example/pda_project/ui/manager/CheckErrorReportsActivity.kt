package com.example.pda_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pda_project.adapters.manager.ErrorReportAdapter
import com.example.pda_project.adapters.manager.ErrorReportManager
import com.example.pda_project.models.manager.ErrorReport
import com.google.firebase.firestore.FirebaseFirestore
class CheckErrorReportsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorReportAdapter: ErrorReportAdapter
    private lateinit var errorReportList: MutableList<ErrorReport>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_error_reports)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        errorReportList = mutableListOf()
        errorReportAdapter = ErrorReportAdapter(errorReportList) { errorReport ->
            val intent = Intent(this, ErrorReportDetailActivity::class.java)
            intent.putExtra("title", errorReport.title)
            intent.putExtra("content", errorReport.content)
            startActivity(intent)
        }
        recyclerView.adapter = errorReportAdapter

        ErrorReportManager.fetchErrorReports { errorReports ->
            errorReportList.addAll(errorReports)
            errorReportAdapter.notifyDataSetChanged()
        }
    }
}
