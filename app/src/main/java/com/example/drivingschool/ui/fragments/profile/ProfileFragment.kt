package com.example.drivingschool.ui.fragments.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var bindingInstructor: FragmentInstructorProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val preferences: PreferencesHelper by lazy {
        PreferencesHelper(requireContext())
    }

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data

 //               viewModel.getProfile()
                lifecycleScope.launch {
                    viewModel.profile.observe(requireActivity()) { state ->
                        when (state) {
                            is UiState.Success -> {
                                Glide.with(binding.ivProfile).load(imageUri)
                                    .into(binding.ivProfile)
                                uploadImage(imageUri)
                            }

                            else -> {}
                        }
                        binding.ivProfile.setImageURI(imageUri)
                    }
                }
            }
        }

    private val PICK_IMAGE_REQUEST = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return if (preferences.role == "student") {
            binding = FragmentProfileBinding.inflate(layoutInflater)
            binding.root
        } else {
            bindingInstructor = FragmentInstructorProfileBinding.inflate(layoutInflater)
            bindingInstructor.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (preferences.role == "student") {
            getProfileData()
            showImage()
            pickImageFromGaller()
            changePassword()
            logout()
        } else {
            getInstructorProfileData()
            pickImageFromGalleryInstructor()
            changePasswordInstructor()
            logoutInstructor()
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
            val etOldPassword = dialog.findViewById<EditText>(R.id.edtOldPassword)
            val etNewPassword = dialog.findViewById<EditText>(R.id.edtNewPassword)
            val etConfirmPassword = dialog.findViewById<EditText>(R.id.edtConfirmPassword)

            val btnSave = dialog.findViewById<MaterialButton>(R.id.btnSavePassword)
            val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)
            btnSave?.setOnClickListener {
                if (etOldPassword?.text?.toString() == preferences.password) {
                    if (etNewPassword?.text.toString() == etConfirmPassword?.text.toString() && etNewPassword?.text.toString().length > 8) {
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
                        if (etOldPassword?.text?.isNotEmpty() == true) {
                            if (etNewPassword?.text.toString() == etConfirmPassword?.text.toString()) {
                                //logic
                                Toast.makeText(
                                    requireContext(),
                                    "password changed",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                dialog.cancel()
                            } else {
                                etNewPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                                etConfirmPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                            }
                        } else {
                            etOldPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
                        }

                    }
                    btnCancel?.setOnClickListener {
                        dialog.cancel()
                    }
                    dialog.show()
                }
            }
        }
    }

    private fun changePasswordInstructor() {
        bindingInstructor.btnChangePassword.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(R.layout.change_password_bottom_sheet)
            val etOldPassword = dialog.findViewById<EditText>(R.id.edtOldPassword)
            val etNewPassword = dialog.findViewById<EditText>(R.id.edtNewPassword)
            val etConfirmPassword = dialog.findViewById<EditText>(R.id.edtConfirmPassword)

            val btnSave = dialog.findViewById<MaterialButton>(R.id.btnSavePassword)
            val btnCancel = dialog.findViewById<MaterialButton>(R.id.btnCancel)
            btnSave?.setOnClickListener {
                if (etOldPassword?.text?.toString() == preferences.password) {
                    if (etNewPassword?.text.toString() == etConfirmPassword?.text.toString() && etNewPassword?.text.toString().length > 8) {
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
                    etOldPassword?.setBackgroundResource(R.drawable.bg_et_change_password_error)
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

                        viewModel.deleteProfilePhoto()
                        //binding.ivProfile.setBackgroundResource(R.drawable.ic_default_photo)
                        //delete photo from api also....
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun pickImageFromGalleryInstructor() {
        bindingInstructor.tvChangePhoto.setOnClickListener {
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
                        bindingInstructor.ivProfile.setBackgroundResource(R.drawable.ic_default_photo)
                        //delete photo from api also....
                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }



    fun pickImageFromGaller() {

        binding.tvChangePhoto.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Изменить фотографию")
            builder.setItems(arrayOf("Выбрать фото", "Удалить фото")) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageResult.launch(intent)
                    }

                    1 -> {

                        viewModel.deleteProfilePhoto()
                        //binding.ivProfile.setBackgroundResource(R.drawable.ic_default_photo)
                        //delete photo from api also....
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
            val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)

            binding.ivProfile.setImageURI(imageUri)
            viewModel.uploadImage(multipartBody)
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            if (preferences.role == "student") {
//
//                val selectedImageUri = data?.data
//                binding.ivProfile.setImageURI(selectedImageUri)
//
//
//                   // val filePath = selectedImageUri?.let { getPathFromUri(it) }
////                    val filePath = getPathFromUri(requireContext(), selectedImageUri!!)
////                    val file = File(filePath!!)
//
//
//                viewModel.updateProfilePhoto(selectedImageUri)
//
//                viewModel.getProfile()
//                lifecycleScope.launch {
//                    viewModel.profile.observe(requireActivity()) { state ->
//                        when (state) {
//                            is UiState.Success -> {
//                                Glide.with(binding.ivProfile).load(data?.data)
//                                    .into(binding.ivProfile)
//                            }
//
//                            else -> {}
//                        }
     //               }
  //              }
//            } else {
//                bindingInstructor.ivProfile.setImageURI(data?.data)
//            }
//        }
//    }


//    @SuppressLint("Range")
//    fun getPathFromUri(uri1: Context, uri: Uri): String {
//        var cursor = requireContext().contentResolver.query(uri, null, null, null, null)
//        cursor?.moveToFirst()
//        var documentId = cursor?.getString(0)
//        documentId = documentId?.substring(documentId.lastIndexOf(":") + 1)
//        cursor?.close()
//
//        cursor = requireContext().contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(documentId), null
//        )
//        cursor?.moveToFirst()
//        val path = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
//        cursor?.close()
//
//        return path ?: ""
//    }

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

    private fun logoutInstructor() {
        bindingInstructor.btnExit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())

            alertDialog.setTitle(getString(R.string.confirm_exit))
            alertDialog.setNegativeButton(getString(R.string.exit)) { alert, _ ->
                alert.cancel()
            }
            alertDialog.setPositiveButton(getString(R.string.confirm)) { alert, _ ->
                preferences.isLoginSuccess = false
                preferences.accessToken = null
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

    private fun getInstructorProfileData() {
        viewModel.getInstructorProfile()
        lifecycleScope.launch {
            viewModel.instructorProfile.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Loading -> {
                        bindingInstructor.progressBar.visibility = View.VISIBLE
                        bindingInstructor.mainContainer.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        bindingInstructor.progressBar.visibility = View.GONE
                        bindingInstructor.mainContainer.visibility = View.VISIBLE
                        Glide.with(bindingInstructor.ivProfile).load(state.data?.profilePhoto)
                            .into(bindingInstructor.ivProfile)
                        bindingInstructor.tvName.text = state.data?.name
                        bindingInstructor.tvSurname.text = state.data?.surname
                        bindingInstructor.tvNumber.text = state.data?.phoneNumber
                        bindingInstructor.tvExperience.text = state.data?.experience.toString()
                        bindingInstructor.tvCar.text = state.data?.car
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


