package com.gd05.brickr.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.database.Repository
import com.gd05.brickr.model.Brick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BrickDetailViewModel (
    private val repository: Repository

): ViewModel() {

    private var _brickDetail = MutableLiveData<Brick>(null)
    val brickDetail: LiveData<Brick>
        get() = _brickDetail

    private val _toast = MutableLiveData<String?>()
    val toast: MutableLiveData<String?>
        get() = _toast

    fun onToastShown() {
        _toast.value = null
    }

    var brick: Brick? = null
        set(value) {
            field = value
            getBrick()
        }

    private fun getBrick() {
        if (brick != null) {
            viewModelScope.launch {
                val _brick = repository.publicGetBrickById(brick!!.brickId)
                _brickDetail.value= _brick
            }
        }
    }

    fun getBrickCategory(categoryId: Int): LiveData<String> {
        val categoryNameLiveData = MutableLiveData<String>()

        viewModelScope.launch {
            val category = repository.publicGetCategoryName(categoryId)
            val categoryName = category?.categoryName ?: "Unknown"
            categoryNameLiveData.postValue(categoryName)
        }

        return categoryNameLiveData
    }


    fun addBrick(brick: Brick) {
        viewModelScope.launch() {
            brick.amount++
            repository.publicInsertBrick(brick)
            getBrick()
        }
    }

    fun removeBrick(brick: Brick) {
        viewModelScope.launch(Dispatchers.Main) {
            if (brick.amount > 1) {
                brick.amount--
                repository.publicInsertBrick(brick)
                getBrick()
            }
        }
    }

    fun destroyBrick(brick: Brick) {
        viewModelScope.launch(Dispatchers.Main) {
            brick.amount = 0
            repository.publicInsertBrick(brick)
            getBrick()
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
                return BrickDetailViewModel(
                    (application as BrickrApplication).appContainer.repository,
                ) as T
            }
        }
    }

}