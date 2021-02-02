package com.turkcell.turkcellsaha.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    var disposables = CompositeDisposable()

    lateinit var binding: DB

    abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onDestroy() {
        if (!disposables.isDisposed) {
            disposables.clear()
        }
        super.onDestroy()
    }
}