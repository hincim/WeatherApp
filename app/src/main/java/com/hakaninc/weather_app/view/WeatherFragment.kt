package com.hakaninc.weather_app.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.hakaninc.weather_app.R
import com.hakaninc.weather_app.api.WeatherAPI
import com.hakaninc.weather_app.databinding.FragmentMainBinding
import com.hakaninc.weather_app.viewmodel.WeatherViewModel
import java.util.*
import kotlin.math.roundToInt

class WeatherFragment : Fragment(){

    private var weatherAPI: WeatherAPI? = null
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: WeatherViewModel
    private var currentCondition: Int? = 0
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    val lat = MutableLiveData<Double>()
    val lon = MutableLiveData<Double>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                }
            }
        }
    }
    private fun getLocation() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (lastKnownLocation != null) {
                lat.value = lastKnownLocation.latitude
                lon.value = lastKnownLocation.longitude
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawable: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.night)
        drawable?.setBounds(10, 100, binding.constraint.width, binding.constraint.height)
        binding.constraint.background = drawable

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        viewModel.getData(lat.value.toString(),lon.value.toString())

        binding.swipe.setOnRefreshListener {
            viewModel.getData(lat.value.toString(),lon.value.toString())
            binding.swipe.isRefreshing = false
        }


        binding.weatherError.visibility = View.GONE
        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.weatherInfo.observe(viewLifecycleOwner){

            it?.let {
                binding.weather = it
                binding.temp.text = it.main!!.temp?.roundToInt().toString()+"°"
                currentCondition = it.weather[0].id
                println(currentCondition)
                getWeatherDisplayData(viewModel.getApplication())
            }
        }
        viewModel.weatherLoading.observe(viewLifecycleOwner){

            it?.let {
                if (it){
                    binding.weatherLoading.visibility = View.VISIBLE
                }else{
                    binding.weatherLoading.visibility = View.GONE
                }
            }
        }
        viewModel.weatherError.observe(viewLifecycleOwner){

            it?.let {
                if (it){
                    binding.weatherLoading.visibility = View.GONE
                    binding.weatherError.visibility = View.VISIBLE
                }else{
                    binding.weatherError.visibility = View.GONE
                }
            }
        }

    }
    private fun getWeatherDisplayData(context: Context){
        currentCondition?.let {

            if (currentCondition!! < 600){
                val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.cloudy)
                drawable?.setBounds(10, 100, binding.constraint.width, binding.constraint.height)
                binding.constraint.background = drawable
                binding.imageViewWeather.setImageResource(R.drawable.cloud)
            }else{
                val cal = Calendar.getInstance()
                val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
                if (hour >= 19 || hour <= 5){
                    val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.night)
                    drawable?.setBounds(10, 100, binding.constraint.width, binding.constraint.height)
                    binding.constraint.background = drawable
                    binding.imageViewWeather.setImageResource(R.drawable.moon)
                }else{
                    val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.sunny)
                    drawable?.setBounds(10, 100, binding.constraint.width, binding.constraint.height)
                    binding.constraint.background = drawable
                    binding.imageViewWeather.setImageResource(R.drawable.sunny_)
                }

            }
        }

    }

}