package com.sunrise.app.ui.main

import android.location.Address
import android.os.Bundle
import android.view.WindowManager
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sunrise.app.R
import kotlinx.android.synthetic.main.activity_main.*


private const val ZOOM_HEIGHT = 11f

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        search_view?.setOnQueryTextListener(onQueryTextListener)

        forecast_recycler_view.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        forecast_recycler_view.adapter = ForecastAdapter(emptyList())
    }

    override fun onResume() {
        super.onResume()
        (google_map as? SupportMapFragment)?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let { it ->
            this.googleMap = it
            it.setOnMapClickListener { latLng ->
                viewModel.getAddress(latLng)?.let { address ->
                    it.clear()
                    search_view.setQuery(
                        address.locality ?: address.adminArea ?: address.featureName, false
                    )
                    pinLocation(address)
                }
            }
        }
    }

    private fun pinLocation(address: Address) {
        val latLng = LatLng(address.latitude, address.longitude)

        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(address.locality)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_HEIGHT))
    }

    private val onQueryTextListener = object : OnQueryTextListener, SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            val address = viewModel.getAddress(query)
            if (address != null) {
                pinLocation(address)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.unknown_location),
                    Toast.LENGTH_LONG
                ).show()
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

}
