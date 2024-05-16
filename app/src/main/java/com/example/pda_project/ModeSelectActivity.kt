package com.example.pda_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class ModeSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_select)

        val pdaExportButton: Button = findViewById(R.id.pdaExportButton)
        val pdaImportButton: Button = findViewById(R.id.pdaImportButton)
        val buttonBottomRight: Button = findViewById(R.id.buttonReportError)

        // Button to go to another Activity
        pdaExportButton.setOnClickListener {
            val intent = Intent(this, PDAExportActivity::class.java)
            startActivity(intent)
        }

        // Button to load a Fragment
        pdaImportButton.setOnClickListener {
            //loadFragment(SampleFragment())
        }

        // Bottom right button action
        val buttonErrorReport = findViewById<Button>(R.id.buttonReportError)
        buttonErrorReport.setOnClickListener {
            val fragment = ReportErrorFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down)
                .replace(R.id.mainLayout, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
