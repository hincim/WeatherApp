package com.hakaninc.weather_app

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.hakaninc.weather_app.model.Info
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver

class WeatherViewModel(application: Application): BaseViewModel(application) {

    private val apiService: WeatherApiService = WeatherApiService()
    private var disposable = CompositeDisposable()

    private val weatherLoading = MutableLiveData<Boolean>()
    private val weatherError = MutableLiveData<Boolean>()
    val weatherInfo = MutableLiveData<Info>()



    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun getData(){

        weatherLoading.value = true

        disposable.add(
            apiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Info>(){
                    override fun onSuccess(t: Info) {

                        weatherInfo.value = t
                        weatherLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        weatherLoading.value = false
                        weatherError.value = true

                    }
                })
        )
    }
}