package com.example.ku2422

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ku2422.adapter.FriendListAdapter
import com.example.ku2422.adapter.ReviewListAdapter
import com.example.ku2422.databinding.FragmentMypageBinding
import com.example.ku2422.databinding.FragmentSocialBinding
import com.example.ku2422.databinding.FragmentSocialListBinding

class SocialListFragment : Fragment() {
    private lateinit var binding: FragmentSocialListBinding
    private lateinit var adapter: ReviewListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    val reviewData: ArrayList<Store> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSocialListBinding.inflate(inflater, container, false)

        // get reviewData
        reviewData.add(Store("2258663590","","user1","건대카페","말차라떼", 4500,"so good", 5.0,"2022.01.01", 887789f,123123f)) //temp
        adapter = ReviewListAdapter(reviewData)

        binding.recyclerReview.adapter = adapter

        layoutManager = LinearLayoutManager(activity)
        binding.recyclerReview.layoutManager = layoutManager

        return binding.root
    }
}