package com.turkcell.turkcellsaha.data.model

import com.google.gson.annotations.SerializedName

data class Summary(
    @SerializedName("content")
    val contents: List<SummaryContent>
)
