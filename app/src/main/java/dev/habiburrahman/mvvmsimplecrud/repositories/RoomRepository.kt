package dev.habiburrahman.mvvmsimplecrud.repositories

import androidx.lifecycle.LiveData
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.AppDao
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables.StocksTable
import kotlinx.coroutines.flow.Flow

class RoomRepository(private val inputDao: AppDao) {

    val getStocks by lazy {
        inputDao.getStocks()
    }

    fun searchStocks(
        inputQuery: String
    ): LiveData<List<StocksTable>> {
        return inputDao.searchStocks(inputQuery)
    }

    suspend fun insertStock(
        inputStock: StocksTable
    ) = inputDao.insertStock(inputStock)

    suspend fun updateStock(
        inputUpdate: StocksTable
    ) = inputDao.updateStock(inputUpdate)

    suspend fun deleteStock(
        inputSelectedStock: StocksTable
    ) = inputDao.deleteStock(inputSelectedStock)

}