package com.example.ku2422

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.ku2422.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {
    lateinit var binding: FragmentMypageBinding

    companion object{
        const val TAG = "MypageFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
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
        var storeInfo = Store("2258663590","말차라떼",4000,"쏘굿굿굿굿",5.0,887789f,123123f)

        StoreDB.insertStore(storeInfo){

        }

    }
}