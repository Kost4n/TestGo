package com.kost4n.testgo.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.kost4n.testgo.R
import com.kost4n.testgo.model.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

class MealsAdapter(private val mealsList: MutableList<Meal>, private val activity: FragmentActivity): RecyclerView.Adapter<MealsAdapter.MealHolder>() {
    class MealHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageMeal = itemView.findViewById<ImageView>(R.id.image_meal)
        val nameMeal = itemView.findViewById<TextView>(R.id.name_meal)
        val ingredientsMeal = itemView.findViewById<TextView>(R.id.ingredients_meal)

        fun bind(listItem: Meal) {
            itemView.setOnClickListener {
                //TODO click
            }
        }
    }

    fun getList() = mealsList

    fun updateAdapter(items: MutableList<Meal>){
        mealsList.clear()
        mealsList.addAll(items)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsAdapter.MealHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_meals, parent, false)
        return MealHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealsAdapter.MealHolder, position: Int) {
        val listItem = mealsList[position]
        holder.bind(listItem)
        holder.nameMeal.text = listItem.strMeal
        holder.ingredientsMeal.text = "${listItem.strIngredient1}, ${listItem.strIngredient2}, ${listItem.strIngredient3}, ${listItem.strIngredient4}, ${listItem.strIngredient5}"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(listItem.strMealThumb)
                val image = BitmapFactory.decodeStream(url.openStream())
                activity.runOnUiThread {
                    holder.imageMeal.setImageBitmap(image)
                }
            } catch (e: IOException) {
                Log.e("error", "can't load image ${listItem.strMeal}")
            }
        }
    }

    override fun getItemCount(): Int = mealsList.size
}