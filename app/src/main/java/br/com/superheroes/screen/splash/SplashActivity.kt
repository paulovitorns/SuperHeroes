package br.com.superheroes.screen.splash

import br.com.superheroes.screen.BaseActivity
import br.com.superheroes.screen.BaseUi

interface SplashUi : BaseUi {
    fun openHomeScreen()
}

class SplashActivity : BaseActivity<SplashPresenter>(), SplashUi {
    override val layoutRes: Int? = null

    override fun openHomeScreen() {
//        Intent(this, HomeActivity::class.java).also {
//            startActivity(it)
//            finish()
//        }
    }
}
