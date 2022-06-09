package com.example.ku2422

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.adapter.FriendListAdapter
import com.example.ku2422.adapter.ReviewListAdapter
import com.example.ku2422.databinding.FragmentMypageBinding
import com.example.ku2422.databinding.FragmentSocialBinding
import com.example.ku2422.databinding.FragmentSocialListBinding

class SocialListFragment(val data: User) : Fragment() {
    private lateinit var binding: FragmentSocialListBinding
    private lateinit var adapter: ReviewListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    var reviewData: ArrayList<Store> = ArrayList()
    var searchData: ArrayList<Store> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSocialListBinding.inflate(inflater, container, false)

        // get reviewData
//        reviewData.add(Store("2258663590","","user1","건대카페","말차라떼",
//            4500,"so good", 5.0,"2022-01-01", 887789f,123123f)) //temp
//        reviewData.add(Store("2258663590","","user1","레스티오","아이스티",
//            4000,"sooooo good", 4.0,"2022-03-01", 887789f,123123f)) //temp
//        reviewData.add(Store("2258663590","","user1","건대식당","감자탕",
//            9000,"good", 5.0,"2022-02-15", 887789f,123123f)) //temp


        adapter = ReviewListAdapter(reviewData,0f,0f,false)

        StoreDB.getStoreById(data.userId) {
            reviewData = it
            adapter.searchItem(it)
            Log.i("StoreDB2", it.toString())
            Log.i("reviewData2", reviewData.toString())
        }

        binding.recyclerReview.adapter = adapter

        layoutManager = LinearLayoutManager(activity)
        binding.recyclerReview.layoutManager = layoutManager

        // Search review
        binding.apply {
            btnSocialListSearch.setOnClickListener {
                val search = editFriendSearch.text?.toString()
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


        return binding.root
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
}