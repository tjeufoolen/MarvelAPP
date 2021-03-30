package nl.avans.marvelapp.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import nl.avans.marvelapp.R

class SensorsFragment : Fragment(), SensorEventListener {

    //region instance variables
    //region View instance variables
    private lateinit var ambientTemperatureView: TextView
    private lateinit var lightView: TextView
    private lateinit var pressureView: TextView
    private lateinit var relativeHumidityView: TextView
    //endregion View instance variables

    private lateinit var sensorManager: SensorManager

    private var ambientTemperatureSensor: Sensor? = null
    private var lightSensor: Sensor? = null
    private var pressureSensor: Sensor? = null
    private var relativeHumiditySensor: Sensor? = null
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

        //Initialize instance variables
        sensorManager = view.context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //Initialize sensors
        ambientTemperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        relativeHumiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
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

}