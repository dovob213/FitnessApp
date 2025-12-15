package com.example.fitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.data.RepositoryProvider
import com.example.fitness.model.ExerciseSet
import com.example.fitness.model.Routine
import com.example.fitness.model.RoutineExercise
import com.example.fitness.model.WorkoutLog
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job

class WorkoutExecutionFragment : Fragment() {

    private val routineRepo by lazy { RepositoryProvider.getRoutineRepository() }
    private val workoutRepo by lazy { RepositoryProvider.getWorkoutRepository() }

    // Arguments
    private val routineId: String by lazy {
        arguments?.getString("routineId") ?: ""
    }
    private val routineName: String by lazy {
        arguments?.getString("routineName") ?: ""
    }

    // UI 요소
    private lateinit var tvProgress: TextView
    private lateinit var tvExerciseName: TextView
    private lateinit var tvTargetInfo: TextView
    private lateinit var tvTimer: TextView
    private lateinit var rvSets: RecyclerView
    private lateinit var btnAddSet: Button
    private lateinit var btnFinishExercise: Button

    // 데이터
    private var routine: Routine? = null
    private var currentExerciseIndex = 0
    private lateinit var setAdapter: ExerciseSetAdapter
    private val completedWorkouts = mutableListOf<WorkoutLog>()

    // 타이머
    private var startTime: Long = 0
    private var isTimerRunning = false
    private var timerJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_execution, container, false)

        initViews(view)
        loadRoutine()

        return view
    }

    private fun initViews(view: View) {
        tvProgress = view.findViewById(R.id.tvProgress)
        tvExerciseName = view.findViewById(R.id.tvExerciseName)
        tvTargetInfo = view.findViewById(R.id.tvTargetInfo)
        tvTimer = view.findViewById(R.id.tvTimer)
        rvSets = view.findViewById(R.id.rvSets)
        btnAddSet = view.findViewById(R.id.btnAddSet)
        btnFinishExercise = view.findViewById(R.id.btnFinishExercise)

        // RecyclerView 설정
        setAdapter = ExerciseSetAdapter()
        rvSets.layoutManager = LinearLayoutManager(requireContext())
        rvSets.adapter = setAdapter

        // 버튼 리스너
        btnAddSet.setOnClickListener {
            addNewSet()
        }

        btnFinishExercise.setOnClickListener {
            finishCurrentExercise()
        }
    }

    private fun loadRoutine() {
        lifecycleScope.launch {
            routine = routineRepo.getRoutine(routineId)
            if (routine == null || routine!!.exercises.isEmpty()) {
                Toast.makeText(requireContext(), "루틴을 불러올 수 없습니다", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                return@launch
            }

            displayCurrentExercise()
            startTimer()
        }
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            startTime = System.currentTimeMillis()

            timerJob = lifecycleScope.launch {
                while (isTimerRunning) {
                    val elapsedTime = (System.currentTimeMillis() - startTime) / 1000
                    val minutes = elapsedTime / 60
                    val seconds = elapsedTime % 60
                    tvTimer.text = String.format("%02d:%02d", minutes, seconds)
                    delay(1000)
                }
            }
        }
    }

    private fun stopTimer(): Long {
        isTimerRunning = false
        timerJob?.cancel()
        return (System.currentTimeMillis() - startTime) / 1000
    }

    private fun displayCurrentExercise() {
        val currentRoutine = routine ?: return
        if (currentExerciseIndex >= currentRoutine.exercises.size) {
            return
        }

        val currentExercise = currentRoutine.exercises[currentExerciseIndex]

        // 진행 상황 표시
        tvProgress.text = "${currentExerciseIndex + 1} / ${currentRoutine.exercises.size}"

        // 운동 정보 표시
        tvExerciseName.text = currentExercise.exercise.name
        tvTargetInfo.text = "${currentExercise.targetSets}세트 × ${currentExercise.targetReps}회 × ${currentExercise.targetWeight}kg"

        // 세트 목록 초기화 (목표 세트 수만큼 기본 세트 추가)
        setAdapter.clearSets()
        for (i in 1..currentExercise.targetSets) {
            setAdapter.addSet(
                weight = currentExercise.targetWeight,
                reps = currentExercise.targetReps
            )
        }
    }

    private fun addNewSet() {
        val currentRoutine = routine ?: return
        val currentExercise = currentRoutine.exercises[currentExerciseIndex]

        setAdapter.addSet(
            weight = currentExercise.targetWeight,
            reps = currentExercise.targetReps
        )
    }

    private fun finishCurrentExercise() {
        val currentRoutine = routine ?: return
        val currentExercise = currentRoutine.exercises[currentExerciseIndex]

        // 현재 운동의 세트 데이터 가져오기
        val sets = setAdapter.getSets()

        if (sets.isEmpty()) {
            Toast.makeText(requireContext(), "최소 1세트를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        // 현재 운동 시간 계산
        val duration = stopTimer()

        // WorkoutLog 생성
        val workoutLog = WorkoutLog(
            id = "",
            exerciseId = currentExercise.exercise.id,
            date = System.currentTimeMillis(),
            sets = sets,
            memo = "",
            durationSeconds = duration
        )
        completedWorkouts.add(workoutLog)

        // 다음 운동으로 이동
        currentExerciseIndex++

        if (currentExerciseIndex >= currentRoutine.exercises.size) {
            // 모든 운동 완료
            saveWorkoutLogs()
        } else {
            // 다음 운동 표시
            displayCurrentExercise()
            // 다음 운동 타이머 재시작
            startTimer()
        }
    }

    private fun saveWorkoutLogs() {
        lifecycleScope.launch {
            try {
                // 모든 운동 기록 저장
                for (log in completedWorkouts) {
                    workoutRepo.saveWorkoutLog(log)
                }

                Toast.makeText(
                    requireContext(),
                    "운동 기록이 저장되었습니다! 🎉",
                    Toast.LENGTH_LONG
                ).show()

                // 홈 화면으로 돌아가기
                findNavController().navigate(R.id.homeFragment)

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "저장 중 오류가 발생했습니다: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimer()
    }
}
