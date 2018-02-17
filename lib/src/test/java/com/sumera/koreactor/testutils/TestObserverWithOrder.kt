package com.sumera.koreactor.testutils

import io.reactivex.observers.TestObserver

class TestObserverWithOrder<T> : TestObserver<T>() {

    var nextValueIndex = 0

    var valuesCount = 0

    fun assertNextValue(value: T) {
        assertValueAt(nextValueIndex, value)
        nextValueIndex += 1
        valuesCount++
    }

    fun assertNextValuesCount() {
        assertValueCount(valuesCount)
    }
}
