package ru.jengle88.deliveryapp.ui.screen.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.FoodItem
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.PromoImageItem
import javax.inject.Inject

class MainFragmentViewModel : ViewModel() {

    // TODO: Заменить на получение извне
    val filtersForFood = persistentListOf("Всё", "Пицца", "Комбо", "Десерты", "Напитки")
    private val foodItems = buildList { repeat(20) { i -> add(FoodItem("Пицца #$i")) } }
        .toPersistentList()

    private val selectedFilterMutableState = MutableStateFlow(0)
    val selectedFilterState = selectedFilterMutableState.asStateFlow()

    private val promoFoodItemsMutableState = MutableStateFlow(
        persistentListOf(
            PromoImageItem("https://drive.google.com/uc?id=1JTawuyeHCPL8FgH6OzZt4suwUjJpSxoP"),
            PromoImageItem("https://drive.google.com/uc?id=1Q9nlg8rQZr2AcpSU_9wYxPD0smOCDSiW"),
            PromoImageItem("https://drive.google.com/uc?id=1JTawuyeHCPL8FgH6OzZt4suwUjJpSxoP"),
            PromoImageItem("https://drive.google.com/uc?id=1Q9nlg8rQZr2AcpSU_9wYxPD0smOCDSiW"),
        )
    )
    val promoFoodItemsState = promoFoodItemsMutableState.asStateFlow()

    private val visibleFoodItemsMutableState = MutableStateFlow(persistentListOf<FoodItem>())
    val visibleFoodItemsState = visibleFoodItemsMutableState.asStateFlow()

    val citiesList = persistentListOf("Москва", "Санкт-Петербург", "Казань", "Воронеж", "Орёл")

    private val currentCityMutableState = MutableStateFlow("Москва")
    val currentCityState = currentCityMutableState.asStateFlow()

    init {
        onVisibleFoodChanged()
    }

    fun onSelectedFilterChanged(newIndex: Int) {
        if (newIndex !in filtersForFood.indices) return
        selectedFilterMutableState.value = newIndex
        onVisibleFoodChanged()
    }

    fun onCurrentCityChanged(newIndex: Int) {
        if (newIndex !in citiesList.indices) return
        currentCityMutableState.value = citiesList[newIndex]
    }

    private fun onVisibleFoodChanged() {
        val selectedFilter = selectedFilterState.value
        if (selectedFilter == 0) {
            visibleFoodItemsMutableState.value = foodItems
        } else {
            val result = mutableListOf<FoodItem>()
            for (i in selectedFilter until foodItems.size step filtersForFood.size) {
                result.add(foodItems[i])
            }
            visibleFoodItemsMutableState.value = result.toPersistentList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return (MainFragmentViewModel() as T)
        }
    }
}