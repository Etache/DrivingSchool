package com.example.drivingschool.ui.fragments.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
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

        activateViews()
        binding.btnLogin.setOnClickListener {
            setLogin()
        }
    }

    private fun activateViews() {
        with(binding) {
            etLogin.addTextChangedListener(loginTextWatcher(etLogin))
            etPassword.addTextChangedListener(loginTextWatcher(etPassword))
        }
    }

    private fun loginTextWatcher(editText: EditText) = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            //TODO: implement later
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (editText.text.isEmpty()) {
                editText.setBackgroundResource(R.drawable.edit_text_bg)
                editText.setBackgroundResource(R.drawable.edit_text_bg)
            } else {
                editText.setBackgroundResource(R.drawable.edit_text_active_bg)
                editText.setBackgroundResource(R.drawable.edit_text_active_bg)
            }
            val user_login = binding.etLogin.text.toString().trim()
            val user_password = binding.etPassword.text.toString().trim()

            if (user_login.isNotEmpty() && user_password.isNotEmpty()) {
                binding.btnLogin.setBackgroundResource(R.drawable.button_active_bg)
            } else {
                binding.btnLogin.setBackgroundResource(R.drawable.button_bg)
            }
        }

        override fun afterTextChanged(s: Editable?) {
            //TODO: implement later
        }

    }

    private fun saveToken(username: String?, password: String?) {
        viewModel.getToken(LoginRequest(username, password)).observe(requireActivity()) { token ->
            preferences.accessToken = token
            Log.d("madimadi", "accessToken: $token")
        }
    }

    private fun setLogin() {
        if (binding.etLogin.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()) {
            if (binding.etLogin.text.toString() == "student" && binding.etPassword.text.toString() == "trewq321") {
                saveToken(binding.etLogin.text.toString(), binding.etPassword.text.toString())
                preferences.isLoginSuccess = true
                findNavController().navigate(R.id.mainFragment)
            } else {
                binding.tvError.setText(R.string.login_error_text)
            }
        }
    }
}