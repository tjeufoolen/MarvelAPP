package nl.avans.marvelapp.fragments.character

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        setButton(view)
    }

    private fun setButton(view: View) {
        val btn = view.findViewById<Button>(R.id.btnCharacterDetailShare)
        btn.setOnClickListener {
            share()
        }
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

    private fun share(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, generateShareDescription())
        try {
            requireActivity().startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "No app installed that can share this!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun generateShareDescription() :String{
        var string = ""

        val shareGreeting = context?.resources?.getString(R.string.share_greeting)
        val shareMentionDescription = context?.resources?.getString(R.string.share_mention_description)
        val shareLookAtHim = context?.resources?.getString(R.string.share_look_at_him)

        string += "$shareGreeting\n"
        string += character?.name
        string += "\n\n"

        if(!character?.description.isNullOrEmpty()){
            string += "$shareMentionDescription And here a description:\n"
            string += character?.description
            string += "\n\n"
        }

        string += "$shareLookAtHim\n"
        string += character?.thumbnail?.url

        return string
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