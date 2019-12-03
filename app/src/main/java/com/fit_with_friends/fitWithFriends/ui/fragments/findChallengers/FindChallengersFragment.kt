package com.fit_with_friends.fitWithFriends.ui.fragments.findChallengers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IConnectionService
import com.fit_with_friends.common.ui.BaseV4Fragment
import com.fit_with_friends.fitWithFriends.enums.FragmentTag
import com.fit_with_friends.fitWithFriends.ui.activities.home.DashboardActivity
import com.fit_with_friends.fitWithFriends.ui.activities.invite.InviteFriendsActivity
import com.fit_with_friends.fitWithFriends.ui.activities.search.SearchActivity
import com.fit_with_friends.fitWithFriends.ui.adapters.pagerAdapter.CommonPagerAdapter
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_competition.pager
import kotlinx.android.synthetic.main.fragment_competition.tabLayout
import kotlinx.android.synthetic.main.fragment_find_challengers.*
import java.util.ArrayList
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FindChallengersFragment : BaseV4Fragment() {

    @Inject
    internal lateinit var iConnectionService: IConnectionService

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var commonPagerAdapter: CommonPagerAdapter
    private var fragmentList: MutableList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setupActivityComponent() {
        App.get(activity).component().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_challengers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabs()
        initPager()
        setFonts()
        setAdapters()
        listeners()
    }

    private fun initTabs() {
        tabLayout?.addTab(tabLayout!!.newTab().setText("Connections"))
        tabLayout?.addTab(tabLayout!!.newTab().setText("Invites"))
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
        fragmentList.add(Fragment.instantiate(activity, ConnectionsFragment::class.java.name))
        fragmentList.add(Fragment.instantiate(activity, InvitesFragment::class.java.name))

        commonPagerAdapter = CommonPagerAdapter(childFragmentManager, fragmentList)
        pager.adapter = commonPagerAdapter
        pager.offscreenPageLimit = 1
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout?.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                changeTabFonts()
                changeSelectedTabFont(tab.position)
                PreferenceHandler.writeInteger(activity, Constants.TAB_POS, tab.position)
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
        rLayout_search.setOnClickListener {
            //rLayout_search_bar.visibility = View.VISIBLE
            val intent = Intent(activity, SearchActivity::class.java)
            startActivityForResult(intent, 115)
        }

        layout_bottom_invite.setOnClickListener {
            val intent = Intent(activity, InviteFriendsActivity::class.java)
            startActivityForResult(intent, 115)
        }
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
            FindChallengersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 115) {
            (activity as DashboardActivity).openFragment(FragmentTag.CHALLENGE.tag)
        }
    }
}
