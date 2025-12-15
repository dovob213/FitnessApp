package com.example.fitness.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitness.R
import com.example.fitness.data.RepositoryProvider
import com.example.fitness.model.UserProfile
import com.google.android.material.slider.Slider
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private val profileRepo by lazy {
        RepositoryProvider.getUserProfileRepository()
    }

    private lateinit var profileManager: ProfileManager

    // UI 요소
    private lateinit var btnSaveProfile: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvLastUpdate: TextView
    private lateinit var tvWeightValue: TextView
    private lateinit var tvAgeValue: TextView
    private lateinit var tvHeightValue: TextView
    private lateinit var tvExperienceLevel: TextView
    private lateinit var tvGoal: TextView
    private lateinit var tvLocation: TextView
    private lateinit var cardExperienceLevel: CardView
    private lateinit var cardGoal: CardView
    private lateinit var cardLocation: CardView

    private var currentProfile: UserProfile? = null

    // 임시 편집 데이터
    private var tempAge: Int = 25
    private var tempHeight: Double = 165.5
    private var tempWeight: Double = 58.0
    private var tempLevel: Int = 0  // 0: 초급, 1: 중급, 2: 상급
    private var tempGoal: String = "체중 감량"
    private var tempLocations: List<String> = listOf("헬스장", "집")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initViews(view)
        profileManager = ProfileManager(profileRepo)
        loadProfile()
        setupListeners()

        return view
    }

    private fun initViews(view: View) {
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)
        tvUserName = view.findViewById(R.id.tvUserName)
        tvLastUpdate = view.findViewById(R.id.tvLastUpdate)
        tvWeightValue = view.findViewById(R.id.tvWeightValue)
        tvAgeValue = view.findViewById(R.id.tvAgeValue)
        tvHeightValue = view.findViewById(R.id.tvHeightValue)
        tvExperienceLevel = view.findViewById(R.id.tvExperienceLevel)
        tvGoal = view.findViewById(R.id.tvGoal)
        tvLocation = view.findViewById(R.id.tvLocation)
        cardExperienceLevel = view.findViewById(R.id.cardExperienceLevel)
        cardGoal = view.findViewById(R.id.cardGoal)
        cardLocation = view.findViewById(R.id.cardLocation)
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            val userId = "user_default"
            currentProfile = profileManager.getProfile(userId)

            if (currentProfile == null) {
                currentProfile = UserProfile(
                    userId = userId,
                    name = "김지수",
                    age = 25,
                    height = 165.5,
                    weight = 58.0,
                    level = 0,
                    place = listOf("헬스장", "집"),
                    goal = "체중 감량"
                )
                profileManager.saveProfile(currentProfile!!)
            }

            displayProfile(currentProfile!!)
        }
    }

    private fun displayProfile(profile: UserProfile) {
        tvUserName.text = profile.name

        // 마지막 업데이트 날짜
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREAN)
        tvLastUpdate.text = "마지막 업데이트: ${dateFormat.format(Date())}"

        // 체중, 나이, 키
        tvWeightValue.text = "${profile.weight.toInt()} kg"
        tvAgeValue.text = "${profile.age} 세"
        tvHeightValue.text = "${profile.height.toInt()} cm"

        // 운동 경험
        tvExperienceLevel.text = when (profile.level) {
            0 -> "초급"
            1 -> "중급"
            2 -> "상급"
            else -> "초급"
        }

        // 운동 목적
        tvGoal.text = profile.goal

        // 운동 장소
        tvLocation.text = profile.place.joinToString(" & ")

        // 임시 데이터 업데이트
        tempAge = profile.age
        tempHeight = profile.height
        tempWeight = profile.weight
        tempLevel = profile.level
        tempGoal = profile.goal
        tempLocations = profile.place
    }

    private fun setupListeners() {
        // 저장 버튼
        btnSaveProfile.setOnClickListener {
            saveProfile()
        }

        // 체중, 나이, 키 카드 클릭 -> 슬라이더 다이얼로그
        tvWeightValue.setOnClickListener { showWeightDialog() }
        tvAgeValue.setOnClickListener { showAgeHeightDialog() }
        tvHeightValue.setOnClickListener { showAgeHeightDialog() }

        // 운동 경험 카드
        cardExperienceLevel.setOnClickListener {
            showLevelDialog()
        }

        // 운동 목적 카드
        cardGoal.setOnClickListener {
            showGoalDialog()
        }

        // 운동 장소 카드
        cardLocation.setOnClickListener {
            showLocationDialog()
        }
    }

    private fun showWeightDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_weight, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val tvWeightDisplay = dialogView.findViewById<TextView>(R.id.tvWeightDisplay)
        val sliderWeight = dialogView.findViewById<Slider>(R.id.sliderWeight)

        sliderWeight.value = tempWeight.toFloat()
        tvWeightDisplay.text = "${tempWeight.toInt()} kg"

        sliderWeight.addOnChangeListener { _, value, _ ->
            tempWeight = value.toDouble()
            tvWeightDisplay.text = "${value.toInt()} kg"
        }

        sliderWeight.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                updateDisplay()
                dialog.dismiss()
            }
        })

        dialog.show()
    }

    private fun showAgeHeightDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_slider, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val tvAgeDisplay = dialogView.findViewById<TextView>(R.id.tvAgeDisplay)
        val tvHeightDisplay = dialogView.findViewById<TextView>(R.id.tvHeightDisplay)
        val sliderAge = dialogView.findViewById<Slider>(R.id.sliderAge)
        val sliderHeight = dialogView.findViewById<Slider>(R.id.sliderHeight)

        sliderAge.value = tempAge.toFloat()
        sliderHeight.value = tempHeight.toFloat()
        tvAgeDisplay.text = "$tempAge 세"
        tvHeightDisplay.text = "$tempHeight cm"

        sliderAge.addOnChangeListener { _, value, _ ->
            tempAge = value.toInt()
            tvAgeDisplay.text = "${value.toInt()} 세"
        }

        sliderHeight.addOnChangeListener { _, value, _ ->
            tempHeight = value.toDouble()
            tvHeightDisplay.text = "$value cm"
        }

        sliderHeight.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                updateDisplay()
                dialog.dismiss()
            }
        })

        dialog.show()
    }

    private fun showLevelDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_select_level, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val cardBeginner = dialogView.findViewById<CardView>(R.id.cardBeginner)
        val cardIntermediate = dialogView.findViewById<CardView>(R.id.cardIntermediate)
        val cardAdvanced = dialogView.findViewById<CardView>(R.id.cardAdvanced)
        val ivBeginnerCheck = dialogView.findViewById<ImageView>(R.id.ivBeginnerCheck)
        val ivIntermediateCheck = dialogView.findViewById<ImageView>(R.id.ivIntermediateCheck)
        val ivAdvancedCheck = dialogView.findViewById<ImageView>(R.id.ivAdvancedCheck)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        // 현재 선택 표시
        updateLevelCheckmarks(ivBeginnerCheck, ivIntermediateCheck, ivAdvancedCheck, tempLevel)

        cardBeginner.setOnClickListener {
            tempLevel = 0
            updateLevelCheckmarks(ivBeginnerCheck, ivIntermediateCheck, ivAdvancedCheck, tempLevel)
        }

        cardIntermediate.setOnClickListener {
            tempLevel = 1
            updateLevelCheckmarks(ivBeginnerCheck, ivIntermediateCheck, ivAdvancedCheck, tempLevel)
        }

        cardAdvanced.setOnClickListener {
            tempLevel = 2
            updateLevelCheckmarks(ivBeginnerCheck, ivIntermediateCheck, ivAdvancedCheck, tempLevel)
        }

        btnSave.setOnClickListener {
            updateDisplay()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateLevelCheckmarks(
        ivBeginner: ImageView,
        ivIntermediate: ImageView,
        ivAdvanced: ImageView,
        level: Int
    ) {
        ivBeginner.visibility = if (level == 0) View.VISIBLE else View.GONE
        ivIntermediate.visibility = if (level == 1) View.VISIBLE else View.GONE
        ivAdvanced.visibility = if (level == 2) View.VISIBLE else View.GONE
    }

    private fun showGoalDialog() {
        val goals = arrayOf("체중 감량", "근육 증가", "체력 향상", "건강 유지")
        val selectedIndex = goals.indexOf(tempGoal)

        AlertDialog.Builder(requireContext())
            .setTitle("운동 목적")
            .setSingleChoiceItems(goals, selectedIndex) { dialog, which ->
                tempGoal = goals[which]
                updateDisplay()
                dialog.dismiss()
            }
            .show()
    }

    private fun showLocationDialog() {
        val locations = arrayOf("헬스장", "집", "야외", "수영장")
        val selectedItems = BooleanArray(locations.size) { i ->
            tempLocations.contains(locations[i])
        }

        AlertDialog.Builder(requireContext())
            .setTitle("운동 장소")
            .setMultiChoiceItems(locations, selectedItems) { _, which, isChecked ->
                selectedItems[which] = isChecked
            }
            .setPositiveButton("확인") { _, _ ->
                tempLocations = locations.filterIndexed { index, _ ->
                    selectedItems[index]
                }
                updateDisplay()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun updateDisplay() {
        tvWeightValue.text = "${tempWeight.toInt()} kg"
        tvAgeValue.text = "$tempAge 세"
        tvHeightValue.text = "${tempHeight.toInt()} cm"
        tvExperienceLevel.text = when (tempLevel) {
            0 -> "초급"
            1 -> "중급"
            2 -> "상급"
            else -> "초급"
        }
        tvGoal.text = tempGoal
        tvLocation.text = tempLocations.joinToString(" & ")
    }

    private fun saveProfile() {
        lifecycleScope.launch {
            try {
                val updatedProfile = currentProfile!!.copy(
                    age = tempAge,
                    height = tempHeight,
                    weight = tempWeight,
                    level = tempLevel,
                    goal = tempGoal,
                    place = tempLocations
                )

                profileManager.updateProfile(updatedProfile)
                currentProfile = updatedProfile

                displayProfile(updatedProfile)

                Toast.makeText(
                    requireContext(),
                    "프로필이 저장되었습니다",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "저장 중 오류가 발생했습니다: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}