package com.example.peoplesontrol.utils

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.peoplesontrol.R
import kotlinx.android.synthetic.main.layout_error.view.*

object Error {
    fun showInternetError(activity: Activity) {
        val layout: View = activity.layoutInflater.inflate(R.layout.layout_error, null)
        val text = layout.findViewById<View>(R.id.text_error) as TextView
        val img = layout.img_error
        text.text = activity.resources.getString(R.string.no_internet)
        text.width = 900
        img.setImageResource(R.drawable.ic_no_internet)
        Toast(activity).apply {
            duration = Toast.LENGTH_SHORT
            this.view = layout
            setGravity(Gravity.TOP, 0, 0)
        }.show()
    }

    fun showError(activity: Activity) {
        val layout: View = activity.layoutInflater.inflate(R.layout.layout_error, null)
        val text = layout.findViewById<View>(R.id.text_error) as TextView
        val img = layout.img_error
        text.text = activity.resources.getString(R.string.error)
        text.width = 900
        img.setImageResource(R.drawable.ic_error)
        Toast(activity).apply {
            duration = Toast.LENGTH_SHORT
            this.view = layout
            setGravity(Gravity.TOP, 0, 0)
        }.show()
    }
}