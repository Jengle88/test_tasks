package ru.jengle88.deliveryapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CategoriesDto(
    @SerializedName("meals")
    val categories: List<CategoryDto>
) {
    override fun toString(): String {
        return super.toString()
    }
}