package com.example.fitness

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.model.WorkoutLog

class WorkoutHistoryAdapter : RecyclerView.Adapter<WorkoutHistoryAdapter.WorkoutHistoryViewHolder>() {

    private var workoutLogs = listOf<Pair<WorkoutLog, String>>()  // Pair of (WorkoutLog, ExerciseName)

    fun setWorkouts(logs: List<Pair<WorkoutLog, String>>) {
        workoutLogs = logs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_history, parent, false)
        return WorkoutHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutHistoryViewHolder, position: Int) {
        val (log, exerciseName) = workoutLogs[position]
        holder.bind(log, exerciseName)
    }

    override fun getItemCount() = workoutLogs.size

    class WorkoutHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvExerciseName: TextView = itemView.findViewById(R.id.tvExerciseName)
        private val tvWorkoutSummary: TextView = itemView.findViewById(R.id.tvWorkoutSummary)
        private val tvWorkoutDuration: TextView = itemView.findViewById(R.id.tvWorkoutDuration)

        fun bind(log: WorkoutLog, exerciseName: String) {
            tvExerciseName.text = exerciseName

            // 평균 무게 계산
            val avgWeight = if (log.sets.isNotEmpty()) {
                log.sets.map { it.weight }.average()
            } else 0.0

            tvWorkoutSummary.text = "${log.sets.size}세트 × 평균 ${avgWeight.toInt()}kg"

            // 운동 시간 표시
            val minutes = log.durationSeconds / 60
            val seconds = log.durationSeconds % 60
            tvWorkoutDuration.text = if (minutes > 0) {
                "운동 시간: ${minutes}분 ${seconds}초"
            } else {
                "운동 시간: ${seconds}초"
            }
        }
    }
}
