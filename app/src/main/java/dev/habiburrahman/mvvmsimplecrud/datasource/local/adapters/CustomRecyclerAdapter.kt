package dev.habiburrahman.mvvmsimplecrud.datasource.local.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import dev.habiburrahman.mvvmsimplecrud.R
import dev.habiburrahman.mvvmsimplecrud.databinding.ItemStockProductBinding
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables.StocksTable
import dev.habiburrahman.mvvmsimplecrud.models.local.ListCollectionModel
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.STOCK

class CustomRecyclerAdapter(
    inputRecyclerViewObjectListener: OnRecyclerViewObjectClickListener,
    inputParam: String,
    inputListCollection: ListCollectionModel
): RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder>(){

    private val listener by lazy { inputRecyclerViewObjectListener }
    private val param by lazy { inputParam }
    private val listCollection by lazy { inputListCollection }

    inner class CustomViewHolder(
        binding: ViewBinding
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        lateinit var mTvTitle: MaterialTextView
        lateinit var mTvBody: MaterialTextView
        private lateinit var mCard: MaterialCardView
        private lateinit var mImage: ShapeableImageView
        private lateinit var mImage1: ShapeableImageView

        init {
            when(param){
                STOCK -> { // init every object in item stock for recycler view
                    mTvTitle = itemView.findViewById(R.id.mtv_title_item_stock)
                    mTvBody = itemView.findViewById(R.id.mtv_body_item_stock)
                    // mCard as card item stock
                    mCard = itemView.findViewById(R.id.m_card_item_stock)
                    // mImage as edit button
                    mImage = itemView.findViewById(R.id.siv_item_stock_edit)
                    // mImage1 as delete button
                    mImage1 = itemView.findViewById(R.id.siv_item_stock_delete)
                    // set each object a click listener
                    mCard.setOnClickListener(this)
                    mImage.setOnClickListener(this)
                    mImage1.setOnClickListener(this)
                }
            }
        }

        override fun onClick(v: View?) {
            if (v != null) {
                listener.onRecyclerViewObjectListener(v, adapterPosition)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSearchList(
        inputNewList: ArrayList<StocksTable>
    ){
        listCollection.listStock = inputNewList
        notifyDataSetChanged()
    }

    interface OnRecyclerViewObjectClickListener{
        fun onRecyclerViewObjectListener(inputView: View, inputPosition: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CustomViewHolder(
            when(param){
                STOCK -> ItemStockProductBinding.inflate(inflater, parent, false)
                else -> throw IllegalArgumentException("Item View Binding Not Properly Define")
            }
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        when(param){
            STOCK -> {
                val item = listCollection.listStock[position]
                holder.mTvTitle.text = item.productNameValue
                holder.mTvBody.text = item.skuValue
            }
        }
    }

    override fun getItemCount(): Int {
        return when(param){
            STOCK -> listCollection.listStock.size
            else -> 0
        }
    }

}