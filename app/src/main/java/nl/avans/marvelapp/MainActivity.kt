package nl.avans.marvelapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nl.avans.marvelapp.services.CharacterRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        CharacterRepository(this).getAll { array ->
//            if (array != null) {
//                findViewById<TextView>(R.id.message).text = array[0].thumbnail.url
//            }
//        }

        CharacterRepository(this).getById(1011334) { obj ->
            if (obj != null) {
                findViewById<TextView>(R.id.message).text = obj.thumbnail.url
            }
        }
    }
}