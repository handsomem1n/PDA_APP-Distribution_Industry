package com.example.pda_project.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.R
import com.example.pda_project.adapters.manager.WarehouseManager
import com.example.pda_project.models.manager.Item
import com.google.firebase.firestore.FirebaseFirestore
class ViewWarehouseItemsActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var itemListAdapter: ArrayAdapter<String>
    private lateinit var itemList: MutableList<Item>
    private lateinit var itemNames: MutableList<String>
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
        WarehouseManager.fetchWarehouseItems(warehouseId) { items ->
            itemList.clear()
            itemList.addAll(items)
            itemNames.clear()
            itemNames.addAll(items.map { "Item: ${it.itemName}, Amount: ${it.amount}" })
            itemListAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fetchWarehouseItems()
        }
    }
}
