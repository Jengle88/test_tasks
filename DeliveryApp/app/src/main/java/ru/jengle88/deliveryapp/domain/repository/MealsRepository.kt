package ru.jengle88.deliveryapp.domain.repository

import ru.jengle88.deliveryapp.data.remote.dto.CategoriesDto
import ru.jengle88.deliveryapp.data.remote.dto.MealsDto

interface MealsRepository {
    suspend fun getRandomMeals(): MealsDto

    suspend fun getCategories(): CategoriesDto
}