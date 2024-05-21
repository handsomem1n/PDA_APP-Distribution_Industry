package com.example.pda_project.ui.manager

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pda_project.R
import com.example.pda_project.models.manager.Warehouse
import com.google.firebase.firestore.FirebaseFirestore

class ManageWarehouseActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var warehouseListAdapter: ArrayAdapter<String>
    private lateinit var warehouseList: MutableList<Warehouse>
    private lateinit var warehouseNames: MutableList<String>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_warehouse)

        listView = findViewById(R.id.listView)
        warehouseList = mutableListOf()
        warehouseNames = mutableListOf()
        warehouseListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, warehouseNames)
        listView.adapter = warehouseListAdapter

        db = FirebaseFirestore.getInstance()
        fetchWarehouseList()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedWarehouse = warehouseList[position]
            val intent = Intent(this, ViewWarehouseItemsActivity::class.java)
            intent.putExtra("warehouseId", selectedWarehouse.id)
            startActivity(intent)
        }
    }

    private fun fetchWarehouseList() {
        db.collection("warehouse")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val warehouse = Warehouse(document.id)
                    warehouseList.add(warehouse)
                    warehouseNames.add("Warehouse ID: ${warehouse.id}")
                }
                warehouseListAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting warehouse documents: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
