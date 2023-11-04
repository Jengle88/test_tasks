package ru.jengle88.deliveryapp.di

import dagger.Binds
import dagger.Module
import ru.jengle88.deliveryapp.data.repository.MealsRepositoryImpl
import ru.jengle88.deliveryapp.domain.repository.MealsRepository

@Module
interface RepositoryModule {

    @Binds
    fun bindMealsRepository(impl: MealsRepositoryImpl): MealsRepository
}