package br.com.superheroes.screen.splash

import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.withDelay
import br.com.superheroes.screen.BasePresenter
import br.com.superheroes.screen.BaseUi
import javax.inject.Inject

@ActivityScope
class SplashPresenter @Inject constructor(
    private val schedulerProvider: SchedulerProvider
) : BasePresenter<BaseUi>() {

    private val splashUi: SplashUi? get() = baseUi()

    override fun onCreate() {
        super.onCreate()
        withDelay(300, schedulerProvider.postWorkerThread()) {
            splashUi?.openHomeScreen()
        }
    }
}
