package com.example.fitness.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitness.R
import com.example.fitness.data.RepositoryProvider
import com.example.fitness.model.UserProfile
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private val profileRepo by lazy {
        RepositoryProvider.getUserProfileRepository()
    }

    private lateinit var profileManager: ProfileManager

    // UI 요소
    private lateinit var tvUserId: TextView
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var tvBmi: TextView
    private lateinit var btnSave: Button
    private lateinit var btnEdit: Button

    private var currentProfile: UserProfile? = null
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // UI 초기화
        initViews(view)

        // Manager 초기화
        profileManager = ProfileManager(profileRepo)

        // 프로필 로드
        loadProfile()

        // 리스너 설정
        setupListeners()

        return view
    }

    private fun initViews(view: View) {
        tvUserId = view.findViewById(R.id.tvUserId)
        etName = view.findViewById(R.id.etName)
        etAge = view.findViewById(R.id.etAge)
        etHeight = view.findViewById(R.id.etHeight)
        etWeight = view.findViewById(R.id.etWeight)
        tvBmi = view.findViewById(R.id.tvBmi)
        btnSave = view.findViewById(R.id.btnSave)
        btnEdit = view.findViewById(R.id.btnEdit)

        // 초기에는 편집 불가
        setEditMode(false)
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            // 임시 사용자 ID (나중에 로그인 기능 추가시 변경)
            val userId = "user_default"

            currentProfile = profileManager.getProfile(userId)

            if (currentProfile == null) {
                // 프로필이 없으면 기본 프로필 생성
                currentProfile = UserProfile(
                    userId = userId,
                    name = "사용자",
                    age = 0,
                    height = 0.0,
                    weight = 0.0
                )
                profileManager.saveProfile(currentProfile!!)
            }

            displayProfile(currentProfile!!)
        }
    }

    private fun displayProfile(profile: UserProfile) {
        tvUserId.text = "ID: ${profile.userId}"
        etName.setText(profile.name)
        etAge.setText(if (profile.age > 0) profile.age.toString() else "")
        etHeight.setText(if (profile.height > 0) profile.height.toString() else "")
        etWeight.setText(if (profile.weight > 0) profile.weight.toString() else "")

        // BMI 계산
        if (profile.height > 0 && profile.weight > 0) {
            val bmi = profileManager.calculateBMI(profile.height, profile.weight)
            tvBmi.text = String.format("BMI: %.1f (%s)", bmi, getBmiCategory(bmi))
        } else {
            tvBmi.text = "BMI: -"
        }
    }

    private fun setupListeners() {
        // 편집 버튼
        btnEdit.setOnClickListener {
            setEditMode(true)
        }

        // 저장 버튼
        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = etName.text.toString().trim()
        val ageText = etAge.text.toString().trim()
        val heightText = etHeight.text.toString().trim()
        val weightText = etWeight.text.toString().trim()

        // 유효성 검사
        if (name.isEmpty()) {
            Toast.makeText(context, "이름을 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageText.toIntOrNull() ?: 0
        val height = heightText.toDoubleOrNull() ?: 0.0
        val weight = weightText.toDoubleOrNull() ?: 0.0

        // 프로필 업데이트
        lifecycleScope.launch {
            val updatedProfile = currentProfile!!.copy(
                name = name,
                age = age,
                height = height,
                weight = weight
            )

            profileManager.updateProfile(updatedProfile)
            currentProfile = updatedProfile

            displayProfile(updatedProfile)
            setEditMode(false)

            Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEditMode(enabled: Boolean) {
        isEditMode = enabled

        etName.isEnabled = enabled
        etAge.isEnabled = enabled
        etHeight.isEnabled = enabled
        etWeight.isEnabled = enabled

        btnEdit.visibility = if (enabled) View.GONE else View.VISIBLE
        btnSave.visibility = if (enabled) View.VISIBLE else View.GONE
    }

    private fun getBmiCategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "저체중"
            bmi < 23 -> "정상"
            bmi < 25 -> "과체중"
            bmi < 30 -> "비만"
            else -> "고도비만"
        }
    }
}