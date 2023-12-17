package com.gd05.brickr.ui.home

import androidx.lifecycle.LiveData
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

class BrickSetDetailViewModel (
    private val repository: Repository

): ViewModel() {

    private var _brickSetDetail = MutableLiveData<BrickSet>(null)
    val brickSetDetail: LiveData<BrickSet>
        get() = _brickSetDetail

    private val _toast = MutableLiveData<String?>()
    val toast: MutableLiveData<String?>
        get() = _toast

    fun onToastShown() {
        _toast.value = null
    }

    var brickSet: BrickSet? = null
        set(value) {
            field = value
            getBrickSet()
        }

    private fun getBrickSet() {
        if (brickSet != null) {
            viewModelScope.launch(Dispatchers.Main) {
                val _brickSet = repository.publicGetBrickSetById(brickSet!!.brickSetId)
                _brickSetDetail.value= _brickSet
            }
        }
    }

    fun getBrickSetTheme(themeId: Int): LiveData<String> {
        val themeNameLiveData = MutableLiveData<String>()

        viewModelScope.launch {
            val theme = repository.publicGetThemeName(themeId)
            val themeName = theme?.themeName ?: "Unknown"
            themeNameLiveData.postValue(themeName)
        }

        return themeNameLiveData
    }

    fun toggleFavorite(brickSet: BrickSet){
        viewModelScope.launch {
            if (brickSet.isFavorite) {
                brickSet.isFavorite = false
                repository.publicInsertBrickSet(brickSet)
                _toast.value = "Eliminado de favoritos"
            } else {
                brickSet.isFavorite = true
                repository.publicInsertBrickSet(brickSet)
                _toast.value = "Añadido a favoritos"
            }
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
                return BrickSetDetailViewModel(
                    (application as BrickrApplication).appContainer.repository,
                ) as T
            }
        }
    }
}