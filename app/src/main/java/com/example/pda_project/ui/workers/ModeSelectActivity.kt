package com.example.pda_project.ui.workers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.ui.workers.outbound.OutboundActivity
import com.example.pda_project.R
import com.example.pda_project.ui.workers.inbound.InboundActivity

class ModeSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_select)

        val pdaExportButton = findViewById<Button>(R.id.pdaExportButton)
        val pdaImportButton = findViewById<Button>(R.id.pdaImportButton)
        val buttonErrorReport = findViewById<Button>(R.id.buttonReportError)

        // 출고 버튼
        pdaExportButton.setOnClickListener {
            activityCall(OutboundActivity::class.java)
        }

        // 입고 버튼
        pdaImportButton.setOnClickListener {
            activityCall(InboundActivity::class.java)
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

    private fun activityCall(activity: Class<*>){
        val intent = Intent(this, activity)
        startActivity(intent)
        finish()
    }
}
