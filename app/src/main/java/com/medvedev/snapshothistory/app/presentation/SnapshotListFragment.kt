package com.medvedev.snapshothistory.app.presentation

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