package com.example.pda_project.ui.manager

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.R
import com.google.firebase.firestore.FirebaseFirestore

class EditItemAmountActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var warehouseId: String
    private lateinit var itemId: String
    private lateinit var itemName: String
    private lateinit var amountEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item_amount)

        warehouseId = intent.getStringExtra("warehouseId") ?: ""
        itemId = intent.getStringExtra("itemId") ?: ""
        itemName = intent.getStringExtra("itemName") ?: ""

        amountEditText = findViewById(R.id.amountEditText)
        val saveButton: Button = findViewById(R.id.saveButton)

        db = FirebaseFirestore.getInstance()

        fetchItemAmount()

        saveButton.setOnClickListener {
            saveUpdatedAmount()
        }
    }

    private fun fetchItemAmount() {
        db.collection("warehouse").document(warehouseId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val itemMap = document.get(itemId) as? Map<*, *>
                    val amount = itemMap?.get("amount") as? Long
                    amountEditText.setText(amount?.toString() ?: "0")
                } else {
                    Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("EditItemAmountActivity", "Error fetching document", exception)
                Toast.makeText(this, "Error fetching document: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUpdatedAmount() {
        val newAmount = amountEditText.text.toString().toLongOrNull()
        if (newAmount != null) {
            db.collection("warehouse").document(warehouseId)
                .update("$itemId.amount", newAmount)
                .addOnSuccessListener {
                    Toast.makeText(this, "수량 업데이트 성공", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("EditItemAmountActivity", "Error updating amount", exception)
                    Toast.makeText(this, "수량 업데이트 오류: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "값이 없습니다", Toast.LENGTH_SHORT).show()
        }
    }
}
