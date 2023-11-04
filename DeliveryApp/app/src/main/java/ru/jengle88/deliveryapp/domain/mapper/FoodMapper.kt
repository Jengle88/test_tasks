package ru.jengle88.deliveryapp.domain.mapper

import ru.jengle88.deliveryapp.data.remote.dto.MealDto
import ru.jengle88.deliveryapp.ui.screen.main_screen.view_object.MealItem

class FoodMapper {
    fun toMealItem(mealDto: MealDto): MealItem {
        return MealItem(
            id = mealDto.idMeal,
            title = mealDto.strMealName,
            category = mealDto.strCategory,
            ingredients = buildIngredientsStr(mealDto),
            price = (50 + Math.random() * 300).toInt(),
            imageUrl = mealDto.strMealThumbUrl
        )
    }

    private fun buildIngredientsStr(mealDto: MealDto): String {
         return buildList {
            add(mealDto.strIngredient1)
            add(mealDto.strIngredient2)
            add(mealDto.strIngredient3)
            add(mealDto.strIngredient4)
            add(mealDto.strIngredient5)
            add(mealDto.strIngredient6)
            add(mealDto.strIngredient7)
            add(mealDto.strIngredient8)
            add(mealDto.strIngredient9)
            add(mealDto.strIngredient10)
            add(mealDto.strIngredient11)
            add(mealDto.strIngredient12)
            add(mealDto.strIngredient13)
            add(mealDto.strIngredient14)
            add(mealDto.strIngredient15)
            add(mealDto.strIngredient16)
            add(mealDto.strIngredient17)
            add(mealDto.strIngredient18)
            add(mealDto.strIngredient19)
            add(mealDto.strIngredient20)
        }
             .filter { it?.isNotBlank() == true }
             .joinToString(separator = ", ")
    }
}