package com.example.antibrokedswu

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.antibrokedswu.databinding.FragmentHomeBinding
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        setupPieChart()
    }

    private fun setupPieChart() = with(binding.pieChart) {
        description.isEnabled = false
        legend.isEnabled = false
        setDrawEntryLabels(false)
        isRotationEnabled = false

        isDrawHoleEnabled = true
        holeRadius = 40f
        transparentCircleRadius = holeRadius
        setHoleColor(Color.WHITE)

        val entries = listOf(
            PieEntry(30f, "식비"),
            PieEntry(20f, "교통"),
            PieEntry(50f, "기타")
        )
        val total = entries.sumOf { it.y.toDouble() }.toFloat()

        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 6f
            selectionShift = 0f
            setDrawValues(false)
            setDrawRoundedSlices(false)
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.chartPink),
                ContextCompat.getColor(requireContext(), R.color.chartBlue),
                ContextCompat.getColor(requireContext(), R.color.chartYellow)
            )
        }
        data = PieData(dataSet)
        setUsePercentValues(false)
        invalidate()

        val marker = SimplePieMarker(requireContext(), total)
        marker.chartView = this
        this.marker = marker
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class SimplePieMarker(
        ctx: Context,
        private val total: Float
    ) : MarkerView(ctx, android.R.layout.simple_list_item_1) {

        private val tv: TextView = findViewById(android.R.id.text1)

        init {
            tv.setBackgroundResource(android.R.drawable.toast_frame)
            tv.setPadding(dp(12), dp(6), dp(12), dp(6))
            tv.textSize = 13f
            tv.setTextColor(Color.BLACK)
        }

        override fun refreshContent(e: com.github.mikephil.charting.data.Entry?, h: Highlight?) {
            if (e is PieEntry) {
                val pct = if (total > 0f) e.y / total * 100f else 0f
                tv.text = "${e.label} (${String.format("%.1f", pct)}%)"
            }
            super.refreshContent(e, h)
        }

        override fun getOffset(): MPPointF {
            return MPPointF(-(width / 2f), -height - dp(8).toFloat())
        }
    }

    private fun dp(v: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), resources.displayMetrics
    ).toInt()
}
