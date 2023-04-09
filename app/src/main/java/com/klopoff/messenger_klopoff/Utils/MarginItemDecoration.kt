package com.klopoff.messenger_klopoff.Utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration private constructor(
    private val context: Context, private var margins: Margins, private var reverseLayout: Boolean
) : RecyclerView.ItemDecoration() {

    constructor(context: Context) : this(
        context, Margins(0, 0, 0, 0), false
    )

    fun setTopMargin(value: Int): MarginItemDecoration {
        margins.top = value.toPx(context)
        return this
    }

    fun setBottomMargin(value: Int): MarginItemDecoration {
        margins.bottom = value.toPx(context)
        return this
    }

    fun setVerticalMargin(value: Int): MarginItemDecoration {
        margins.top = value.toPx(context)
        margins.bottom = value.toPx(context)
        return this
    }

    fun setLeftMargin(value: Int): MarginItemDecoration {
        margins.left = value.toPx(context)
        return this
    }

    fun setRightMargin(value: Int): MarginItemDecoration {
        margins.right = value.toPx(context)
        return this
    }

    fun setHorizontalMargin(value: Int): MarginItemDecoration {
        margins.left = value.toPx(context)
        margins.right = value.toPx(context)
        return this
    }

    fun setReverseLayout(value: Boolean): MarginItemDecoration {
        reverseLayout = value
        return this
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            if (reverseLayout) {
                top = margins.top
                if (parent.getChildAdapterPosition(view) == if (reverseLayout) 0 else parent.childCount - 1) {
                    bottom = margins.bottom
                }
            } else {
                if (parent.getChildAdapterPosition(view) == if (reverseLayout) parent.childCount - 1 else 0) {
                    top = margins.top
                }
                bottom = margins.bottom
            }
            left = margins.left
            right = margins.right
        }
    }

    private data class Margins(
        var top: Int, var bottom: Int, var left: Int, var right: Int
    )
}