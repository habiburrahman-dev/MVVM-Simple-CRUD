package dev.habiburrahman.mvvmsimplecrud.datasource.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables.StocksTable
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Query("select * from product_stocks order by id asc")
    fun getStocks(): LiveData<List<StocksTable>>

    @Query("select * from product_stocks where sku like :inputSearch")
    fun searchStocks(inputSearch: String): LiveData<List<StocksTable>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStock(inputStock: StocksTable)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStock(inputUpdate: StocksTable)

    @Delete
    suspend fun deleteStock(inputSelectedStock: StocksTable)

}