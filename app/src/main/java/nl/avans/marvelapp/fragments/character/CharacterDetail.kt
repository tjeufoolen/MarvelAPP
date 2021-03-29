package nl.avans.marvelapp.fragments.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nl.avans.marvelapp.R

class CharacterDetail : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_character_detail, container, false)
        val character = arguments?.getString("character")
        character?.let{
            view.findViewById<TextView>(R.id.character_detail_name).text = character
        }

        // Inflate the layout for this fragment
        return view
    }
}