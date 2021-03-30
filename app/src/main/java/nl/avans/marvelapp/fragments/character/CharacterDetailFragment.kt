package nl.avans.marvelapp.fragments.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import nl.avans.marvelapp.R
import nl.avans.marvelapp.models.Character
import nl.avans.marvelapp.repositories.utils.VolleyRequestQueue

private const val CHARACTER_ARGUMENT = "character"

class CharacterDetailFragment : Fragment() {
    private var character: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            character = it.getParcelable(CHARACTER_ARGUMENT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_character_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFields(view)
    }

    private fun setFields(view: View){
        view.findViewById<TextView>(R.id.tvCharacterDetailName)?.text = character?.name
        setDescription(character?.description)
        setImage(view)
    }

    private fun setImage(view: View) {
        val photoView = view.findViewById<NetworkImageView>(R.id.nivCharacterDetailPhoto)
        val imgUrl = character?.thumbnail?.url
        val imgLoader = VolleyRequestQueue.getInstance(view.context).imageLoader
        photoView.setDefaultImageResId(R.drawable.loading)
        photoView.setErrorImageResId(R.drawable.error)
        photoView.setImageUrl(imgUrl, imgLoader)
    }
    private fun setDescription(description: String?){
        val textView = view?.findViewById<TextView>(R.id.tvCharacterDetailDescription)

        if(description != null && description.isNotEmpty()){
            textView?.text = description
            return
        }

        textView?.text = context?.resources?.getString(R.string.description_unavailable)
    }

    companion object{
        fun newInstance(character: Character) =
            CharacterDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHARACTER_ARGUMENT, character)
                }
            }
    }
}