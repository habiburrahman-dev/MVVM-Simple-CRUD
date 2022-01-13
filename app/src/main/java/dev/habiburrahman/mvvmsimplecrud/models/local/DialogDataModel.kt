package dev.habiburrahman.mvvmsimplecrud.models.local

import com.google.gson.annotations.SerializedName
import dev.habiburrahman.mvvmsimplecrud.models.remotes.StockItemModel
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.DIALOG_TYPE
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.LEFT_BUTTON
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.MESSAGE
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.PATH
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.RIGHT_BUTTON
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.STOCK
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.TITLE
import java.io.Serializable

data class DialogDataModel(

    @SerializedName(DIALOG_TYPE)
    var dialogTypeValue: String?

): Serializable {

    @SerializedName(TITLE)
    var titleValue: String? = null

    @SerializedName(MESSAGE)
    var bodyValue: String? = null

    @SerializedName(LEFT_BUTTON)
    var leftButtonTextValue: String? = null

    @SerializedName(RIGHT_BUTTON)
    var rightButtonTextValue: String? = null

    @SerializedName(PATH)
    var pathValue: String? = null

    @SerializedName(STOCK)
    var stockData: StockItemModel? = null

}
