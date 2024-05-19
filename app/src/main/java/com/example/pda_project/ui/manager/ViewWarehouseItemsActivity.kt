package com.example.pda_project.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.R
import com.example.pda_project.models.manager.Item
import com.google.firebase.firestore.FirebaseFirestore

class ViewWarehouseItemsActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var itemListAdapter: ArrayAdapter<String>
    private lateinit var itemList: MutableList<Item>
    private lateinit var itemNames: MutableList<String>
    private lateinit var db: FirebaseFirestore
    private lateinit var warehouseId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_warehouse_items)

        warehouseId = intent.getStringExtra("warehouseId") ?: ""

        listView = findViewById(R.id.listView)
        itemList = mutableListOf()
        itemNames = mutableListOf()
        itemListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemNames)
        listView.adapter = itemListAdapter

        db = FirebaseFirestore.getInstance()
        fetchWarehouseItems()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItem = itemList[position]
            val intent = Intent(this, EditItemAmountActivity::class.java)
            intent.putExtra("warehouseId", warehouseId)
            intent.putExtra("itemId", selectedItem.id)
            intent.putExtra("itemName", selectedItem.itemName)
            startActivityForResult(intent, 1)
        }
    }

    private fun fetchWarehouseItems() {
        itemList.clear()
        itemNames.clear()
        db.collection("warehouse").document(warehouseId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    document.data?.forEach { (itemId, value) ->
                        val itemMap = value as? Map<*, *>
                        val itemName = itemMap?.get("item") as? String
                        val amount = itemMap?.get("amount") as? Long
                        if (itemName != null && amount != null) {
                            val item = Item(itemId, itemName, amount.toInt())
                            itemList.add(item)
                            itemNames.add("Item: $itemName, Amount: $amount")
                        }
                    }
                    itemListAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching document: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fetchWarehouseItems()
        }
    }
}
