package com.example.drivingschool.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drivingschool.databinding.ChangePasswordBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialog : BottomSheetDialogFragment() {

//    private lateinit var binding : ChangePasswordBottomSheetBinding
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
////        binding = ChangePasswordBottomSheetBinding.inflate(layoutInflater)
////        return binding.root
//        return inflater.inflate(com.example.drivingschool.R.layout.change_password_bottom_sheet, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    override fun onStart() {
//        super.onStart()
//        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//    }

//    override fun onStart() {
//        super.onStart()
//        val sheetContainer = requireView().parent as? ViewGroup ?: return
//        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener { dialogInterface ->
//            val bottomSheetDialog =
//                dialogInterface as BottomSheetDialog
//            setupFullHeight(bottomSheetDialog)
//        }
//        return dialog
//    }


//    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
//        val bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
//        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
//        val layoutParams = bottomSheet.layoutParams
//        val windowHeight = getWindowHeight()
//        if (layoutParams != null) {
//            layoutParams.height = windowHeight
//        }
//        bottomSheet.layoutParams = layoutParams
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        (context as Activity).findViewById<View>(Window.ID_ANDROID_CONTENT).height
//    }

//    private fun getWindowHeight(): Int {
//        // Calculate window height for fullscreen use
//        val displayMetrics = DisplayMetrics()
//        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        return displayMetrics.heightPixels
//    }
}