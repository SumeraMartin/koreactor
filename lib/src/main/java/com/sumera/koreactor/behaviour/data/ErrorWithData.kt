package com.sumera.koreactor.behaviour.data

data class ErrorWithData<out INPUT>(
        val data: INPUT,
        val error: Throwable
)
