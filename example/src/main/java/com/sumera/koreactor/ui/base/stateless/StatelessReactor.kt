package com.sumera.koreactor.ui.base.stateless

import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class StatelessReactor @Inject constructor(): MviReactor<EmptyState>() {

    override fun createInitialState(): EmptyState {
        return EmptyState
    }

    override fun bind(actions: Observable<MviAction<EmptyState>>) {
       // Nothing
    }
}
