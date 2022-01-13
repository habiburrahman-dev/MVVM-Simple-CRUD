package dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.CREATED_AT
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.ID
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.PRICE
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.PRODUCT_NAME
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.QTY
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.SKU
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.STATUS
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UNIT
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UPDATE_AT

@Entity(tableName = "product_stocks")
data class StocksTable(

    @PrimaryKey
    @ColumnInfo(name = ID)
    val idValue: Int?,

    @ColumnInfo(name = SKU)
    val skuValue: String?,

    @ColumnInfo(name = PRODUCT_NAME)
    val productNameValue: String?,

    @ColumnInfo(name = QTY)
    val qtyValue: String?,

    @ColumnInfo(name = PRICE)
    val priceValue: String?,

    @ColumnInfo(name = STATUS)
    val statusValue: String?,

    @ColumnInfo(name = UNIT)
    val unitValue: String?,

    @ColumnInfo(name = UPDATE_AT)
    var updateAtValue: String?,

    @ColumnInfo(name = CREATED_AT)
    var createdAtValue: String?

)
