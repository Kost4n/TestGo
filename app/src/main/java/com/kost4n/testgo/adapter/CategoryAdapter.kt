package com.kost4n.testgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kost4n.testgo.R
import com.kost4n.testgo.model.Category

class CategoryAdapter(
    private val categoryList: MutableList<Category>,
    private val onRecordClickListener: OnRecordClickListener
    ): RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    inner class CategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameCategory = itemView.findViewById<TextView>(R.id.name_category)

        fun bind(listItem: Category, position: Int) {
            nameCategory.text = listItem.strCategory
            itemView.setOnClickListener {
                onRecordClickListener.onClick(listItem, itemView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_categories, parent, false)
        return CategoryHolder(itemView)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) =
        holder.bind(categoryList[position], position)

    interface OnRecordClickListener {
        fun onClick(category: Category, view: View)
    }
}
