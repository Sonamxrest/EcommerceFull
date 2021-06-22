package com.xrest.shopify.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(val lst:MutableList<Fragment>,fragment:FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragment,lifecycle) {
    override fun getItemCount(): Int {
        return lst.size
    }

    override fun createFragment(position: Int): Fragment {
        return lst[position]
    }
}