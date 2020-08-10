package com.varlamov.android.ui.global

import co.lujun.androidtagview.TagView
import com.varlamov.android.App
import com.varlamov.android.Screens

object TagViewClickListener : TagView.OnTagClickListener {

    override fun onTagClick(position: Int, text: String?) {
        App.router.navigateTo(Screens.ByTagScreen(text!!))
    }

    override fun onSelectedTagDrag(position: Int, text: String?) {}

    override fun onTagLongClick(position: Int, text: String?) {}

    override fun onTagCrossClick(position: Int) {}
}