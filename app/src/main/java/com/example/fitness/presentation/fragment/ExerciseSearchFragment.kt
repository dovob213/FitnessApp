package com.example.fitness.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness.R
import com.example.fitness.model.Exercise
import com.example.fitness.model.ExerciseCategory
import com.example.fitness.model.DifficultyLevel
import com.example.fitness.data.repository.ExerciseRepositoryImpl
import com.example.fitness.presentation.adapter.ExerciseAdapter
import com.example.fitness.presentation.viewmodel.ExerciseViewModel
import com.example.fitness.presentation.viewmodel.ExerciseViewModelFactory
import com.google.android.material.chip.ChipGroup

/**
 * 운동 검색 및 추천 Fragment
 * - MVVM 패턴의 View 역할
 * - ViewModel 관찰하여 UI 업데이트
 */
class ExerciseSearchFragment : Fragment(), ExerciseAdapter.ExerciseItemListener {

    // ViewModel과 Adapter
    private lateinit var viewModel: ExerciseViewModel
    private lateinit var adapter: ExerciseAdapter

    // View 참조들 (onViewCreated에서 초기화)
    private lateinit var searchView: SearchView
    private lateinit var chipGroupCategory: ChipGroup
    private lateinit var chipGroupDifficulty: ChipGroup

    // 현재 사용자 레벨 (실제로는 프로필에서 가져와야 함)
    private var userLevel: DifficultyLevel = DifficultyLevel.BEGINNER

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment 레이아웃 inflate
        return inflater.inflate(R.layout.fragment_exercise_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View 참조 초기화
        initViews(view)

        // ViewModel 설정
        setupViewModel()

        // RecyclerView 설정
        setupRecyclerView(view)

        // 검색 기능 설정
        setupSearchView()

        // 필터 칩 설정
        setupFilters()

        // ViewModel 관찰 시작
        observeViewModel()

        // 추천 운동 로드
        loadRecommendations()
    }

    /**
     * View 참조 초기화
     */
    private fun initViews(view: View) {
        searchView = view.findViewById(R.id.search_view)
        chipGroupCategory = view.findViewById(R.id.chip_group_category)
        chipGroupDifficulty = view.findViewById(R.id.chip_group_difficulty)
    }

    /**
     * ViewModel 초기화
     * - Repository 생성
     * - Factory로 ViewModel 생성
     */
    private fun setupViewModel() {
        val repository = ExerciseRepositoryImpl()
        val factory = ExerciseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ExerciseViewModel::class.java]
    }

    /**
     * RecyclerView 설정
     */
    private fun setupRecyclerView(view: View) {
        // Adapter 생성 (this = ExerciseItemListener)
        adapter = ExerciseAdapter(this)

        // RecyclerView 찾기
        val recyclerView = view.findViewById<androidx.recyclerview.widget.RecyclerView>(
            R.id.rv_exercises
        )

        recyclerView.apply {
            // LayoutManager 설정 (수직 리스트)
            layoutManager = LinearLayoutManager(context)

            // Adapter 연결
            this.adapter = this@ExerciseSearchFragment.adapter

            // 구분선 추가
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    /**
     * 검색 기능 설정
     */
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 검색 버튼 클릭 시
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchExercises(it) }
                return true
            }

            // 검색어 입력 중
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    when {
                        it.isEmpty() -> viewModel.resetFilters()
                        it.length >= 2 -> viewModel.searchExercises(it)
                    }
                }
                return true
            }
        })
    }

    /**
     * 필터 칩 설정
     */
    private fun setupFilters() {
        // 카테고리 필터
        chipGroupCategory.setOnCheckedChangeListener { _, checkedId ->
            val category = when (checkedId) {
                R.id.chip_stretching -> ExerciseCategory.STRETCHING
                R.id.chip_strength -> ExerciseCategory.STRENGTH
                R.id.chip_cardio -> ExerciseCategory.CARDIO
                R.id.chip_flexibility -> ExerciseCategory.FLEXIBILITY
                R.id.chip_balance -> ExerciseCategory.BALANCE
                else -> null
            }
            viewModel.filterByCategory(category)
        }

        // 난이도 필터
        chipGroupDifficulty.setOnCheckedChangeListener { _, checkedId ->
            val difficulty = when (checkedId) {
                R.id.chip_beginner -> DifficultyLevel.BEGINNER
                R.id.chip_intermediate -> DifficultyLevel.INTERMEDIATE
                R.id.chip_advanced -> DifficultyLevel.ADVANCED
                else -> null
            }
            viewModel.filterByDifficulty(difficulty)
        }
    }

    /**
     * ViewModel 관찰
     * - LiveData 변경 시 UI 업데이트
     */
    private fun observeViewModel() {
        // 운동 리스트 관찰
        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            // Adapter에 새 데이터 전달
            adapter.submitList(exercises)

            // 결과 개수 표시 (TextView가 있다면)
            // tvResultCount.text = "총 ${exercises.size}개의 운동"
        }

        // 로딩 상태 관찰
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // ProgressBar 표시/숨김
            view?.findViewById<View>(R.id.progress_bar)?.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        // 에러 관찰
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // 추천 운동 관찰
        viewModel.recommendedExercises.observe(viewLifecycleOwner) { recommended ->
            // 추천 섹션 업데이트
            updateRecommendedSection(recommended)
        }
    }

    /**
     * 추천 운동 로드
     */
    private fun loadRecommendations() {
        viewModel.loadRecommendationsByLevel(userLevel, 4)
    }

    /**
     * 추천 섹션 업데이트
     */
    private fun updateRecommendedSection(exercises: List<Exercise>) {
        // TODO: 추천 운동 UI 업데이트
        // 예: 상단에 추천 운동 카드들 표시
    }

    // ================================================
    // ExerciseAdapter.ExerciseItemListener 구현
    // ================================================

    /**
     * 운동 항목 클릭 시
     */
    override fun onExerciseClick(exercise: Exercise) {
        Toast.makeText(
            context,
            "선택: ${exercise.name}",
            Toast.LENGTH_SHORT
        ).show()

        // TODO: 운동 상세 화면으로 이동
        // 또는 상세 다이얼로그 표시
    }

    /**
     * 루틴에 추가 버튼 클릭 시
     */
    override fun onAddToRoutineClick(exercise: Exercise) {
        // TODO: 승준님의 루틴 모듈과 연동
        Toast.makeText(
            context,
            "${exercise.name}을(를) 루틴에 추가했습니다",
            Toast.LENGTH_SHORT
        ).show()
    }
}