package com.example.ku2422

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.adapter.FriendListAdapter
import com.example.ku2422.adapter.ReviewListAdapter
import com.example.ku2422.databinding.DialogAddFriendBinding
import com.example.ku2422.databinding.FragmentMypageBinding
import com.example.ku2422.databinding.FragmentSocialBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SocialFragment : Fragment() {
    private lateinit var binding: FragmentSocialBinding
    private lateinit var adapter: FriendListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var friendData: ArrayList<User> = ArrayList()
    var searchData: ArrayList<User> = ArrayList()

    val scope = CoroutineScope(Dispatchers.IO)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSocialBinding.inflate(inflater, container, false)

        // get friendData
        friendData.add(User("20202020", "홍길동", "", 3)) //temp
        friendData.add(User("20111111", "고길동", "", 2)) //temp

        adapter = FriendListAdapter(friendData)
        adapter.itemClickListener = object: FriendListAdapter.OnItemClickListener {
            override fun onItemClick(data: User, btn: Int) {
                if (btn == 0) {
                    // SocialListFragment로 이동
                    val frag = SocialListFragment(data)
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

        // Search review
        binding.apply {
            btnSocialSearch.setOnClickListener {
                val search = editSocialSearch.text?.toString()
                if (search == "") {
                    adapter.searchItem(friendData)
                }
                else {
                    if(!searchData.isEmpty())
                        searchData.clear()
                    for (friend in friendData) {
                        if (friend.userName.contains(search.toString())) {
                            searchData.add(friend)
                        }
                    }
                    adapter.searchItem(searchData)
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
        val alertDialog = AlertDialog.Builder(this.context).create()
        var view = layoutInflater.inflate(R.layout.dialog_add_friend, null)
        alertDialog.setView(view)

        view.findViewById<Button>(R.id.btn_add_friend).setOnClickListener {
            val dlgText = view.findViewById<EditText>(R.id.edit_friend_id).text.toString()
            val id =GlobalApplication.getInstance().getValue("userId")
            UserDB.adduserFriend(id!!,dlgText){

            }
        }
        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun getFriend(){
        val id =GlobalApplication.getInstance().getValue("userId")
        var friendInfo:ArrayList<String> = arrayListOf()
        var flag = false

        scope.launch {
            UserDB.getFriendList(id!!){
                for(i in 0 until it.size){
                    friendInfo.add(it[i])
                }
                flag = true
            }
        }

        scope.launch {
            while(!flag){ }

            if(flag){
                Log.e("SocialFragment", "getFriend : ${friendInfo}", )
            }
        }
    }

    companion object {
        fun newInstance() : SocialFragment {
            return SocialFragment()
        }
    }

}