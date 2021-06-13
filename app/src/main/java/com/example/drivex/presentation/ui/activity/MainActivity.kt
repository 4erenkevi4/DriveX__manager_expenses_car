package com.example.drivex.presentation.ui.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.drivex.R
import com.example.drivex.presentation.ui.dialogs.AddExpDialogFragment
import com.example.drivex.presentation.ui.dialogs.AddPayDialogFragment
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton
import com.nightonke.boommenu.BoomMenuButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var boomMenu: BoomMenuButton
    private lateinit var playerStart: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerStart = MediaPlayer.create(this, R.raw.engine_start)
        playerStart.start()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setBoomMenu() {
        val intentMain = Intent(this, MainActivity::class.java)
        val context = this
        val intentFuell = Intent(this, FuelActivity::class.java)
        val intentService = Intent(this, ServiceActivity::class.java)
        val intentMap = Intent(this, MapsActivity::class.java)
        val dialogPayment = AddPayDialogFragment(application, intentMain,context)
        val dialogShopping = AddExpDialogFragment(application, intentMain,context)
        val buttonFuel = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_car)
            .normalText("Заправка")
            .listener { startActivity(intentFuell) }
        val buttonService = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_service)
            .normalText("Добавить ТО")
            .listener { startActivity(intentService) }
        val buttonPayments = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_money)
            .normalText("Платеж")
            .listener { dialogPayment.show(supportFragmentManager, "payments_dialog") }
        val buttonExpenses = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_shopping)
            .normalText("Покупка")
            .listener { dialogShopping.show(supportFragmentManager, "shopping_dialog") }
        val buttonDriving = TextOutsideCircleButton.Builder()
            .normalImageRes(R.drawable.ic_map)
            .normalText("Поездка")
            .listener { startActivity(intentMap) }
        boomMenu.addBuilder(buttonFuel)
        boomMenu.addBuilder(buttonService)
        boomMenu.addBuilder(buttonPayments)
        boomMenu.addBuilder(buttonExpenses)
        boomMenu.addBuilder(buttonDriving)
    }













}