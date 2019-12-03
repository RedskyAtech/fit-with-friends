package com.fit_with_friends.fitWithFriends.ui.activities.invite

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.fit_with_friends.App
import com.fit_with_friends.common.contracts.services.IConnectionService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import com.fit_with_friends.fitWithFriends.impl.models.ContactModel
import com.fit_with_friends.fitWithFriends.ui.adapters.contacts.ContactsListAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.activity_invite_friends.*
import kotlinx.android.synthetic.main.activity_notification.rLayout_back_arrow
import javax.inject.Inject
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.fit_with_friends.R
import java.util.*

class InviteFriendsActivity : BaseAppCompactActivity() {
    @Inject
    internal lateinit var iConnectionService: IConnectionService
    private lateinit var contactsListAdapter: ContactsListAdapter
    private var contactModelArrayList: MutableList<ContactModel> = ArrayList()
    private var selectedContactsList: MutableList<String> = ArrayList()
    private var invitationType: String = String()
    private var mPermissionAll = 1
    private var mPermissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_friends)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        listeners()
        setAdapters()
        getPermissions()
        layout_email.performClick()
        ActivityCompat.requestPermissions(
            this@InviteFriendsActivity,
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
            1
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LongOperation().execute("")
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(
                    this@InviteFriendsActivity,
                    "Permission denied to read your External storage",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LongOperation : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String {
            getContactList()
            return "Loading"
        }

        override fun onPostExecute(result: String) {
            contactsListAdapter.notifyDataSetChanged()
            stopAnim()
        }

        override fun onPreExecute() {
            startAnim()
        }

        override fun onProgressUpdate(vararg values: Void) {}
    }

    private fun getPermissions() {
        if (!hasPermissions(this, *mPermissions)) {
            ActivityCompat.requestPermissions(this, mPermissions, mPermissionAll)
        } /*else {
            LongOperation().execute("")
        }*/
    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    private fun getContactList() {
        contactModelArrayList.clear()
        val cr = contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cur?.count ?: 0 > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id), null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNo = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        val contactModel = ContactModel()
                        contactModel.name = name
                        contactModel.number = phoneNo
                        contactModelArrayList.add(contactModel)
                        Log.i("name", "Name: $name")
                        Log.i("phone", "Phone Number: $phoneNo")
                    }
                    pCur.close()
                }
            }
        }
        runOnUiThread {
            contactsListAdapter.notifyDataSetChanged()
        }
        cur?.close()
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }
        layout_email.setOnClickListener {
            imageView_radio_email.setImageResource(R.mipmap.radio_selected)
            imageView_radio_phone.setImageResource(R.mipmap.radio_unselected)
            layout_email_detail.visibility = View.VISIBLE
            recyclerView_contacts.visibility = View.GONE
            invitationType = "email"
        }
        layout_phone.setOnClickListener {
            imageView_radio_email.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_phone.setImageResource(R.mipmap.radio_selected)
            recyclerView_contacts.visibility = View.VISIBLE
            layout_email_detail.visibility = View.GONE
            invitationType = "phone"
        }
        button_send.setOnClickListener {
            if (invitationType == "email") {
                if (editText_email.text.toString().trim().isEmpty()) {
                    CommonMethods.alertTopError(this@InviteFriendsActivity, "Email required")
                } else {
                    sendInvitation()
                }
            } else {
                sendInvitation()
            }
        }
    }

    private fun sendInvitation() {
        startAnim()
        val connectionModel = ConnectionModel()
        val input = PageInput()
        if (invitationType == "email") {
            input.query.add("invitation_type", 1)
            input.query.add("invitation_by", editText_email.text.toString().trim())
            input.query.add("message", editText_message.text.toString().trim())
        } else {
            connectionModel.invitation_type = "2"
            connectionModel.invitation_by = contacts(selectedContactsList.toString())
        }
        iConnectionService.inviteFriend(Constants.BASE_URL + Constants.INVITE_FRIEND,
            connectionModel, input,
            object : AsyncResult<ConnectionModel> {
                override fun success(connectionModel: ConnectionModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@InviteFriendsActivity, "Invitation send successfully")
                        //Toast.makeText(this@InviteFriendsActivity, "Invitation send successfully", Toast.LENGTH_LONG).show()
                        val handler = Handler()
                        handler.postDelayed({
                            finish()
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        if (error == "") {
                            CommonMethods.alertTopError(
                                this@InviteFriendsActivity,
                                "Trial accounts cannot send messages to unverified numbers purchase a Twilio number to send messages to unverified numbers."
                            )
                        } else {
                            CommonMethods.alertTopError(this@InviteFriendsActivity, error)
                        }
                    }
                }
            })
    }

    private fun contacts(toString: String): String? {
        return toString
            .replace("[", "")  //remove the right bracket
            .replace("]", "")  //remove the left bracket
            .replace(" ", "")  //remove extra the space
            .trim()
    }

    private fun setAdapters() {
        contactsListAdapter = ContactsListAdapter(this, contactModelArrayList, asyncResult)
        recyclerView_contacts.layoutManager = LinearLayoutManager(this)
        recyclerView_contacts.adapter = contactsListAdapter
    }

    private var asyncResult: AsyncResult<ContactModel> = object : AsyncResult<ContactModel> {
        override fun success(contactModel: ContactModel) {
            if (contactModel.isChecked) {
                if (contactModel.number.contains("+")) {
                    selectedContactsList.add(contactModel.number)
                } else {
                    selectedContactsList.add("+91" + contactModel.number)
                }
            } else {
                //selectedContactsList.removeAt(contactModel.pos)
            }
        }

        override fun error(error: String) {
        }
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        if (loader_invite != null) {
            loader_invite.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (loader_invite != null) {
            loader_invite.smoothToHide()
        }
    }
}