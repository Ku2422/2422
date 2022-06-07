package com.example.ku2422

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import noman.googleplaces.*
class HomeFragment : Fragment(),OnMapReadyCallback,PlacesListener, GoogleMap.OnMarkerClickListener  {



    var scope = CoroutineScope(Dispatchers.Main)
    lateinit var UID : String

    //마커 클릭시 정보 저장
    lateinit var markerName : String
    lateinit var markerLoc : LatLng

    //mainactivity 저장
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity

    }

    var check = true
    //GOOGLEMAP

    lateinit var google: GoogleMap
    lateinit var mView: MapView

    //GPS
    val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var locGps = LatLng(37.2892313,126.8164737)
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationRequest2: LocationRequest
    lateinit var locationCallback1: LocationCallback
    var startupdate = false

    //검색용
    var placesClient: PlacesClient? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView = inflater.inflate(R.layout.fragment_home, container, false)
        UID =GlobalApplication.getInstance().getValue("userId")!!
        initLocation()
        //구글 맵 뷰
        mView = rootView.findViewById(R.id.map)
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)

        //버튼 이벤트 처리
        var btn = rootView.findViewById<Button>(R.id.button)
        var btn2 = rootView.findViewById<Button>(R.id.button2)

        btn.setOnClickListener {
            //GPS 버튼 생성 후 현재 위치 부르는 용도
            if(check) {
                setMaptoGPS(locGps)
            }
            else
                showGPSSetting()

        }

        btn2.setOnClickListener {
            //USERID MENU 수정 예정, DB연동 확인
            val dialog = ReviewDialog(mainActivity)
            dialog.clickAdd(object : ReviewDialog.ClickListener{
                override fun ClickBtn(menu :String, price: Int, review: String, rating: Int) {
                    var tmpStore = Store(UID,menu,price.toInt(),review,rating.toDouble(),markerLoc.latitude.toFloat(),markerLoc.longitude.toFloat())
                    StoreDB.insertStore(tmpStore){

                    }
                }
            })

            dialog.showDlg()
        }

        //GPS 세팅

        //구글 맵 검색 창
        if(!Places.isInitialized()) {
            Places.initialize(mainActivity.applicationContext,"AIzaSyB3XIBu35OK_npiXVicBiXfV6ge-NEta24")
        }
        placesClient = Places.createClient(mainActivity)
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment)as AutocompleteSupportFragment?



        autocompleteFragment!!.setPlaceFields(
            listOf(
                com.google.android.libraries.places.api.model.Place.Field.ID,
                com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {

            }

            override fun onPlaceSelected(place: com.google.android.libraries.places.api.model.Place) {
                setMaptoGPS(place.latLng)
            }
        })

        return rootView
    }

    fun setMaptoGPS(tmpLoc : LatLng) {
        //GPS 해당장소 불러서 주변에 마커찍기
        showPlaceInformation(tmpLoc)
        google.moveCamera(CameraUpdateFactory.newLatLngZoom(tmpLoc, 16.0f))

    }

    override fun onMapReady(googleMap: GoogleMap) {

        google = googleMap
        google.setOnMarkerClickListener(this)
        setMaptoGPS(locGps)
        }

    //마커 클릭시 (마커관련정보 가져올수있음)
    override fun onMarkerClick(marker: Marker): Boolean {
        marker.showInfoWindow()
        markerName = marker.title.toString()
        markerLoc = marker.position
        return true
    }


    override fun onPlacesStart() {


    }

    override fun onPlacesSuccess(places: MutableList<Place>?) {
        scope.launch {
            for (place in places!!) {
                val option = MarkerOptions()
                option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                val tempLoc = LatLng(place.latitude, place.longitude)
                option.position(tempLoc)
                option.title(place.name)
                google.addMarker(option)
            }
        }
    }

    override fun onPlacesFailure(e: PlacesException?) {

    }

    override fun onPlacesFinished() {


    }

    fun showPlaceInformation(location: LatLng) {
        google.clear()

        val nrplace = NRPlaces.Builder().listener(this)
            .key("AIzaSyBmw17hP4pUDk-2XHzOA6laMgbCFiNYXRI")
            .radius(1000)
            .type(PlaceType.RESTAURANT)
            .latlng(location.latitude, location.longitude)
            .build()
            .execute()


        val nrplace2 = NRPlaces.Builder().listener(this)
            .key("AIzaSyBmw17hP4pUDk-2XHzOA6laMgbCFiNYXRI")
            .radius(1000)
            .type(PlaceType.CAFE)
            .latlng(location.latitude, location.longitude)
            .build()
            .execute()

    }


    //GPS SETTING
    //gps 셋팅 작업 화면 끝나면 돌아옴
    val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (checkGPSProvider()) {
                startLocationUpdates()
                check = true
            } else {
                check = false
            }
        }
    //결과 받기
    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                (permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false)
                        || permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                )) -> {
                    //둘다 허용
                    startLocationUpdates()
                    check = true
                }
                else -> {
                    check = false
                    showGPSSetting()
                }
            }
        }

    private fun checkGPSProvider(): Boolean {
        val locationManager = mainActivity.getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }


    private fun showGPSSetting() {
        val builder = AlertDialog.Builder(mainActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n" + "위치 설정을 허용하시겠습니까?")
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val GpsSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activityResultLauncher.launch(GpsSettingIntent)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
        })
        builder.create().show()
    }

    private fun showPermissionRequestDlg() {
        val builder = AlertDialog.Builder(mainActivity)
        builder.setTitle("위치 서비스 제공")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n" + "위치 설정을 허용하시겠습니까?")
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            locationPermissionRequest.launch(permissions)
        })

        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
            check = false
            dialog.dismiss()
        })
        builder.create().show()
    }

    private fun checkFineLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            mainActivity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCoarseLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            mainActivity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity)
        locationRequest = LocationRequest.create().apply {
            interval = 100000
            fastestInterval = 50000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationRequest2 = LocationRequest.create().apply {
            interval = 100000
            fastestInterval = 50000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }

        locationCallback1 = object : LocationCallback() {
            override fun onLocationResult(locationR: LocationResult) {
                if (locationR.locations.size == 0) {
                    return
                }
                locGps = LatLng(
                    locationR.locations[locationR.locations.size - 1].latitude,
                    locationR.locations[locationR.locations.size - 1].longitude
                )
            }
        }
    }

    private fun stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback1)
        startupdate = false
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        when {
            checkFineLocationPermission() -> {
                if (!checkGPSProvider()) {
                    showGPSSetting()
                } else {
                    startupdate = true
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest, locationCallback1,
                        Looper.getMainLooper()
                    )
                }
            }
            checkCoarseLocationPermission() -> {
                startupdate = true
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest2, locationCallback1,
                    Looper.getMainLooper()
                )
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                mainActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                showGPSSetting()
            }
            else -> {
                showPermissionRequestDlg()
            }
        }
    }

    //LifeCycle
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
        if (!startupdate&&check)
            startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        mView.onPause()
        stopLocationUpdate()
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


