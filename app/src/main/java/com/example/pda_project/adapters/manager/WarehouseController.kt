package com.example.pda_project.adapters.manager

import android.util.Log
import com.example.pda_project.models.manager.Item
import com.example.pda_project.models.manager.Warehouse
import com.google.firebase.firestore.FirebaseFirestore

object WarehouseController {
    private val db = FirebaseFirestore.getInstance()

    fun fetchWarehouseList(callback: (List<Warehouse>) -> Unit) {
        db.collection("warehouse")
            .get()
            .addOnSuccessListener { result ->
                val warehouses = result.map { document -> Warehouse(document.id) }
                callback(warehouses)
            }
            .addOnFailureListener { exception ->
                Log.e("WarehouseManager", "Error getting warehouse documents", exception)
            }
    }

    fun fetchWarehouseItems(warehouseId: String, callback: (List<Item>) -> Unit) {
        db.collection("warehouse").document(warehouseId)
            .get()
            .addOnSuccessListener { document ->
                val items = document.data?.mapNotNull { (itemId, value) ->
                    val itemMap = value as? Map<*, *>
                    val itemName = itemMap?.get("item") as? String
                    val amount = itemMap?.get("amount") as? Long
                    if (itemName != null && amount != null) {
                        Item(itemId, itemName, amount.toInt())
                    } else {
                        null
                    }
                } ?: emptyList()
                callback(items)
            }
            .addOnFailureListener { exception ->
                Log.e("WarehouseManager", "Error fetching warehouse items", exception)
            }
    }

    fun updateItemAmount(warehouseId: String, itemId: String, newAmount: Long, callback: (Boolean) -> Unit) {
        db.collection("warehouse").document(warehouseId)
            .update("$itemId.amount", newAmount)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                Log.e("WarehouseManager", "Error updating amount", exception)
                callback(false)
            }
    }
}
