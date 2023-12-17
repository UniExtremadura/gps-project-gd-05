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
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.data.mapper.toBrick
import com.gd05.brickr.database.Repository
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
        fun onBrickSetBricksClick(brick: Brick)
    }

    private lateinit var repository: Repository
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
        val appContainer = (this.activity?.application as BrickrApplication).appContainer
        repository = appContainer.repository
        _binding!!.setBricksRemoveButton.setOnClickListener {
            onRemoveClick()
        }
        _binding!!.setBricksVerifyButton.setOnClickListener {
            onVerifyClick()
        }
        _binding!!.setBricksAddButton.setOnClickListener {
            onAddClick()
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

    private fun onAddClick() {
        showMessage("Guardando piezas en el inventario...", requireContext())
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val localBricksList = ArrayList<Brick>()

                // Iteramos sobre la lista de piezas del set
                for (apiBrick in bricksetBricks) {
                    //Buscamos la pieza en nuestra base de datos
                    val localBrick = repository.publicGetBrickById(apiBrick.brickId)
                    //Si la pieza existe en el inventario, se suma la cantidad, si no, se inserta la nueva pieza
                    if(localBrick == null){
                        localBricksList.add(apiBrick)
                    }else{
                        // Sumamos la cantidad de piezas del set a las que ya tenemos
                        localBrick.amount = localBrick.amount + apiBrick.amount
                        localBricksList.add(localBrick)
                    }
                }
                // Guarda las cantidades de las piezas modificadas en la BBDD
                localBricksList.forEach { brick -> repository.publicInsertBrick(brick) }
                // Se muestra un mensaje de confirmación en pantalla, pero no se realiza ninguna navegación
                context?.let {
                    showMessage(
                        "¡Se han guardado correctamente todas las piezas del set!",
                        it
                    )
                }
                updateLocalAmounts()
            }

        }
    }

    private fun onRemoveClick() {
        showMessage("Eliminando piezas...", requireContext())
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {


                val localBricks = ArrayList<Brick>()

                for (apiBrick in bricksetBricks) {
                    val localBrick = repository.publicGetBrickById(apiBrick.brickId)

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
                localBricks.forEach { brick -> repository.publicInsertBrick(brick) }
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
        showMessage("Verificando piezas...", requireContext())
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                for (apiBrick in bricksetBricks) {
                    val localBrick = repository.publicGetBrickById(apiBrick.brickId)
                    // Si no lo tenemos en el inventario o no hay suficiente cantidad
                    if (localBrick == null || apiBrick.amount > localBrick.amount) {
                        context?.let { showMessage("No tienes todas las piezas", it) }
                        return@withContext
                    }
                }
                context?.let{showMessage("Tienes todas las piezas del set", it)}
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
                    val localBrick = repository.publicGetBrickById(apiBrick.brickId)
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
                        val existingBrick = bricksetBricks.find { it.brickId == apiBrick.brickId }

                        if (existingBrick != null) {
                            // Si ya existe, sumar la cantidad
                            existingBrick.amount += apiBrick.amount
                            // Actualizar la cantidad en el mapa también
                            amounts[apiBrick.brickId] = existingBrick.amount
                        } else {
                            // Si no existe, agregar la nueva pieza a la lista y al mapa
                            bricksetBricks.add(apiBrick)
                            amounts.put(apiBrick.brickId, apiBrick.amount)
                        }

                    }
                }
                updateLocalAmounts()
                page++
                hasNext = response.body()?.next != null

                Thread.sleep(1000)
            } while (hasNext)
        }
    }

    private fun loadBrickSetParts2(){
        lifecycleScope.launch {repository.publicGetBricksOfBrickSet(set.brickSetId)}

        updateLocalAmounts()
    }

    private fun setUpRecyclerView() {
        adapter = BrickSetPartsAdapter(
            bricks = bricksetBricks,
            amounts = amounts,
            localAmount = localAmounts,
            onClick = {
                listener.onBrickSetBricksClick(it)
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