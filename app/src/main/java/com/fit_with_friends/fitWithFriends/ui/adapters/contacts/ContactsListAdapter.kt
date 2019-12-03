package com.fit_with_friends.fitWithFriends.ui.adapters.contacts

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.fitWithFriends.impl.models.ContactModel
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import kotlinx.android.synthetic.main.list_contacts.view.*

class ContactsListAdapter(
    var activity: Activity?,
    var contactModelArrayList: MutableList<ContactModel>,
    var asyncResult: AsyncResult<ContactModel>
) : RecyclerView.Adapter<ContactsListAdapter.ViewHolder>() {

    private var capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_contacts, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactModelArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (contactModelArrayList[position].name != null) {
            holder.itemView.textView_name.text =
                capitalizeFirstWord.capitalizeWords(contactModelArrayList[position].name)
        }

        if (contactModelArrayList[position].number != null) {
            holder.itemView.textView_number.text = contactModelArrayList[position].number
        }

        holder.itemView.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                contactModelArrayList[position].pos = position
                contactModelArrayList[position].isChecked = isChecked
                asyncResult.success(contactModelArrayList[position])
            } else {
                contactModelArrayList[position].pos = position
                contactModelArrayList[position].isChecked = isChecked
                asyncResult.success(contactModelArrayList[position])
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}