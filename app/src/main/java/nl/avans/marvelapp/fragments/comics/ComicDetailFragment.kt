package nl.avans.marvelapp.fragments.comics

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
import nl.avans.marvelapp.models.Comic
import nl.avans.marvelapp.repositories.utils.VolleyRequestQueue

private const val COMIC_ARGUMENT = "comic"

class ComicDetailFragment : Fragment() {
    private var comic: Comic? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            comic = it.getParcelable(COMIC_ARGUMENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comic_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFields(view)
    }

    private fun setFields(view: View){
        view.findViewById<TextView>(R.id.tvComicDetailName)?.text = comic?.title
        view.findViewById<TextView>(R.id.tvComicDetailEanValue)?.text = comic?.ean
        view.findViewById<TextView>(R.id.tvComicDetailIsbnValue)?.text = comic?.ean
        view.findViewById<TextView>(R.id.tvComicDetailPagesValue)?.text = comic?.pageCount?.toString()
        setDescription(comic?.description)
        setImage(view)
        setButton(view)
    }

    private fun setButton(view: View) {
        val btn = view.findViewById<Button>(R.id.btnComicDetailShare)
        btn.setOnClickListener {
            share()
        }
    }

    private fun setImage(view: View) {
        val photoView = view.findViewById<NetworkImageView>(R.id.nivComicDetailPhoto)
        val imgUrl = comic?.thumbnail?.url
        val imgLoader = VolleyRequestQueue.getInstance(view.context).imageLoader
        photoView.setDefaultImageResId(R.drawable.loading)
        photoView.setErrorImageResId(R.drawable.error)
        photoView.setImageUrl(imgUrl, imgLoader)
    }

    private fun setDescription(description: String?){
        val textView = view?.findViewById<TextView>(R.id.tvComicDetailDescription)

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
            Toast.makeText(context, context?.resources?.getString(R.string.no_shareable_app_installed), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun generateShareDescription() :String{
        var string = ""

        val shareGreeting = context?.resources?.getString(R.string.share_comic_greeting)
        val shareMentionDescription = context?.resources?.getString(R.string.share_comic_mention_description)
        val shareLookAtHim = context?.resources?.getString(R.string.share_comic_look_at_it)

        string += "$shareGreeting\n"
        string += comic?.title
        string += "\n\n"

        if(!comic?.description.isNullOrEmpty()){
            string += "$shareMentionDescription\n"
            string += comic?.description
            string += "\n\n"
        }

        string += "$shareLookAtHim\n"
        string += comic?.thumbnail?.url

        return string
    }

    companion object {
        fun newInstance(comic: Comic) =
            ComicDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(COMIC_ARGUMENT, comic)
                }
            }
    }
}