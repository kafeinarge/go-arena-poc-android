package com.turkcell.turkcellsaha.common

import com.turkcell.turkcellsaha.Status

class StatusViewState(val status: Status) {

    fun isLoading() = status is Status.Loading

    fun getErrorMessage() = if (status is Status.Error) status.exception.message else ""

    fun shouldShowErrorMessage() = status is Status.Error
}