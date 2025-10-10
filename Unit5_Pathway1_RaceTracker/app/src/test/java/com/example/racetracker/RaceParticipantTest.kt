/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.racetracker

import com.example.racetracker.ui.RaceParticipant
import kotlinx.coroutines.launch // Thêm import này
import kotlinx.coroutines.test.advanceTimeBy // Thêm import này
import kotlinx.coroutines.test.runCurrent // Thêm import này
import kotlinx.coroutines.test.runTest // Thêm import này
import org.junit.Assert.assertEquals
import org.junit.Test

class RaceParticipantTest {
    private val raceParticipant = RaceParticipant(
        name = "Player 1",
        maxProgress = 100,
        progressDelayMillis = 100L,
        initialProgress = 0,
        progressIncrement = 1
    )

    @Test
    fun raceParticipant_RaceStarted_ProgressUpdated() = runTest {
        val expectedProgress = 1
        // 1. Bắt đầu cuộc đua trong một coroutine riêng
        launch { raceParticipant.run() }

        // 2. "Tua nhanh" thời gian ảo đúng bằng khoảng delay
        advanceTimeBy(raceParticipant.progressDelayMillis)

        // 3. Thực thi các tác vụ đang chờ tại thời điểm hiện tại
        runCurrent()

        // 4. Kiểm tra kết quả
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_RaceFinished_ProgressUpdated() = runTest {
        // 1. Bắt đầu cuộc đua
        launch { raceParticipant.run() }

        // 2. "Tua nhanh" đến hết cuộc đua
        advanceTimeBy(raceParticipant.maxProgress * raceParticipant.progressDelayMillis)

        // 3. Thực thi các tác vụ đang chờ
        runCurrent()

        // 4. Kiểm tra kết quả
        assertEquals(100, raceParticipant.currentProgress)
    }
}