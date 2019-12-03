package com.fit_with_friends.fitWithFriends.ui.fragments.findChallengers

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fit_with_friends.App

import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IConnectionService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.Page
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseV4Fragment
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import com.fit_with_friends.fitWithFriends.ui.adapters.connections.ConnectionsAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.fragment_connections.*
import java.util.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConnectionsFragment : BaseV4Fragment() {

    @Inject
    internal lateinit var iConnectionService: IConnectionService

    private lateinit var connectionsAdapter: ConnectionsAdapter
    private var connectionsModelList: MutableList<ConnectionModel> = ArrayList()

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
        return inflater.inflate(R.layout.fragment_connections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFonts()
        setAdapters()
        listeners()
        getAllConnectionsFromServer()
    }

    private fun getAllConnectionsFromServer() {
        startAnim()
        iConnectionService.getAllConnectionsFromServer(
            Constants.BASE_URL + Constants.GET_ALL_CONNECTION,
            null,
            PageInput(),
            object : AsyncResult<Page<ConnectionModel>> {
                override fun success(connectionModelPage: Page<ConnectionModel>) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (swipeRefresh_connections != null) {
                            swipeRefresh_connections.isRefreshing = false
                        }
                        if (connectionModelPage.body.size != 0) {
                            if (recyclerView_connections != null) {
                                recyclerView_connections.visibility = View.VISIBLE
                                textView_no_connections.visibility = View.GONE
                            }
                            connectionsModelList.clear()
                            connectionsModelList.addAll(connectionModelPage.body)
                            connectionsAdapter.notifyDataSetChanged()
                        } else {
                            if (recyclerView_connections != null) {
                                recyclerView_connections.visibility = View.GONE
                                textView_no_connections.visibility = View.VISIBLE
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
                        if (swipeRefresh_connections != null) {
                            swipeRefresh_connections.isRefreshing = false
                        }
                        if (error == "java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 74 path \$.body"
                            || error == "Internet connection is slow"
                        ) {
                            //TODO logout
                            CommonMethods.startNextActivity(activity!!)
                        }
                    }
                }
            })
    }

    private fun setFonts() {
    }

    private fun setAdapters() {
        connectionsAdapter = ConnectionsAdapter(activity, connectionsModelList, asyncResult)
        recyclerView_connections.layoutManager = LinearLayoutManager(activity)
        recyclerView_connections.adapter = connectionsAdapter
    }

    private var asyncResult: AsyncResult<ConnectionModel> = object : AsyncResult<ConnectionModel> {
        override fun success(connectionModel: ConnectionModel) {

        }

        override fun error(error: String) {

        }
    }

    private fun listeners() {
        swipeRefresh_connections.setOnRefreshListener {
            swipeRefresh_connections.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
            getAllConnectionsFromServer()
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
            ConnectionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun startAnim() {
        if (avi_loader_connections != null) {
            avi_loader_connections.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_connections != null) {
            avi_loader_connections.smoothToHide()
        }
    }
}
