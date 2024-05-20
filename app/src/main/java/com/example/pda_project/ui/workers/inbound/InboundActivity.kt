package com.example.pda_project.ui.workers.inbound

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentResultListener
import com.example.pda_project.R
import com.example.pda_project.models.Item
import com.example.pda_project.ui.workers.BarcodeScannerFragment
import com.example.pda_project.ui.workers.ModeSelectActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InboundActivity : AppCompatActivity() {

    private val inboundCache = mutableMapOf<String, Item>() // productId -> Item
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var warehouseNumber: String = ""
    private lateinit var userEmail: String
    private lateinit var warehouseSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbound)

        userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "사용자 이메일을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
        }

        warehouseSpinner = findViewById(R.id.warehouseSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.warehouse_numbers,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            warehouseSpinner.adapter = adapter
        }

        warehouseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedWarehouse = parent.getItemAtPosition(position).toString()
                if (selectedWarehouse != "Select Warehouse") {
                    warehouseNumber = selectedWarehouse
                } else {
                    warehouseNumber = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                warehouseNumber = ""
            }
        }

        findViewById<Button>(R.id.buttonOpenScanner).setOnClickListener {
            openScanner()
        }

        findViewById<Button>(R.id.buttonCompleteInbound).setOnClickListener {
            completeInbound()
        }

        supportFragmentManager.setFragmentResultListener("barcode_scan", this, FragmentResultListener { _, bundle ->
            val result = bundle.getString("barcode")
            Log.d("InboundActivity", "Scanned barcode: $result")
            if (result != null) {
                processScannedBarcode(result)
            }
        })
    }

    private fun openScanner() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainScreen, BarcodeScannerFragment(), "BarcodeScannerFragment")
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun processScannedBarcode(barcode: String) {
        Item.fetchItemDetails(this, barcode) { item ->
            if (item != null) {
                val currentItem = inboundCache[barcode]
                if (currentItem != null) {
                    currentItem.amount += 1
                } else {
                    inboundCache[barcode] = item
                }
                Toast.makeText(this, "물품 추가: ${item.itemName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun completeInbound() {
        if (inboundCache.isEmpty()) {
            Toast.makeText(this, "No items to inbound", Toast.LENGTH_SHORT).show()
            return
        }

        if (warehouseNumber.isEmpty()) {
            Toast.makeText(this, "Please select a warehouse number", Toast.LENGTH_SHORT).show()
            return
        }

        val documentName = "$warehouseNumber-$userEmail"
        val warehouseRef = firestore.collection("warehouseTemp").document(documentName)
        val updateData = mutableMapOf<String, Any>()

        inboundCache.forEach { (productId, item) ->
            updateData[productId] = mapOf("item" to item.itemName, "amount" to item.amount)
        }

        warehouseRef.set(updateData)
            .addOnSuccessListener {
                Toast.makeText(this, "입고가 성공적으로 되었습니다", Toast.LENGTH_SHORT).show()
                clearInboundCache()
                startActivity(Intent(this, ModeSelectActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "입고 처리 오류: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearInboundCache() {
        inboundCache.clear()
        val sharedPref = getSharedPreferences("pda_project", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("inbound_cache")
            apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.clearFragmentResultListener("barcode_scan")
    }
}
