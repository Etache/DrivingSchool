package com.example.drivingschool.ui.fragments.enroll.student

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.Dates
import com.example.drivingschool.databinding.FragmentSelectInstructorBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.student.adapter.SelectInstructorAdapter
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SelectInstructorFragment :
    BaseFragment<FragmentSelectInstructorBinding, EnrollViewModel>() {
    override fun getViewBinding(): FragmentSelectInstructorBinding = FragmentSelectInstructorBinding.inflate(layoutInflater)

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

    private fun onClick(workWindows: Dates, name: String, id: String) {
        val bundle = Bundle()
        bundle.putString(Constants.FULL_NAME, name)
        bundle.putString(Constants.INSTRUCTOR_ID_ENROLL, id)
        bundle.putParcelable(Constants.WORK_WINDOWS, workWindows)
        findNavController().navigate(R.id.selectDateTimeFragment, bundle)
    }

}