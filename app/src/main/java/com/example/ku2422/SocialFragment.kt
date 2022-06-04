package com.example.ku2422

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.adapter.FriendListAdapter
import com.example.ku2422.adapter.ReviewListAdapter
import com.example.ku2422.databinding.DialogAddFriendBinding
import com.example.ku2422.databinding.FragmentMypageBinding
import com.example.ku2422.databinding.FragmentSocialBinding


class SocialFragment : Fragment() {
    private lateinit var binding: FragmentSocialBinding
    private lateinit var adapter: FriendListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    val friendData: ArrayList<User> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSocialBinding.inflate(inflater, container, false)

        // get friendData
        friendData.add(User("20202020", "홍길동", "", 3)) //temp
        adapter = FriendListAdapter(friendData)
        adapter.itemClickListener = object: FriendListAdapter.OnItemClickListener {
            override fun onItemClick(data: User, btn: Int) {
                if (btn == 0) {
                    // SocialListFragment로 이동
                    val frag = SocialListFragment()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, frag)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                else {
                    // delete friend
                }
            }
        }

        binding.btnFriend.setOnClickListener {
            addFriend()
        }

        binding.recyclerSocial.adapter = adapter

        layoutManager = LinearLayoutManager(activity)
        binding.recyclerSocial.layoutManager = layoutManager

        return binding.root
    }

    private fun addFriend() {
        // add friend
        val builder = AlertDialog.Builder(this.context)
        var view = layoutInflater.inflate(R.layout.dialog_add_friend, null)
        builder.setView(view)

        builder.show()
    }

    companion object {
        fun newInstance() : SocialFragment {
            return SocialFragment()
        }
    }

}