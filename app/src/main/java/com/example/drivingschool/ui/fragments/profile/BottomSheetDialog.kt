package com.example.drivingschool.ui.fragments.profile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.databinding.ChangePasswordBottomSheetBinding
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: ChangePasswordBottomSheetBinding

    private lateinit var viewModel: ProfileViewModel
    private lateinit var networkConnection: NetworkConnection

    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        @Suppress("DEPRECATION")
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
        networkConnection = NetworkConnection(requireContext())
        super.onViewCreated(view, savedInstanceState)
        //changePassword()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =
            BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener { itDialog ->
            val bottomSheetDialog = itDialog as BottomSheetDialog
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
            if (binding.edtOldPassword.text.isNotEmpty() && binding.etNewPassword.text.isNotEmpty() && binding.etConfirmPassword.text.isNotEmpty()) {
                if (binding.edtOldPassword.text.toString() == preferences.password) {
                    if (binding.etNewPassword.text.toString().length >= 6) {
                        if (binding.etNewPassword.text.toString() == binding.etConfirmPassword.text.toString()) {
                            networkConnection.observe(viewLifecycleOwner) {
                                if (it) {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        viewModel.changePassword(
                                            PasswordRequest(
                                                binding.edtOldPassword.text.toString(),
                                                binding.etNewPassword.text.toString()
                                            )
                                        )
                                    }
                                }
                            }
                            preferences.password = binding.etNewPassword.text.toString()
                            showAlertDialog(getString(R.string.password_successfully_changed))
                            dialog?.dismiss()
                        } else {
                            binding.tvError.text = getString(R.string.password_are_not_the_same)
                        }
                    } else {
                        binding.tvError.text =
                            getString(R.string.password_should_include_more_than_6_symbols)
                    }
                } else {
                    binding.tvError.text = getString(R.string.old_password_is_not_correct)
                }
            } else {
                binding.tvError.text = getString(R.string.please_fill_all_the_fields)
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
