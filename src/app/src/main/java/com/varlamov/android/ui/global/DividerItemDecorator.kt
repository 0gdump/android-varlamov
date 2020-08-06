package com.varlamov.android.ui.global

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class DividerItemDecorator : ItemDecoration {

    private val attrs = intArrayOf(android.R.attr.listDivider)
    private var divider: Drawable?

    constructor(context: Context) {
        val styledAttributes = context.obtainStyledAttributes(attrs)

        divider = styledAttributes.getDrawable(0)

        styledAttributes.recycle()
    }

    constructor(context: Context, resId: Int) {
        divider = ContextCompat.getDrawable(context, resId)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (divider == null) return

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        (0 until parent.childCount - 1).forEach { i ->

            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight

            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(canvas)
        }
    }
}