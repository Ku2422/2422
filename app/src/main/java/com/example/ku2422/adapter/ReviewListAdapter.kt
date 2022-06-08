package com.example.ku2422.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.Store
import com.example.ku2422.databinding.ListReviewBinding

class ReviewListAdapter(var reviewLists: ArrayList<Store>): RecyclerView.Adapter<ReviewListAdapter.ReviewListHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: Store)
    }

    var itemClickListener: OnItemClickListener ?= null

    fun sortItem(pos: Int) {
        when(pos) {
            0-> {
                reviewLists = ArrayList(reviewLists.sortedByDescending { it.date })
                notifyDataSetChanged()
            }
            1-> {
                reviewLists = ArrayList(reviewLists.sortedByDescending { it.star })
                notifyDataSetChanged()
            }
            2-> {
                // 거리순
            }
        }

    }

    fun searchItem(newLists: ArrayList<Store>) {
        reviewLists = newLists
        notifyDataSetChanged()
    }

//    fun updateItem()

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
        holder.binding.itemRestaurant.text = reviewLists[position].storeName // Store data class에 restaurant 추가 필요
//        holder.binding.itemWriterImg
        holder.binding.itemWriter.text = reviewLists[position].uesrName
        holder.binding.itemRating.rating = reviewLists[position].star.toFloat()
        holder.binding.itemMenu.text = reviewLists[position].menu
        holder.binding.itemPrice.text = reviewLists[position].price.toString()
        holder.binding.itemReview.text = reviewLists[position].review
        holder.binding.itemDate.text = reviewLists[position].date
    }

    override fun getItemCount(): Int {
        return reviewLists.size
    }
}