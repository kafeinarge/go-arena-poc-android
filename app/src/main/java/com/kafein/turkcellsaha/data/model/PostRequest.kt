package com.kafein.turkcellsaha.data.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class PostRequest(
    @SerializedName("text")
    val text: String,
    @SerializedName("abc")
    val file: File?
)