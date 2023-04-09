package com.klopoff.messenger_klopoff.HomeActivity.NewChatFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klopoff.messenger_klopoff.R
import com.klopoff.messenger_klopoff.databinding.ItemFoundedPersonBinding

class FoundedPersonAdapter(
    private val foundedPersons: MutableList<FoundedPerson>
) : RecyclerView.Adapter<FoundedPersonAdapter.ViewHolder>() {

    private var itemClickListener: NewChatFragment.FoundedPersonItemClickListener? = null

    inner class ViewHolder(
        private val itemBinding: ItemFoundedPersonBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(foundedPerson: FoundedPerson) {
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
        val view =
            ItemFoundedPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foundedPerson = foundedPersons[position]
        holder.bind(foundedPerson)
    }

    override fun getItemCount(): Int {
        return foundedPersons.size
    }

    fun setItemClickListener(listener: NewChatFragment.FoundedPersonItemClickListener?) {
        itemClickListener = listener
    }
}