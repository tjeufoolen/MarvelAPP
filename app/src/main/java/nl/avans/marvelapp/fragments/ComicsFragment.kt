package nl.avans.marvelapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.avans.marvelapp.R
import nl.avans.marvelapp.fragments.comics.ComicDetailFragment
import nl.avans.marvelapp.fragments.comics.ComicListFragment
import nl.avans.marvelapp.models.Comic

class ComicsFragment : Fragment(), ComicListFragment.IOnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            childFragmentManager.beginTransaction().replace(R.id.fmvComicContainer, ComicListFragment(), "comicList").commit()
        }
    }

    override fun onComicSelected(comic: Comic){
        val fragment = ComicDetailFragment.newInstance(comic)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            childFragmentManager.beginTransaction()
                .replace(R.id.fmvComicContainer, fragment, "comicDetail")
                .addToBackStack(null)
                .commit()
            return
        }

        childFragmentManager.beginTransaction().replace(R.id.fcvComicDetails, fragment, "comicDetail").commit()
    }

}