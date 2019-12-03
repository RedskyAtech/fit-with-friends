package com.fit_with_friends.fitWithFriends.ui.fragments.motivation

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fit_with_friends.App

import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IMotivationService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.ui.BaseV4Fragment
import com.fit_with_friends.fitWithFriends.impl.models.MotivationModel
import com.fit_with_friends.fitWithFriends.impl.models.MotivationTypeModel
import com.fit_with_friends.fitWithFriends.ui.adapters.motivation.MotivationTypeAdapter
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.fragment_yesterday_motivation.*
import java.util.ArrayList
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class YesterdayMotivationFragment : BaseV4Fragment() {

    @Inject
    internal lateinit var iMotivationService: IMotivationService

    private lateinit var motivationTypeAdapter: MotivationTypeAdapter
    private var motivationModelList: MutableList<MotivationTypeModel> = ArrayList()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun setupActivityComponent() {
        App.get(activity).component().inject(this)
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
        return inflater.inflate(R.layout.fragment_yesterday_motivation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listeners()
        setAdapter()
        getYesterdayMotivation()
    }

    private fun getYesterdayMotivation() {
        startAnim()
        val motivationModel = MotivationModel()
        iMotivationService.todayMotivation(
            Constants.BASE_URL + Constants.DAILY_MOTIVATION,
            null,
            motivationModel,
            object : AsyncResult<MotivationModel> {
                override fun success(motivationModel: MotivationModel) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (swipeRefresh_yesterday_motivation != null) {
                            swipeRefresh_yesterday_motivation.isRefreshing = false
                        }
                        if (motivationModel.yesterday_motivation != null) {
                            if (motivationModel.yesterday_motivation.size != 0) {
                                if (recyclerView_yesterday_motivation != null) {
                                    recyclerView_yesterday_motivation.visibility = View.VISIBLE
                                }
                                if (textView_yesterday_motivation != null) {
                                    textView_yesterday_motivation.visibility = View.GONE
                                }
                                motivationModelList.clear()
                                motivationModelList.addAll(motivationModel.yesterday_motivation)
                                motivationTypeAdapter.notifyDataSetChanged()
                            } else {
                                if (recyclerView_yesterday_motivation != null) {
                                    recyclerView_yesterday_motivation.visibility = View.GONE
                                }
                                if (textView_yesterday_motivation != null) {
                                    textView_yesterday_motivation.visibility = View.VISIBLE
                                }
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
                        if (swipeRefresh_yesterday_motivation != null) {
                            swipeRefresh_yesterday_motivation.isRefreshing = false
                        }
                    }
                }
            })
    }

    private fun listeners() {
        swipeRefresh_yesterday_motivation.setOnRefreshListener {
            swipeRefresh_yesterday_motivation.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
            getYesterdayMotivation()
        }
    }

    private fun setAdapter() {
        motivationTypeAdapter = MotivationTypeAdapter(activity, motivationModelList, asyncResult)
        recyclerView_yesterday_motivation.layoutManager = LinearLayoutManager(activity)
        recyclerView_yesterday_motivation.adapter = motivationTypeAdapter
    }

    private var asyncResult: AsyncResult<String> = object : AsyncResult<String> {
        override fun success(data: String) {

        }

        override fun error(error: String) {

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
            YesterdayMotivationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun startAnim() {
        if (avi_loader_yesterday_motivation != null) {
            avi_loader_yesterday_motivation.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_yesterday_motivation != null) {
            avi_loader_yesterday_motivation.smoothToHide()
        }
    }
}
