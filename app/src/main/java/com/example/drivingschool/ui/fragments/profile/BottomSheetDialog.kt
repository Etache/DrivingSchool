package com.example.drivingschool.ui.fragments.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.databinding.ChangePasswordBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: ChangePasswordBottomSheetBinding
    private val viewModel: ProfileViewModel by viewModels()

    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ChangePasswordBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changePassword()
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
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun changePassword() {
        //dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        //dialog.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        val etOldPassword = binding.tvOldPassword
        val etNewPassword = binding.tvNewPassword
        val etConfirmPassword = binding.tvConfirmPassword
        val tvError = binding.tvError

        val btnSave = binding.btnSavePassword
        val btnCancel = binding.btnCancel

        btnSave.setOnClickListener {
            if (etOldPassword.text.toString().isNotEmpty() && etNewPassword.text.toString().isNotEmpty() && etConfirmPassword.text.toString().isNotEmpty()) {
                if (etOldPassword.text?.toString() == preferences.password) {
                    if (etNewPassword.text.toString().length >= 6) {
                        if (etNewPassword.text.toString() == etConfirmPassword.text.toString()) {
                            etNewPassword.setBackgroundResource(R.drawable.edit_text_bg)
                            etConfirmPassword.setBackgroundResource(R.drawable.edit_text_bg)
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.changePassword(
                                    PasswordRequest(
                                        etOldPassword.text.toString(),
                                        etNewPassword.text.toString()
                                    )
                                )
                            }
                            preferences.password = etNewPassword.text.toString()
                            showAlertDialog("Пароль успешно изменен")
                            dialog?.dismiss()
                        } else {
                            tvError.text = "Пароли не совпадают"
                        }
                    } else {
                        tvError.text = "Пароль должен содержать не менее 6 символов";
                    }
                } else {
                    tvError.text = "Неверный старый пароль";
                }
            } else {
                tvError.text = "Заполните все поля"
            }
        }
        btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun showAlertDialog(message: String) {
        val alert = AlertDialog.Builder(requireContext())
        alert.setMessage(message)
        alert.setPositiveButton(getString(R.string.ok), null)
        alert.show()
    }
}