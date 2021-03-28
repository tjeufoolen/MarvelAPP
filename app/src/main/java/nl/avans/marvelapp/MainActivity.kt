package nl.avans.marvelapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import nl.avans.marvelapp.fragments.CharactersFragment
import nl.avans.marvelapp.fragments.ComicsFragment
import nl.avans.marvelapp.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize fragments
        val charactersFragment = CharactersFragment()
        val comicsFragment = ComicsFragment()
        val settingsFragment = SettingsFragment()

        // Set default startup fragment
        setCurrentFragment(charactersFragment)

        // Handle bottom bar navigation
        findViewById<BottomNavigationView>(R.id.bnvNavigationBar).setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.iCharacters -> setCurrentFragment(charactersFragment)
                R.id.iComics -> setCurrentFragment(comicsFragment)
                R.id.iSettings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }


//    private lateinit var characterRepository: CharacterRepository
//    private var recyclerView: RecyclerView? = null
//    private var recyclerViewAdapter: RecyclerViewAdapter? = null
//    var rowsArrayList: ArrayList<Character?> = ArrayList()
//    var isLoading = false


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        recyclerView = findViewById(R.id.recyclerView)
//        characterRepository = CharacterRepository(this)
//
//        populateData()
//        initAdapter()
//        initScrollListener()
//    }

//    private fun populateData() {
//        characterRepository.getAll { characterList ->
//            characterList?.forEach{ character ->
//                rowsArrayList.add(character)
//            }
//            recyclerViewAdapter!!.notifyDataSetChanged()
//        }
//    }
//
//    private fun initAdapter() {
//        recyclerViewAdapter = RecyclerViewAdapter(rowsArrayList)
//        recyclerView!!.adapter = recyclerViewAdapter
//    }
//
//    private fun initScrollListener() {
//        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
//                if (!isLoading) {
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) {
//                        //bottom of list!
//                        recyclerView.post {
//                            loadMore()
//                        }
//                        isLoading = true
//                    }
//                }
//            }
//        })
//    }
//
//    private fun loadMore() {
//        rowsArrayList.add(null)
//        recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
//        Handler(Looper.getMainLooper()).postDelayed({
//            rowsArrayList.removeAt(rowsArrayList.size - 1)
//            val scrollPosition: Int = rowsArrayList.size
//            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)
//
//            characterRepository.getPaginated(scrollPosition) { newCharacters ->
//                newCharacters?.forEach { character ->
//                    rowsArrayList.add(character)
//                }
//                recyclerViewAdapter!!.notifyDataSetChanged()
//                isLoading = false
//            }
//
//        }, 2000)
//    }
}