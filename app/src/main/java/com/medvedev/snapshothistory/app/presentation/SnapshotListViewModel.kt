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

    private val _filteredSnapshotList = MutableLiveData<List<Snapshot>>()
    val filteredSnapshotList: LiveData<List<Snapshot>>
        get() = _filteredSnapshotList

    private val _invalidInput = MutableLiveData<Int>()
    val invalidInput: LiveData<Int>
        get() = _invalidInput

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String>
        get() = _selectedDate

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

    fun setSelectedDate(selectedYear: Int, selectedMonth: Int, selectedDay: Int) {
        val day = if (selectedDay < 10) {
            "0$selectedDay"
        } else "$selectedDay"
        val month = if (selectedMonth + 1 < 10) {
            "0${selectedMonth + 1}"
        } else "${selectedMonth + 1}"

        val selectedDate = "$day$month$selectedYear"
        _selectedDate.value = selectedDate
    }

    companion object {
        private const val ONLY_NUMBERS = "^\\d+\$"
    }
}