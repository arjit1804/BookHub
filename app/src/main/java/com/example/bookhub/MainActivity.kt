package com.example.bookhub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var frameLayout: FrameLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView:NavigationView

    //defining the variable for checking the selected menu item from the navigation view
    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        frameLayout=findViewById(R.id.frame)
        toolbar =findViewById(R.id.toolbar)
        navigationView=findViewById(R.id.navigationView)

        setupToolbar()

        openDashboard()


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            //for checking the items in the navigation view
            if (previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId){
                R.id.dashboard ->{supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, DashboardFragment())
                    //.addToBackStack("Dashboard")
                    .commit()

                    supportActionBar?.title = "Dashboard"
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, FavouritesFragment())
                    //.addToBackStack("Favourites")
                    .commit()

                    supportActionBar?.title = "Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, ProfileFragment())
                    //.addToBackStack("Profile")
                    .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, AboutAppFragment())
                    //.addToBackStack("About App")
                    .commit()

                    supportActionBar?.title = "About App"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }
    fun setupToolbar(){
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId

        if (id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard(){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame,fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"

        //making the default checkable whenever the apps opens up the dashboard activity will be checked
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){
            /*if the opened activity or fragment is other then the dashboard then on back pressed the activity wil go on
            * to the dashboard activity otherwise the else part is going to execute */
            !is DashboardFragment -> openDashboard()

            else -> super.onBackPressed()
        }
    }
}