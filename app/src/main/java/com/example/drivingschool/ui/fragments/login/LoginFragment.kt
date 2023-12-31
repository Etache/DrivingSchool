package com.example.drivingschool.ui.fragments.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.login.LoginRequest
import com.example.drivingschool.databinding.FragmentLoginBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.activity.CheckRoleCallBack
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override val viewModel: LoginViewModel by viewModels()
    private var callback: CheckRoleCallBack? = null

    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }
    private lateinit var networkConnection: NetworkConnection

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CheckRoleCallBack) {
            callback = context
        } else {
            //todo
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        networkConnection = NetworkConnection(requireContext())
        super.onViewCreated(view, savedInstanceState)

        activateViews()
        binding.btnLogin.setOnClickListener {
            context?.let { it1 -> hideKeyboard(context = it1, view) }
            setLogin()
        }

        binding.etPassword.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                binding.btnLogin.performClick()
                return@setOnKeyListener true
            }
            false
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        hidePasswordIcon()
    }

    private fun hidePasswordIcon() {
        with(binding) {
            val showPasswordIcon = R.drawable.ic_password_eye_on
            val hidePasswordIcon = R.drawable.ic_password_eye_off
            tilPassword.setEndIconDrawable(hidePasswordIcon)
            binding.tilPassword.isEndIconVisible = false
            tilPassword.setEndIconOnClickListener {
                etPassword?.let {
                    val selection = it.selectionEnd
                    if (it.transformationMethod is PasswordTransformationMethod) {
                        it.transformationMethod = null
                        tilPassword.setEndIconDrawable(showPasswordIcon)
                    } else {
                        // Переключаемся на скрытие пароля
                        it.transformationMethod = PasswordTransformationMethod.getInstance()
                        tilPassword.setEndIconDrawable(hidePasswordIcon)
                    }
                    it.setSelection(selection)
                }
            }
        }
    }

    private fun hideKeyboard(context: Context, view: View?) {
        val hide = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun setLogin() {
        if (binding.etLogin.text.isNotEmpty() && binding.etPassword.text.isNotEmpty()) {
            saveToken(binding.etLogin.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun saveToken(username: String, password: String) {
        lifecycleScope.launch {
            networkConnection.observe(viewLifecycleOwner) {
                if (it) viewModel.getToken(LoginRequest(username, password))
            }
            viewModel.token.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Loading -> {
                        closeViews()
                    }

                    is UiState.Success -> {
                        preferences.accessToken = state.data?.accessToken
                        preferences.refreshToken = state.data?.refreshToken
                        preferences.role = state.data?.role
                        if (preferences.accessToken != null) {
                            preferences.isLoginSuccess = true
                            preferences.password = binding.etPassword.text.toString()
                            callback?.checkRole()
                            if (preferences.role == "instructor") {
                                findNavController().navigate(R.id.instructorMainFragment)
                            } else if (preferences.role == "student") {
                                findNavController().navigate(R.id.mainFragment)
                            }
                        }
                    }

                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        openViews()
                        binding.tvError.setText(R.string.login_error_text)
                    }
                }
            }
        }

    }

    private fun activateViews() {
        with(binding) {
            etLogin.addTextChangedListener(loginTextWatcher(etLogin))
            etPassword.addTextChangedListener(loginTextWatcherPassword(etPassword))
        }
    }

    private fun loginTextWatcher(editText: EditText) = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (editText.text.isEmpty()) {
                editText.setBackgroundResource(R.drawable.edit_text_bg)
            } else {
                editText.setBackgroundResource(R.drawable.edit_text_active_bg)
            }
            val userLogin = binding.etLogin.text.toString().trim()
            val userPassword = binding.etPassword.text.toString().trim()

            if (userLogin.isNotEmpty() && userPassword.isNotEmpty()) {
                binding.btnLogin.setBackgroundResource(R.drawable.button_active_bg)
                binding.btnLogin.isEnabled = true
            } else {
                binding.btnLogin.setBackgroundResource(R.drawable.button_bg)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }
    private fun loginTextWatcherPassword(editText: EditText) = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.tilPassword.isEndIconVisible = true
            if (editText.text.isEmpty()) {
                binding.tilPassword.isEndIconVisible = false
                editText.setBackgroundResource(R.drawable.edit_text_bg)
            } else {
                binding.tilPassword.isEndIconVisible = true
                editText.setBackgroundResource(R.drawable.edit_text_active_bg)
            }
            val userLogin = binding.etLogin.text.toString().trim()
            val userPassword = binding.etPassword.text.toString().trim()

            if (userLogin.isNotEmpty() && userPassword.isNotEmpty()) {
                binding.btnLogin.setBackgroundResource(R.drawable.button_active_bg)
                binding.btnLogin.isEnabled = true
            } else {
                binding.btnLogin.setBackgroundResource(R.drawable.button_bg)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    private fun closeViews() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            ivLogo.visibility = View.GONE
            tvTitle.visibility = View.GONE
            tvError.visibility = View.GONE
            llEditTexts.visibility = ViewGroup.GONE
            btnLogin.visibility = View.GONE
        }
    }

    private fun openViews() {
        with(binding) {
            progressBar.visibility = View.GONE
            ivLogo.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            llEditTexts.visibility = ViewGroup.VISIBLE
            btnLogin.visibility = View.VISIBLE
        }
    }

}