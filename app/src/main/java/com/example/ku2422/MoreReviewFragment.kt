package com.example.ku2422

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ku2422.databinding.FragmentMoreReviewBinding


class MoreReviewFragment(val data: Store) : Fragment() {
    private var binding: FragmentMoreReviewBinding ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreReviewBinding.inflate(inflater, container, false)

        binding!!.itemLocate.text = data.locationX.toString()
        binding!!.itemReview.text = data.review

        return binding!!.root
    }

    companion object {
        fun newInstance(data: Store): MoreReviewFragment {
            return MoreReviewFragment(data)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}