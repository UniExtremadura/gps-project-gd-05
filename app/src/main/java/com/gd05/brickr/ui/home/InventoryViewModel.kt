package com.gd05.brickr.ui.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.R
import com.gd05.brickr.database.Repository
import com.gd05.brickr.model.Brick
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val repository: Repository

): ViewModel(){
    var inventoryBricks = repository.inventoryBricks

    private var currentCategory: Int? = null
    private var currentQuery: String? = null


    private val _toast = MutableLiveData<String?>()
    val toast: MutableLiveData<String?>
        get() = _toast

    fun onToastShown() {
        _toast.value = null
    }



    fun getFilteredInventoryBricks(categoryId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.publicGetFilteredInventoryBricks(categoryId)
        }
    }

    fun getSearchedFilteredInventoryBricks(query: String, categoryId: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.publicGetSearchedFilteredInventoryBricks(query, categoryId)
        }
    }

    fun getSearchedInventoryBricks(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            query?.let { repository.publicGetSearchedInventoryBricks(query) }
        }
    }

    fun handleChipSelection(chip: Chip) {
        currentCategory = when (chip.id) {
            R.id.chip1 -> 1
            R.id.chip2 -> 11
            R.id.chip3 -> 9
            R.id.chip4 -> 23
            else -> null
        }

        if (currentCategory != null) {
            getFilteredInventoryBricks(currentCategory!!)
            _toast.value = "Seleccionaste: ${chip.text}"
        } else {
            resetCategory()
        }
    }

    fun resetCategory() {
        currentCategory = null
    }

    fun handleSearchSubmit(query: String?) {
        currentQuery = query
        loadSearchInventory()
    }

    fun handleSearchChange(newText: String?) {
        currentQuery = newText
        loadSearchInventory()
    }

    private fun loadSearchInventory() {
        viewModelScope.launch(Dispatchers.Main) {
            currentQuery?.let { getSearchedInventoryBricks(currentQuery!!) }

        }
    }


    fun addBrick(brick: Brick) {
        viewModelScope.launch(Dispatchers.Main) {
            brick.amount++
            repository.publicInsertBrick(brick)
        }
    }

    fun removeBrick(brick: Brick) {
        viewModelScope.launch(Dispatchers.Main) {
            if (brick.amount > 1) {
                brick.amount--
                repository.publicInsertBrick(brick)
            }
        }
    }

    fun destroyBrick(brick: Brick) {
        viewModelScope.launch(Dispatchers.Main) {
            brick.amount = 0
            repository.publicInsertBrick(brick)
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object :
            ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return InventoryViewModel(
                    (application as BrickrApplication).appContainer.repository,
                ) as T
            }
        }
    }
}
