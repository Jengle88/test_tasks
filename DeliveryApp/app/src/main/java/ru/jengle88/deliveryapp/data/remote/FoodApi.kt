package ru.jengle88.deliveryapp.data.remote

import retrofit2.http.GET
import ru.jengle88.deliveryapp.data.remote.dto.CategoriesDto
import ru.jengle88.deliveryapp.data.remote.dto.MealsDto

interface FoodApi {

    @GET("random.php")
    suspend fun getRandomMeals(): MealsDto

    @GET("list.php?c=list")
    suspend fun getMealCategory(): CategoriesDto
}