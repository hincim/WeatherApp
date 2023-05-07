package com.hakaninc.weather_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.hakaninc.weather_app.databinding.FragmentMainBinding

class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        viewModel.getData()

        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.weatherInfo.observe(viewLifecycleOwner){

            it?.let {
                binding.textViewDer.text = it.main!!.temp.toString()
            }
        }
    }
}