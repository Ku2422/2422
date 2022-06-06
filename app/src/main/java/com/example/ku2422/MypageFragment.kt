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


class MypageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private lateinit var adapter: ReviewListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var userManager: LoginRepository
    lateinit var userId: String
    var reviewData: ArrayList<Store> = ArrayList()
    var searchData: ArrayList<Store> = ArrayList()
    var data = mutableListOf<Store>()

    companion object{
        const val TAG = "MypageFragment"

        fun newInstance() : MypageFragment {
            return MypageFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater, container, false)

//        userId = GlobalApplication.getInstance().getValue("userId")!!
//        StoreDB.getStoreById("userId") {
//            reviewData.add(it)
//        }

        // get reviewData
        reviewData.add(Store("2258663590","","user1","건대카페","말차라떼",
            4500,"so good", 5.0,"2022-01-01", 887789f,123123f)) //temp
        reviewData.add(Store("2258663590","","user1","레스티오","아이스티",
            4000,"sooooo good", 4.0,"2022-03-01", 887789f,123123f)) //temp
        reviewData.add(Store("2258663590","","user1","건대식당","감자탕",
            9000,"good", 5.0,"2022-02-15", 887789f,123123f)) //temp

        adapter = ReviewListAdapter(reviewData)

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
        // var storeInfo = Store("2258663590","","user1","건대카페","말차라떼", 4500,"so good", 5.0,"2022.01.01", 887789f,123123f)

        val id =GlobalApplication.getInstance().getValue("userId")
        UserDB.adduserFriend(id!!,"2258618761"){

        }
//        Log.i(TAG, "testGetValue: $id")

//        var storeInfo = Store("2258663590","말차라떼",4000,"쏘굿굿굿굿",5.0,887789f,123123f)
//
//        StoreDB.insertStore(storeInfo){
//
//        }

    }
}