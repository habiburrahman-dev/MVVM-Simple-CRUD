package dev.habiburrahman.mvvmsimplecrud.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.AppRoom
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables.StocksTable
import dev.habiburrahman.mvvmsimplecrud.datasource.remotes.InstanceApi
import dev.habiburrahman.mvvmsimplecrud.models.remotes.BaseResponseApiModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.LoginResponseModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.StockItemModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.UserAccountDataModel
import dev.habiburrahman.mvvmsimplecrud.repositories.RemoteRepository
import dev.habiburrahman.mvvmsimplecrud.repositories.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(inputApplication: Application) : AndroidViewModel(inputApplication) {

    private val stockRepository by lazy {
        RemoteRepository(InstanceApi.restaurantAPI)
    }
    private val roomRepository by lazy {
        RoomRepository(
            AppRoom.getDatabase(inputApplication).appDao()
        )
    }

    val registerResponse: LiveData<BaseResponseApiModel<UserAccountDataModel>>
    val loginResponse: LiveData<LoginResponseModel>
    val addStockResponse: LiveData<StockItemModel>
    val deleteStockResponse: LiveData<StockItemModel>
    val updateStockResponse: LiveData<StockItemModel>
    val searchStockResponse: LiveData<StockItemModel>
    val loadStocks: LiveData<List<StocksTable>>

    init {
        registerResponse = stockRepository.registerResponseData
        loginResponse = stockRepository.loginResponseData
        addStockResponse = stockRepository.addStockResponseData
        deleteStockResponse = stockRepository.deleteStockResponseData
        updateStockResponse = stockRepository.updateStockResponseData
        searchStockResponse = stockRepository.searchStockResponseData
        loadStocks = roomRepository.getStocks
    }

    fun doRegisterAccount(
        inputUserData: UserAccountDataModel,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.registerAccount(inputUserData)
        }
    }

    fun doLoginAccount(
        inputUserData: UserAccountDataModel,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.loginAccount(inputUserData)
        }
    }

    fun doCrudStockItem(
        inputContext: Context,
        inputPath: String, // path either be: add || update || delete || search
        inputStockItem: StockItemModel,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.crudStock(
                inputContext,
                inputPath, // path either be: add || update || delete || search
                inputStockItem
            )
        }
    }

    fun searchStocksFromRoomDb(
        inputQuery: String,
    ): LiveData<List<StocksTable>> {
        return roomRepository.searchStocks("%$inputQuery%")
    }

    fun saveStocksToRoomDb(
        inputStock: StocksTable,
    ) = viewModelScope.launch(Dispatchers.IO) {
        roomRepository.insertStock(inputStock)
    }

    fun updateStocksInRoomDb(
        inputUpdate: StocksTable,
    ) = viewModelScope.launch(Dispatchers.IO) {
        roomRepository.updateStock(inputUpdate)
    }

    fun removeStocksInRoomDb(
        inputSelectedStock: StocksTable,
    ) = viewModelScope.launch(Dispatchers.IO) {
        roomRepository.deleteStock(inputSelectedStock)
    }

}