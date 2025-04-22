package com.tcontur.login_tcontur.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tcontur.login_tcontur.ui.data.models.Paradero
import kotlin.math.*


object LocationUtils {

    fun obtenerUbicacionActual(context: Context, onLocationResult: (Location?) -> Unit) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Ubicación", "Permisos de ubicación no concedidos")
            onLocationResult(null)
            return
        }

        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
            else -> null
        }

        if (provider == null) {
            Log.e("Ubicación", "No hay proveedor de ubicación disponible")
            onLocationResult(null)
            return
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("Ubicación", "Ubicación obtenida: ${location.latitude}, ${location.longitude}")
                onLocationResult(location)
                locationManager.removeUpdates(this)
            }

            override fun onProviderDisabled(provider: String) {
                Log.w("Ubicación", "Proveedor deshabilitado: $provider")
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        locationManager.requestLocationUpdates(provider, 0L, 0f, listener)
    }

    fun calcularDistancia(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double
    ): Double {
        val radioTierra = 6371e3 // en metros

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a =
            sin(dLat / 2).pow(2.0) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(
                dLon / 2
            ).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radioTierra * c
    }

    fun encontrarParaderoMasCercano(
        latitudActual: Double, longitudActual: Double, paraderos: List<Paradero>
    ): Paradero? {
        return paraderos.minByOrNull {
            calcularDistancia(latitudActual, longitudActual, it.latitud, it.longitud)
        }
    }
}