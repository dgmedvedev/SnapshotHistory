package com.medvedev.snapshothistory.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.databinding.FragmentSnapshotBinding
import com.squareup.picasso.Picasso

class SnapshotFragment : Fragment() {

    private var _binding: FragmentSnapshotBinding? = null
    private val binding: FragmentSnapshotBinding
        get() = _binding ?: throw RuntimeException("SnapshotFragment == null")

    private var filePath: String = UNDEFINED_FILE_PATH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filePath = requireArguments().getString(ARG_IMAGE_PATH, UNDEFINED_FILE_PATH)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSnapshotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displaySnapshot(filePath = filePath)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displaySnapshot(filePath: String) {
        try {
            Picasso.get()
                .load(filePath)
                .into(binding.ivSnapshot)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.snapshot_not_found), Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val UNDEFINED_FILE_PATH = ""
        private const val ARG_IMAGE_PATH = "imagePath"

        fun getInstance(filePath: String): SnapshotFragment =
            SnapshotFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_PATH, filePath)
                }
            }
    }
}