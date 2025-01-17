package com.medvedev.snapshothistory.app.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.data.repository.SnapshotRepositoryImpl
import com.medvedev.snapshothistory.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val vm by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, MainViewModelFactory(applicationContext))[MainViewModel::class.java]
    }

    private var uri: Uri? = null

    override fun onStart() {
        super.onStart()
        vm.startCamera(this@MainActivity, binding.viewFinder)
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

        bindViewModel()
        setListeners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainViewModel.REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                vm.startCamera(this@MainActivity, binding.viewFinder)
            } else {
                showToast(R.string.permissions_must_be_granted)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        vm.stopCamera()
    }

    private fun bindViewModel() {
        vm.hasPermissions.observe(this) { hasPermissions ->
            if (!hasPermissions) {
                ActivityCompat.requestPermissions(
                    this,
                    MainViewModel.REQUIRED_PERMISSIONS,
                    MainViewModel.REQUEST_CODE_PERMISSIONS
                )
            }
        }
        vm.hasSelectedFolder.observe(this) {

        }
        vm.resultPhotoCapture.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListeners() {
        binding.cameraButton.setOnClickListener {
            getOutputDirectory()
        }
        binding.snapshotListButton.setOnClickListener {
        }
    }

    private fun showToast(messageId: Int) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
    }

    private fun getOutputDirectory() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        firstActivityResultLauncher.launch(intent)
    }

    private var firstActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val resultUri: Uri? = data?.data
                uri = resultUri
                Log.d(SnapshotRepositoryImpl.LOG_TAG, "uri registerForActivityResult: $uri")
                lifecycleScope.launch(Dispatchers.Main) {
                    vm.onCameraButtonPressed(uri = uri, contentResolver = contentResolver)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, R.string.select_folder, Toast.LENGTH_SHORT).show()
            }
        }
}