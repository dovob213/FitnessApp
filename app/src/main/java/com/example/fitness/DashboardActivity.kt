package com.example.fitness

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitness.R
import com.example.fitness.analytics.*
import com.example.fitness.model.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. 데이터 준비 (실제로는 DB나 SharedPref에서 불러와야 함)
        val myHistory = getSampleHistory()
        val myPreset = getSamplePreset() // 목표 비교용

        // 2. 분석 매니저 호출
        val analysis = WorkoutAnalyticsManager.analyze(
            logs = myHistory,
            period = AnalysisPeriod.WEEKLY,
            preset = myPreset
        )

        // 3. UI 업데이트
        updateSummaryUI(analysis)
        updateDailyProgressUI(analysis.dailyCompletionRate)
        updateMuscleUI(analysis.muscleDistribution)
    }

    // 상단 요약 카드 업데이트
    private fun updateSummaryUI(result: AnalysisResult) {
        // 시간
        val tvTotalTime = findViewById<TextView>(R.id.tvTotalTime)
        val pbTime = findViewById<ProgressBar>(R.id.progressTime)

        val hours = result.totalTimeMinutes / 60
        val mins = result.totalTimeMinutes % 60
        tvTotalTime.text = "${hours}h ${mins}m"
        pbTime.progress = 70 // 예: 목표 시간 대비 퍼센트 로직 추가 가능

        // 칼로리
        val tvTotalCal = findViewById<TextView>(R.id.tvTotalCalorie)
        tvTotalCal.text = String.format("%,d kcal", result.totalCalories)

        // 일수
        val tvTotalDays = findViewById<TextView>(R.id.tvTotalDays)
        val pbDays = findViewById<ProgressBar>(R.id.progressDays)
        tvTotalDays.text = "${result.workoutDaysCount} / 7 days"
        pbDays.progress = (result.workoutDaysCount / 7.0 * 100).toInt()
    }

    // 요일별 진행률 업데이트
    private fun updateDailyProgressUI(dailyRates: Map<String, Int>) {
        // XML에 id를 pbMon, pbTue 등으로 지었다고 가정
        val weekDays = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
        val viewIds = listOf(R.id.pbMon, R.id.pbTue, /* ... */ ) // 실제 ID 리스트

        // 각 요일별 프로그레스바 찾아서 값 세팅
        // (여기선 단순화하여 월요일만 예시)
        val monRate = dailyRates["MON"] ?: 0
        findViewById<ProgressBar>(R.id.pbMon).progress = monRate

        // ... 반복문으로 처리
    }

    // 부위별 분포 업데이트
    private fun updateMuscleUI(distribution: Map<String, Int>) {
        val upperRate = distribution["상체"] ?: 0
        val lowerRate = distribution["하체"] ?: 0

        findViewById<ProgressBar>(R.id.pbUpperBody).progress = upperRate
        findViewById<TextView>(R.id.tvUpperText).text = "${upperRate}%"

        findViewById<ProgressBar>(R.id.pbLowerBody).progress = lowerRate
        // ...
    }

    // 테스트용 샘플 데이터 생성
    private fun getSampleHistory(): List<WorkoutLog> {
        return listOf(
            WorkoutLog(exerciseId = "ex005", date = System.currentTimeMillis(), sets = listOf()),
            WorkoutLog(exerciseId = "ex007", date = System.currentTimeMillis(), sets = listOf())
        )
    }

    private fun getSamplePreset(): WeeklyPreset? {
        // 빈 프리셋 반환 (실제로는 저장된 프리셋 로드)
        return WeeklyPreset("p01", "My Plan")
    }
}