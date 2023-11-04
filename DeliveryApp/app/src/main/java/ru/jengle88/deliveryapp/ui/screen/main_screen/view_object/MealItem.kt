package ru.jengle88.deliveryapp.ui.screen.main_screen.view_object

data class MealItem(
    val id: String,
    val title: String,
    val category: String,
    val ingredients: String,
    val price: Int,
    val imageUrl: String
) {
    override fun toString(): String {
        return super.toString()
    }
}