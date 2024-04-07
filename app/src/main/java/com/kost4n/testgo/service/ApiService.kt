package com.kost4n.testgo.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface ApiService {
    @GET("categories.php")
    suspend fun getCategories(): Response<ResponseBody>
    @GET("search.php?s")
    suspend fun getMeals(): Response<ResponseBody>

    companion object Factory {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://themealdb.com/api/json/v1/1/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

object SearchRepositoryProvider {
    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(ApiService.create())
    }
}

class SearchRepository(private val apiService: ApiService) {
    suspend fun getCategories() = apiService.getCategories()
    suspend fun getMeals() = apiService.getMeals()
}
