package com.example.pda_project.ui.workers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.ui.workers.outbound.ToteScanActivity
import com.example.pda_project.R

class ModeSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_select)

        val pdaExportButton = findViewById<Button>(R.id.pdaExportButton)
        val pdaImportButton = findViewById<Button>(R.id.pdaImportButton)
        val buttonErrorReport = findViewById<Button>(R.id.buttonReportError)

        // 출고 버튼
        pdaExportButton.setOnClickListener {
            val intent = Intent(this, ToteScanActivity::class.java)
            startActivity(intent)
        }

        // 입고 버튼
        pdaImportButton.setOnClickListener {
            //loadFragment(SampleFragment())
        }

        // 오류보고 버튼
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
