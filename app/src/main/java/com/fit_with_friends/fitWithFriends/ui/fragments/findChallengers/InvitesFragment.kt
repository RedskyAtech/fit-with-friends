package com.fit_with_friends.fitWithFriends.ui.fragments.findChallengers

import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.fit_with_friends.fitWithFriends.ui.adapters.invite.InviteAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.fragment_invites.*
import java.util.ArrayList
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InvitesFragment : BaseV4Fragment() {

    @Inject
    internal lateinit var iConnectionService: IConnectionService

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var inviteAdapter: InviteAdapter
    private var inviteModelList: MutableList<ConnectionModel> = ArrayList()

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
        return inflater.inflate(R.layout.fragment_invites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFonts()
        listeners()
        setAdapters()
        getPendingInviteListFromServer()
    }

    private fun getPendingInviteListFromServer() {
        startAnim()
        iConnectionService.getPendingInviteListFromServer(
            Constants.BASE_URL + Constants.GET_PENDING_INVITE_LIST,
            null,
            PageInput(),
            object : AsyncResult<Page<ConnectionModel>> {
                override fun success(connectionModelPage: Page<ConnectionModel>) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (swipeRefresh_invites != null) {
                            swipeRefresh_invites.isRefreshing = false
                        }
                        if (connectionModelPage.body.size != 0) {
                            if (recyclerView_invites != null) {
                                recyclerView_invites.visibility = View.VISIBLE
                                textView_no_invites.visibility = View.GONE
                            }
                            inviteModelList.clear()
                            inviteModelList.addAll(connectionModelPage.body)
                            inviteAdapter.notifyDataSetChanged()
                        } else {
                            if (recyclerView_invites != null) {
                                recyclerView_invites.visibility = View.GONE
                                textView_no_invites.visibility = View.VISIBLE
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
                        if (swipeRefresh_invites != null) {
                            swipeRefresh_invites.isRefreshing = false
                        }
                        if (error == "java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 74 path \$.body") {
                            //TODO nothing
                        }
                    }
                }
            })
    }

    private fun setFonts() {
    }

    private fun setAdapters() {
        inviteAdapter = InviteAdapter(activity, inviteModelList, asyncResultReject, asyncResultResend)
        recyclerView_invites.layoutManager = LinearLayoutManager(activity)
        recyclerView_invites.adapter = inviteAdapter
    }

    private var asyncResultReject: AsyncResult<ConnectionModel> = object : AsyncResult<ConnectionModel> {
        override fun success(connectionModel: ConnectionModel) {
            declineRequest(connectionModel)
        }

        override fun error(error: String) {

        }
    }

    private var asyncResultResend: AsyncResult<ConnectionModel> = object : AsyncResult<ConnectionModel> {
        override fun success(connectionModel: ConnectionModel) {
            resendRequest(connectionModel)
        }

        override fun error(error: String) {

        }
    }

    private fun resendRequest(mConnectionModel: ConnectionModel) {
        startAnim()
        val connectionModel = ConnectionModel()
        val input = PageInput()
        if (mConnectionModel.friend_detail.email.contains("@")) {
            input.query.add("invitation_type", 1)
            input.query.add("invitation_by", mConnectionModel.friend_detail.email)
            input.query.add("message", "")
        } else {
            connectionModel.invitation_type = "2"
            connectionModel.invitation_by = mConnectionModel.friend_detail.email
        }
        iConnectionService.inviteFriend(Constants.BASE_URL + Constants.INVITE_FRIEND,
            connectionModel, input,
            object : AsyncResult<ConnectionModel> {
                override fun success(connectionModel: ConnectionModel) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(activity!!, "Invitation send successfully")
                        //Toast.makeText(this@InviteFriendsActivity, "Invitation send successfully", Toast.LENGTH_LONG).show()
                        val handler = Handler()
                        handler.postDelayed({
                            getPendingInviteListFromServer()
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (error == "") {
                            CommonMethods.alertTopError(
                                activity!!,
                                "Trial accounts cannot send messages to unverified numbers purchase a Twilio number to send messages to unverified numbers."
                            )
                        } else {
                            CommonMethods.alertTopError(activity!!, error)
                        }
                    }
                }
            })
    }

    private fun declineRequest(mConnectionModel: ConnectionModel) {
        startAnim()
        val connectionModel = ConnectionModel()
        connectionModel.request_id = mConnectionModel.id.toInt()
        iConnectionService.declineRequest(
            Constants.BASE_URL + Constants.DECLINE_REQUEST,
            null,
            connectionModel,
            object : AsyncResult<ConnectionModel> {
                override fun success(connectionModel: ConnectionModel) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(activity!!, "Successfully canceled")
                        getPendingInviteListFromServer()
                    }
                }

                override fun error(error: String) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        Toast.makeText(activity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun listeners() {
        swipeRefresh_invites.setOnRefreshListener {
            swipeRefresh_invites.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
            getPendingInviteListFromServer()
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
            InvitesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun startAnim() {
        if (avi_loader_invite != null) {
            avi_loader_invite.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_invite != null) {
            avi_loader_invite.smoothToHide()
        }
    }
}
