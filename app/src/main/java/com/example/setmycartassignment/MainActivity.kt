package com.example.setmycartassignment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.setmycartassignment.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView : BottomNavigationView = binding.bottomNavigationView

        binding.bottomNavigationView.visibility = View.VISIBLE


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.findNavController()

        navView.setupWithNavController(navController)



        setSupportActionBar(binding.toolbar)
        // supportActionBar?.title = "Home"

        binding.toolbar.setBackgroundColor(resources.getColor(R.color.white));
        binding.toolbar.setTitleTextColor(Color.BLACK)

        //toolbar.navigationIcon?.setTint(Color.BLACK)

        binding.navView.setNavigationItemSelectedListener(this)



        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, 0, 0
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                Toast.makeText(this, "Share Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.rate -> {
                Toast.makeText(this, "Rate Clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.version -> {
                Toast.makeText(this, "Version Clicked", Toast.LENGTH_SHORT).show()
            }



        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}