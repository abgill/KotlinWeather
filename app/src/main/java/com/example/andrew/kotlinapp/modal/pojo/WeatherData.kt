package com.example.andrew.kotlinapp.modal.pojo

import com.example.andrew.kotlinapp.modal.pojo.Day

/**
 * Created by beefhead on 6/16/2017.
 */

class WeatherData(val city: City = City(),
                  val list : Array<Day> = emptyArray()){

}
