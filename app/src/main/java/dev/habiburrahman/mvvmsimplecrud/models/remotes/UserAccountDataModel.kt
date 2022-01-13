package dev.habiburrahman.mvvmsimplecrud.models.remotes

import com.google.gson.annotations.SerializedName
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.CREATED_AT
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.EMAIL
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.ID
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.PASSWORD
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UPDATE_AT
import java.io.Serializable

data class UserAccountDataModel(

    @SerializedName(EMAIL)
    val emailValue: String?

): Serializable {

    @SerializedName(ID)
    var idValue: Int? = null

    @SerializedName(PASSWORD)
    var passwordValue: String? = null

    @SerializedName(UPDATE_AT)
    var updateAtValue: String? = null

    @SerializedName(CREATED_AT)
    var createdAtValue: String? = null

}
