package com.example.drivingschool.ui.fragments.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.databinding.ChangePasswordBottomSheetBinding
import com.example.drivingschool.ui.activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: ChangePasswordBottomSheetBinding

    //private val viewModel: ProfileViewModel by viewModels()
    private lateinit var viewModel: ProfileViewModel

    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChangePasswordBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //changePassword()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
            com.google.android.material.bottomsheet.BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
            changePassword()
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun changePassword() {
        binding.btnSavePassword.setOnClickListener {
            if (binding.edtOldPassword.text.isNotEmpty() && binding.edtNewPassword.text.isNotEmpty() && binding.edtConfirmPassword.text.isNotEmpty()) {
                if (binding.edtOldPassword.text.toString() == preferences.password) {
                    if (binding.edtNewPassword.text.toString().length >= 6) {
                        if (binding.edtNewPassword.text.toString() == binding.edtConfirmPassword.text.toString()) {
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.changePassword(
                                    PasswordRequest(
                                        binding.edtOldPassword.text.toString(),
                                        binding.edtNewPassword.text.toString()
                                    )
                                )
                            }
                            preferences.password = binding.edtNewPassword.text.toString()
                            showAlertDialog("Пароль успешно изменен")
                            dialog?.dismiss()
                        } else {
                            binding.tvError.text = "Пароли не совпадают"
                        }
                    } else {
                        binding.tvError.text = "Пароль должен содержать не менее 6 символов"
                    }
                } else {
                    binding.tvError.text = "Неверный старый пароль";
                }
            } else {
                binding.tvError.text = "Заполните все поля"
            }
        }
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun showAlertDialog(message: String) {
        val alert = AlertDialog.Builder(requireContext())
        alert.setMessage(message)
        alert.setPositiveButton("ok", null)
        alert.show()
    }
}
