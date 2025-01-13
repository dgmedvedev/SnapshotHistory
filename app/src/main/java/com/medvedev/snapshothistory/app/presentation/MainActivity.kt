package com.medvedev.snapshothistory.app.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val vm by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, MainViewModelFactory(applicationContext))[MainViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        vm.startCamera(this@MainActivity, binding.previewView)
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainViewModel.REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                vm.startCamera(this@MainActivity, binding.previewView)
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
    }

    private fun showToast(messageId: Int) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show()
    }
}