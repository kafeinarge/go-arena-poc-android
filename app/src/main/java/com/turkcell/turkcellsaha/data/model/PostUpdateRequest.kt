package com.turkcell.turkcellsaha.data.model

import com.google.gson.annotations.SerializedName

data class PostUpdateRequest(
    @SerializedName("text")
    val text: String
)
