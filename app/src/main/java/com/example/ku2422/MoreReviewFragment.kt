package com.example.ku2422

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ku2422.databinding.FragmentMoreReviewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.lang.NumberFormatException
import java.util.*


class MoreReviewFragment(val data: Store) : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener{
    private var binding: FragmentMoreReviewBinding ?= null
    var tmpLoc = LatLng(data.locationX.toDouble(),data.locationY.toDouble())
    lateinit var mView: MapView
    lateinit var google: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreReviewBinding.inflate(inflater, container, false)
        val geoCoder = Geocoder(this.context,Locale.getDefault())

        mView = binding!!.map2
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        lateinit var tempLoc : List<Address>
        try {
           tempLoc = geoCoder.getFromLocation(data.locationX.toDouble(),data.locationY.toDouble(),1)
        }catch (e: IOException){

        }
        if(!tempLoc.isEmpty()) {
            var addr: Address = tempLoc.get(0)
            binding!!.itemLocate.text = addr.getAddressLine(0).toString()
        }else{
            binding!!.itemLocate.text = "Lat : "+data.locationX.toString()+" Lng : "+data.locationY.toString()
        }
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


    override fun onMapReady(googleMap: GoogleMap) {
        google = googleMap
        google.moveCamera(CameraUpdateFactory.newLatLngZoom(tmpLoc,17f))
        google.setOnMarkerClickListener(this)
        addMarker()
    }

    private fun addMarker() {
        val option = MarkerOptions()
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        option.position(tmpLoc)
        option.title(data.storeName)
        google.addMarker(option)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
     marker.showInfoWindow()
        return true
    }

    override fun onStart() {
        super.onStart()
        mView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mView.onResume()

    }

    override fun onPause() {
        super.onPause()
        mView.onPause()

    }

    override fun onDestroy() {
        super.onDestroy()
        mView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }
}
