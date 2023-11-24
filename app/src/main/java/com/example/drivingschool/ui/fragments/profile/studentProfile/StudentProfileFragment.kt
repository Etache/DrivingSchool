package com.example.drivingschool.ui.fragments.profile.studentProfile

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
import android.system.Os.bind
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.local.sharedpreferences.PreferencesHelper
import com.example.drivingschool.databinding.FragmentInstructorProfileBinding
import com.example.drivingschool.databinding.FragmentStudentProfileBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.activity.MainActivity
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
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
import javax.inject.Inject

@AndroidEntryPoint
class StudentProfileFragment :
    BaseFragment<FragmentStudentProfileBinding, ProfileViewModel>(R.layout.fragment_student_profile) {
    //override fun getViewBinding() = FragmentStudentProfileBinding.inflate(layoutInflater)
    override val binding by viewBinding(FragmentStudentProfileBinding::bind)
    override val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var preferences: PreferencesHelper

    @Inject
    lateinit var networkConnection: NetworkConnection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        getProfileData()
        zoomImage()
        pickImageFromGallery()
        changePassword()
        logout()

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                uploadImage(imageUri)
                networkConnection.observe(viewLifecycleOwner) {
                    if (it) viewModel.getProfile()
                }
                viewModel.profile.observe(requireActivity()) { state ->
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

    private fun zoomImage() {
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
            val fragment = com.example.drivingschool.ui.fragments.profile.BottomSheetDialog()
            fragment.show(parentFragmentManager, getString(R.string.tag))
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
            ) { _, which ->
                when (which) {
                    0 -> {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageResult.launch(intent)
                    }

                    1 -> {
                        networkConnection.observe(viewLifecycleOwner) {
                            if (it) viewModel.deleteProfilePhoto()
                        }
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
                requireContext().contentResolver.openFileDescriptor(
                    uri,
                    getString(R.string.r),
                    null
                ) ?: return
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file =
                File(requireContext().cacheDir, requireContext().contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            val requestBody =
                file.asRequestBody(getString(R.string.image_request_body).toMediaTypeOrNull())
            val multipartBody =
                MultipartBody.Part.createFormData(getString(R.string.image), file.name, requestBody)
            networkConnection.observe(viewLifecycleOwner) {
                if (it) viewModel.updateProfilePhoto(multipartBody)
            }

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

    private fun logout() {
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
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(Constants.INTENT_IS_LOGGED_OUT, true)
                activity?.startActivity(intent)
                alert.cancel()
            }
            alertDialog.create().show()
        }
    }

    private fun getProfileData() {
        networkConnection.observe(viewLifecycleOwner) {
            if (it) viewModel.getProfile()
        }
        lifecycleScope.launch {
            viewModel.profile.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.viewVisibility(true)
                        binding.mainContainer.viewVisibility(false)
                    }

                    is UiState.Success -> {
                        binding.progressBar.viewVisibility(false)
                        binding.mainContainer.viewVisibility(true)
                        Picasso.get().load(state.data?.profilePhoto?.big) //changed to big
                            .into(binding.ivProfile)
                        binding.tvName.text = state.data?.name
                        binding.tvSurname.text = state.data?.surname
                        binding.tvLastname.text = state.data?.lastname
                        binding.tvNumber.text = state.data?.phoneNumber
                        binding.tvGroup.text = state.data?.group?.name
                        binding.tvAveragePoint.text = "Средний балл: ${state.data?.rate}"
                        binding.tvNumberOfLessons.text = state.data?.numberOfLessons
                        Log.d("madimadi", "getStudentProfileData in Fragment: ${state.data}")
                    }

                    is UiState.Empty -> {
                        showToast(getString(R.string.empty_state))
                    }

                    is UiState.Error -> {
                        showToast(state.msg.toString())
                    }

                    else -> {
                        //todo
                    }
                }
            }
        }
    }

}


