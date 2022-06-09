package com.example.ku2422

import android.content.Intent
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
import com.example.ku2422.adapter.ReviewListAdapter
import com.example.ku2422.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {
    private lateinit var binding: FragmentMypageBinding
    private lateinit var adapter: ReviewListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var userId: String
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
        binding.textMyUID.text = "내 UID: "+userId
        Log.i("userId", userId)

        check = requireArguments().getBoolean("check")
        lat = requireArguments().getFloat("lat")
        lng = requireArguments().getFloat("lng")

        adapter = ReviewListAdapter(reviewData,lat,lng,check)

        StoreDB.getStoreById(userId) {
            reviewData = it
            adapter.searchItem(it)
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

        binding.apply {
            // Search review
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
            btnLogout.setOnClickListener {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
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


}