package com.example.andrew.kotlinapp.modal

import com.example.andrew.kotlinapp.modal.pojo.WeatherData
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.InputStream


/**
 * Created by beefhead on 6/16/2017.
 */

class Decoder{
    fun decodeForecast(json : InputStream): WeatherData {
        val mapper : ObjectMapper = ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)

        return mapper.readValue(json, WeatherData::class.java)
    }

}