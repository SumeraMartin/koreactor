# Changelog

## [0.0.8] - 21.03.2018

### Changed

#### Reactor
- reactor now contains `onSaveInstanceState` and `onRestoreSaveInstanceState` methods which allows to persist state across orientation changes

#### Behaviours
- `InfinityLoadingBehaviour` now takes `Single<Int>` as argument instead of `Int` to be able to work with savedInstanceState

## [0.0.7]

The previous versions of this library contained mostly initial implementation with only few changes
