package ru.jengle88.deliveryapp.ui.screen.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.jengle88.deliveryapp.common.ApiResult
import ru.jengle88.deliveryapp.domain.use_case.GetCategoriesUseCase
import ru.jengle88.deliveryapp.domain.use_case.GetMealsUseCase
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.MealItem
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.PromoMealImageItem
import javax.inject.Inject

class MainFragmentViewModel(
    private val getMealsUseCase: GetMealsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private var mealItems = persistentListOf<MealItem>()

    private val mealsCategoriesMutableState = MutableStateFlow(persistentListOf("All"))
    val mealsCategoriesState = mealsCategoriesMutableState.asStateFlow()

    private val selectedCategoryMutableState = MutableStateFlow(0)
    val selectedCategoryState = selectedCategoryMutableState.asStateFlow()

    private val promoMealItemsMutableState = MutableStateFlow(
        persistentListOf(
            PromoMealImageItem("https://drive.google.com/uc?id=1JTawuyeHCPL8FgH6OzZt4suwUjJpSxoP"),
            PromoMealImageItem("https://drive.google.com/uc?id=1Q9nlg8rQZr2AcpSU_9wYxPD0smOCDSiW"),
            PromoMealImageItem("https://drive.google.com/uc?id=1JTawuyeHCPL8FgH6OzZt4suwUjJpSxoP"),
            PromoMealImageItem("https://drive.google.com/uc?id=1Q9nlg8rQZr2AcpSU_9wYxPD0smOCDSiW"),
        )
    )
    val promoMealItemsState = promoMealItemsMutableState.asStateFlow()

    private val visibleMealItemsMutableState = MutableStateFlow(persistentListOf<MealItem>())
    val visibleMealsState = visibleMealItemsMutableState.asStateFlow()
    private val mealsLoadingMutableState = MutableStateFlow(LoadingState.NONE)
    val mealsLoadingState = mealsLoadingMutableState.asStateFlow()

    val citiesList = persistentListOf("Москва", "Санкт-Петербург", "Казань", "Воронеж", "Орёл")

    private val currentCityMutableState = MutableStateFlow("Москва")
    val currentCityState = currentCityMutableState.asStateFlow()

    init {
        uploadCategories()
        uploadMeals()
    }

    private fun uploadCategories() {
        getCategoriesUseCase.invoke()
            .onEach { apiResult ->
                when (apiResult) {
                    is ApiResult.Loading -> {}
                    is ApiResult.Success -> {
                        val result = buildList {
                            add("All")
                            addAll(apiResult.result ?: emptyList())
                        }.toPersistentList()
                        mealsCategoriesMutableState.value = result
                        selectedCategoryMutableState.value = 0
                    }
                    is ApiResult.Failure -> {}
                }
            }
            .launchIn(viewModelScope)
    }

    fun onReloadClick() {
        uploadCategories()
        uploadMeals()
    }

    private fun uploadMeals() {
        getMealsUseCase.invoke(20)
            .onEach { apiResult ->
                when (apiResult) {
                    is ApiResult.Loading -> {
                        mealsLoadingMutableState.value = LoadingState.LOADING
                    }
                    is ApiResult.Success -> {
                        mealItems = (apiResult.result ?: emptyList()).toPersistentList()
                        mealsLoadingMutableState.value = LoadingState.SUCCESS
                        onVisibleMealsChanged()
                    }
                    is ApiResult.Failure -> {
                        mealsLoadingMutableState.value = LoadingState.FAILURE
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSelectedFilterChanged(newIndex: Int) {
        if (newIndex !in mealsCategoriesMutableState.value.indices) return
        selectedCategoryMutableState.value = newIndex
        onVisibleMealsChanged()
    }

    fun onCurrentCityChanged(newIndex: Int) {
        if (newIndex !in citiesList.indices) return
        currentCityMutableState.value = citiesList[newIndex]
    }

    private fun onVisibleMealsChanged() {
        val selectedFilter = selectedCategoryState.value
        if (selectedFilter == 0) {
            visibleMealItemsMutableState.value = mealItems
        } else {
            val currentMeals = mealItems.toList()
            val selectedCategory = mealsCategoriesState.value[selectedFilter]
            val result = currentMeals.filter { it.category == selectedCategory }

            visibleMealItemsMutableState.value = result.toPersistentList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val getMealsUseCase: GetMealsUseCase,
        private val getCategoriesUseCase: GetCategoriesUseCase,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return (MainFragmentViewModel(
                getMealsUseCase,
                getCategoriesUseCase
            ) as T)
        }
    }
}