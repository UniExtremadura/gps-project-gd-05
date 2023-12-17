package com.gd05.brickr.ui.search

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.database.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel (
    private val repository: Repository

): ViewModel(){
    var searchedBricks = repository.searchedBricks
    var searchedSets = repository.searchedSets

    private val _toast = MutableLiveData<String?>()
    val toast: MutableLiveData<String?>
        get() = _toast

    fun onToastShown() {
        _toast.value = null
    }

    private val _searchQuery = MutableLiveData<String?>()
    val searchQuery: MutableLiveData<String?>
        get() = _searchQuery

    fun onSearchButtonClicked(query: String?) {
        _searchQuery.value = query
        //doSearch(query)
    }

    /*
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

    }*/

    fun searchBricks(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.publicSearchBricks(query)
        }
    }

    fun searchSets(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.publicSearchBrickSets(query)
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
                return SearchViewModel(
                    (application as BrickrApplication).appContainer.repository,
                ) as T
            }
        }
    }
}
