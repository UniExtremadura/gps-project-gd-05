package com.gd05.brickr.ui.search

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd05.brickr.R
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.data.api.BricksRequest
import com.gd05.brickr.data.api.SearchRequest
import com.gd05.brickr.data.mapper.toBrick
import com.gd05.brickr.data.mapper.toSet
import com.gd05.brickr.databinding.FragmentSearchBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.util.BACKGROUND
import com.gd05.brickr.util.hideKeyboardFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Inicializar con "sets" marcado
//
class SearchFragment : Fragment() {
    private var loadedBricks: List<Brick> = emptyList()
    private lateinit var bricksAdapter: SearchBricksAdapter

    private var loadedSets: List<BrickSet> = emptyList()
    private lateinit var setsAdapter: SearchSetsAdapter

    private lateinit var binding: FragmentSearchBinding
    private lateinit var listener: OnSearchClickListener
    private lateinit var searchView: SearchView

    interface OnSearchClickListener {
        fun onSearchBrickClick(brick: Brick)
        fun onSearchSetClick(set: BrickSet)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSearchClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.rvSearchList.layoutManager = LinearLayoutManager(context)

        binding.searchSetsButton.setOnClickListener { onSearchSetsButton() }

        binding.searchBricksButton.setOnClickListener { onSearchBricksButton() }

        initializeAdapters()
        return binding.root
    }

    private fun onSearchSetsButton() {
        binding.searchBricksButton.isChecked = false
        repaintSetsToShow()
    }

    private fun onSearchBricksButton() {
        binding.searchSetsButton.isChecked = false
        repaintBricksToShow()
    }

    override fun onStart() {
        super.onStart()
        loadElementsOnList()
    }

    fun loadElementsOnList() {
        if (!loadedBricks.isEmpty() && binding.searchBricksButton.isChecked) {
            repaintBricksToShow()
        } else if (!loadedSets.isEmpty() && binding.searchSetsButton.isChecked) {
            repaintSetsToShow()
        }
    }

    fun repaintBricksToShow() {
        binding.rvSearchList.adapter = bricksAdapter
        bricksAdapter.updateData(loadedBricks)
    }

    fun repaintSetsToShow() {
        binding.rvSearchList.adapter = setsAdapter
        setsAdapter.updateData(loadedSets)
    }

    private fun initializeAdapters() {
        initializeBricksAdapter()
        initializeSetsAdapter()
    }

    private fun initializeBricksAdapter() {
        bricksAdapter = SearchBricksAdapter(
            bricks = loadedBricks,
            onClick = {
                listener.onSearchBrickClick(it)
            },
            onLongClick = {
                Toast.makeText(
                    context, "Long click on: " + it.name,
                    Toast.LENGTH_SHORT
                ).show()
            },
            context = context
        )
    }

    private fun initializeSetsAdapter() {
        setsAdapter = SearchSetsAdapter(
            sets = loadedSets,
            onClick = {
                listener.onSearchSetClick(it)
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
    }

    /**This method is in charge of inflate the menu*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_home, menu)

        searchView = menu.findItem(R.id.search_view)?.actionView as SearchView

        /**We cast the searchItem to SearchView*/
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /**This method is in charge of handle the text change*/
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboardFrom(context, view)
                doSearch(query)
                return true
            }
        }
        )
    }

    private fun doSearch(query: String?) {
        if (query == null)
            return
        if (binding.searchBricksButton.isChecked) {
            searchBricks(query)
        } else if (binding.searchSetsButton.isChecked) {
            searchSets(query)
        } else {
            Toast.makeText(context, "No se ha seleccionado un tipo de búsqueda", Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun searchSets(query: String) {
        BACKGROUND.submit {
            val request = SearchRequest(search = query)
            RebrickableService.searchSet(request).execute().body()?.let {
                loadedSets = it.results.map { apiSet -> apiSet.toSet() }
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        repaintSetsToShow()
                    }
                }
            }
        }
    }

    private fun searchBricks(query: String) {
        BACKGROUND.submit {
            val request = BricksRequest(search = query)
            RebrickableService.searchBricks(request).execute().body()?.let {
                loadedBricks = it.results.map { apiBrick -> apiBrick.toBrick() }
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        repaintBricksToShow()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}