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
    private var latitude: Double = DEFAULT_LATITUDE
    private var longitude: Double = DEFAULT_LONGITUDE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
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
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() {
        binding.locationButton.setOnClickListener {
            launchFragment(MapFragment.getInstance(latitude, longitude))
        }
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

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun parseParams() {
        filePath = requireArguments().getString(ARG_IMAGE_PATH, UNDEFINED_FILE_PATH)
        latitude = requireArguments().getDouble(ARG_LATITUDE, DEFAULT_LATITUDE)
        longitude = requireArguments().getDouble(ARG_LONGITUDE, DEFAULT_LONGITUDE)
    }

    companion object {
        private const val UNDEFINED_FILE_PATH = ""
        private const val ARG_IMAGE_PATH = "imagePath"
        private const val ARG_LATITUDE = "latitude"
        private const val ARG_LONGITUDE = "longitude"
        private const val DEFAULT_LATITUDE = 37.7749
        private const val DEFAULT_LONGITUDE = -122.4194

        fun getInstance(filePath: String, latitude: Double, longitude: Double): SnapshotFragment =
            SnapshotFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_PATH, filePath)
                    putDouble(ARG_LATITUDE, latitude)
                    putDouble(ARG_LONGITUDE, longitude)
                }
            }
    }
}