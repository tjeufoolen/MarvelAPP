package nl.avans.marvelapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.avans.marvelapp.R
import nl.avans.marvelapp.RecyclerViewAdapter
import nl.avans.marvelapp.models.Character
import nl.avans.marvelapp.services.CharacterRepository

class CharactersFragment : Fragment() {

    private lateinit var characterRepository: CharacterRepository
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    var rowsArrayList: ArrayList<Character?> = ArrayList()
    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        characterRepository = CharacterRepository(view.context)

        populateData()
        initAdapter()
        initScrollListener()
    }

    private fun populateData() {
        characterRepository.getAll { characterList ->
            characterList?.forEach{ character ->
                rowsArrayList.add(character)
            }
            recyclerViewAdapter!!.notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter(rowsArrayList)
        recyclerView!!.adapter = recyclerViewAdapter
    }

    private fun initScrollListener() {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) {
                        //bottom of list!
                        recyclerView.post {
                            loadMore()
                        }
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        rowsArrayList.add(null)
        recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        Handler(Looper.getMainLooper()).postDelayed({
            rowsArrayList.removeAt(rowsArrayList.size - 1)
            val scrollPosition: Int = rowsArrayList.size
            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)

            characterRepository.getPaginated(scrollPosition) { newCharacters ->
                newCharacters?.forEach { character ->
                    rowsArrayList.add(character)
                }
                recyclerViewAdapter!!.notifyDataSetChanged()
                isLoading = false
            }

        }, 2000)
    }

}