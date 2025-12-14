package com.example.health.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.model.Exercise

/**
 * 운동 목록을 표시하는 RecyclerView Adapter
 * - 어댑터 패턴 활용
 * - ViewHolder 패턴으로 성능 최적화
 */
class ExerciseAdapter(
    private val listener: ExerciseItemListener
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    /**
     * 클릭 이벤트를 처리하는 인터페이스
     * - 개방-폐쇄 원칙(OCP): 인터페이스로 확장 가능
     */
    interface ExerciseItemListener {
        fun onExerciseClick(exercise: Exercise)
        fun onAddToRoutineClick(exercise: Exercise)
    }

    // 운동 목록 데이터
    private var exercises = listOf<Exercise>()

    /**
     * 새로운 운동 리스트로 업데이트
     */
    fun submitList(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    /**
     * ViewHolder 생성
     * - 레이아웃을 inflate하여 ViewHolder 생성
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    /**
     * ViewHolder에 데이터 바인딩
     */
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercises[position])
    }

    /**
     * 아이템 개수 반환
     */
    override fun getItemCount(): Int = exercises.size

    /**
     * ViewHolder 클래스
     * - 각 아이템 뷰를 관리
     * - findViewById를 한 번만 호출 (성능 최적화)
     */
    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // View 참조들 (lazy로 지연 초기화)
        private val tvExerciseName: TextView = itemView.findViewById(R.id.tv_exercise_name)
        private val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        private val tvDifficulty: TextView = itemView.findViewById(R.id.tv_difficulty)
        private val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
        private val tvCalories: TextView = itemView.findViewById(R.id.tv_calories)
        private val tvTargetMuscles: TextView = itemView.findViewById(R.id.tv_target_muscles)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val btnAddToRoutine: Button = itemView.findViewById(R.id.btn_add_to_routine)

        /**
         * 데이터를 뷰에 바인딩
         */
        fun bind(exercise: Exercise) {
            // 기본 정보 표시
            tvExerciseName.text = exercise.name
            tvCategory.text = exercise.category.displayName
            tvDifficulty.text = exercise.difficulty.displayName
            tvDuration.text = "${exercise.durationMinutes}분"
            tvCalories.text = "${exercise.caloriesPerSession} kcal"

            // 타겟 근육 표시
            tvTargetMuscles.text = "타겟: ${exercise.targetMuscles.joinToString(", ")}"

            // 설명 표시
            tvDescription.text = exercise.description

            // 클릭 이벤트 설정
            itemView.setOnClickListener {
                listener.onExerciseClick(exercise)
            }

            btnAddToRoutine.setOnClickListener {
                listener.onAddToRoutineClick(exercise)
            }
        }
    }
}