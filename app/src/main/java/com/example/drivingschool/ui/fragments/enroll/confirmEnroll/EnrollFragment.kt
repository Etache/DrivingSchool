package com.example.drivingschool.ui.fragments.enroll.confirmEnroll

import android.accessibilityservice.GestureDescription
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentEnrollBinding


class EnrollFragment : Fragment() {

    private lateinit var binding : FragmentEnrollBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnrollBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnEnroll.setOnClickListener{
            //query to enroll
            showDialog()
        }
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Запись забронирована")
            .setPositiveButton("Oк", DialogInterface.OnClickListener { dialog, id ->
                findNavController().navigate(R.id.mainFragment)
                dialog.cancel()
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}