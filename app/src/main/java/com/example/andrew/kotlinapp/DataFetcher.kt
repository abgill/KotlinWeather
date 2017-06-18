package com.example.andrew.kotlinapp

import android.os.AsyncTask
import com.example.andrew.kotlinapp.modal.Decoder
import com.example.andrew.kotlinapp.modal.pojo.WeatherData
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by andrew on 6/17/17.
 */
class DataFetcher {
    private fun fetchData(urlStr:String) : WeatherData{
        val url:URL = URL(urlStr)
        val httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        return Decoder().decodeForecast(httpConnection.inputStream)
    }

    companion object{
        fun fetchWeatherData(urlStr:String) : WeatherData{
            return DataFetcher().fetchData(urlStr)
        }
    }
}