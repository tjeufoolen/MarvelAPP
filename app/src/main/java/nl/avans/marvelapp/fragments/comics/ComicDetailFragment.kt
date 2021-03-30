package nl.avans.marvelapp.fragments.comics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.NetworkImageView
import nl.avans.marvelapp.R
import nl.avans.marvelapp.models.Comic
import nl.avans.marvelapp.repositories.utils.VolleyRequestQueue
import org.w3c.dom.Text

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
    }

    private fun setImage(view: View) {
        val photoView = view.findViewById<NetworkImageView>(R.id.nivComicDetailPhoto)
        val imgUrl = comic?.thumbnail?.url
        val imgLoader = VolleyRequestQueue.getInstance(view.context).imageLoader
        photoView.setDefaultImageResId(R.drawable.ic_comics_black)
        photoView.setErrorImageResId(R.drawable.ic_comics_black)
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

    companion object {
        fun newInstance(comic: Comic) =
            ComicDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(COMIC_ARGUMENT, comic)
                }
            }
    }
}