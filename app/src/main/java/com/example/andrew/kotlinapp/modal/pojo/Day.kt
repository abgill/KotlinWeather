package com.example.andrew.kotlinapp.modal.pojo

/**
 * Created by beefhead on 6/16/2017.
 */
data class Day(val dt: Long = 0,
               var temp: Temp = Temp(),
               var weather: Array<Weather> = emptyArray()) {
}