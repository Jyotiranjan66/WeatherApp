package com.example.weathernow.main.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.example.weathernow.helper.util.ToastUtil
import com.example.weathernow.main.base.BaseRepo.ApiResultType.CANCELLED
import com.example.weathernow.main.state.ApiRenderState
import kotlinx.coroutines.launch
import com.example.weathernow.BR
import com.example.weathernow.R

abstract class BaseAct<binding : ViewDataBinding, VM : BaseVM>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity(), View.OnClickListener {

    protected abstract val vm: VM?
    protected lateinit var binding: binding
    protected abstract fun renderState(apiRenderState: ApiRenderState)
    protected abstract val hasProgress: Boolean
    private var progress: ObservableField<Boolean>? = null
    protected abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<binding>(this, layoutId).apply {
            lifecycleOwner = this@BaseAct

            vm?.let {
             //   setVariable(BR.vm, it)

                lifecycleScope.launch {
                    it.state().collect {
                        renderState(it)
                    }
                }

                lifecycleScope.launch {
                    it.apiError.collect {
                        if (it.resultType != CANCELLED) {
                            hideProgress()
                            it.error?.let {
                                errorToast(it)
                            }
                        }
                    }
                }
            }
          //  setVariable(BR.click, this@BaseAct)

            if (hasProgress) {
                progress = ObservableField()
                setVariable(BR.showProgress, progress)
            }
        }

        init()
    }

    fun hideProgress() {
        progress?.set(false)
    }

    fun showProgress() {
        progress?.set(true)
    }

    fun errorToast(message: String, callback: ((Boolean) -> Unit)? = null) {
        ToastUtil.errorSnackBar(message, binding.root, callback)
    }

    fun successToast(message: String, callback: ((Boolean) -> Unit)? = null) {
        ToastUtil.successSnackBar(message, binding.root, callback)
    }

    override fun onClick(p0: View?) {

    }
}