package com.kost4n.testgo.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.kost4n.testgo.model.Category
import com.kost4n.testgo.model.Meal
import com.kost4n.testgo.service.SearchRepositoryProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MenuViewModel: ViewModel() {
    val categoryList = MutableLiveData<MutableList<Category>>()
    val mealList = MutableLiveData<MutableList<Meal>>()
    private val repository = SearchRepositoryProvider.provideSearchRepository()

    fun getCategories() = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getCategories()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()?.string()
                        )
                    )
                    val parse = prettyJson.removeSurrounding("{\"categories\":", "}")
                    categoryList.value = gson.fromJson(parse, Array<Category>::class.java).toMutableList()
                    Log.d("Pretty Printed JSON :", prettyJson)
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }

    fun getMeals() = CoroutineScope(Dispatchers.IO).launch {
        val response = repository.getMeals()
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val gson = GsonBuilder().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()?.string()
                    )
                )
                val parse = prettyJson.removeSurrounding("{\"meals\":", "}")
                mealList.value = gson.fromJson(parse, Array<Meal>::class.java).toMutableList()
                Log.d("Pretty Printed JSON :", prettyJson)
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }

    fun updateMealsByCategory(categoryName: String) =CoroutineScope(Dispatchers.IO).launch {
        val response = repository.getMeals()
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                val gson = GsonBuilder().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()?.string()
                    )
                )
                val parse = prettyJson.removeSurrounding("{\"meals\":", "}")
                mealList.value = gson.fromJson(parse, Array<Meal>::class.java).toMutableList()
                mealList.value?.removeAll { meal -> meal.strCategory != categoryName }

                Log.d("Pretty Printed JSON :", prettyJson)
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }
}
