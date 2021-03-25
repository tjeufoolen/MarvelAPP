package nl.avans.marvelapp

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nl.avans.marvelapp.models.Character
import nl.avans.marvelapp.services.CharacterRepository

class MainActivity : AppCompatActivity() {
    lateinit var characterRepository: CharacterRepository
    var recyclerView: RecyclerView? = null
    var recyclerViewAdapter: RecyclerViewAdapter? = null
    var rowsArrayList: ArrayList<Character?> = ArrayList()
    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        characterRepository = CharacterRepository(this)

    populateData()
        initAdapter()
        initScrollListener()
    }

    private fun populateData() {
        characterRepository.getAll { characterlist ->
            characterlist?.forEach{ character ->
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
                        recyclerView.post(Runnable(){
                            loadMore()
                        })
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        rowsArrayList.add(null)
        recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        val handler = Handler()
        handler.postDelayed(Runnable {
            rowsArrayList.removeAt(rowsArrayList.size - 1)
            val scrollPosition: Int = rowsArrayList.size
            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)

            characterRepository.getPaginated(scrollPosition){ newCharacters ->
                newCharacters?.forEach{ character ->
                    rowsArrayList.add(character)
                }
                recyclerViewAdapter!!.notifyDataSetChanged()
                isLoading = false
            }

        }, 2000)
    }
}