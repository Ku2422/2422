package com.example.ku2422.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.Store
import com.example.ku2422.User
import com.example.ku2422.databinding.ListSocialBinding

class FriendListAdapter(var friendLists: ArrayList<User>): RecyclerView.Adapter<FriendListAdapter.FriendListHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: User, btn: Int)
    }
    var itemClickListener: OnItemClickListener?= null

    fun searchItem(newLists: ArrayList<User>) {
        friendLists = newLists
        notifyDataSetChanged()
    }

    inner class FriendListHolder(val binding: ListSocialBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnListFriend.setOnClickListener {
                itemClickListener?.onItemClick(friendLists[adapterPosition], 0)
            }
            binding.btnDeleteFriend.setOnClickListener {
                itemClickListener?.onItemClick(friendLists[adapterPosition], 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListHolder {
        val binding = ListSocialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendListHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendListHolder, position: Int) {
        holder.binding.friendName.text = friendLists[position].userName
        holder.binding.friendTotal.text = "총 리뷰 수 : " + friendLists[position].totalReviewNum.toString()
    }

    override fun getItemCount(): Int {
        return friendLists.size
    }
}