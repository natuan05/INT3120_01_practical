package com.example.marsphotos.data

/**
 * Interface định nghĩa các phần phụ thuộc sẽ được cung cấp.
 */
interface AppContainer {
    val marsPhotosRepository: MarsPhotosRepository
}