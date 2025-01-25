package com.medvedev.snapshothistory.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.domain.model.Snapshot
import com.medvedev.snapshothistory.domain.usecase.GetSnapshotListUseCase
import java.util.regex.Pattern

class SnapshotListViewModel(
    getSnapshotListUseCase: GetSnapshotListUseCase
) : ViewModel() {

    val snapshotListFromDB: LiveData<List<Snapshot>> = getSnapshotListUseCase()

    private var _filteredSnapshotList = MutableLiveData<List<Snapshot>>()
    val filteredSnapshotList: LiveData<List<Snapshot>>
        get() = _filteredSnapshotList

    private var _invalidInput = MutableLiveData<Int>()
    val invalidInput: LiveData<Int>
        get() = _invalidInput

    private fun textIsValid(text: String): Boolean =
        Pattern.matches(ONLY_NUMBERS, text) || text.isEmpty()

    fun filterList(desired: String) {
        if (textIsValid(text = desired)) {
            _filteredSnapshotList.value =
                snapshotListFromDB.value?.filter { it.name.contains(desired) }
            _invalidInput.value = R.string.text_is_valid
        } else {
            _invalidInput.value = R.string.invalid_input
        }
    }

    companion object {
        private const val ONLY_NUMBERS = "^\\d+\$"
    }
}