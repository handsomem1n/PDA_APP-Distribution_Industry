package com.example.pda_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.pda_project.ui.manager.ManageWarehouseActivity
import com.example.pda_project.ui.workers.inbound.InboundAcceptActivity

class ManagerModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_mode)

        findViewById<Button>(R.id.inboundAcceptButton).setOnClickListener {
            // 입고승인 액티비티 시작
            activityCall(InboundAcceptActivity::class.java)
        }

        findViewById<Button>(R.id.ManageWarehouseButton).setOnClickListener {
            // 재고확인 및 수정 액티비티
            activityCall(ManageWarehouseActivity::class.java)
        }

        findViewById<Button>(R.id.buttonCheckErrorReports).setOnClickListener {
            // 오류 보고서 확인 액티비티 시작
            activityCall(CheckErrorReportsActivity::class.java)
        }
    }

    private fun activityCall(activity: Class<*>){
        val intent = Intent(this, activity)
        startActivity(intent)
    }
}
