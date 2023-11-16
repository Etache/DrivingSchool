package com.example.drivingschool.ui.fragments.profile.instructorProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.databinding.FragmentInstructorProfileBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.activity.MainActivity
import com.example.drivingschool.ui.fragments.profile.ProfileViewModel
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class InstructorProfileFragment :
    BaseFragment<FragmentInstructorProfileBinding, ProfileViewModel>(R.layout.fragment_instructor_profile) {

    override val binding: FragmentInstructorProfileBinding by viewBinding(
        FragmentInstructorProfileBinding::bind
    )
    override val viewModel: ProfileViewModel by viewModels()

    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    override fun initialize() {
        getInstructorProfileData()
        showImage()
        pickImageFromGallery()
        changePasswordInstructor()
        logoutInstructor()
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getInstructorProfile()
            binding.swipeRefresh.isRefreshing = false
        }
    }


    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                uploadImage(imageUri)
                viewModel.getInstructorProfile()
                viewModel.instructorProfile.observe(requireActivity()) { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Picasso.get().load(state.data?.profilePhoto?.small).memoryPolicy(
                                MemoryPolicy.NO_CACHE
                            ).networkPolicy(NetworkPolicy.NO_CACHE).into(binding.ivProfile)

                        }

                        else -> {}
                    }
                }
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

    private fun changePasswordInstructor() {
        binding.btnChangePassword.setOnClickListener {
            val fragment = com.example.drivingschool.ui.fragments.profile.BottomSheetDialog()
            fragment.show(parentFragmentManager, "TAG")
        }
    }

    private fun pickImageFromGallery() {
        binding.tvChangePhoto.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.change_photo))
            builder.setItems(
                arrayOf(
                    getString(R.string.choose_photo),
                    getString(R.string.delete_photo)
                )
            ) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageResult.launch(intent)
                    }

                    1 -> {
                        binding.ivProfile.setImageDrawable(null)
                        viewModel.deleteProfilePhoto()
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun uploadImage(imageUri: Uri?) {
        imageUri?.let { uri ->
            val parcelFileDescriptor =
                requireContext().contentResolver.openFileDescriptor(uri, "r", null) ?: return
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file =
                File(requireContext().cacheDir, requireContext().contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
            viewModel.updateProfilePhoto(multipartBody)
        }
    }

    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(uri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            it.close()
        }
        return name
    }

    private fun logoutInstructor() {
        binding.btnExit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())

            alertDialog.setTitle(getString(R.string.confirm_exit))
            alertDialog.setNegativeButton(getString(R.string.cancel)) { alert, _ ->
                alert.cancel()
            }
            alertDialog.setPositiveButton(getString(R.string.confirm)) { alert, _ ->
                preferences.isLoginSuccess = false
                preferences.accessToken = null
                preferences.refreshToken = null
                preferences.password = null
                preferences.role = null
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("isLoggedOut", true)
                activity?.startActivity(intent)
                alert.cancel()
            }
            alertDialog.create().show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getInstructorProfileData() {
        viewModel.getInstructorProfile()
        lifecycleScope.launch {
            viewModel.instructorProfile.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainContainer.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE
                        Picasso.get().load(state.data?.profilePhoto?.small).into(binding.ivProfile)
                        binding.tvName.text = state.data?.name
                        binding.tvSurname.text = state.data?.surname
                        binding.tvNumber.text = state.data?.phoneNumber

                        val experience = state.data?.experience
                        if (experience != null) {
                            when (experience) {
                                in 1..4 -> {
                                    binding.tvExperience.text = "$experience года"
                                }

                                in 5..9 -> {
                                    binding.tvExperience.text = "$experience лет"
                                }

                                else -> {
                                    binding.tvExperience.text = "$experience лет"
                                }
                            }
                        }

                        binding.tvCar.text = state.data?.car
                        Log.d("madimadi", "getInstructorProfileData in Fragment: ${state.data}")
                        Log.d("madimadi", "tokenInstructor in Fragment: ${preferences.accessToken}")
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