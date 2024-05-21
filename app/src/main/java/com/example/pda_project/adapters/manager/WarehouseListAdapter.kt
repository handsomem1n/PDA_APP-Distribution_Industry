package com.example.pda_project.adapters.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pda_project.R
import com.example.pda_project.models.manager.Warehouse

class WarehouseListAdapter(
    private val warehouseList: List<Warehouse>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<WarehouseListAdapter.WarehouseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehouseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_warehouse, parent, false)
        return WarehouseViewHolder(view)
    }

    override fun onBindViewHolder(holder: WarehouseViewHolder, position: Int) {
        val warehouse = warehouseList[position]
        holder.warehouseIdTextView.text = "Warehouse ID: ${warehouse.id}"
        holder.itemView.setOnClickListener {
            onItemClick(warehouse.id)
        }
    }

    override fun getItemCount() = warehouseList.size

    class WarehouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val warehouseIdTextView: TextView = itemView.findViewById(R.id.warehouseIdTextView)
    }
}
