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
import androidx.fragment.app.Fragment
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentProfileBinding



class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private val PICK_IMAGE_REQUEST = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pickImageFromGallery()
        showAlertDialog()
        showDialog()
    }

    private fun showDialog() {
        binding.btnChangePassword.setOnClickListener {
//            val dialog = Dialog(requireContext())
//            val binding = ChangePasswordStudentProfileBinding.inflate(layoutInflater)
//            dialog.setContentView(binding.root)
//            dialog.show()
            val adb = AlertDialog.Builder(requireContext())
            val d: Dialog = adb.setView(R.layout.change_password_student_profile).create()
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(d.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            binding.ivProfile.setImageURI(data?.data)

        }

    }


    private fun showAlertDialog() {

        binding.btnExit.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())

            alertDialog.setTitle("Подтвердите выход")
            alertDialog.setNegativeButton("Назад") { alert, _ ->
                alert.cancel()
            }
            alertDialog.setPositiveButton("Подтвердить") { alert, _ ->
                //логика
                alert.cancel()
            }
            alertDialog.create().show()
        }
    }
}


