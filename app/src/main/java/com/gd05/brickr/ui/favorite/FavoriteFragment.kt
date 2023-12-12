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
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.database.Repository
import com.gd05.brickr.databinding.FragmentFavoriteBinding
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.ui.search.FavoriteAdapter
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {
    private var loadedSets: List<BrickSet> = emptyList()
    private lateinit var setsAdapter: FavoriteAdapter
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var db: BrickrDatabase
    private lateinit var repository: Repository
    private lateinit var listener: OnFavoriteClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFavoriteClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSearchClickListener")
        }
        db = BrickrDatabase.getInstance(context)!!
        repository = Repository.getInstance(
            db.brickDao(),
            db.brickSetDao(),
            db.categoryDao(),
            db.themeDao(),
            RebrickableService
        )
    }

    override fun onStart() {
        super.onStart()
        subscribeSetsUi(setsAdapter)
        lifecycleScope.launch {
            repository.publicFavoriteBrickSet()
        }
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

    private fun subscribeSetsUi(setsAdapter: FavoriteAdapter) {
        repository.favoriteSets.observe(viewLifecycleOwner) { sets ->
            setsAdapter.updateData(sets)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply{}
            }
    }
}