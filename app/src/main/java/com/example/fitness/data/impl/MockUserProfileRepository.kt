package com.example.fitness.data.impl

import com.example.fitness.data.UserProfileRepository
import com.example.fitness.model.UserProfile

/* 파이어베이스에 실제 데이터 입력 구현 이전에,
    가라로 만들어 둔 샘플 데이터 파일입니다. 개발 때 테스트용. 나중에 지울 듯
 */

class MockUserProfileRepository : UserProfileRepository {
    // 메모리에만 저장. 앱 끄면 사라지게
    private val profiles = mutableMapOf<String, UserProfile>()

    // 테스트용 가라 데이터입니다.
    init {
        // 샘플 프로필
        profiles["user001"] = UserProfile(
            userId = "user001",
            name = "홍길동",
            age = 25,
            height = 175.0,
            weight = 70.0,
            level = 1,
            place = listOf("헬스장", "집"),
            goal = "근육 증량"
        )
    }

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return profiles[userId]
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        profiles[profile.userId] = profile
        println("Mock: 프로필 저장 - ${profile.name}")
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        profiles[profile.userId] = profile
        println("Mock: 프로필 업데이트 - ${profile.name}")
    }
}
