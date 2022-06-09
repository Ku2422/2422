package com.example.ku2422

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.StoreDB.getStoreById
import com.example.ku2422.adapter.ReviewListAdapter
import com.example.ku2422.data.LoginDataSource
import com.example.ku2422.data.LoginRepository
import com.example.ku2422.data.model.LoggedInUser
import com.example.ku2422.databinding.FragmentMoreReviewBinding
import com.example.ku2422.databinding.FragmentMypageBinding
import com.kakao.sdk.user.UserApiClient


class MypageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private lateinit var adapter: ReviewListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var userManager: LoginRepository
    lateinit var userId: String
    lateinit var currUser: User
    var reviewData: ArrayList<Store> = ArrayList()
    var searchData: ArrayList<Store> = ArrayList()
    var data = mutableListOf<Store>()
    var lat = 0f
    var lng = 0f
    var check = false

    companion object{
        const val TAG = "MypageFragment"

        var INSTANCE : MypageFragment? = null
        fun getInstance() = INSTANCE?: MypageFragment().also {
            INSTANCE = it
            it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        userId = GlobalApplication.getInstance().getValue("userId")!!
//        userId = "2258618761"
        binding.textMyUID.text = "내 UID: "+userId
        Log.i("userId", userId)

        check = requireArguments().getBoolean("check")
        lat = requireArguments().getFloat("lat")
        lng = requireArguments().getFloat("lng")

        adapter = ReviewListAdapter(reviewData,lat,lng,check)

        StoreDB.getStoreById(userId) {
            reviewData = it
            adapter.searchItem(it)
            Log.i("StoreDB", it.toString())
            Log.i("reviewData", reviewData.toString())
        }


        // To more review
        adapter.itemClickListener = object: ReviewListAdapter.OnItemClickListener {
            override fun onItemClick(data: Store) {
                val frag = MoreReviewFragment(data)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, frag)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
        binding.recyclerReview.adapter = adapter

        layoutManager = LinearLayoutManager(activity)
        binding.recyclerReview.layoutManager = layoutManager

        // Search review
        binding.apply {
            btnMypageSearch.setOnClickListener {
                val search = editSearch.text?.toString()
                if (search == "") {
                    adapter.searchItem(reviewData)
                }
                else {
                    if(!searchData.isEmpty())
                        searchData.clear()
                    for (review in reviewData) {
                        if (review.storeName.contains(search.toString())) {
                            searchData.add(review)
                        }
                    }
                    adapter.searchItem(searchData)
                }
            }
        }

        initSpinner()

        return binding!!.root
    }

    private fun initSpinner() {

        val spinner_list = resources.getStringArray(R.array.spinner_list)
        val spinneradapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            spinner_list
        )
        binding.apply {
            spinner.adapter = spinneradapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    adapter.sortItem(position)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        testclickevent()   //예시
    }


    //예시 로그아웃 버튼 누르면 음식점 저장 기능으로 해놓음
    private fun testclickevent(){
        binding.btnLogout.setOnClickListener {
            testGetValue()
        }
    }

    //예시
    private fun testGetValue() {

        val id =GlobalApplication.getInstance().getValue("userId")

        StoreDB.getStoreById(id!!){
            Log.e(TAG, "testGetValue: ${it[0].storeName +" "+it[0].price +" "
                    +it[1].storeName + " " + it[1].price}" )

        }
//        val StoreInfo = Store(id!!,"sdfsdfsdf.jpg","윤혁","왕소구이",
//            "간장불백",7000,"구구구ㅜㄱ구ㅜㅅ",5.0, "2022-06-07",
//            233334f,2323232111f)
//        StoreDB.insertStore(StoreInfo){
//
//        }
//        UserDB.getFriendList(id!!){
//            friendId = it[1]
//            Log.e(TAG, "testGetValue: ${friendId}", )
//        }
//        UserDB.deleteUser(id!!){
//
//        }

//        UserDB.getUserById(id!!){
//            Log.e(TAG, "getUserById: ${it.friendId}", )
//        }
//        UserDB.adduserFriend(id!!,"201811222"){
//
//        }
//        Log.i(TAG, "testGetValue: $id")

//        var storeInfo = Store("2258663590","말차라떼",4000,"쏘굿굿굿굿",5.0,887789f,123123f)
//
//        StoreDB.insertStore(storeInfo){
//
//        }

    }
}