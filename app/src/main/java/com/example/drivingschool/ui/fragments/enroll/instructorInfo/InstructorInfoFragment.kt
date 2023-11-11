package com.example.drivingschool.ui.fragments.enroll.instructorInfo

import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentInstructorInfoBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.adapter.InstructorCommentAdapter
import com.example.drivingschool.ui.fragments.enroll.adapter.SelectInstructorAdapter
import com.example.drivingschool.ui.fragments.enroll.commentsList
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstructorInfoFragment : Fragment() {

    private lateinit var binding: FragmentInstructorInfoBinding
    private lateinit var adapter: InstructorCommentAdapter
    private val viewModel: EnrollViewModel by viewModels()
    private var id: Int ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructorInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = InstructorCommentAdapter(commentsList)
        binding.rvInstructorProfileComments.adapter = adapter
        binding.rvInstructorProfileComments.layoutManager = LinearLayoutManager(context)

        id = arguments?.getInt(SelectInstructorAdapter.ID_KEY)
        Log.d("madimadi", "instructor id in fragment: ${id}")

        getInstructorProfile()
        showImage()

        adapter.notifyDataSetChanged()
    }


    private fun getInstructorProfile() {
        viewModel.getInstructorById(id = id!!)
        lifecycleScope.launch {
            viewModel.instructorDetails.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.clContainer.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.clContainer.visibility = View.VISIBLE

                        binding.tvName.text = state.data?.name
                        binding.tvSurname.text = state.data?.surname
                        binding.tvExpienceNum.text = state.data?.experience.toString()
                        binding.tvNumber.text = state.data?.phoneNumber
                        binding.tvCarName.text = state.data?.car

                        val httpsImageUrl = state.data?.profilePhoto  //replace("http://", "https://")
                        Picasso.get()
                            .load(httpsImageUrl)
                            .placeholder(R.drawable.ic_default_photo)
                            .into(binding.ivProfileImage)

                        Log.d("madimadi", "getInstructorDetails in fragment: ${state.data}")
                    }

                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), "Error: ${state.msg}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showImage() {
        binding.ivProfileImage.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.show_photo_profile)
            val image = dialog.findViewById<ImageView>(R.id.image)
            if (binding.ivProfileImage.drawable != null) {
                image.setImageBitmap((binding.ivProfileImage.drawable as BitmapDrawable).bitmap) //crash
            } else {
                image.setImageResource(R.drawable.ic_default_photo)
            }
            dialog.window?.setBackgroundDrawableResource(R.drawable.ic_default_photo)
            dialog.show()
        }
    }
}