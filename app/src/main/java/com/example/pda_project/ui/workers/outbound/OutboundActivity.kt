package com.example.pda_project.ui.workers.outbound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentResultListener
import com.example.pda_project.ui.workers.BarcodeScannerFragment
import com.example.pda_project.R
import com.example.pda_project.ui.workers.Scanner
import com.google.firebase.firestore.FirebaseFirestore
class OutboundActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var scanner: Scanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outbound)

        firestore = FirebaseFirestore.getInstance()
        scanner = Scanner(this)

        findViewById<Button>(R.id.buttonOpenScanner).setOnClickListener {
            scanner.openScanner()
        }

        // 바코드 스캔 결과를 받기 위한 리스너 등록
        supportFragmentManager.setFragmentResultListener("barcode_scan", this, FragmentResultListener { _, bundle ->
            val result = bundle.getString("barcode")
            // 스캔된 바코드 값을 로그로 출력
            Log.d("ToteScanActivity", "Scanned barcode: $result")
            // 스캔된 바코드 값을 Toast로 출력
            if (result != null && result.startsWith("tote")) {
                // 바코드 값을 저장
                saveBarcodeToCache(result)
                // Firestore에서 문서 가져오기 및 삭제
                fetchAndRemoveDocument(result)
            } else {
                Toast.makeText(this, "tote 바코드를 찍어야 합니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveBarcodeToCache(barcode: String) {
        val sharedPref = getSharedPreferences("pda_project", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("scanned_barcode", barcode)
            apply()
        }
    }

    private fun fetchAndRemoveDocument(toteId: String) {
        firestore.collection("outboundList")
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    try {
                        val docId = document.id
                        val warehouseNumber = docId.split("-")[0]
                        val items = document.data.mapNotNull { entry ->
                            val itemId = entry.key
                            val itemData = entry.value as? Map<*, *>
                            val amount = itemData?.get("amount") as? Number
                            val itemName = itemData?.get("item") as? String
                            if (itemData != null && amount != null && itemName != null) {
                                Item(itemId, amount.toInt(), itemName)
                            } else {
                                null
                            }
                        }
                        // 캐시에 문서 데이터 저장
                        saveDocumentToCache(toteId, warehouseNumber, items)
                        startActivity(Intent(this, WarehouseScanActivity::class.java))
                        finish()
                    } catch (e: Exception) {
                        Log.e("ToteScanActivity", "Error processing document", e)
                        Toast.makeText(this, "문서를 처리하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ToteScanActivity", "Error getting documents: ", exception)
                Toast.makeText(this, "문서를 가져오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDocumentToCache(toteId: String, warehouseNumber: String, items: List<Item>) {
        val sharedPref = getSharedPreferences("pda_project", Context.MODE_PRIVATE)
        val itemsString = items.joinToString(separator = ";") { "${it.id},${it.amount},${it.item}" }
        with(sharedPref.edit()) {
            putString("scanned_tote_id", toteId)
            putString("warehouse_number", warehouseNumber)
            putString("items_list", itemsString)
            apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Fragment result listener 해제
        supportFragmentManager.clearFragmentResultListener("barcode_scan")

        // BarcodeScannerFragment가 카메라 리소스를 해제하도록 요청
        val fragment = supportFragmentManager.findFragmentByTag("BarcodeScannerFragment")
        if (fragment is BarcodeScannerFragment) {
            fragment.releaseResources()
        }
    }

    data class Item(val id: String, val amount: Int?, val item: String?)
}
