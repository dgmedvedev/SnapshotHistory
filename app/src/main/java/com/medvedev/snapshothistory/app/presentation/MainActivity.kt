package com.medvedev.snapshothistory.app.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.app.presentation.MainViewModel.Companion.REQUIRED_PERMISSIONS
import com.medvedev.snapshothistory.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val vm by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, MainViewModelFactory(applicationContext))[MainViewModel::class.java]
    }

    private val resultLauncherOfPermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(this, R.string.permissions_must_be_granted, Toast.LENGTH_SHORT)
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
                        vm.onCameraButtonPressed(uri = uri, contentResolver = contentResolver)
                    }
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, R.string.select_folder, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkingPermissions()
        bindViewModel()
        setListeners()
    }

    private fun checkingPermissions() {
        if (allPermissionsGranted()) {
            vm.startCamera(this, binding.viewFinder)
        } else {
            requestPermissions()
        }
    }

    private fun bindViewModel() {
        vm.resultPhotoCapture.observe(this) { result ->
            val message = when (result) {
                getString(R.string.processing_failed) -> getString(R.string.allowed_directories)
                else -> result
            }
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setListeners() {
        binding.cameraButton.setOnClickListener {
            chooseOutputDirectory()
        }
        binding.snapshotListButton.setOnClickListener {
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        resultLauncherOfPermissions.launch(REQUIRED_PERMISSIONS)
    }

    private fun chooseOutputDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        resultLauncherChosenDirectory.launch(intent)
    }
}