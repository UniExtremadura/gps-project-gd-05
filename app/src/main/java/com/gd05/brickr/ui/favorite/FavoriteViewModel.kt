package com.gd05.brickr.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.database.Repository
import com.gd05.brickr.model.BrickSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel (
    private val repository: Repository

): ViewModel(){
    //var favoriteSets: MutableLiveData<List<BrickSet>> = MutableLiveData(emptyList())
    var favoriteSets = repository.favoriteSets

    private val _toast = MutableLiveData<String?>()
    val toast: MutableLiveData<String?>
        get() = _toast

    fun onToastShown() {
        _toast.value = null
    }

    /*
    fun deleteSetFromFavorites(set: BrickSet) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.deleteSetFromFavorites(set)
        }
    }*/


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
                return FavoriteViewModel(
                    (application as BrickrApplication).appContainer.repository,
                ) as T
            }
        }
    }
}
