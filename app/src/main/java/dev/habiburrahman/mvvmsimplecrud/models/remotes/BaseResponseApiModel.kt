package dev.habiburrahman.mvvmsimplecrud.models.remotes

import com.google.gson.annotations.SerializedName
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.DATA
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.MESSAGE
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.SUCCESS

data class BaseResponseApiModel<T>(

    @SerializedName(SUCCESS)
    val isSuccess: Boolean?,

    @SerializedName(MESSAGE)
    val getMessage: String?,

    @SerializedName(DATA)
    val getData: T?

)
