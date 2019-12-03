package com.fit_with_friends.fitWithFriends.ui.adapters.pagerAdapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class CommonPagerAdapter(supportFragmentManager: FragmentManager, private val fragments: List<Fragment>) :
    FragmentStatePagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}
