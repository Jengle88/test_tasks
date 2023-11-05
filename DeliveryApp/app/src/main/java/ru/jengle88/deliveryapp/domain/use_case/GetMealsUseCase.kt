package ru.jengle88.deliveryapp.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.jengle88.deliveryapp.common.ApiResult
import ru.jengle88.deliveryapp.domain.mapper.FoodMapper
import ru.jengle88.deliveryapp.domain.repository.MealsRepository
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.MealItem

class GetMealsUseCase(
    private val mealsRepository: MealsRepository
) {
    operator fun invoke(amount: Int = 20): Flow<ApiResult<List<MealItem>>> = flow {
        emit(ApiResult.Loading())

        val result = mutableSetOf<MealItem>()
        val mapper = FoodMapper()

        try {
            while (result.size < amount) {
                val newMeal = mealsRepository.getRandomMeals()
                if (newMeal.meals.isNotEmpty())
                    result.add(mapper.toMealItem(newMeal.meals.first()))
            }
            emit(ApiResult.Success(result.toList()))
        }
        catch (e: Exception) {
            emit(ApiResult.Failure(e.message))
        }
    }
}