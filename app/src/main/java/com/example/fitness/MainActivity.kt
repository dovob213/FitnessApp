package com.example.fitness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.fitness.data.RepositoryProvider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Repository 초기화 (Phase 1: Mock, Phase 2: Firebase)
        initializeRepositories()

        // Navigation 설정
        setupNavigation()
    }

    private fun initializeRepositories() {
        // Phase 1: Mock 사용
        RepositoryProvider.initializeWithMock()

        // Phase 2: Firebase 사용
        // RepositoryProvider.initialize()
    }

    private fun setupNavigation() {
        // NavHostFragment를 통해 NavController 가져오기
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Bottom Navigation과 연결
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
    }
}
