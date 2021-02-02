package com.turkcell.turkcellsaha.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.turkcell.turkcellsaha.ui.base.BaseFragment
import com.turkcell.turkcellsaha.R
import com.turkcell.turkcellsaha.data.local.SharedPrefs
import com.turkcell.turkcellsaha.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val layoutRes: Int = R.layout.fragment_login

    private val viewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLogin()

        SharedPrefs.init(requireContext())

        binding.btnLogin.setOnClickListener {
            validateForLogin()
        }
    }

    private fun initLogin() {
        viewModel.loginLiveData_.observe(viewLifecycleOwner, {
            SharedPrefs.saveToken(it.token)
            findNavController().navigate(R.id.navigation_home)
        })

        viewModel.status_.observe(viewLifecycleOwner, {
            if (it.isLoading()) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }

            if (it.shouldShowErrorMessage()) {
                Toast.makeText(requireContext(), it.getErrorMessage(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validateForLogin() {
        if (binding.etUserName.text.isNullOrEmpty()) {
            binding.etUserName.error = getString(R.string.warning_user_name)
        }

        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.etUserName.error = getString(R.string.warning_password)
        }

        if (!binding.etUserName.text.isNullOrEmpty() && !binding.etPassword.text.isNullOrEmpty()) {
            viewModel.login(binding.etUserName.text.toString(), binding.etPassword.text.toString())
        }
    }
}