package com.hakaninc.weather_app.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hakaninc.weather_app.R
import com.hakaninc.weather_app.databinding.FragmentMainBinding
import com.hakaninc.weather_app.viewmodel.WeatherViewModel
import java.time.LocalTime
import java.util.*

class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: WeatherViewModel
    private var currentCondition: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        viewModel.getData()

        binding.swipe.setOnRefreshListener {
            viewModel.getData()
            binding.swipe.isRefreshing = false
        }
        binding.button.setOnClickListener {
            Navigation.findNavController(it).navigate(WeatherFragmentDirections.actionMainFragmentToMapsFragment())
        }

        binding.weatherError.visibility = View.GONE
        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.weatherInfo.observe(viewLifecycleOwner){

            it?.let {
                binding.weather = it
                binding.temp.text = it.main!!.temp.toString().substring(0,2)+"°"

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
            }else{
                val cal = Calendar.getInstance()
                val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
                if (hour >= 19 || hour >= 5){
                    val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.night)
                    drawable?.setBounds(10, 100, binding.constraint.width, binding.constraint.height)
                    binding.constraint.background = drawable
                }else{
                    val drawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.sunny)
                    drawable?.setBounds(10, 100, binding.constraint.width, binding.constraint.height)
                    binding.constraint.background = drawable
                }

            }
        }

    }
}