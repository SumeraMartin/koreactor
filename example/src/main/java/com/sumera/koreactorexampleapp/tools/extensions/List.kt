package com.sumera.koreactorexampleapp.tools.extensions

inline fun <T> List<T>.replaceWithPredicate(predicate: (T) -> Boolean, transform: (T) -> T) : List<T> {
	return map { data ->
		if (predicate(data)) {
			return@map transform(data)
		}
		return@map data
	}
}