package com.turkcell.turkcellsaha.data.model

import com.google.gson.annotations.SerializedName

data class Wall(
    @SerializedName("content")
    val contents: List<WallContent>
)
