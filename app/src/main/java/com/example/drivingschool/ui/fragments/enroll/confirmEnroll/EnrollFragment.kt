package com.example.drivingschool.ui.fragments.enroll.confirmEnroll

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentEnrollBinding

class EnrollFragment : Fragment() {

    private lateinit var binding: FragmentEnrollBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnrollBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmEnroll.setOnClickListener {
            showConfirmAlert()
        }
    }

    private fun showConfirmAlert() {
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setNeutralButton("Ок") {dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}