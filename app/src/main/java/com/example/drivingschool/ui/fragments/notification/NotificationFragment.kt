package com.example.drivingschool.ui.fragments.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.drivingschool.databinding.FragmentNotificationBinding
import com.example.drivingschool.tools.UiState
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var adapter: NotificationAdapter
    private val viewModel : NotificationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentNotificationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       getNotifications()
    }

    private fun getNotifications(){
        lifecycleScope.launch{
            viewModel.getNotifications()
            viewModel.notifications.observe(viewLifecycleOwner, Observer { state ->
                when(state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.mainContainer.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.mainContainer.visibility = View.VISIBLE


                        if (state.data?.notifications != null) {
                            adapter = NotificationAdapter(state.data?.notifications!!)
                            binding.rvNotification.adapter = adapter
                            Log.d(
                                "ololo",
                                "getInstructorDetails in fragment: ${state.data}"
                            )
                        } else{
                            Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
                        }

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