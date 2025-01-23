package com.medvedev.snapshothistory.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        binding.rvSnapshotList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindViewModel() {
        vm.snapshotList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun onSnapshotItemClickListener(): (Snapshot) -> Unit = { snapshot ->
        Toast.makeText(requireContext(), "${snapshot.id}", Toast.LENGTH_SHORT).show()
        launchFragment(SnapshotFragment.getInstance(snapshot.filePath))
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