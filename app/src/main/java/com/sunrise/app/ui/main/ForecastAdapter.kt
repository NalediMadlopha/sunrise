package com.sunrise.app.ui.main

import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sunrise.app.R
import com.sunrise.app.model.Forecast
import kotlinx.android.synthetic.main.forecast_list_item.view.date_textview
import kotlinx.android.synthetic.main.forecast_list_item.view.description_textview
import kotlinx.android.synthetic.main.forecast_list_item.view.min_max_textview
import kotlinx.android.synthetic.main.forecast_list_item_today.view.*
import java.text.SimpleDateFormat
import java.util.*

private const val DD_MMM_YYYY_DATE_FORMAT = "dd MMMM yyyy"
private const val DAY_OF_WEEK_EEEE_FORMAT = "EEEE"

class ForecastAdapter(private var forecastList: List<Forecast>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return when (viewType) {
            ViewType.TODAY -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_list_item_today, parent, false)
                    .let { ForecastViewHolder(it) }
            }
            else -> {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.forecast_list_item, parent, false)
                    .let { ForecastViewHolder(it) }
            }
        }
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ViewType.TODAY
        } else if (position != 0) {
            ViewType.OTHER
        } else {
            super.getItemViewType(position)
        }
    }

    override fun getItemCount() = forecastList.size

    fun updateData(forecastList: List<Forecast>) {
        this.forecastList = forecastList
        notifyDataSetChanged()
    }

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context = itemView.context

        fun bind(forecast: Forecast, position: Int) {
            if (position == 0) {
                itemView.weather_icon.setImageDrawable(getWeatherArt(forecast.weather.first().main))
                itemView.location_name_textview?.text = getLocationName()
                itemView.date_textview?.text = context.getString(R.string.today)
                itemView.temperature_textview?.text =
                    context.getString(R.string.degrees, forecast.temp.day.toInt())

                itemView.min_max_textview?.text = context.getString(
                    R.string.min_max_feels_like,
                    forecast.temp.max.toInt(),
                    forecast.temp.min.toInt(),
                    forecast.feels_like.day.toInt()
                )
            } else {
                itemView.weather_icon.setImageDrawable(getWeatherArt(forecast.weather.first().main))
                itemView.date_textview?.text = getForecastDate()
                itemView.min_max_textview?.text = context.getString(
                    R.string.min_max,
                    forecast.temp.max.toInt(),
                    forecast.temp.min.toInt(),
                )
            }

            forecast.weather.first().let {
                itemView.description_textview.text = it.description.split(' ')
                    .joinToString(" ") { it.capitalize(Locale.getDefault()) }
            }
        }

        private fun getWeatherArt(weatherDescription: String): Drawable {
            return when(weatherDescription) {
                "Clear" -> {
                    context.resources.getDrawable(R.mipmap.art_clear, null)
                }
                "Clouds" -> {
                    context.resources.getDrawable(R.mipmap.art_clouds, null)
                }
                "Rain" -> {
                    context.resources.getDrawable(R.mipmap.art_rain, null)
                }
                "Snow" -> {
                    context.resources.getDrawable(R.mipmap.art_snow, null)
                }
                "Storm" -> {
                    context.resources.getDrawable(R.mipmap.art_storm, null)
                } else -> {
                    context.resources.getDrawable(R.mipmap.art_clear, null)
                }
            }
        }

        private fun getLocationName(): String? {
            val sharedPreferences = context.getSharedPreferences(
                "SUNRISE_SHARED_PREFERENCES",
                AppCompatActivity.MODE_PRIVATE
            )
            val locationName = sharedPreferences.getString("location_name", "")
            return locationName
        }

        private fun getForecastDate(): String {
            var date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, position)
            date = calendar.time
            val dayOfWeek = SimpleDateFormat(DAY_OF_WEEK_EEEE_FORMAT).format(date)

            return dayOfWeek + ", " + DateFormat.format(DD_MMM_YYYY_DATE_FORMAT, date).toString()
        }

    }

    object ViewType {
        const val TODAY = 0
        const val OTHER = 1
    }

}