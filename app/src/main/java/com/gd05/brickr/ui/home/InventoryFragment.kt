package com.gd05.brickr.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.R
import com.gd05.brickr.database.Repository
import com.gd05.brickr.databinding.FragmentInventoryBinding
import com.gd05.brickr.model.Brick
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InventoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InventoryFragment : Fragment() {

    private lateinit var listener: OnInventoryClickListener
    interface OnInventoryClickListener{
        fun onInventoryBrickClick(brick: Brick)
    }

    //TODO declaramos la variable que va a contener la base de datos
    private lateinit var searchView: SearchView
    private var category: Int? = null


    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: InventoryAdapter

    //TODO variable para almacenar los favoritos del usuario por ahora vacia
    private var inventoryBricks: List<Brick> = emptyList()
    private val viewModel: InventoryViewModel by viewModels { InventoryViewModel.Factory }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_home, menu)
        val searchItem = menu.findItem(R.id.search_view)
        searchView = searchItem.actionView as SearchView

        // Configura un listener para manejar los eventos de búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Este método se llama cuando se envía la búsqueda (p. ej., al presionar "Enter").
                // Puedes realizar la lógica de filtrado aquí.
                viewModel.handleSearchSubmit(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Este método se llama cuando el texto de búsqueda cambia.
                // Puedes realizar la lógica de filtrado en tiempo real aquí.
                viewModel.handleSearchChange(newText)
                return true
            }
        })
        // Muestra el teclado virtual cuando se expande el SearchView
        searchView.setOnSearchClickListener {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInventoryClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnInventoryClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null) {
                // Lógica según la chip seleccionada
                viewModel.handleChipSelection(chip)
            } else {
                viewModel.resetCategory()
            }
        }
        setUpRecyclerView()
    }
    

    private fun setUpRecyclerView() {
        adapter = InventoryAdapter(
            bricks = inventoryBricks,
            onClick = {
                listener.onInventoryBrickClick(it)
            },
            onLongClick = {
                Toast.makeText(
                    context,
                    "Long click on: " + it.name,
                    Toast.LENGTH_SHORT
                ).show()
            },
            onAddClick = {
                viewModel.addBrick(it)
            },
            onRemoveClick = {
                viewModel.removeBrick(it)
            },

            onDestroyClick = {
                viewModel.destroyBrick(it)
            },
            context = context
        )
        with(binding) {
                rvInventoryList.layoutManager = LinearLayoutManager(context)
                rvInventoryList.adapter = adapter
            }
        android.util.Log.d("InventoryFragment", "setUpRecyclerView")
        subscribeInventoryBricksUi(adapter)
    }

    private fun subscribeInventoryBricksUi(adapter: InventoryAdapter) {
        viewModel.inventoryBricks.observe(viewLifecycleOwner) { bricks ->
            adapter.updateData(bricks)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InventoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}