package com.sumera.koreactor.testutils

fun on(description: String, action: () -> Unit) {
    action()
}

fun it(description: String, action: () -> Unit) {
    action()
}