package com.turkcell.turkcellsaha.data.model

import com.google.gson.annotations.SerializedName

data class SummaryContent(
    @SerializedName("user")
    val user: User,
    @SerializedName("paidCount")
    val paidCount: Int,
    @SerializedName("unpaidCount")
    val unpaidCount: Int,
    @SerializedName("totalGoal")
    val totalGoal: Int,
    @SerializedName("year")
    val year: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("category")
    val category: String
)
