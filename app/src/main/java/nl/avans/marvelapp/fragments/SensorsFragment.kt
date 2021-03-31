package nl.avans.marvelapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import nl.avans.marvelapp.R

class SensorsFragment : Fragment(), SensorEventListener {

    //region instance variables
    private val PERMISSION_ID = 1337

    //region View instance variables
    private lateinit var ambientTemperatureView: TextView
    private lateinit var lightView: TextView
    private lateinit var pressureView: TextView
    private lateinit var relativeHumidityView: TextView
    private lateinit var longitudeView: TextView
    private lateinit var latitudeView: TextView
    private lateinit var fetchLocationButton: Button
    //endregion View instance variables

    private lateinit var sensorManager: SensorManager
    private var ambientTemperatureSensor: Sensor? = null
    private var lightSensor: Sensor? = null
    private var pressureSensor: Sensor? = null
    private var relativeHumiditySensor: Sensor? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    //endregion instance variables

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initialize view instance variables
        ambientTemperatureView = view.findViewById(R.id.tvAmbientTemperature)
        lightView = view.findViewById(R.id.tvLight)
        pressureView = view.findViewById(R.id.tvPressure)
        relativeHumidityView = view.findViewById(R.id.tvRelativeHumidity)
        longitudeView = view.findViewById(R.id.tvLongitude)
        latitudeView = view.findViewById(R.id.tvLatitude)
        fetchLocationButton = view.findViewById(R.id.bFetchLocation)

        //Initialize instance variables
        sensorManager = view.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //Initialize sensors
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        relativeHumiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        // Initialize gps
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocationButton.setOnClickListener { getLastLocation() }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val value = event?.values!![0].toString()
        updateView(event.sensor.type, value)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()

        if (isSensorAvailable(ambientTemperatureSensor))
            sensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        if (isSensorAvailable(lightSensor))
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        if (isSensorAvailable(pressureSensor))
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
        if (isSensorAvailable(relativeHumiditySensor))
            sensorManager.registerListener(this, relativeHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        if (isSensorAvailable(ambientTemperatureSensor))
            sensorManager.unregisterListener(this, ambientTemperatureSensor)
        if (isSensorAvailable(lightSensor))
            sensorManager.unregisterListener(this, lightSensor)
        if (isSensorAvailable(pressureSensor))
            sensorManager.unregisterListener(this, pressureSensor)
        if (isSensorAvailable(relativeHumiditySensor))
            sensorManager.unregisterListener(this, relativeHumiditySensor)
    }

    private fun updateView(type: Int, value: String) {
        when(type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE -> ambientTemperatureView.text = "${value}ºC"
            Sensor.TYPE_LIGHT -> lightView.text = "${value}lx"
            Sensor.TYPE_PRESSURE -> pressureView.text = "${value}hPa"
            Sensor.TYPE_RELATIVE_HUMIDITY -> relativeHumidityView.text = "${value}%"
        }
    }

    private fun isSensorAvailable(sensor: Sensor?) : Boolean {
        return sensor != null
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        longitudeView.text = location.longitude.toString()
                        latitudeView.text = location.latitude.toString()
                    }
                }
            } else {
                Toast.makeText(view?.context, "Please enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission") // We already check for the permission elsewhere
    private fun getNewLocation() {
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 2
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()!!
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            longitudeView.text = lastLocation.longitude.toString()
            latitudeView.text = lastLocation.latitude.toString()
        }
    }

    private fun checkPermission() : Boolean {
        val context = view?.context!!
        return ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled() : Boolean {
        val locationManager = view?.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "You have the permission")
            }
        }
    }

}