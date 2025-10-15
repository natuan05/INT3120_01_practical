package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import android.net.Uri
import androidx.work.workDataOf
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI

private const val TAG = "BlurWorker"

class BlurWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {

        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)

        // Hiển thị thông báo cho người dùng biết công việc đã bắt đầu
        makeStatusNotification(
            applicationContext.resources.getString(R.string.blurring_image),
            applicationContext
        )

        // Chạy tác vụ trên luồng I/O để không chặn luồng chính
        return withContext(Dispatchers.IO) {
            // Thêm độ trễ để mô phỏng công việc chạy chậm, giúp dễ quan sát
            delay(DELAY_TIME_MILLIS)

            return@withContext try {

                require(!resourceUri.isNullOrBlank()) {
                    applicationContext.resources.getString(R.string.invalid_input_uri)
                }
                val resolver = applicationContext.contentResolver
                val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )

                // 2. Làm mờ ảnh
                val output = blurBitmap(picture, 1)

                // 3. Ghi ảnh đã làm mờ vào một tệp tạm
                val outputUri = writeBitmapToFile(applicationContext, output)

                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

                makeStatusNotification(
                    "Output is $outputUri",
                    applicationContext
                )

                Result.success()

            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_applying_blur),
                    throwable
                )
                Result.failure()
            }
        }
    }
}