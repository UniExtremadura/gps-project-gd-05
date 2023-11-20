package com.gd05.brickr.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gd05.brickr.R
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.databinding.FragmentInventoryBinding
import com.gd05.brickr.dummy.dummyBricks
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.Category
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
        fun onBrickClick(brick: Brick)
    }

    //TODO declaramos la variable que va a contener la base de datos
    private lateinit var db: BrickrDatabase

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: InventoryAdapter

    //TODO variable para almacenar los favoritos del usuario por ahora vacia
    private var inventoryBricks: List<Brick> = emptyList()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BrickrDatabase.getInstance(context)!!
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

    private fun loadDatabase(){
        lifecycleScope.launch {
            var cat = Category(9, "prueba")
            db.categoryDao().insertCategory(cat)
            var brickExample = Brick("5", "prueba", 9, 2002, 2006, "b", "a", 5)
            db.brickDao().insert(brickExample)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO Metodos para cargar la base de datos y el inventario, eliminar solo loadDatabase cuando se implemente la API
        loadDatabase()
        loadInventory()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        adapter = InventoryAdapter(
            bricks = inventoryBricks,
            onClick = {
                listener.onBrickClick(it)
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
                rvInventoryList.layoutManager = LinearLayoutManager(context)
                rvInventoryList.adapter = adapter
            }
        android.util.Log.d("InventoryFragment", "setUpRecyclerView")
    }

    private fun loadInventory(){
        lifecycleScope.launch {
            //TODO metodo para obtener las piezas del inventario
            inventoryBricks = db.brickDao().getInventoryBricks()
            adapter.updateData(inventoryBricks)
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