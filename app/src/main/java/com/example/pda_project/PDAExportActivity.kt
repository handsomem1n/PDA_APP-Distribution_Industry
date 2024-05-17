package com.example.pda_project

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.commit

class PDAExportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pda_export)

        findViewById<Button>(R.id.buttonOpenScanner).setOnClickListener {
            openScanner()
        }

        // 바코드 스캔 결과를 받기 위한 리스너 등록
        supportFragmentManager.setFragmentResultListener("barcode_scan", this, FragmentResultListener { requestKey, bundle ->
            val result = bundle.getString("barcode")
            // 스캔된 바코드 값을 로그로 출력
            Log.d("PDAExportActivity", "Scanned barcode: $result")
            // 스캔된 바코드 값을 Toast로 출력
            Toast.makeText(this, "Scanned barcode: $result", Toast.LENGTH_SHORT).show()
        })
    }

    private fun openScanner() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainScreen, BarcodeScannerFragment())
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }
}
