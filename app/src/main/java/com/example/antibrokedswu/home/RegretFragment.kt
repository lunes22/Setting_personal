package com.example.antibrokedswu.home

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.R

class RegretFragment : Fragment(R.layout.fragment_regret) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bar1 = view.findViewById<View>(R.id.bar1)
        val bar2 = view.findViewById<View>(R.id.bar2)
        val bar3 = view.findViewById<View>(R.id.bar3)

        val values = listOf(40f, 30f, 30f)
        val total = values.sum()

        (bar1.layoutParams as LinearLayout.LayoutParams).apply {
            weight = values[0]; bar1.layoutParams = this
        }
        (bar2.layoutParams as LinearLayout.LayoutParams).apply {
            weight = values[1]; bar2.layoutParams = this
        }
        (bar3.layoutParams as LinearLayout.LayoutParams).apply {
            weight = values[2]; bar3.layoutParams = this
        }

        bar1.setOnClickListener {
            val percent = values[0] / total * 100
            showPopup(it, "문구류", percent)
        }
        bar2.setOnClickListener {
            val percent = values[1] / total * 100
            showPopup(it, "식비", percent)
        }
        bar3.setOnClickListener {
            val percent = values[2] / total * 100
            showPopup(it, "취미", percent)
        }
    }

    private fun showPopup(anchor: View, category: String, percent: Float) {
        val textView = TextView(requireContext()).apply {
            text = "$category (${String.format("%.1f", percent)}%)"
            setTextColor(resources.getColor(android.R.color.black, null))
            setPadding(30, 20, 30, 20)
            setBackgroundResource(R.drawable.tooltip_bg)
            textSize = 14f
        }

        val popupWindow = PopupWindow(
            textView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        popupWindow.elevation = 8f
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = false

        popupWindow.showAsDropDown(anchor, 0, -anchor.height - 40, Gravity.TOP)
        anchor.postDelayed({ popupWindow.dismiss() }, 1000)
    }
}
