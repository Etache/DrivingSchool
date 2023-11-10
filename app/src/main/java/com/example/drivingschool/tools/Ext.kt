package com.example.drivingschool.tools

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.viewVisibility(visibility: Boolean) {
    if (visibility) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}

fun Fragment.showToast(msg: String) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

//fun ImageView.setImage(img: String?) {
//    Log.e("ololo", "setImage: $img")
//
//    val httpsImageUrl = img?.replace("http://", "https://")
//
//    Picasso.get()
//        .load(httpsImageUrl)
//        .placeholder(R.drawable.ic_default_photo)
//        .into(this)
//}