package com.example.drivex.presentation.ui.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.drivex.R
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_SOUND
import com.example.drivex.utils.Constans
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SHOPPING
import com.example.drivex.utils.Constans.PAYMENT_TYPE
import com.google.android.material.navigation.NavigationView
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton
import com.nightonke.boommenu.BoomMenuButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var prefs: SharedPreferences
    private lateinit var appBarConfiguration: AppBarConfiguration
    var boomMenu: BoomMenuButton? = null

    private lateinit var playerStart: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boomMenu = findViewById(R.id.boom_menu)
        setBoomMenu()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_car,
                R.id.nav_notify,
                R.id.nav_map,
                R.id.nav_stat,
                R.id.nav_settings,
                R.id.nav_info
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (prefs.contains(TYPE_SOUND)) {
            startMusic(prefs.getBoolean(TYPE_SOUND, false))
        }
        requestPermissions()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3 && resultCode == RESULT_OK) {
            val imageUri = data?.data
            val args = bundleOf(Pair("IMAGE_URI", imageUri))
            replaceFragment(R.id.action_global_nav_settings, args)
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.FOREGROUND_SERVICE
                    ),
                    Constans.REQUEST_CODE_GET_PERMISSION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    Constans.REQUEST_CODE_GET_PERMISSION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        " Permissions was granted.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "Permission denied to read your External storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun startMusic(isNeedSound: Boolean) {
        if (isNeedSound) {
            playerStart = MediaPlayer.create(this, R.raw.engine_start)
            playerStart.start()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setBoomMenu() {
        val intentFuell = Intent(this, FuelActivity::class.java)
        val intentService = Intent(this, ServiceActivity::class.java)
        val intentMap = Intent(this, MapsActivity::class.java)
        val buttonFuel = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_car)
            .normalText(getString(R.string.refuel))
            .listener { startActivity(intentFuell.putExtra(PAYMENT_TYPE, REFUEL)) }
        val buttonService = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_service)
            .normalText(getString(R.string.add_service))
            .listener { startActivity(intentService) }
        val buttonPayments = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_money)
            .normalText(getString(R.string.payment))
            .listener { startActivity(intentFuell.putExtra(PAYMENT_TYPE, PAYMENT)) }
        val buttonExpenses = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_shopping)
            .normalText(getString(R.string.your_buy))
            .listener { startActivity(intentFuell.putExtra(PAYMENT_TYPE, SHOPPING)) }
        val buttonDriving = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_map)
            .normalText(getString(R.string.add_ride))
            .listener { startActivity(intentMap) }
        boomMenu!!.builders =
            arrayListOf(buttonFuel, buttonService, buttonPayments, buttonExpenses, buttonDriving)
    }

    fun replaceFragment(navID: Int, args: Bundle? = null) {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        navController.navigate(navID, args)
    }


}