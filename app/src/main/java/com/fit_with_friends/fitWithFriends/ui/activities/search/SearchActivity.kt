package com.fit_with_friends.fitWithFriends.ui.activities.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IConnectionService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.Page
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import com.fit_with_friends.fitWithFriends.ui.adapters.connections.ConnectionsAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.editText_search
import kotlinx.android.synthetic.main.activity_search.rLayout_back_arrow
import java.util.ArrayList
import javax.inject.Inject

class SearchActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iConnectionService: IConnectionService

    private lateinit var connectionsAdapter: ConnectionsAdapter
    private var connectionsModelList: MutableList<ConnectionModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setFonts()
        setAdapters()
        listeners()
        addWatcher()
    }

    private fun addWatcher() {
        editText_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.isEmpty() && editable.toString() == "") {

                } else {
                    getSearchResultFromServer(editable.toString())
                }
            }
        })
    }

    private fun getSearchResultFromServer(toString: String) {
        startAnim()
        val connectionModel = ConnectionModel()
        val input = PageInput()
        input.query.add("keyword", toString)
        iConnectionService.searchFriend(
            Constants.BASE_URL + Constants.SEARCH_FRIEND,
            null, connectionModel, input,
            object : AsyncResult<Page<ConnectionModel>> {
                override fun success(connectionModelPage: Page<ConnectionModel>) {
                    runOnUiThread {
                        stopAnim()
                        connectionsModelList.clear()
                        connectionsModelList.addAll(connectionModelPage.body)
                        connectionsAdapter.notifyDataSetChanged()
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        if (error == "java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 74 path \$.body") {
                            //TODO nothing
                        } else {
                            Toast.makeText(this@SearchActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
    }

    private fun setFonts() {
    }

    private fun setAdapters() {
        connectionsAdapter = ConnectionsAdapter(this@SearchActivity, connectionsModelList, asyncResult)
        recyclerView_search.layoutManager = LinearLayoutManager(this)
        recyclerView_search.adapter = connectionsAdapter
    }

    private var asyncResult: AsyncResult<ConnectionModel> = object : AsyncResult<ConnectionModel> {
        override fun success(connectionModel: ConnectionModel) {
            sendRequest(connectionModel)
        }

        override fun error(error: String) {

        }
    }

    private fun sendRequest(mConnectionModel: ConnectionModel) {
        startAnim()
        val connectionModel = ConnectionModel()
        connectionModel.friend_id = mConnectionModel.id
        val input = PageInput()
        iConnectionService.sendRequest(Constants.BASE_URL + Constants.SEND_REQUEST,
            connectionModel, input,
            object : AsyncResult<ConnectionModel> {
                override fun success(connectionModel: ConnectionModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@SearchActivity, "Invitation Sent Successfully.")
                        getSearchResultFromServer(editText_search.text.toString())
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        Toast.makeText(this@SearchActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }
    }


    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        if (avi_loader_search != null) {
            avi_loader_search.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_search != null) {
            avi_loader_search.smoothToHide()
        }
    }
}
