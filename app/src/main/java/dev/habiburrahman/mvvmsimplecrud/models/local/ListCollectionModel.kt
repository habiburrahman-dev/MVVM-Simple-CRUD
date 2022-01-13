package dev.habiburrahman.mvvmsimplecrud.models.local

import com.google.gson.annotations.SerializedName
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables.StocksTable
import java.io.Serializable

class ListCollectionModel: Serializable {

    @SerializedName("stock_list")
    var listStock: ArrayList<StocksTable> = ArrayList()

}