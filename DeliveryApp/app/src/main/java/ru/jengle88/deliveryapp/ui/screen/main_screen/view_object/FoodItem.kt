package ru.jengle88.deliveryapp.ui.screen.main_screen.view_object

data class FoodItem(
    val title: String,
    val ingredients: String = "$title, $title, $title, $title, $title, $title, $title, $title",
    val price: Int = 345,
    val imageUrl: String = "https://drive.google.com/uc?export=download&id=1FUQ6QG96hNu_TAlkXXNA-b3n4lxM5Ohd"
)