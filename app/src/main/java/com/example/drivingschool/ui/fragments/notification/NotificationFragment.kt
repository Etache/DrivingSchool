package com.example.drivingschool.ui.fragments.notification

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentNotificationBinding
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding, NotificationViewModel>(R.layout.fragment_notification) {

    override val binding by viewBinding(FragmentNotificationBinding::bind)
    override val viewModel: NotificationViewModel by viewModels()

    private var adapter = NotificationAdapter(emptyList())

    override fun initialize() {
        getNotifications()
    }

    private fun getNotifications() {
        lifecycleScope.launch {
            viewModel.getNotifications()
            viewModel.notifications.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainContainer.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE

                        val sortedNewNotifications =
                            state.data?.notifications?.sortedWith(compareByDescending { it.created_at })

                        if (state.data?.notifications != null) {
                            adapter = NotificationAdapter(sortedNewNotifications!!)
                            adapter.notifyDataSetChanged()
                            binding.rvNotification.adapter = adapter
                            Log.e("ololo", "notification: ${state.data?.notifications}")
                        } else {
                            Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
                        }

                        viewModel.readNotifications()
                    }

                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}