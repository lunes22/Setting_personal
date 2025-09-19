package com.example.antibrokedswu


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.antibrokedswu.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val icons = listOf(
        R.drawable.ic_home,
        R.drawable.ic_calendar,
        R.drawable.ic_report,
        R.drawable.ic_settings
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 탭 아이콘 추가
        icons.forEach { iconRes ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setIcon(iconRes))
        }

        // 초기 화면
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, HomeFragment())
                .commit()
        }

        // 탭 클릭 시 fragment 교체
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val fragment = when (tab.position) {
                    0 -> HomeFragment()
                    1 -> CalendarFragment()
                    2 -> ReportFragment()
                    else -> SettingsFragment()
                }
                supportFragmentManager.beginTransaction()
                    .replace(binding.container.id, fragment)
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}