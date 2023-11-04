package ru.jengle88.deliveryapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MealsDto(
    @SerializedName("meals")
    val meals: List<MealDto>
) {
    override fun toString(): String {
        return super.toString()
    }
}