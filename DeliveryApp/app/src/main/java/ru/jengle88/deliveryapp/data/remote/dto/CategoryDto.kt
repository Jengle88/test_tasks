package ru.jengle88.deliveryapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("strCategory")
    val strCategory: String
) {
    override fun toString(): String {
        return super.toString()
    }
}