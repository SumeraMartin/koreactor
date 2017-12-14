package com.sumera.koreactor.lib.reactor.data.either

sealed class Either<out L, out R>

data class EitherLeft<out T>(val data: T) : Either<T, Nothing>()

data class EitherRight<out T>(val data: T) : Either<Nothing, T>()

inline fun <E, R, T> Either<E, R>.fold(leftAction: (E) -> T, rightAction: (R) -> T): T {
	return when (this) {
		is EitherLeft -> leftAction(data)
		is EitherRight -> rightAction(data)
	}
}

