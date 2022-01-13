package dev.habiburrahman.mvvmsimplecrud.models.remotes

import com.google.gson.annotations.SerializedName
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.CREATED_AT
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.ERROR
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.ID
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.IMAGE
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.PRICE
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.PRODUCT_NAME
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.QTY
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.SKU
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.STATUS
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UNIT
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UPDATE_AT
import java.io.Serializable

data class StockItemModel(

    @SerializedName(SKU)
    val skuValue: String?

): Serializable {

    @SerializedName(PRODUCT_NAME)
    var productNameValue: String? = null

    @SerializedName(QTY)
    var qtyValue: String? = null

    @SerializedName(PRICE)
    var priceValue: String? = null

    @SerializedName(STATUS)
    var statusValue: String? = null

    @SerializedName(UNIT)
    var unitValue: String? = null

    @SerializedName(ID)
    var idValue: Int? = null

    @SerializedName(IMAGE)
    var imageUrlValue: String? = null

    @SerializedName(ERROR)
    var errorValue: String? = null

    @SerializedName(UPDATE_AT)
    var updateAtValue: String? = null

    @SerializedName(CREATED_AT)
    var createdAtValue: String? = null

}