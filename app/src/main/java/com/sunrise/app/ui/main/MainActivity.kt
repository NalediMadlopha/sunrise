package com.sunrise.app

import android.location.Address
import android.os.Bundle
import android.view.WindowManager
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sunrise.app.ui.main.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


private const val ZOOM_HEIGHT = 20f

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main)
        (google_map as? SupportMapFragment)?.getMapAsync(this)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        searchview?.setOnQueryTextListener(onQueryTextListener)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap?.let { it ->
            this.googleMap = it
            it.setOnMapClickListener { latLng ->
                viewModel.getAddress(latLng)?.let { address ->
                    it.clear()
                    searchview.setQuery(address.locality ?: address.adminArea ?: address.featureName, false)
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
                Toast.makeText(this@MainActivity, getString(R.string.unknown_location), Toast.LENGTH_LONG).show()
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

}
