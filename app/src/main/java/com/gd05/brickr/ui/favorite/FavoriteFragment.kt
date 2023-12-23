package com.gd05.brickr.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd05.brickr.R
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.databinding.FragmentFavoriteBinding
import com.gd05.brickr.dummy.favoriteSet
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.ui.search.FavoriteAdapter
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private var loadedSets: List<BrickSet> = emptyList()
    private lateinit var setsAdapter: FavoriteAdapter
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var database: BrickrDatabase
    private lateinit var listener: OnFavoriteClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFavoriteClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSearchClickListener")
        }
        database = BrickrDatabase.getInstance(context)!!
    }

    override fun onStart() {
        super.onStart()
        Thread.sleep(1000)
        lifecycleScope.launch { database.brickSetDao().insert(favoriteSet[0]) }
        Thread.sleep(1000)
        lifecycleScope.launch {
            loadedSets = database.brickSetDao().findFavorites()
            setsAdapter.updateData(loadedSets)
        }
        if(!loadedSets.isEmpty())
            setsAdapter.updateData(loadedSets)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        binding.setsList.layoutManager = LinearLayoutManager(context)
        initializeSetsAdapter()
        return binding.root
    }
    private fun initializeSetsAdapter() {
        setsAdapter = FavoriteAdapter(
            sets = loadedSets,
            onClick = {
                listener.onFavoriteClickListener(it)
            },
            onLongClick = {
                Toast.makeText(
                    context,
                    it.name,
                    Toast.LENGTH_SHORT
                ).show()
            },
            context = context
        )

        binding.setsList.adapter = setsAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply{}
            }
    }
}