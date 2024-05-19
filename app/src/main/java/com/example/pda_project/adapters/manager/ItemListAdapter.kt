package com.example.pda_project.adapters.manager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pda_project.R
import com.example.pda_project.models.manager.Item

class ItemListAdapter(
    private val itemList: List<Item>,
    private val onItemClick: (String, String) -> Unit
) : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_warehouse_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemNameTextView.text = item.itemName
        holder.itemView.setOnClickListener {
            onItemClick(item.id, item.itemName)
        }
    }

    override fun getItemCount() = itemList.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
    }
}
