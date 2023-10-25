package com.example.drivingschool.ui.fragments.profile

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.databinding.FragmentProfileBinding
import com.example.drivingschool.tools.UiState
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel : ProfileViewModel by viewModels()
    private val preferences : PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    private val PICK_IMAGE_REQUEST = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getProfileData()
        pickImageFromGallery()
        showAlertDialog()
        showDialog()
    }

    private fun showDialog() {
        binding.btnChangePassword.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(R.layout.change_password_bottom_sheet)
            val etOldPassword = dialog.findViewById<EditText>(R.id.edtOldPassword)
            val etNewPassword = dialog.findViewById<EditText>(R.id.edtNewPassword)
            val etConfirmPassword = dialog.findViewById<EditText>(R.id.edtConfirmPassword)

            val btnSave = dialog.findViewById<MaterialButton>(R.id.btnSavePassword)
            val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)
            btnSave?.setOnClickListener {
                if(etOldPassword?.text?.isNotEmpty() == true) {
                    if(etNewPassword?.text.toString() == etConfirmPassword?.text.toString()) {
                        //logic
                        Toast.makeText(requireContext(), "password changed", Toast.LENGTH_SHORT).show()
                        dialog.cancel()
                    } else {
                        etNewPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                        etConfirmPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                    }
                } else {
                    etOldPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                }

            }
            btnCancel?.setOnClickListener{
                dialog.cancel()
            }
            dialog.show()

//            val dialog = Dialog(requireContext())
//            val binding = ChangePasswordStudentProfileBinding.inflate(layoutInflater)
//            dialog.setContentView(binding.root)
//            dialog.show()
//            val adb = AlertDialog.Builder(requireContext())
//            val d: Dialog = adb.setView(R.layout.change_password_student_profile).create()
//            val lp = WindowManager.LayoutParams()
//            lp.copyFrom(d.window!!.attributes)
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT
//            d.show()
//            d.window!!.attributes = lp
        }

    }

    private fun pickImageFromGallery() {
        binding.tvChangePhoto.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Изменить фотографию")
            builder.setItems(arrayOf("Выбрать фото", "Удалить фото")) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    }

                    1 -> {
                        binding.ivProfile.setBackgroundResource(R.drawable.ic_default_photo)
                        //delete photo from api also....
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            binding.ivProfile.setImageURI(data?.data)
        }

    }

    private fun showAlertDialog() {
        binding.btnExit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())

            alertDialog.setTitle(getString(R.string.confirm_exit))
            alertDialog.setNegativeButton(getString(R.string.exit)) { alert, _ ->
                alert.cancel()
            }
            alertDialog.setPositiveButton(getString(R.string.confirm)) { alert, _ ->
                preferences.isLoginSuccess = false
                findNavController().navigate(R.id.loginFragment)
                //логика
                alert.cancel()
            }
            alertDialog.create().show()
        }
    }

    private fun getProfileData() {
        viewModel.getProfile()
        lifecycleScope.launch {
            viewModel.profile.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        //TODO
                    }

                    is UiState.Success -> {
                        Glide.with(binding.ivProfile).load(state.data?.profile?.profilePhoto).into(binding.ivProfile)
                        binding.tvName.text = state.data?.profile?.name
                        binding.tvSurname.text = state.data?.profile?.surname
                        binding.tvNumber.text = state.data?.profile?.phoneNumber
                        binding.tvGroup.text = state.data?.group?.name
                    }

                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}


