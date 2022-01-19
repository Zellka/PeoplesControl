package com.example.peoplesontrol.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.peoplesontrol.R
import com.example.peoplesontrol.ui.view.appeal.AppealFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_number,
    R.string.tab_name,
    R.string.tab_rating,
    R.string.tab_date
)

class AppealPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return AppealFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 4
    }
}