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
import com.example.pda_project.ui.workers.ModeSelectActivity
import com.example.pda_project.ui.workers.Scanner
import com.google.firebase.firestore.FirebaseFirestore

class ItemsScanActivity : AppCompatActivity() {

    private lateinit var itemTextView: TextView
    private lateinit var openScannerButton: Button
    private lateinit var scanner: Scanner

    private lateinit var toteId: String
    private lateinit var warehouseNumber: String
    private lateinit var documentId: String
    private lateinit var items: MutableList<Item>
    private lateinit var originalItems: List<Item>  // 원래의 아이템 목록을 저장

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_scan)

        itemTextView = findViewById(R.id.itemTextView)
        openScannerButton = findViewById(R.id.openScannerButton)
        scanner = Scanner(this)

        // 캐시에서 데이터 불러오기
        loadDataFromCache()

        // 원래의 아이템 목록을 복사하여 저장
        originalItems = items.map { it.copy() }

        updateItemTextView()

        openScannerButton.setOnClickListener {
            scanner.openScanner()
        }

        // 바코드 스캔 결과를 받기 위한 리스너 등록
        supportFragmentManager.setFragmentResultListener("barcode_scan", this, FragmentResultListener { requestKey, bundle ->
            val result = bundle.getString("barcode")
            // 스캔된 바코드 값을 로그로 출력
            Log.d("ItemsScanActivity", "Scanned barcode: $result")
            // 스캔된 바코드 값을 Toast로 출력
            if (result != null) {
                processScannedBarcode(result)
            }
        })
    }

    private fun processScannedBarcode(barcode: String) {
        if (items.isNotEmpty()) {
            val currentItem = items[0]
            if (barcode == currentItem.id) {
                currentItem.amount -= 1
                if (currentItem.amount <= 0) {
                    items.removeAt(0)
                }
                updateItemTextView()
            } else {
                Toast.makeText(this, "잘못된 바코드입니다. 올바른 바코드를 스캔하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        if (items.isEmpty()) {
            Toast.makeText(this, "모든 품목 처리가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            updateWarehouseStock()
        }
    }

    private fun updateWarehouseStock() {
        val warehouseDocRef = firestore.collection("warehouse").document(warehouseNumber)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(warehouseDocRef)
            originalItems.forEach { item ->
                val itemMap = snapshot.get(item.id) as? Map<*, *>
                if (itemMap != null) {
                    val currentAmount = itemMap["amount"] as? Long ?: 0L
                    val newAmount = currentAmount - item.amount
                    transaction.update(warehouseDocRef, "${item.id}.amount", newAmount)
                }
            }
            null
        }.addOnSuccessListener {
            Log.d("ItemsScanActivity", "Warehouse stock updated successfully")
            clearItemsFromCache()
            startActivity(Intent(this, ModeSelectActivity::class.java))
            finish()
        }.addOnFailureListener { e ->
            Log.e("ItemsScanActivity", "Error updating warehouse stock", e)
            Toast.makeText(this, "창고 재고 업데이트 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateItemTextView() {
        if (items.isNotEmpty()) {
            val currentItem = items[0]
            itemTextView.text = "ID: ${currentItem.id}, Item: ${currentItem.item}, Amount: ${currentItem.amount}"
        } else {
            itemTextView.text = "모든 품목 처리가 완료되었습니다."
        }
    }

    private fun loadDataFromCache() {
        val sharedPref = getSharedPreferences("pda_project", Context.MODE_PRIVATE)

        toteId = sharedPref.getString("scanned_tote_id", "") ?: ""
        warehouseNumber = sharedPref.getString("warehouse_number", "") ?: ""
        documentId = sharedPref.getString("document_id", "") ?: ""

        val itemsString = sharedPref.getString("items_list", "") ?: ""
        if (itemsString.isEmpty()) {
            Log.e("ItemsScanActivity", "Items list is empty.")
            Toast.makeText(this, "품목 목록을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        } else {
            items = itemsString.split(";").mapNotNull { itemStr ->
                val parts = itemStr.split(",")
                if (parts.size == 3) {
                    val id = parts[0]
                    val amount = parts[1].toIntOrNull() ?: 0
                    val item = parts[2]
                    Item(id, amount, item)
                } else {
                    null
                }
            }.toMutableList()
            Log.d("ItemsScanActivity", "Loaded items: $items")
        }

        if (warehouseNumber.isEmpty()) {
            Log.e("ItemsScanActivity", "Warehouse number is empty.")
            Toast.makeText(this, "창고 번호를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("ItemsScanActivity", "Loaded warehouse number: $warehouseNumber")
        }
    }

    private fun clearItemsFromCache() {
        val sharedPref = getSharedPreferences("pda_project", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("items_list")
            apply()
        }
    }

    data class Item(val id: String, var amount: Int, val item: String) : java.io.Serializable {
        fun copy(): Item = Item(id, amount, item)
    }
}
