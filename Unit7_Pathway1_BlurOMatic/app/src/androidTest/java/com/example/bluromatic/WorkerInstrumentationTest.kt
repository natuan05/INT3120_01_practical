package com.example.bluromatic

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bluromatic.workers.CleanupWorker
import com.example.bluromatic.workers.BlurWorker
import com.example.bluromatic.workers.SaveImageToFileWorker
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf


@RunWith(AndroidJUnit4::class)
class WorkerInstrumentationTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun cleanupWorker_doWork_resultSuccess() {
        val worker = TestListenableWorkerBuilder<CleanupWorker>(context).build()
        runBlocking {
            val result = worker.doWork()
            assertTrue(result is ListenableWorker.Result.Success)
        }
    }

    @Test
    fun blurWorker_doWork_resultSuccessReturnsUri() {
        val mockUriInput = KEY_IMAGE_URI to "android.resource://com.example.bluromatic/drawable/android_cupcake"
        val worker = TestListenableWorkerBuilder<BlurWorker>(context)
            .setInputData(workDataOf(mockUriInput))
            .build()

        runBlocking {
            val result = worker.doWork()
            val resultUri = result.outputData.getString(KEY_IMAGE_URI)

            assertTrue(result is ListenableWorker.Result.Success)
            assertTrue(result.outputData.keyValueMap.containsKey(KEY_IMAGE_URI))
            assertTrue(
                resultUri?.startsWith("file:///data/user/0/com.example.bluromatic/files/blur_filter_outputs/blur-filter-output-")
                    ?: false
            )
        }
    }
    @Test
    fun saveImageToFileWorker_doWork_resultSuccessReturnsUrl() {
        val mockUriInput = KEY_IMAGE_URI to "android.resource://com.example.bluromatic/drawable/android_cupcake"
        val worker = TestListenableWorkerBuilder<SaveImageToFileWorker>(context)
            .setInputData(workDataOf(mockUriInput))
            .build()

        runBlocking {
            val result = worker.doWork()
            val resultUri = result.outputData.getString(KEY_IMAGE_URI)

            assertTrue(result is ListenableWorker.Result.Success)
            assertTrue(result.outputData.keyValueMap.containsKey(KEY_IMAGE_URI))
            assertTrue(
                resultUri?.startsWith("content://media/external/images/media/")
                    ?: false
            )
        }
    }
}