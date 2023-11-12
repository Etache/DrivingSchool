package com.example.drivingschool.tools

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.drivingschool.R

fun View.viewVisibility(visibility: Boolean) {
    if (visibility) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun TextView.timePressed() {
    val normalBackground = R.drawable.calendar_time_selector_normal
    val pressedBackground = R.drawable.calendar_time_selector
    var isPressed = false

    this.setOnClickListener {
        if (isPressed) {
            this.setBackgroundResource(normalBackground)
            this.setTextColor(resources.getColor(R.color.gray))
        } else {
            this.setBackgroundResource(pressedBackground)
            this.setTextColor(resources.getColor(R.color.light_blue))
        }
        isPressed = !isPressed
    }
}