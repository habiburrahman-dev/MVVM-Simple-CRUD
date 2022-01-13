package dev.habiburrahman.mvvmsimplecrud.datasource.remotes

import dev.habiburrahman.mvvmsimplecrud.models.remotes.BaseResponseApiModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.LoginResponseModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.StockItemModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.UserAccountDataModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SourceApi {

    @POST("register")
    suspend fun registerAccount(
        @Body inputBody: UserAccountDataModel // body only have value of email and pass
    ): Response<BaseResponseApiModel<UserAccountDataModel>>

    @POST("auth/login")
    suspend fun loginAccount(
        @Body inputBody: UserAccountDataModel // body only have value of email and pass
    ): Response<LoginResponseModel>

    @POST("item/{crud}")
    suspend fun crudStock(
        @Header("Authorization") inputToken: String,
        @Path("crud") inputPath: String, // path either be: add || update || delete || search
        @Body inputBody: StockItemModel
    ): Response<StockItemModel>

}