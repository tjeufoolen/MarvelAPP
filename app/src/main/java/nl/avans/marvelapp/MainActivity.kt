package nl.avans.marvelapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nl.avans.marvelapp.services.CharacterRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CharacterRepository(this).getAll { array ->
            findViewById<TextView>(R.id.message).text = array[0].name
        }
    }
}