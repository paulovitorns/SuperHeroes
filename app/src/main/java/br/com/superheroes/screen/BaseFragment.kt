package br.com.superheroes.screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment<T : BasePresenter<BaseUi>> : Fragment(), BaseUi {

    abstract val layoutRes: Int

    @Inject
    lateinit var presenter: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setUi(this)
        setupBundle(savedInstanceState)
        setupViews()
        presenter.onCreate()
    }

    open fun setupBundle(savedInstanceState: Bundle?) {}

    open fun setupViews() {}

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveState()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
