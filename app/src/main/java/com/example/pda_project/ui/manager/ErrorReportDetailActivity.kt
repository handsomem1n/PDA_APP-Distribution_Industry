package com.example.pda_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class ErrorReportDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_report_detail)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val contentTextView: TextView = findViewById(R.id.contentTextView)

        titleTextView.text = title
        contentTextView.text = content
    }
}
