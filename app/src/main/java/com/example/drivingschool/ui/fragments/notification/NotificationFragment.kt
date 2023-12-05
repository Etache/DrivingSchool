package com.example.drivingschool.ui.fragments.notification

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentNotificationBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding, NotificationViewModel>() {
    override fun getViewBinding(): FragmentNotificationBinding =
        FragmentNotificationBinding.inflate(layoutInflater)

    override val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter

    override fun initialize() {
        getNotifications()
    }

    private fun getNotifications() {
        lifecycleScope.launch {
            viewModel.getNotifications()
            viewModel.notifications.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainContainer.visibility = View.GONE
                        binding.noNotification.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        val sortedNewNotifications =
                            state.data?.notifications?.sortedWith(compareByDescending { it.createdAt })

                        if (state.data != null && sortedNewNotifications != null) {
                            binding.progressBar.visibility = View.GONE
                            binding.mainContainer.visibility = View.VISIBLE
                            binding.noNotification.visibility = View.GONE
                            adapter = NotificationAdapter(sortedNewNotifications)
                            adapter.notifyDataSetChanged()
                            binding.rvNotification.adapter = adapter
                            viewModel.readNotifications()
                        } else {
                            //showToast("null")
                            binding.mainContainer.visibility = View.VISIBLE
                            binding.noNotification.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                        }

                    }

                    is UiState.Empty -> {
                        showToast(getString(R.string.empty_state))
                        binding.mainContainer.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.noNotification.visibility = View.VISIBLE
                    }

                    is UiState.Error -> {
                        showToast(state.msg.toString())
                    }
                }
            }
        }
    }
}