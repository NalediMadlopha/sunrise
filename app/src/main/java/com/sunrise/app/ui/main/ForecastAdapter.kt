package com.sunrise.app.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sunrise.app.R
import com.sunrise.app.model.Forecast
import kotlinx.android.synthetic.main.forecast_list_item_today.view.*
import java.util.*

class ForecastAdapter(private val forecastList: List<Forecast>) :
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

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val context = itemView.context

        fun bind(forecast: Forecast, position: Int) {
            if (position == 0) {
                itemView.location_name_textview?.text = "Johannesburg"
                itemView.date_textview?.text = "Today"
                itemView.temperature_textview?.text =
                    context.getString(R.string.degrees, forecast.temp.day.toInt())

                itemView.min_max_textview?.text = context.getString(
                    R.string.min_max_feels_like,
                    forecast.temp.max.toInt(),
                    forecast.temp.min.toInt(),
                    forecast.feels_like.day.toInt()
                )
            } else {
                itemView.date_textview?.text = "Mon, 2${position + 1} November"
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
    }

    object ViewType {
        const val TODAY = 0
        const val OTHER = 1
    }

}