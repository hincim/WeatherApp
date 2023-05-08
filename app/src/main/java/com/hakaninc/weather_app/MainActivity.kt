package com.hakaninc.weather_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.hakaninc.weather_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener{


    private lateinit var binding: ActivityMainBinding
    var city = MutableLiveData<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.toolbar.title = "Hava Durumu"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.black))
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as androidx.appcompat.widget.SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            city.value = query!!
        }
        val bundle = Bundle()
        bundle.putString("city",city.value)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        println(newText)
        return true
    }
}