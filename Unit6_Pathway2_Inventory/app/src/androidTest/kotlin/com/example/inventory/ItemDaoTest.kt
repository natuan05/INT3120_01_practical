package com.example.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    private lateinit var itemDao: ItemDao
    private lateinit var inventoryDatabase: InventoryDatabase

    private var item1 = Item(1, "Apples", 10.0, 20)
    private var item2 = Item(2, "Bananas", 15.0, 97)

    /**
     * Hàm này chạy TRƯỚC mỗi bài kiểm thử.
     * Nó tạo ra một cơ sở dữ liệu tạm thời trong bộ nhớ.
     */
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Sử dụng cơ sở dữ liệu trong bộ nhớ vì dữ liệu sẽ biến mất sau khi kiểm thử kết thúc.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            // Cho phép chạy truy vấn trên luồng chính, chỉ dành cho mục đích kiểm thử.
            .allowMainThreadQueries()
            .build()
        itemDao = inventoryDatabase.itemDao()
    }

    /**
     * Hàm này chạy SAU mỗi bài kiểm thử.
     * Nó đóng kết nối cơ sở dữ liệu.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }


    // Các bài kiểm thử sẽ được thêm vào đây
    private suspend fun addOneItemToDb() {
        itemDao.insert(item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        // 1. Hành động: Thêm một mặt hàng vào DB
        addOneItemToDb()
        // 2. Lấy tất cả mặt hàng từ DB
        val allItems = itemDao.getAllItems().first()
        // 3. Xác nhận: Danh sách chứa đúng 1 mặt hàng và mặt hàng đó khớp với item1
        assertEquals(allItems[0], item1)
    }

    private suspend fun addTwoItemsToDb() {
        itemDao.insert(item1)
        itemDao.insert(item2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        // 1. Hành động: Thêm hai mặt hàng vào DB
        addTwoItemsToDb()
        // 2. Lấy tất cả mặt hàng từ DB
        val allItems = itemDao.getAllItems().first()
        // 3. Xác nhận: Danh sách chứa đúng 2 mặt hàng và chúng khớp với item1 và item2
        assertEquals(allItems[0], item1)
        assertEquals(allItems[1], item2)
    }
}