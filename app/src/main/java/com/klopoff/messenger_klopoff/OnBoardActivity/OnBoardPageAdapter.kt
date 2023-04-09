package com.klopoff.messenger_klopoff.OnBoardActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klopoff.messenger_klopoff.databinding.ItemOnboardPageBinding

class OnBoardPageAdapter(
    private val pages: List<OnBoardPage>
) : RecyclerView.Adapter<OnBoardPageAdapter.OnBoardPageViewHolder>() {

    inner class OnBoardPageViewHolder(private val itemBinding: ItemOnboardPageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(page: OnBoardPage) {
            itemBinding.ivThumbnail.setImageResource(page.thumbnailResId)
            itemBinding.tvShortTitle.text = page.shortTitle
            itemBinding.tvShortDescription.text = page.shortDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardPageViewHolder {
        val view =
            ItemOnboardPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnBoardPageViewHolder, position: Int) {
        val page = pages[position]
        holder.bind(page)
    }

    override fun getItemCount(): Int {
        return pages.size
    }
}