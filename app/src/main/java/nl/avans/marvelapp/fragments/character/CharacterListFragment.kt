package nl.avans.marvelapp.fragments.character

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.avans.marvelapp.R
import nl.avans.marvelapp.RecyclerViewAdapter
import nl.avans.marvelapp.fragments.CharactersFragment
import nl.avans.marvelapp.models.Character
import nl.avans.marvelapp.services.CharacterRepository
import java.io.InvalidClassException

class CharacterListFragment : Fragment() {

    interface IOnClickListener{
        fun onCharacterSelected(character: Character)
    }

    private lateinit var listener: IOnClickListener
    private lateinit var characterRepository: CharacterRepository
    private lateinit var recyclerView: RecyclerView

    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    var rowsArrayList: ArrayList<Character?> = ArrayList()
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listener = parentFragment as IOnClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvCharacterList)
        characterRepository = CharacterRepository(view.context)

        populateData()
        initAdapter()
        initScrollListener()
        addClickListener()
    }

    private fun addClickListener() {
        recyclerView.addOnItemClickListener(listener)
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
        recyclerView.adapter = recyclerViewAdapter
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun RecyclerView.addOnItemClickListener(onClickListener: IOnClickListener) {
        this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    rowsArrayList[holder.adapterPosition]?.let { selectedCharacter ->
                        onClickListener.onCharacterSelected(
                            selectedCharacter
                        )
                    }
                }
            }
        })
    }
}