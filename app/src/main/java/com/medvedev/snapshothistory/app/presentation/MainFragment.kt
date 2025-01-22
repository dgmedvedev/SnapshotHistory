package com.medvedev.snapshothistory.app.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.app.presentation.MainViewModel.Companion.REQUIRED_PERMISSIONS
import com.medvedev.snapshothistory.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("MainFragment == null")

    private val vm by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            MainViewModelFactory(requireContext().applicationContext)
        )[MainViewModel::class.java]
    }

    private val resultLauncherOfPermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(context, R.string.permissions_must_be_granted, Toast.LENGTH_SHORT)
                    .show()
            } else {
                vm.startCamera(this, binding.viewFinder)
            }
        }

    private val resultLauncherChosenDirectory: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri: Uri? = data?.data
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        vm.onCameraButtonPressed(
                            uri = uri,
                            contentResolver = requireContext().contentResolver
                        )
                    }
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(context, R.string.select_folder, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkingPermissions()
        bindViewModel()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkingPermissions() {
        if (allPermissionsGranted()) {
            vm.startCamera(this, binding.viewFinder)
        } else {
            requestPermissions()
        }
    }

    private fun bindViewModel() {
        vm.resultPhotoCapture.observe(viewLifecycleOwner) { result ->
            val message = when (result) {
                getString(R.string.processing_failed) -> getString(R.string.allowed_directories)
                else -> result
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListeners() {
        binding.cameraButton.setOnClickListener {
            chooseOutputDirectory()
        }
        binding.snapshotListButton.setOnClickListener {
            val snapshotListFragment = SnapshotListFragment.getInstance()
            launchFragment(fragment = snapshotListFragment)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        resultLauncherOfPermissions.launch(REQUIRED_PERMISSIONS)
    }

    private fun chooseOutputDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        resultLauncherChosenDirectory.launch(intent)
    }

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}