package com.sumera.koreactor.ui.feature.main.contract

import com.sumera.koreactor.lib.reactor.data.MviAction

sealed class MainActions : MviAction<MainState>

object OnInfinityButtonClickAction :  MainActions()

object OnToDoButtonClickAction : MainActions()

object OnCounterButtonCLickAction : MainActions()
