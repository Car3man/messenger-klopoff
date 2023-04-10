package com.klopoff.messenger_klopoff.decorations

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.klopoff.messenger_klopoff.utils.UiUtils

class ItemMarginsDecoration private constructor(
    private val context: Context, private var margins: Margins, private var reverseLayout: Boolean
) : RecyclerView.ItemDecoration() {

    constructor(context: Context) : this(
        context, Margins(0, 0, 0, 0), false
    )

    fun setTopMargin(value: Int): ItemMarginsDecoration {
        margins.top = UiUtils.toPx(context, value)
        return this
    }

    fun setBottomMargin(value: Int): ItemMarginsDecoration {
        margins.bottom = UiUtils.toPx(context, value)
        return this
    }

    fun setVerticalMargin(value: Int): ItemMarginsDecoration {
        margins.top = UiUtils.toPx(context, value)
        margins.bottom = UiUtils.toPx(context, value)
        return this
    }

    fun setLeftMargin(value: Int): ItemMarginsDecoration {
        margins.left = UiUtils.toPx(context, value)
        return this
    }

    fun setRightMargin(value: Int): ItemMarginsDecoration {
        margins.right = UiUtils.toPx(context, value)
        return this
    }

    fun setHorizontalMargin(value: Int): ItemMarginsDecoration {
        margins.left = UiUtils.toPx(context, value)
        margins.right = UiUtils.toPx(context, value)
        return this
    }

    fun setReverseLayout(value: Boolean): ItemMarginsDecoration {
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
        var top: Int,
        var bottom: Int,
        var left: Int,
        var right: Int
    )
}