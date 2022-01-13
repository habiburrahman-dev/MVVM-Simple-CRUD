package dev.habiburrahman.mvvmsimplecrud.models.remotes

import com.google.gson.annotations.SerializedName
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.ERROR
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.TOKEN
import java.io.Serializable

data class LoginResponseModel(

    @SerializedName(TOKEN)
    val tokenValue: String?

): Serializable {

    @SerializedName(ERROR)
    var errorValue: String? = null

}
