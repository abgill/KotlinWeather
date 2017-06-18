package com.example.andrew.kotlinapp.modal

import org.junit.Assert.*
import org.junit.Test
import java.io.File

/**
 * Created by andrew on 6/17/17.
 */
class DecoderTest{
    @Test
    fun testDecoder(){
        val inputStream = File("test.json").inputStream()
        val weatherData = Decoder().decodeForecast(inputStream)
        val i = 4
    }
}