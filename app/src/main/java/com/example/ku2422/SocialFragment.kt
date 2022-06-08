package com.example.ku2422

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.adapter.FriendListAdapter
import com.example.ku2422.databinding.FragmentSocialBinding
import kotlinx.coroutines.*


class SocialFragment : Fragment() {

    companion object{
        var INSTANCE : SocialFragment? = null
        fun getInstance() = INSTANCE?: SocialFragment().also {
            INSTANCE = it
            it
        }
    }
    private lateinit var binding: FragmentSocialBinding
    private lateinit var adapter: FriendListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var friendData: ArrayList<User> = ArrayList()
    var searchData: ArrayList<User> = ArrayList()
    var friendId: ArrayList<String> = arrayListOf()
    val mainViewModel: MainViewModel by activityViewModels()

    val friendInfoList = ArrayList<User>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSocialBinding.inflate(inflater, container, false)

        getFriendID()

        setMainViewModelProperties()
        initLayoutProperties()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.friendUserInfoList.clear()

    }


    private fun initLayoutProperties() {
        // Search review
        binding.apply {
            btnSocialSearch.setOnClickListener {
                val search = editSocialSearch.text?.toString()
                if (search == "") {
                    adapter.searchItem(friendData)
                } else {
                    if (!searchData.isEmpty())
                        searchData.clear()
                    for (friend in friendData) {
                        if (friend.userName.contains(search.toString())) {
                            searchData.add(friend)
                        }
                    }
                    adapter.searchItem(searchData)
                }
            }
            btnFriend.setOnClickListener {
                addFriend()
            }
        }
    }

    private fun setMainViewModelProperties() {
        mainViewModel.run {
            friendIdListLiveData.observe(this@SocialFragment){ friendIdUpdated ->
                this@SocialFragment.friendId = friendIdUpdated
                getFriendInfo(this@SocialFragment.friendId)
            }
            friendInfoLiveData.observe(this@SocialFragment) {
                friendUserInfoList.add(it)
                friendInfoListLiveData.value = friendUserInfoList
            }
            friendInfoListLiveData.observe(this@SocialFragment){
                if (it.size == this@SocialFragment.friendId.size){
                    adapter = FriendListAdapter(it.clone() as ArrayList<User>)
                    adapter.itemClickListener = object : FriendListAdapter.OnItemClickListener {
                        override fun onItemClick(data: User, btn: Int) {
                            if (btn == 0) {
                                // SocialListFragment로 이동
                                val frag = SocialListFragment(data)
                                val transaction =
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.frame, frag)
                                transaction.addToBackStack(null)
                                transaction.commit()
                            } else {
                                // delete friend
                            }
                        }
                    }
                    binding.run {
                        recyclerSocial.adapter = adapter
                        binding.recyclerSocial.adapter = adapter
                        recyclerSocial.layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    }
                    //adapter.notifyDataSetChanged()
                }else{
                    Log.e("SocialFragment", "onViewCreated: 몇개? : ${it}", )
                }
            }
        }
    }
    private fun addFriend() {
        // add friend
        val alertDialog = AlertDialog.Builder(this.context).create()
        var view = layoutInflater.inflate(R.layout.dialog_add_friend, null)
        alertDialog.setView(view)

        view.findViewById<Button>(R.id.btn_add_friend).setOnClickListener {
            val dlgText = view.findViewById<EditText>(R.id.edit_friend_id).text.toString()
            val id = GlobalApplication.getInstance().getValue("userId")
            UserDB.adduserFriend(id!!, dlgText) {

            }
        }
        view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun getFriendID() {
        val id = GlobalApplication.getInstance().getValue("userId")

        if (mainViewModel.friendIdListLiveData.value == null) {
            mainViewModel.getFriendId(id!!)
        }
    }

}