package com.fit_with_friends.fitWithFriends.ui.fragments.motivation

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fit_with_friends.R
import com.fit_with_friends.common.ui.BaseV4Fragment
import com.fit_with_friends.fitWithFriends.ui.adapters.pagerAdapter.CommonPagerAdapter
import kotlinx.android.synthetic.main.fragment_competition.*
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MotivationFragment : BaseV4Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var commonPagerAdapter: CommonPagerAdapter
    private var fragmentList: MutableList<Fragment> = ArrayList()

    override fun setupActivityComponent() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_motivation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabs()
        initPager()
        setFonts()
        setAdapters()
        listeners()
    }

    private fun initTabs() {
        tabLayout?.addTab(tabLayout!!.newTab().setText("Today's Motivation"))
        tabLayout?.addTab(tabLayout!!.newTab().setText("Yesterday's Motivation"))
        tabLayout?.tabTextColors = resources.getColorStateList(R.color.color_purple)
        changeTabFonts()
        changeSelectedTabFont(0)
        tabLayout?.tabGravity = TabLayout.GRAVITY_FILL
    }

    private fun changeSelectedTabFont(position: Int) {
        val viewGroup = tabLayout?.getChildAt(0) as ViewGroup
        val viewGroupTab = viewGroup.getChildAt(position) as ViewGroup
        val tabChildCount = viewGroupTab.childCount
        for (j in 0 until tabChildCount) {
            val tabViewChild = viewGroupTab.getChildAt(j)
            if (tabViewChild is TextView) {
                tabViewChild.typeface = arialBold
                tabViewChild.setTextColor(resources.getColor(R.color.color_purple))

            }
        }
    }

    private fun changeTabFonts() {
        val viewGroup = tabLayout?.getChildAt(0) as ViewGroup
        val tabCount = viewGroup.childCount

        for (i in 0 until tabCount) {
            val viewGroupTab = viewGroup.getChildAt(i) as ViewGroup
            val tabChildCount = viewGroupTab.childCount
            for (j in 0 until tabChildCount) {
                val tabViewChild = viewGroupTab.getChildAt(j)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = arialRegular
                    tabViewChild.setTextColor(resources.getColor(R.color.gray))
                }
            }
        }
    }

    private fun initPager() {
        fragmentList.add(Fragment.instantiate(activity, TodayMotivationFragment::class.java.name))
        fragmentList.add(Fragment.instantiate(activity, YesterdayMotivationFragment::class.java.name))

        commonPagerAdapter = CommonPagerAdapter(childFragmentManager, fragmentList)
        pager.adapter = commonPagerAdapter
        pager.offscreenPageLimit = 1
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout?.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                changeTabFonts()
                changeSelectedTabFont(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun setFonts() {
    }

    private fun setAdapters() {
    }

    private fun listeners() {
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            MotivationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
