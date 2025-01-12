package com.medvedev.snapshothistory.app.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val vm by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, MainViewModelFactory(applicationContext))[MainViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            vm.startCamera(this@MainActivity, binding.previewView)
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
        requestPermissions()
    }

    override fun onStop() {
        super.onStop()
        vm.stopCamera()
    }

//        override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_CODE_PERMISSIONS) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startCamera()
//            } else {
//                Toast.makeText(
//                    this,
//                    R.string.permission_camera_denied,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

    private fun allPermissionsGranted(): Boolean =
        MainViewModel.REQUIRED_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                baseContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

    private fun requestPermissions() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                MainViewModel.REQUIRED_PERMISSIONS,
                MainViewModel.REQUEST_CODE_PERMISSIONS
            )
        }
    }
}