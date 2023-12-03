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
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.data.api.BrickSetBricksResponse
import com.gd05.brickr.data.mapper.toApiBrick
import com.gd05.brickr.data.mapper.toBrick
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.databinding.FragmentBricksetPartsBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.util.BACKGROUND
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class BrickSetPartsFragment : Fragment() {

    private lateinit var listener: OnBrickSetPartsClickListener

    interface OnBrickSetPartsClickListener {
        fun onBrickSetBricksClick(brick: String)
    }

    private lateinit var db: BrickrDatabase
    private var _binding: FragmentBricksetPartsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrickSetPartsAdapter
    private val args: BrickSetPartsFragmentArgs by navArgs()

    private var bricksetBricks: MutableList<Brick> = mutableListOf<Brick>()
    private val amounts = mutableMapOf<String, Int>()
    private val localAmounts = mutableMapOf<String, Int>()
    private lateinit var set: BrickSet

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
        db = context?.let { BrickrDatabase.getInstance(it) }!!
        _binding!!.setBricksRemoveButton.setOnClickListener {
            onRemoveClick()
        }
        _binding!!.setBricksVerifyButton.setOnClickListener {
            onVerifyClick()
        }

        return binding.root
    }

    private fun showMessage(message: String, con: Context) {
        Log.d(
            this.javaClass.canonicalName,
            message
        )
        Toast.makeText(
            con,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun onRemoveClick() {
        showMessage("Eliminando piezas...", requireContext())
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {

                val brickDao = db.brickDao()
                val localBricks = ArrayList<Brick>()

                for (apiBrick in bricksetBricks) {
                    val localBrick = brickDao.findById(apiBrick.brickId)

                    // Si no lo tenemos en el inventario o no hay suficiente cantidad
                    if (localBrick == null || apiBrick.amount > localBrick.amount) {
                        context?.let { showMessage("No tienes todas las piezas", it) }
                        return@withContext
                    }
                    // Reducimos la cantidad en nuestro local
                    localBrick.amount = localBrick.amount - apiBrick.amount

                    // Guardamos en la lista
                    localBricks.add(localBrick)
                }
                // Eliminar las piezas de la BBDD
                localBricks.forEach { brick -> db.brickDao().insert(brick) }
                context?.let {
                    showMessage(
                        "Se han eliminado correctamente todas las piezas del set",
                        it
                    )
                }
                updateLocalAmounts()
            }
        }
    }

    private fun onVerifyClick(){
        var flag = 0
        lifecycleScope.launch {
            while (flag == 0) {
                for ((brickId, quantity) in amounts) {
                    Log.d("BrickSetPartsFragment", "brickId: $brickId, quantity: $quantity")
                    if (db.brickDao().findById(brickId) == null) {
                        flag = 1
                    } else if (db.brickDao().findById(brickId).amount != amounts[brickId]) {
                        flag = 1
                    }
                }
            }
        }
        lifecycleScope.launch {
            delay(100)
            if(flag == 1){
                Toast.makeText(
                    context,
                    "No tienes todas las piezas del set",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Tienes todas las piezas del set",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        set = args.brickSet
        loadBrickSetParts()
        setUpRecyclerView()
    }

    private fun updateLocalAmounts() {
        bricksetBricks.forEach { apiBrick ->
            run {
                lifecycleScope.launch {
                    val localBrick = db.brickDao().findById(apiBrick.brickId)
                    if (localBrick != null)
                        localAmounts.put(apiBrick.brickId, localBrick.amount)
                    else
                        localAmounts.put(apiBrick.brickId, 0)
                    adapter.updateData(bricksetBricks, amounts, localAmounts)
                }
            }
        }
    }

    private fun loadBrickSetParts() {
        if(!bricksetBricks.isEmpty()) {
            updateLocalAmounts()
            return
        }
        BACKGROUND.submit {
            var page = 1
            var hasNext: Boolean

            do {
                val response =
                    RebrickableService.getSetBricks(setNum = set.brickSetId, page = page, 100)
                        .execute()

                response.body()?.results?.forEach { element ->
                    run {
                        val apiBrick = element.toBrick()
                        bricksetBricks.add(apiBrick)
                        amounts.put(apiBrick.brickId, apiBrick.amount)
                    }
                }
                updateLocalAmounts()
                /*lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        adapter.updateData(bricksetBricks, amounts, localAmounts)
                    }
                }*/
                page++
                hasNext = response.body()?.next != null

                Thread.sleep(1000)
            } while (hasNext)
        }
    }

    private fun setUpRecyclerView() {
        adapter = BrickSetPartsAdapter(
            bricks = bricksetBricks,
            amounts = amounts,
            localAmount = localAmounts,
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
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BrickSetPartsFragment()
    }

}