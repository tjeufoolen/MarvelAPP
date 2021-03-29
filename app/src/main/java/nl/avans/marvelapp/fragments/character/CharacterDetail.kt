package nl.avans.marvelapp.fragments.character

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import nl.avans.marvelapp.R
import nl.avans.marvelapp.models.Character
import nl.avans.marvelapp.services.utils.VolleyRequestQueue
import java.util.*
import kotlin.concurrent.schedule

private const val CHARACTER_ARGUMENT = "character"

class CharacterDetail : Fragment() {
    private var character: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            character = it.getParcelable(CHARACTER_ARGUMENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_character_detail, container, false)
        setFields(view)

        // Inflate the layout for this fragment
        return view
    }

    private fun setFields(view: View){
        view.findViewById<TextView>(R.id.tvCharacterDetailName)?.text = character?.name
        setImage(view)
    }

    private fun setImage(view: View) {
        val photoView = view.findViewById<NetworkImageView>(R.id.nivCharacterDetailPhoto)
        val imgUrl = character?.thumbnail?.url
        val imgLoader = VolleyRequestQueue.getInstance(view.context).imageLoader
        photoView.setDefaultImageResId(R.drawable.ic_launcher_background)
        photoView.setErrorImageResId(R.drawable.ic_launcher_foreground)
        photoView.setImageUrl(character?.thumbnail?.url, imgLoader)
    }

    companion object{
        fun newInstance(character: Character) =
            CharacterDetail().apply {
                arguments = Bundle().apply {
                    putParcelable(CHARACTER_ARGUMENT, character)
                }
            }
    }
}