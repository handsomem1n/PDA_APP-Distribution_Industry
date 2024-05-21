package com.example.pda_project.helpers

import android.view.View
import android.widget.TextView
import com.example.pda_project.R
import com.example.pda_project.models.Item

class ItemBindingHelper {
    companion object {
        fun bind(itemView: View, item: Item, onItemClick: (String, String) -> Unit) {
            val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
            itemNameTextView.text = item.itemName
            itemView.setOnClickListener {
                onItemClick(item.productId, item.itemName)
            }
        }
    }
}
