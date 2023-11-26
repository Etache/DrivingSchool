package com.example.drivingschool.ui.fragments.enroll.instructorInfo

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentInstructorInfoBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.adapter.InstructorCommentAdapter
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstructorInfoFragment : Fragment() {

    private lateinit var binding: FragmentInstructorInfoBinding
    private lateinit var adapter: InstructorCommentAdapter
    private val viewModel: EnrollViewModel by viewModels()
    private var id: Int? = null
    private lateinit var networkConnection: NetworkConnection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstructorInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkConnection = NetworkConnection(requireContext())
        binding.rvInstructorProfileComments.layoutManager = LinearLayoutManager(context)
        binding.rvInstructorProfileComments.isNestedScrollingEnabled = false
        id = arguments?.getInt(Constants.ID_KEY)
        Log.d("madimadi", "instructor id in fragment: ${id}")

        networkConnection.observe(viewLifecycleOwner) {
            if (it) getInstructorProfile()
        }

        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner) {
                if (it) getInstructorProfile()
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }

        showImage()
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

                        val experience = state.data?.experience
                        if (experience != null) {
                            when (experience) {
                                1 -> {
                                binding.tvExperience.text = context?.getString(R.string.experience_year, experience)
                                }
                                in 1..4 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 22..24 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 32..34 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 42..44 -> {
                                binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 52..54 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 62..64 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 72..74 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 82..84 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 92..94 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_2__4, experience)
                                }
                                in 5..9 -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_5__9, experience)
                                }
                                else -> {
                                    binding.tvExperience.text = context?.getString(R.string.experience_years_5__9, experience)
                                }
                            }
                        }
                        binding.tvNumber.text = state.data?.phoneNumber
                        binding.tvCarName.text = state.data?.car


                        if (state.data?.feedbacks != null) {
                            adapter = InstructorCommentAdapter(state.data?.feedbacks!!)
                            binding.rvInstructorProfileComments.adapter = adapter
                            Log.d(
                                "ololo",
                                "getInstructorDetails in fragment: ${state.data?.feedbacks}"
                            )
                        }


                        val httpsImageUrl = state.data?.profilePhoto?.big?.replace("http://", "https://")
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
                        Toast.makeText(requireContext(), "Error: ${state.msg}", Toast.LENGTH_SHORT)
                            .show()
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