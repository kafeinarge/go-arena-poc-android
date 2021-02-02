package com.kafein.turkcellsaha.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WallContent(
    @SerializedName("user")
    val user: User,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("preview")
    val preview: String?,
    @SerializedName("text")
    val text: String,
    @SerializedName("creationDate")
    val creationDate: String,
    @SerializedName("approval")
    val approval: String
) : Serializable
