package com.gd05.brickr.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd05.brickr.R
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.data.mapper.toApiBrick
import com.gd05.brickr.data.mapper.toBrick
import com.gd05.brickr.databinding.FragmentBricksetPartsBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.util.BACKGROUND
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrickSetPartsFragment : Fragment() {

    private lateinit var listener: OnBrickSetPartsClickListener

    interface OnBrickSetPartsClickListener {
        fun onBrickSetBricksClick(brick: String)
    }

    private var _binding: FragmentBricksetPartsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrickSetPartsAdapter
    private val args: BrickSetPartsFragmentArgs by navArgs()

    private var bricksetBricks: MutableList<Brick> = mutableListOf<Brick>()
    private val amounts = mutableMapOf<String, Int>()
    private var brickSet: BrickSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BrickSetPartsFragment.OnBrickSetPartsClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnBrickPartsClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBricksetPartsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brickSet = args.brickSet
        loadBrickSetParts()
        setUpRecyclerView()
    }

    private fun loadBrickSetParts() {
        if (brickSet != null) {
            var next: String? = null
            var i: Int = 1
            BACKGROUND.submit {
                do {
                    RebrickableService.getSetBricks(setNum = brickSet!!.brickSetId, i, 100)
                        .execute()
                        .body()?.let {

                            /* Add parts to list without duplicates */
                            it.results.forEach { apiBrick ->
                                apiBrick.part!!.let { brick ->
                                    if (!amounts.contains(brick.partNum!!)) {
                                        bricksetBricks.add(brick.toApiBrick().toBrick())
                                        amounts[brick.partNum!!] = apiBrick.quantity!!
                                    } else {
                                        amounts[brick.partNum!!] =
                                            amounts[brick.partNum!!]!! + apiBrick.quantity!!
                                    }
                                }
                            }
                            lifecycleScope.launch {
                                withContext(Dispatchers.Main) {
                                    Log.d("BrickSetDetailFragment", "SIZE ${bricksetBricks.size}")
                                    Toast.makeText(
                                        context,
                                        R.string.brickset_bricks_loaded,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    adapter.updateData(bricksetBricks)
                                }
                            }
                            i++
                            next = it.next

                        }
                    Thread.sleep(1000)
                } while (next != null)
            }
        }
    }

    private fun setUpRecyclerView() {
        Log.d("BrickSetPartsFragment", "setUpRecyclerView - SIZE = ${bricksetBricks.size}")
        adapter = BrickSetPartsAdapter(
            bricks = bricksetBricks,
            amounts = amounts,
            onClick = {
                listener.onBrickSetBricksClick(it.brickId!!)
            },
            onLongClick = {
                Toast.makeText(
                    context,
                    "Long click on: " + it.name,
                    Toast.LENGTH_SHORT
                ).show()
            },
            onAddClick = {
                Toast.makeText(
                    context,
                    "Add: " + it.name,
                    Toast.LENGTH_SHORT
                ).show()
            },
            onRemoveClick = {
                Toast.makeText(
                    context,
                    "Remove: " + it.name,
                    Toast.LENGTH_SHORT
                ).show()
            },
            context = context
        )
        with(binding) {
            rvSetBricksList.layoutManager = LinearLayoutManager(context)
            rvSetBricksList.adapter = adapter
        }
        android.util.Log.d("BrickSetPartsFragment", "setUpRecyclerView")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment BrickDetailFragment.
         */
        @JvmStatic
        fun newInstance() =
            BrickSetDetailFragment()
    }

}