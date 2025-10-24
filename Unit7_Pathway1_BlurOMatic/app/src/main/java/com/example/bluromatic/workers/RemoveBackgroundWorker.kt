package com.example.bluromatic.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.R
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

private const val TAG = "RemoveBackgroundWorker"

class RemoveBackgroundWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        val resourceUri = inputData.getString(KEY_IMAGE_URI)



        return withContext(Dispatchers.IO) {
            delay(DELAY_TIME_MILLIS)

            return@withContext try {
                require(!resourceUri.isNullOrBlank()) {
                    applicationContext.resources.getString(R.string.invalid_input_uri)
                }
                val resolver = applicationContext.contentResolver
                val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))
                )

                // Tạo một trình phân đoạn selfie
                val options = SelfieSegmenterOptions.Builder()
                    .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                    .build()
                val segmenter = Segmentation.getClient(options)
                val image = InputImage.fromBitmap(picture, 0)

                // Chạy phân đoạn
                val resultBuffer = Tasks.await(segmenter.process(image)).buffer
                val mask = removeBackgroundFromMask(picture, resultBuffer)

                // Tạo ảnh mới với nền đã bị xóa
                val output = removeBackgroundFromMask(picture, resultBuffer)

                // Ghi ảnh đã xử lý vào tệp tạm
                val outputUri = writeBitmapToFile(applicationContext, output)

                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
                Result.success(outputData)
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_removing_background),
                    throwable
                )
                Result.failure()
            }
        }
    }

    // Các hàm trợ giúp để xử lý mặt nạ (mask) và bitmap
    @WorkerThread
    private fun removeBackgroundFromMask(image: Bitmap, mask: ByteBuffer): Bitmap {
        val bitmap = Bitmap.createBitmap(
            image.width,
            image.height,
            image.config ?: Bitmap.Config.ARGB_8888
        )
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val backgroundConfidence =
                    ((1.0 - mask.float) * 255).toInt().coerceIn(0, 255)

                if (backgroundConfidence >= 100) { // Tự tin đây là NỀN
                    bitmap.setPixel(x, y, Color.TRANSPARENT)
                } else { // Nếu không, đây là TIỀN CẢNH
                    bitmap.setPixel(x, y, image.getPixel(x, y)) // <-- SAO CHÉP PIXEL GỐC
                }
            }
        }
        mask.rewind()
        return bitmap
    }

    @WorkerThread
    private fun createForegroundBitmap(image: Bitmap, mask: Bitmap): Bitmap {
        // SỬA LỖI Ở ĐÂY: Cung cấp giá trị mặc định nếu image.config là null
        val result = Bitmap.createBitmap(
            image.width,
            image.height,
            image.config ?: Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(result)
        canvas.drawBitmap(image, Matrix(), null) // Vẽ ảnh gốc
        canvas.drawBitmap(mask, Matrix(), null) // Vẽ lớp mặt nạ (đã đục lỗ) lên trên
        return result
    }
}