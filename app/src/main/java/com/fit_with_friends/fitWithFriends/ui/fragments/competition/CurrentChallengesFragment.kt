package com.fit_with_friends.fitWithFriends.ui.fragments.competition

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fit_with_friends.App

import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IChallengeService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.contracts.tool.PagePagination
import com.fit_with_friends.common.ui.BaseV4Fragment
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.ui.activities.challenge.ChallengeDetailsActivity
import com.fit_with_friends.fitWithFriends.ui.adapters.challenge.CurrentChallengesAdapter
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.fragment_current_challenges.*
import java.util.ArrayList
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CurrentChallengesFragment : BaseV4Fragment() {

    @Inject
    internal lateinit var iChallengeService: IChallengeService

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var currentChallengesAdapter: CurrentChallengesAdapter
    private var currentChallengesModelList: MutableList<ChallengeModel> = ArrayList()

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
        return inflater.inflate(R.layout.fragment_current_challenges, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFonts()
        setAdapters()
        listeners()
        getAllChallengesFromServer()
    }

    private fun getAllChallengesFromServer() {
        startAnim()
        val input = PageInput()
        input.query.add("challenge_type", 1)
        iChallengeService.getAllChallengesFromServer(
            Constants.BASE_URL + Constants.GET_ALL_CHALLENGES,
            null,
            input,
            object : AsyncResult<PagePagination<ChallengeModel>> {
                override fun success(challengeModelPage: PagePagination<ChallengeModel>) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (swipeRefresh_current != null) {
                            swipeRefresh_current.isRefreshing = false
                        }
                        if (challengeModelPage.body.data.size != 0) {
                            if (recyclerView_current_challenges != null) {
                                recyclerView_current_challenges.visibility = View.VISIBLE
                            }
                            if (textView_no_current_challenges != null) {
                                textView_no_current_challenges.visibility = View.GONE
                            }
                            currentChallengesModelList.clear()
                            currentChallengesModelList.addAll(challengeModelPage.body.data)
                            currentChallengesAdapter.notifyDataSetChanged()
                        } else {
                            if (recyclerView_current_challenges != null) {
                                recyclerView_current_challenges.visibility = View.GONE
                            }
                            if (textView_no_current_challenges != null) {
                                textView_no_current_challenges.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                override fun error(error: String) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (swipeRefresh_current != null) {
                            swipeRefresh_current.isRefreshing = false
                        }
                    }
                }
            })
    }

    private fun setFonts() {
    }

    private fun setAdapters() {
        currentChallengesAdapter = CurrentChallengesAdapter(activity, currentChallengesModelList, asyncResult)
        recyclerView_current_challenges.layoutManager = LinearLayoutManager(activity)
        recyclerView_current_challenges.adapter = currentChallengesAdapter
    }

    private var asyncResult: AsyncResult<ChallengeModel> = object : AsyncResult<ChallengeModel> {
        override fun success(challengeModel: ChallengeModel) {
            val intent = Intent(activity, ChallengeDetailsActivity::class.java)
            intent.putExtra(Constants.CHALLENGE_MODEL, challengeModel)
            intent.putExtra(Constants.ACTIVITY_TYPE, "current")
            startActivity(intent)
        }

        override fun error(error: String) {

        }
    }

    private fun listeners() {
        swipeRefresh_current.setOnRefreshListener {
            swipeRefresh_current.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
            getAllChallengesFromServer()
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
        fun newInstance(param1: String, param2: String) =
            CurrentChallengesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun startAnim() {
        if (avi_loader_current != null) {
            avi_loader_current.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_current != null) {
            avi_loader_current.smoothToHide()
        }
    }
}
