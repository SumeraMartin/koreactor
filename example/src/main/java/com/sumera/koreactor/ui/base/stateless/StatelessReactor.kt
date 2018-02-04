package com.sumera.koreactor.ui.base.stateless

import com.sumera.koreactor.injection.PerActivity
import com.sumera.koreactorlib.reactor.MviReactor
import com.sumera.koreactorlib.reactor.data.MviAction
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
