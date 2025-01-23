package com.medvedev.snapshothistory.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.usecase.GetSnapshotListUseCase

class SnapshotListViewModel(
    getSnapshotListUseCase: GetSnapshotListUseCase
) : ViewModel() {

    val snapshotList: LiveData<List<Snapshot>> = getSnapshotListUseCase()
}