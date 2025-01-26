package com.medvedev.snapshothistory.app.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.app.presentation.adapter.SnapshotAdapter
import com.medvedev.snapshothistory.databinding.FragmentSnapshotListBinding
import com.medvedev.snapshothistory.domain.model.Snapshot
import java.util.Calendar

class SnapshotListFragment : Fragment() {

    private var _binding: FragmentSnapshotListBinding? = null
    private val binding: FragmentSnapshotListBinding
        get() = _binding ?: throw RuntimeException("SnapshotListFragment == null")

    private val vm: SnapshotListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            ViewModelFactory(requireContext().applicationContext)
        )[SnapshotListViewModel::class.java]
    }

    private val adapter: SnapshotAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SnapshotAdapter(onSnapshotItemClickListener())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSnapshotListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setListeners()
        binding.rvSnapshotList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewModel() {
        vm.snapshotListFromDB.observe(viewLifecycleOwner) { snapshotList ->
            adapter.submitList(snapshotList)
        }
        vm.filteredSnapshotList.observe(viewLifecycleOwner) { snapshotList ->
            adapter.submitList(snapshotList)
        }
        vm.invalidInput.observe(viewLifecycleOwner) {
            binding.tilSearch.error =
                when (it) {
                    R.string.invalid_input -> getString(R.string.warning_text)
                    else -> null
                }
        }
        vm.selectedDate.observe(viewLifecycleOwner) { selectedDate ->
            binding.etSearch.setText(selectedDate)
        }
    }

    private fun onSnapshotItemClickListener(): (Snapshot) -> Unit = { snapshot ->
        launchFragment(
            SnapshotFragment.getInstance(
                snapshot.filePath,
                snapshot.latitude,
                snapshot.longitude
            )
        )
    }

    private fun setListeners() {
        binding.etSearch.addTextChangedListener { text ->
            vm.filterList(text.toString())
        }
        binding.btnCalendar.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                vm.setSelectedDate(selectedYear, selectedMonth, selectedDay)
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun getInstance(): SnapshotListFragment {
            return SnapshotListFragment()
        }
    }
}