package com.example.drivingschool.ui.fragments.enroll.selectInstructor

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.drivingschool.R
import com.example.drivingschool.data.models.InstructorRequest
import com.example.drivingschool.data.models.LoginRequest
import com.example.drivingschool.databinding.FragmentInstructorInfoBinding

class InstructorInfoFragment : Fragment() {

    private lateinit var binding: FragmentInstructorInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructorInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

//    private fun openFullScreenImage() {
//        val instructor: InstructorRequest
//
//        binding.ivProfileImage.setOnClickListener {
//            val dialog = Dialog(requireActivity())
//            dialog.setContentView(binding.root)
//
//            binding.ivProfileImage.setImageResource()
//
//            dialog.show()
//        }
//
////        Glide
////            .with(binding.ivProfileImage.context)
////            .load(instructor.profilePhoto)
////            .circleCrop()
////            .placeholder(R.drawable.default_pfp)
////            .into(binding.ivProfileImage)
//    }
}