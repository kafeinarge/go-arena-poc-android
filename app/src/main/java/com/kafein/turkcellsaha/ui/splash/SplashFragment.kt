package com.kafein.turkcellsaha.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kafein.turkcellsaha.ui.base.BaseFragment
import com.kafein.turkcellsaha.R
import com.kafein.turkcellsaha.data.local.SharedPrefs
import com.kafein.turkcellsaha.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override val layoutRes: Int = R.layout.fragment_splash


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPrefs.init(requireContext())
        Handler(Looper.getMainLooper()).postDelayed({
            val token = SharedPrefs.readToken()
            if (token.isNullOrEmpty()) {
                findNavController().navigate(R.id.login_fragment)
            } else {
                findNavController().navigate(R.id.navigation_home)
            }

        }, 1000)

    }
}