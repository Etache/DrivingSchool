package com.example.drivingschool.ui.fragments.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            setLogin()
        }

        activateViews()
    }

    private fun activateViews() {
        if(binding.etLogin.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()) {
            binding.btnLogin.setBackgroundColor(resources.getColor(R.color.blue))
        } else {
            binding.btnLogin.setBackgroundColor(resources.getColor(R.color.gray_btn))
        }
    }

    private fun saveToken(username: String?, password: String?) {
        viewModel.getToken(LoginRequest(username, password)).observe(requireActivity()) { token ->
            preferences.accessToken = token
            Log.d("madimadi", "accessToken: $token")
        }
    }

    private fun setLogin() {
        if (binding.etLogin.text.toString() == "student" && binding.etPassword.text.toString() == "trewq321") {
            saveToken(binding.etLogin.text.toString(), binding.etPassword.text.toString())
            preferences.isLoginSuccess = true
            findNavController().navigate(R.id.mainFragment)
        } else {
            binding.etLogin.setBackgroundResource(R.drawable.edit_text_error_bg)
            binding.etPassword.setBackgroundResource(R.drawable.edit_text_error_bg)
            binding.tvError.visibility = View.VISIBLE
        }
    }
}