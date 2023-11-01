package com.example.drivingschool.ui.fragments.profile.studentProfile

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.drivingschool.R
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.data.models.PasswordRequest
import com.example.drivingschool.databinding.FragmentInstructorProfileBinding
import com.example.drivingschool.databinding.FragmentProfileBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.profile.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var bindingInstructor: FragmentInstructorProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val preferences: PreferencesHelper by lazy {
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
        if(preferences.role == "instructor") {
            findNavController().navigate(R.id.instructorProfileFragment)
        } else {
            getProfileData()
            showImage()
            pickImageFromGallery()
            changePassword()
            logout()
        }
    }

    private fun showImage() {
        binding.ivProfile.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.show_photo_profile)
            val image = dialog.findViewById<ImageView>(R.id.image)
            if (binding.ivProfile.drawable != null) {
                image.setImageBitmap((binding.ivProfile.drawable as BitmapDrawable).bitmap)
            } else {
                image.setImageResource(R.drawable.ic_default_photo)
            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.ic_default_photo)
            dialog.show()
        }
    }

    private fun changePassword() {
        binding.btnChangePassword.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(R.layout.change_password_bottom_sheet)
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

            val etOldPassword = dialog.findViewById<EditText>(R.id.edtOldPassword)
            val etNewPassword = dialog.findViewById<EditText>(R.id.edtNewPassword)
            val etConfirmPassword = dialog.findViewById<EditText>(R.id.edtConfirmPassword)

            val btnSave = dialog.findViewById<MaterialButton>(R.id.btnSavePassword)
            val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)

            val tvError = dialog.findViewById<TextView>(R.id.tv_error)

            btnSave?.setOnClickListener {
                if (etOldPassword?.text?.toString() == preferences.password) {
                    etOldPassword?.setBackgroundResource(R.drawable.edit_text_bg)
                    //if(etNewPassword?.text.toString().length >= 6)
                    if (etNewPassword?.text.toString() == etConfirmPassword?.text.toString()) {
                        etNewPassword?.setBackgroundResource(R.drawable.edit_text_bg)
                        etConfirmPassword?.setBackgroundResource(R.drawable.edit_text_bg)
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.changePassword(
                                PasswordRequest(
                                    etOldPassword?.text.toString(),
                                    etNewPassword?.text.toString()
                                )
                            )
                        }
                        preferences.password = etNewPassword?.text.toString()
                        Toast.makeText(requireContext(), "пароль изменен", Toast.LENGTH_SHORT)
                            .show()
                        dialog.cancel()
                    } else {
                        etNewPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                        etConfirmPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                    }
                } else {
                    tvError?.text = "Неверный старый пароль"
                }

            }
            btnCancel?.setOnClickListener {
                dialog.cancel()
            }
            dialog.show()
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
            if (preferences.role == "student") {
                binding.ivProfile.setImageURI(data?.data)
            } else {
                bindingInstructor.ivProfile.setImageURI(data?.data)
            }
        }

    }

    private fun logout() {
        binding.btnExit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())

            alertDialog.setTitle(getString(R.string.confirm_exit))
            alertDialog.setNegativeButton(getString(R.string.exit)) { alert, _ ->
                alert.cancel()
            }
            alertDialog.setPositiveButton(getString(R.string.confirm)) { alert, _ ->
                preferences.isLoginSuccess = false
                preferences.accessToken = null
                findNavController().navigate(R.id.loginFragment)
                alert.cancel()
            }
            alertDialog.create().show()
        }
    }

    private fun getProfileData() {
        viewModel.getProfile()
        lifecycleScope.launch {
            viewModel.profile.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainContainer.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE
                        Glide.with(binding.ivProfile).load(state.data?.profilePhoto)
                            .into(binding.ivProfile)
                        binding.tvName.text = state.data?.name
                        binding.tvSurname.text = state.data?.surname
                        binding.tvNumber.text = state.data?.phoneNumber
                        binding.tvGroup.text = state.data?.group?.name
                        Log.d("madimadi", "getProfileData in Fragment: ${state.data}")
                        Log.d("madimadi", "token in Fragment: ${preferences.accessToken}")
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


