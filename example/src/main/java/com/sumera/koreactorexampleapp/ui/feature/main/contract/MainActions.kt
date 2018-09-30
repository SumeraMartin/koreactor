package com.sumera.koreactorexampleapp.ui.feature.main.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class MainActions : MviAction<MainState>

object OnInfinityButtonClickAction :  MainActions()

object OnToDoButtonClickAction : MainActions()

object OnCounterButtonCLickAction : MainActions()

object OnSimpleLoadingButtonClickAction : MainActions()

object OnTimerButtonClickAction : MainActions()

object OnToastsButtonClickAction : MainActions()
