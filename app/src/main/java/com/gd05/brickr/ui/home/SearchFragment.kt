package com.gd05.brickr.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd05.brickr.R
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.data.api.BricksRequest
import com.gd05.brickr.data.mapper.toBrick
import com.gd05.brickr.databinding.FragmentSearchBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.util.BACKGROUND
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO variable para almacenar los favoritos del usuario por ahora vacia
private var searchBricks: List<Brick> = emptyList()

private var _binding: FragmentSearchBinding? = null
private val binding get() = _binding!!
private lateinit var adapter: SearchAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private lateinit var listener: OnSearchClickListener

    interface OnSearchClickListener {
        fun onSearchBrickClick(brick: Brick)
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //db = BrickrDatabase.getInstance(context)!!
        if (context is OnSearchClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSearchClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        if (searchBricks.size != 0) {
            adapter.updateData(searchBricks)
        }
    }

    private fun setUpRecyclerView() {
        adapter = SearchAdapter(
            bricks = searchBricks,
            onClick = {
                listener.onSearchBrickClick(it)
            },
            onLongClick = {

                Toast.makeText(
                    context,
                    "Long click on: " + it.name,
                    Toast.LENGTH_SHORT
                ).show()
            },
            context = context
        )
        with(binding) {
            rvSearchList.layoutManager = LinearLayoutManager(context)
            rvSearchList.adapter = adapter
        }
        android.util.Log.d("SearchFragment", "setUpRecyclerView")
    }

    /**This method is in charge of inflate the menu*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_home, menu)

        val searchItem = menu?.findItem(R.id.action_search)

        /**We find the search item*/
        val searchView = searchItem?.actionView as SearchView
        /**We cast the searchItem to SearchView*/
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /**This method is in charge of handle the text change*/
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            /**This method is in charge of handle the text submit*/
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    BACKGROUND.submit {
                        /* Initialize bricks list before adding bricks*/
                        var bricks: List<Brick> = listOf()

                        val request = BricksRequest(search = query)
                        RebrickableService.searchBricks(request).execute().body()?.let {
                            val bricks = it.results.map { apiBrick -> apiBrick.toBrick() }
                            searchBricks = bricks

                            lifecycleScope.launch {
                                withContext(Dispatchers.Main) {
                                    adapter.updateData(bricks)
                                }
                            }
                        }
                    }
                }
                return true
            }
        }
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LibraryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}