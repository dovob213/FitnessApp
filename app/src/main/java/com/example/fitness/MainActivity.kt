package com.example.fitness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
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
        // RepositoryProvider.initializeWithMock()

        // Phase 2: Firebase 사용
        RepositoryProvider.initialize()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Bottom Navigation과 연결
        bottomNav.setupWithNavController(navController)
    }
}
