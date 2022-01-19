package dev.habiburrahman.mvvmsimplecrud.utils.handler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.button.MaterialButton
import dev.habiburrahman.mvvmsimplecrud.databinding.CustomDialogCrudStockItemBinding
import dev.habiburrahman.mvvmsimplecrud.databinding.CustomDialogDetailStockBinding
import dev.habiburrahman.mvvmsimplecrud.databinding.CustomDialogNoticeBinding
import dev.habiburrahman.mvvmsimplecrud.databinding.CustomDialogRequestCredentialBinding
import dev.habiburrahman.mvvmsimplecrud.models.local.DialogDataModel
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.CRUD_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.LOGIN_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.NOTICE_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.REGISTER_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.STOCK_DETAIL_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UPDATE
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CustomDialogHandler(
    inputListener: AlertDialogObjectListener,
    inputDataDialog: DialogDataModel,
    inputViewGroup: ViewGroup
) : AppCompatDialogFragment() {

    private val listener by lazy { inputListener }
    private val dialogData by lazy { inputDataDialog }
    private val parent by lazy { inputViewGroup }
    private lateinit var alertDialog: AlertDialog

    override fun onPause() {
        super.onPause()
        this.dismissAllowingStateLoss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val inflater by lazy { requireActivity().layoutInflater }
        val rootView by lazy {
            when (dialogData.dialogTypeValue) {
                LOGIN_DIALOG, REGISTER_DIALOG -> showInputCredentialDialog(inflater)
                CRUD_DIALOG -> showCrudStockItemDialog(inflater)
                NOTICE_DIALOG -> showNoticeDialog(inflater)
                STOCK_DETAIL_DIALOG -> showStockDetailDialog(inflater)
                else -> null
            }
        }
        alertDialog = requireActivity().let {
            AlertDialog.Builder(it).create()
        }.apply {
            setView(rootView)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        return alertDialog
    }

    private fun showInputCredentialDialog(inputInflater: LayoutInflater): View {
        return CustomDialogRequestCredentialBinding.inflate(inputInflater, parent, false)
            .apply {
                mtvDialogTitle.text = dialogData.titleValue
                mBtnClose.text = dialogData.leftButtonTextValue
                mBtnProcess.text = dialogData.rightButtonTextValue
                setOnclickButton(mBtnClose, mBtnProcess)
            }.root
    }

    private fun showCrudStockItemDialog(inputInflater: LayoutInflater): View {
        return CustomDialogCrudStockItemBinding.inflate(inputInflater, parent, false)
            .apply {
                runBlocking {
                    when (dialogData.pathValue) {
                        UPDATE -> {
                            mTilSku.visibility = View.GONE
                            mTilStatus.visibility = View.GONE
                            etPrice.setText(dialogData.stockData?.priceValue.toString())
                            etProductName.setText(dialogData.stockData?.productNameValue.toString())
                            etQty.setText(dialogData.stockData?.qtyValue.toString())
                            etUnit.setText(dialogData.stockData?.unitValue.toString())
                        }
                        else -> {
                            mTilSku.visibility = View.VISIBLE
                            mTilStatus.visibility = View.VISIBLE
                        }
                    }
                    launch {
                        mtvDialogTitle.text = dialogData.titleValue
                        mBtnClose.text = dialogData.leftButtonTextValue
                        mBtnCrudProcess.text = dialogData.rightButtonTextValue
                        setOnclickButton(mBtnClose, mBtnCrudProcess)
                    }
                }
            }.root
    }

    private fun showNoticeDialog(inputInflater: LayoutInflater): View {
        return CustomDialogNoticeBinding.inflate(inputInflater, parent, false).apply {
            mtvNotificationTitle.text = dialogData.titleValue
            mtvNotificationBody.text = dialogData.bodyValue
            mBtnClose.text = dialogData.leftButtonTextValue
            mBtnNoticeProcess.text = dialogData.rightButtonTextValue
            dialogData.dialogTypeValue = dialogData.pathValue
            setOnclickButton(mBtnClose, mBtnNoticeProcess)
        }.root
    }

    private fun showStockDetailDialog(inputInflater: LayoutInflater): View {
        return CustomDialogDetailStockBinding.inflate(inputInflater, parent, false).apply {
            mtvDialogDetailStockProductNameValue.text = dialogData.stockData?.productNameValue
            mtvDialogDetailStockSkuValue.text = dialogData.stockData?.skuValue
            mtvDialogDetailStockQtyValue.text = dialogData.stockData?.qtyValue
            mtvDialogDetailStockPriceValue.text = dialogData.stockData?.priceValue
            mtvDialogDetailStockUnitValue.text = dialogData.stockData?.unitValue
            mtvDialogDetailStockStatusValue.text = dialogData.stockData?.statusValue
            mBtnClose.text = dialogData.leftButtonTextValue
            mBtnClose.setOnClickListener {
                listener.onDialogObjectListener(it, alertDialog, dialogData)
            }
        }.root
    }

    private fun setOnclickButton(
        inputLeftButton: MaterialButton,
        inputRightButton: MaterialButton,
    ) {
        inputLeftButton.setOnClickListener {
            listener.onDialogObjectListener(it, alertDialog, dialogData)
        }
        inputRightButton.setOnClickListener {
            listener.onDialogObjectListener(it, alertDialog, dialogData)
        }
    }

    interface AlertDialogObjectListener {
        fun onDialogObjectListener(
            inputView: View,
            inputAlertDialog: AlertDialog,
            inputDialogData: DialogDataModel,
        )
    }
}