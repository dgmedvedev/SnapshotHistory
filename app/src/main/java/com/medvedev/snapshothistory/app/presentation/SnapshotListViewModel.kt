package com.medvedev.snapshothistory.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.usecase.GetSnapshotListUseCase

class SnapshotListViewModel(
    getSnapshotListUseCase: GetSnapshotListUseCase
) : ViewModel() {

    val snapshotListFromDB: LiveData<List<Snapshot>> = getSnapshotListUseCase()

    private var _filteredSnapshotList = MutableLiveData<List<Snapshot>>()
    val filteredSnapshotList: LiveData<List<Snapshot>>
        get() = _filteredSnapshotList

    fun filterList(desired: String) {
        _filteredSnapshotList.value = snapshotListFromDB.value?.filter { it.name.contains(desired) }
    }
}