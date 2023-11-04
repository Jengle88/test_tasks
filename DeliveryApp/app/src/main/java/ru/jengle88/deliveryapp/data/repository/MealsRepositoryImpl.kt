package ru.jengle88.deliveryapp.data.repository

import ru.jengle88.deliveryapp.data.remote.FoodApi
import ru.jengle88.deliveryapp.data.remote.dto.CategoriesDto
import ru.jengle88.deliveryapp.data.remote.dto.MealsDto
import ru.jengle88.deliveryapp.domain.repository.MealsRepository
import javax.inject.Inject

class MealsRepositoryImpl @Inject constructor(
    private val foodApi: FoodApi
): MealsRepository {
    override suspend fun getRandomMeals(): MealsDto {
        return foodApi.getRandomMeals()
    }

    override suspend fun getCategories(): CategoriesDto {
        return foodApi.getMealCategory()
    }
}