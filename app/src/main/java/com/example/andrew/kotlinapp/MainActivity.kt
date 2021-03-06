package com.example.andrew.kotlinapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import com.example.andrew.kotlinapp.modal.DBSTATUS
import com.example.andrew.kotlinapp.modal.db.Dao
import com.example.andrew.kotlinapp.modal.db.DbHelper
import com.example.andrew.kotlinapp.modal.pojo.Day
import com.example.andrew.kotlinapp.modal.pojo.WeatherData
import java.text.SimpleDateFormat
import java.util.*
import android.support.v7.widget.DividerItemDecoration
import android.widget.ImageView
import java.io.InputStream
import java.net.URL
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    var dataStatus:DBSTATUS = DBSTATUS(20002,0)
    var db:SQLiteDatabase? = null
    var weatherData:WeatherData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchDataTask(this).execute(String.format(getString(R.string.weater_url),
                dataStatus.zip,getString(R.string.weather_api_key)))


        val dbHelper :DbHelper = DbHelper(this)
        if(db == null || !db?.isOpen!!) {
            db = dbHelper.writableDatabase
        }

        this.dataStatus = Dao.Companion.getDataStatus(db!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //super.onCreateOptionsMenu(menu)
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.settings_button -> {
                val intent : Intent = Intent(this, Settings::class.java)
                intent.putExtra("zip",dataStatus.zip)
                intent.putExtra("lastUpdated",dataStatus.lastUpdated)

                startActivityForResult(intent,3)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 3){
            this.dataStatus.zip = data?.getIntExtra("zip",0)!!
            Dao.Companion.writeDataStatus(this.db!!, DBSTATUS(dataStatus.zip, Date().time))

            fetchDataTask(this).execute(String.format(getString(R.string.weater_url),
                    dataStatus.zip,getString(R.string.weather_api_key)))
        }
    }

    inner class fetchDataTask(val context: MainActivity) : AsyncTask<String, Void, Void>() {
        var weatherData: WeatherData? = null
        val iconMap: MutableMap<String,Drawable> = HashMap()
        override fun doInBackground(vararg params: String?): Void? {
            weatherData = DataFetcher.Companion.fetchWeatherData(params[0]!!)



            for(day in weatherData?.list!!){
                val icon = day.weather[0].icon
                val urlStr = String.format("http://openweathermap.org/img/w/%s.png",icon)
                try {
                    if(iconMap.get(icon) == null) {
                        iconMap.put(icon, LoadImageFromWebOperations(urlStr)!!)
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                    continue
                }

            }

            return null
        }

        fun LoadImageFromWebOperations(url: String): Drawable? {
            try {
                val `is` = URL(url).getContent() as InputStream
                val d = Drawable.createFromStream(`is`, "src name")
                return d
            } catch (e: Exception) {
                return null
            }

        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            context.weatherData = this.weatherData

            val rv = context.findViewById(R.id.wetherView) as RecyclerView
            val layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            val adapter = WeatherAdapter(context.weatherData!!,context,iconMap)

            rv.layoutManager = layoutManager
            rv.adapter = adapter

            val dividerItemDecoration = DividerItemDecoration(rv.getContext(),
                    layoutManager.orientation)
            rv.addItemDecoration(dividerItemDecoration)
        }
    }

    inner class WeatherAdapter(private val weatherData: WeatherData,
                               private val context: MainActivity,
                               private val icons: MutableMap<String,Drawable>):
            RecyclerView.Adapter<WeatherAdapter.ViewHolder>(){

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            holder?.bindItems(weatherData.list[position])
        }

        override fun getItemCount(): Int {
            return weatherData.list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).
                    inflate(R.layout.weather_adapter, parent, false))
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            fun bindItems(day: Day){
                val highLow = itemView.findViewById(R.id.high_low) as TextView
                val dayView = itemView.findViewById(R.id.day) as TextView
                highLow.text = day.temp.max.toString() + ", " + day.temp.min.toString()

                val icon = itemView.findViewById(R.id.weather_icon)as ImageView
                icon.setImageDrawable(icons[day.weather[0].icon])

                val unixSeconds: Long = day.dt
                val date = Date(unixSeconds * 1000L)
                val sdf = SimpleDateFormat("EE MM/dd")
                sdf.timeZone = TimeZone.getDefault()
                val formattedDate = sdf.format(date)
                dayView.text = formattedDate
            }
        }
    }

    override fun onDestroy() {
        if(db != null){
            db?.close()
        }
        super.onDestroy()
    }

}

