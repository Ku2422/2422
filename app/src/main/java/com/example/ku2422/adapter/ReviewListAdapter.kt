package com.example.ku2422.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.Store
import com.example.ku2422.databinding.ListReviewBinding

class ReviewListAdapter(val reviewLists: ArrayList<Store>): RecyclerView.Adapter<ReviewListAdapter.ReviewListHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: Store)
    }

    var itemClickListener: OnItemClickListener ?= null

    inner class ReviewListHolder(val binding: ListReviewBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(reviewLists[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListHolder {
        val binding = ListReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewListHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewListHolder, position: Int) {
//        holder.binding.itemRestaurant.text = reviewLists[position]. // Store data class에 restaurant 추가 필요
//        holder.binding.itemWriterImg
//        holder.binding.itemWriter.text
//        holder.binding.itemRating.rating = reviewLists[position].star
        holder.binding.itemPrice.text = reviewLists[position].menu + "/" + reviewLists[position].price.toString()
        holder.binding.itemReview.text = reviewLists[position].review
//        holder.binding.itemDate
    }

    override fun getItemCount(): Int {
        return reviewLists.size
    }
}