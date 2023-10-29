package com.example.drivingschool.ui.fragments.instructorProfile

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.drivingschool.R
import com.example.drivingschool.databinding.ChangePasswordBottomSheetBinding
import com.example.drivingschool.databinding.FragmentInstructorProfileBinding

class InstructorProfileFragment : Fragment() {

    private lateinit var binding: FragmentInstructorProfileBinding

    private val PICK_IMAGE_REQUEST = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstructorProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pickImageFromGallery()
        showDialog()
    }

    private fun showDialog() {
        binding.btnChangePassword.setOnClickListener {
            val adb = AlertDialog.Builder(requireContext())
            val d: Dialog = adb.setView(R.layout.change_password_bottom_sheet).create()
            val binding = ChangePasswordBottomSheetBinding.inflate(layoutInflater)
            d.setContentView(binding.root)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(d.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            d.show()
            d.window!!.attributes = lp
        }

    }


    private fun pickImageFromGallery() {
        binding.tvChangePhoto.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Изменить фотографию")
            builder.setItems(arrayOf("Открыть галерею", "Удалить фото")) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    }

                    1 -> {
                        // Обработка нажатия на элемент 2

                    }
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            binding.ivProfile.setImageURI(data?.data)

        }

    }


}