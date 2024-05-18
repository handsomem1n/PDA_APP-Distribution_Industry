package com.example.pda_project.ui.workers.outbound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentResultListener
import com.example.pda_project.R
import com.example.pda_project.ui.workers.BarcodeScannerFragment

class WarehouseScanActivity : AppCompatActivity() {

    private lateinit var warehouseNumberTextView: TextView
    private var warehouseNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warehouse_scan)

        warehouseNumberTextView = findViewById(R.id.warehouseNumberTextView)

        // 저장된 데이터 불러오기
        loadWarehouseNumberFromCache()

        warehouseNumberTextView.text = "찾아야 할 창고 번호: $warehouseNumber"

        findViewById<Button>(R.id.buttonOpenScanner).setOnClickListener {
            openScanner()
        }

        // 바코드 스캔 결과를 받기 위한 리스너 등록
        supportFragmentManager.setFragmentResultListener("barcode_scan", this, FragmentResultListener { requestKey, bundle ->
            val result = bundle.getString("barcode")
            // 스캔된 바코드 값을 로그로 출력
            Log.d("WarehouseScanActivity", "Scanned barcode: $result")
            // 스캔된 바코드 값을 Toast로 출력
            if (result != null && result.startsWith("warehouse-")) {
                val scannedWarehouseNumber = result.removePrefix("warehouse-")
                if (scannedWarehouseNumber == warehouseNumber) {
                    // 다음 액티비티로 넘어가기
                    val intent = Intent(this, ItemsScanActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "올바른 창고 바코드를 스캔하세요.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "창고 바코드를 찍어야 합니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openScanner() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainScreen, BarcodeScannerFragment(), "BarcodeScannerFragment")
        transaction.addToBackStack(null)
        transaction.commit() // commitAllowingStateLoss 대신 commit 사용
    }

    private fun loadWarehouseNumberFromCache() {
        val sharedPref = getSharedPreferences("pda_project", Context.MODE_PRIVATE)
        warehouseNumber = sharedPref.getString("warehouse_number", "") ?: ""
        if (warehouseNumber.isEmpty()) {
            Log.e("WarehouseScanActivity", "Warehouse number is empty.")
            Toast.makeText(this, "창고 번호를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("WarehouseScanActivity", "Loaded warehouse number: $warehouseNumber")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Fragment result listener 해제
        supportFragmentManager.clearFragmentResultListener("barcode_scan")
    }
}
