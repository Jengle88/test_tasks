package ru.jengle88.deliveryapp.di

import dagger.Module
import dagger.Provides
import ru.jengle88.deliveryapp.domain.repository.MealsRepository
import ru.jengle88.deliveryapp.domain.use_case.GetCategoriesUseCase
import ru.jengle88.deliveryapp.domain.use_case.GetMealsUseCase

@Module
object UseCaseModule {

    @Provides
    fun provideGetMealsUseCase(mealsRepository: MealsRepository): GetMealsUseCase {
        return GetMealsUseCase(mealsRepository)
    }

    @Provides
    fun provideGetIngredientsUseCase(mealsRepository: MealsRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(mealsRepository)
    }
}