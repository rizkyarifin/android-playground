package com.sample.playground.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sample.playground.utils.ext.showToast

abstract class BaseFragment<B : ViewDataBinding, V : BaseViewModel> : Fragment() {

    var baseActivity: BaseActivity<*, *>? = null
    lateinit var mBinding: B
    lateinit var mViewModel: V

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun getBindingVariable(): Int
    abstract fun getVM(): V
    abstract fun letStart()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>){
            val activity = context
            this.baseActivity = activity
            activity.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mViewModel = if (::mViewModel.isInitialized) mViewModel else getVM()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding.root
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.setVariable(getBindingVariable(), mViewModel)
        mBinding.executePendingBindings()

        mViewModel.apply {
            baseActivity?.let {
                showMessage.observe(it, Observer {
                    if (it != null) {
                        showToast(it)
                    }
                })
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        letStart()
    }


    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }
}