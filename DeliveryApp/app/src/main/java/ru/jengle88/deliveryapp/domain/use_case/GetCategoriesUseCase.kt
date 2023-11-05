package ru.jengle88.deliveryapp.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.jengle88.deliveryapp.common.ApiResult
import ru.jengle88.deliveryapp.domain.repository.MealsRepository

class GetCategoriesUseCase(
    private val mealsRepository: MealsRepository
) {
    operator fun invoke(): Flow<ApiResult<List<String>>> = flow {
        emit(ApiResult.Loading())

        try {
            val categories = mealsRepository.getCategories().categories.map { it.strCategory }
            emit(ApiResult.Success(categories))
        } catch (e: Exception) {
            emit(ApiResult.Failure(e.message))
        }
    }
}