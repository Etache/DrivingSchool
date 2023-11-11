package com.example.drivingschool.ui.fragments.enroll.selectDate

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class GridSpacingItemDecoration(
    private val spacing: Int,
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = spacing;
        } else {
            outRect.top = 0
        }
//        val position = parent.getChildAdapterPosition(view!!) // item position
//        val column = position % spanCount // item column
//        if (includeEdge) {
//            outRect.left =
//                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
//            outRect.right =
//                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
//            if (position < spanCount) { // top edge
//                outRect.top = spacing
//            }
//            outRect.bottom = spacing // item bottom
//        } else {
//            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
//            outRect.right =
//                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//            if (position >= spanCount) {
//                outRect.top = spacing // item top
//            }
//        }
    }
}