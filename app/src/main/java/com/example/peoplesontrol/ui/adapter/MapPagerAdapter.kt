package com.example.peoplesontrol.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.peoplesontrol.ui.view.main.MapCategoryFragment

private val TAB_TITLES = arrayOf(
    "Все",
    "Состояние дорог",
    "Городское пространство",
    "Нарушение ПДД",
    "Нарушение КЗОТ",
    "Криминал",
    "Бродячие животные"
)

class MapPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return MapCategoryFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return 7
    }
}