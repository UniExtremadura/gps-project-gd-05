package com.gd05.brickr.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd05.brickr.databinding.FragmentFavoriteBinding
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.ui.search.FavoriteAdapter

class FavoriteFragment : Fragment() {
    private var loadedSets: List<BrickSet> = emptyList()
    private lateinit var setsAdapter: FavoriteAdapter
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var listener: OnFavoriteClickListener

    private val viewModel: FavoriteViewModel by viewModels { FavoriteViewModel.Factory }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFavoriteClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSearchClickListener")
        }
    }

    override fun onStart() {
        super.onStart()
        subscribeSetsUi(setsAdapter)
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
            },
            context = context
        )

        binding.setsList.adapter = setsAdapter
    }

    private fun subscribeSetsUi(setsAdapter: FavoriteAdapter) {
        viewModel.favoriteSets.observe(viewLifecycleOwner) { sets ->
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