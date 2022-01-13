package dev.habiburrahman.mvvmsimplecrud.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import dev.habiburrahman.mvvmsimplecrud.datasource.local.AppSharedPreference
import dev.habiburrahman.mvvmsimplecrud.datasource.remotes.SourceApi
import dev.habiburrahman.mvvmsimplecrud.models.remotes.BaseResponseApiModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.LoginResponseModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.StockItemModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.UserAccountDataModel
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.ADD
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.DELETE
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.SEARCH
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UPDATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemoteRepository(private val inputApi: SourceApi) {

    val registerResponseData: MutableLiveData<BaseResponseApiModel<UserAccountDataModel>> =
        MutableLiveData()
    val loginResponseData: MutableLiveData<LoginResponseModel> = MutableLiveData()
    val addStockResponseData: MutableLiveData<StockItemModel> = MutableLiveData()
    val deleteStockResponseData: MutableLiveData<StockItemModel> = MutableLiveData()
    val updateStockResponseData: MutableLiveData<StockItemModel> = MutableLiveData()
    val searchStockResponseData: MutableLiveData<StockItemModel> = MutableLiveData()

    fun registerAccount(
        inputUserData: UserAccountDataModel,
    ) = CoroutineScope(Dispatchers.IO).launch {
        registerResponseData.apply {
            inputApi.registerAccount(inputUserData).apply {
                when {
                    isSuccessful -> {
                        postValue(body())
                    }
                    else -> throw Throwable(errorBody().toString())
                }
            }

        }
    }

    fun loginAccount(
        inputUserData: UserAccountDataModel,
    ) = CoroutineScope(Dispatchers.IO).launch {
        loginResponseData.apply {
            inputApi.loginAccount(inputUserData).apply {
                when {
                    isSuccessful -> {
                        postValue(body())
                    }
                    else -> postValue(
                        LoginResponseModel(
                            null
                        ).apply {
                            errorValue = "Something Error Please Try Again"
                        }
                    )
                }
            }

        }
    }

    fun crudStock(
        inputContext: Context,
        inputPath: String, // path either be: add || update || delete || search
        inputStockItem: StockItemModel,
    ) = CoroutineScope(Dispatchers.IO).launch {
        when (inputPath) {
            ADD -> addStockResponseData
            DELETE -> deleteStockResponseData
            UPDATE -> updateStockResponseData
            SEARCH -> searchStockResponseData
            else -> throw IllegalStateException("Unknown Path")
        }.apply {
            inputApi.crudStock(
                inputToken = "Bearer ${AppSharedPreference(inputContext).getTokenCredential()}",
                inputPath = inputPath, // path either be: add || update || delete || search
                inputBody = inputStockItem
            ).apply {
                when {
                    isSuccessful -> {
                        postValue(body())
                    }
                    else -> postValue(
                        StockItemModel(
                            null
                        ).apply {
                            errorValue = "Something Wrong"
                        }
                    )
                }
            }
        }
    }

}