package com.example.pda_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class ManagerModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_mode)

        findViewById<Button>(R.id.buttonApproveStock).setOnClickListener {
            // 입고승인 액티비티 시작
            //val intent = Intent(this, ApproveStockActivity::class.java)
            //startActivity(intent)
        }

        findViewById<Button>(R.id.buttonCheckAndEditStock).setOnClickListener {
            // 재고확인 및 수정 액티비티 시작
            //val intent = Intent(this, CheckAndEditStockActivity::class.java)
            //startActivity(intent)
        }

        findViewById<Button>(R.id.buttonCheckErrorReports).setOnClickListener {
            // 오류 보고서 확인 액티비티 시작
            val intent = Intent(this, CheckErrorReportsActivity::class.java)
            startActivity(intent)
        }
    }
}
