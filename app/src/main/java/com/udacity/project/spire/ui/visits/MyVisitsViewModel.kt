package com.udacity.project.spire.ui.visits

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.udacity.project.spire.data.repository.BuildingRepository
import com.udacity.project.spire.domain.model.Building
import com.udacity.project.spire.domain.model.VisitStatus
import com.udacity.project.spire.ui.common.ErrorEvent
import com.udacity.project.spire.ui.common.Event
import kotlinx.coroutines.flow.catch

/**
 * ViewModel for MyVisitsFragment.
 * Filters buildings by visit status (Visited, Bucket List, Not Visited).
 */
class MyVisitsViewModel(
    private val repository: BuildingRepository
) : ViewModel() {

    private data class VisitFilter(
        val status: VisitStatus,
        val version: Int = 0
    )

    private val _filter = MutableLiveData(VisitFilter(VisitStatus.VISITED))
    val currentStatus: LiveData<VisitStatus> = _filter.map { it.status }

    private val _errorEvent = MutableLiveData<Event<ErrorEvent>>()
    val errorEvent: LiveData<Event<ErrorEvent>> = _errorEvent

    val buildings: LiveData<List<Building>> = _filter.switchMap { filter ->
        repository.getBuildingsByVisitStatus(filter.status)
            .catch { exception ->
                Log.e("MyVisitsViewModel", "Failed to load buildings by visit status", exception)
                _errorEvent.value = Event(
                    ErrorEvent(
                        message = "Unable to load buildings. Please try again.",
                        throwable = exception
                    )
                )
                emit(emptyList())
            }
            .asLiveData()
    }

    fun setFilterStatus(status: VisitStatus) {
        val current = _filter.value
        if (current?.status != status) {
            _filter.value = VisitFilter(status = status)
        }
    }

    /** Forces a reload of the current filter (used by Retry). */
    fun retryLoad() {
        val current = _filter.value ?: VisitFilter(VisitStatus.VISITED)
        _filter.value = current.copy(version = current.version + 1)
    }
}

/**
 * ViewModelFactory for MyVisitsViewModel.
 */
class MyVisitsViewModelFactory(
    private val repository: BuildingRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyVisitsViewModel::class.java)) {
            return MyVisitsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
