package com.example.drivingschool.ui.fragments.enroll.selectInstructor

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.Date
import com.example.drivingschool.databinding.FragmentSelectInstructorBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.adapter.SelectInstructorAdapter
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectInstructorFragment :
    BaseFragment<FragmentSelectInstructorBinding, EnrollViewModel>(R.layout.fragment_select_instructor) {

    override val binding by viewBinding(FragmentSelectInstructorBinding::bind)
    override val viewModel: EnrollViewModel by viewModels()

    @Inject
    lateinit var networkConnection: NetworkConnection

    private lateinit var adapter: SelectInstructorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SelectInstructorAdapter(this::onClick)
        networkConnection = NetworkConnection(requireContext())

        networkConnection.observe(viewLifecycleOwner) {
            if (it) getInstructorsList()
        }

        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner) {
                if (it) getInstructorsList()
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun getInstructorsList() {
        lifecycleScope.launch {
            viewModel.getInstructors()
            viewModel.instructors.observe(requireActivity()) { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding.progressBar.viewVisibility(true)
                        binding.clContainer.viewVisibility(false)
                    }

                    is UiState.Success -> {
                        binding.progressBar.viewVisibility(false)
                        binding.clContainer.viewVisibility(true)
                        val instructors = uiState.data
                        adapter.updateList(instructors ?: emptyList())
                        binding.rvInstructorList.adapter = adapter
                    }

                    is UiState.Error -> {
                        showToast(uiState.msg.toString())
                    }

                    is UiState.Empty -> {
                        showToast(getString(R.string.empty_state))
                    }
                }

            }
        }
    }

    private fun onClick(workWindows: ArrayList<Date>, name: String, id: String) {
        val bundle = Bundle()
        bundle.putString(Constants.FULL_NAME, name)
        bundle.putString(Constants.INSTRUCTOR_ID_ENROLL, id)
        bundle.putSerializable(Constants.WORK_WINDOWS, workWindows)
        findNavController().navigate(R.id.selectDateTimeFragment, bundle)
    }
}