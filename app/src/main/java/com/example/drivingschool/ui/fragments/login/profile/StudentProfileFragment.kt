package com.example.drivingschool.ui.fragments.login.profile

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.drivingschool.databinding.ChangePasswordStudentProfileBinding
import com.example.drivingschool.databinding.FragmentStudentProfileBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentStudentProfileBinding

    private val PICK_IMAGE_REQUEST = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pickImageFromGallery()
        showAlertDialog()
        //showDialog()


    }

    private fun showDialog() {
        binding.btnChangePassword.setOnClickListener {
            val dialog = Dialog(requireContext())
            val binding = ChangePasswordStudentProfileBinding.inflate(layoutInflater)
            dialog.setContentView(binding.root)
            dialog.show()
        }

    }


    private fun pickImageFromGallery() {
        binding.imProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            binding.imProfile.setImageURI(data?.data)

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


