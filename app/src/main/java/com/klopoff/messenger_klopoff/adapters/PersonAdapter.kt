package com.klopoff.messenger_klopoff.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klopoff.messenger_klopoff.models.Person
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.databinding.ItemPersonBinding
import com.klopoff.messenger_klopoff.fragments.CreateChatFragment

class PersonAdapter(
    private val persons: MutableList<Person>
) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    private var itemClickListener: CreateChatFragment.PersonItemClickListener? = null

    inner class ViewHolder(
        private val itemBinding: ItemPersonBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(foundedPerson: Person) {
            itemBinding.tvUsername.text = foundedPerson.userName

            if (foundedPerson.userAvatar != null) {
                itemBinding.ivAvatar.setImageBitmap(foundedPerson.userAvatar)
            } else {
                itemBinding.ivAvatar.setImageResource(R.drawable.ic_avatar_placeholder)
            }

            itemBinding.root.setOnClickListener { itemClickListener?.onClick(foundedPerson) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = persons[position]
        holder.bind(person)
    }

    override fun getItemCount(): Int {
        return persons.size
    }

    fun setItemClickListener(listener: CreateChatFragment.PersonItemClickListener?) {
        itemClickListener = listener
    }
}