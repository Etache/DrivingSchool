package com.example.drivingschool.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {

    abstract val binding: VB
    abstract val viewModel: VM

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = getViewBinding()
//        return binding.root
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
        setupRequests()
        setupSubscribes()
    }

    //abstract fun getViewBinding(): VB

    protected open fun initialize() {
    }

    protected open fun setupListeners() {
    }

    protected open fun setupRequests() {
    }

    protected open fun setupSubscribes() {
    }

}