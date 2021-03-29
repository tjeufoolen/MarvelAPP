package nl.avans.marvelapp.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import nl.avans.marvelapp.R
import nl.avans.marvelapp.fragments.character.CharacterDetail
import nl.avans.marvelapp.fragments.character.CharacterListFragment
import nl.avans.marvelapp.models.Character


class CharactersFragment : Fragment(), CharacterListFragment.IOnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            childFragmentManager.beginTransaction().replace(R.id.fmvCharacterContainer, CharacterListFragment(), "characterList").commit()
        }
    }

    override fun onCharacterSelected(character: Character){
        val fragment = CharacterDetail()
        fragment.arguments = bundleOf("character" to character.name)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            childFragmentManager.beginTransaction()
                .replace(R.id.fmvCharacterContainer, fragment, "characterDetail")
                .addToBackStack(null)
                .commit()

            val somethingwithdebugging = childFragmentManager.backStackEntryCount

            return
        }

        childFragmentManager.beginTransaction().replace(R.id.fcvCharacterDetails, fragment, "characterDetail").commit()
    }
}