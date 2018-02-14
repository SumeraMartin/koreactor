package com.sumera.koreactor.behaviour.data

data class OutputData<out INPUT, out OUTPUT>(
        val input: INPUT,
        val output: OUTPUT
)
