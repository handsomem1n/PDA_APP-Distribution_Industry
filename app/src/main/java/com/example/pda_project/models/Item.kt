package com.example.pda_project.models

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

data class Item(
    val productId: String,
    val itemName: String,
    var amount: Long
) {
    companion object {
        private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

        fun fetchItemDetails(context: Context, productId: String, onResult: (Item?) -> Unit) {
            firestore.collection("itemsData").document(productId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val itemName = document.getString("item") ?: "Unknown Item"
                        val item = Item(productId, itemName, 1)
                        onResult(item)
                    } else {
                        Toast.makeText(context, "Item not found in itemsData", Toast.LENGTH_SHORT).show()
                        onResult(null)
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error fetching item details: ${exception.message}", Toast.LENGTH_SHORT).show()
                    onResult(null)
                }
        }
    }
}
