package dev.habiburrahman.mvvmsimplecrud

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.habiburrahman.mvvmsimplecrud.databinding.ActivityMainBinding
import dev.habiburrahman.mvvmsimplecrud.datasource.local.AppSharedPreference
import dev.habiburrahman.mvvmsimplecrud.datasource.local.adapters.CustomRecyclerAdapter
import dev.habiburrahman.mvvmsimplecrud.datasource.local.room.tables.StocksTable
import dev.habiburrahman.mvvmsimplecrud.models.local.DialogDataModel
import dev.habiburrahman.mvvmsimplecrud.models.local.ListCollectionModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.StockItemModel
import dev.habiburrahman.mvvmsimplecrud.models.remotes.UserAccountDataModel
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.ADD
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.CRUD_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.LOGIN_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.NOTICE_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.REGISTER_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.SEARCH
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.STOCK
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.STOCK_DETAIL_DIALOG
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.UPDATE
import dev.habiburrahman.mvvmsimplecrud.utils.handler.CustomDialogHandler
import dev.habiburrahman.mvvmsimplecrud.viewmodels.AppViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), CustomDialogHandler.AlertDialogObjectListener,
    CustomRecyclerAdapter.OnRecyclerViewObjectClickListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    // late init collection
    private lateinit var binding: ActivityMainBinding
    private lateinit var appViewModel: AppViewModel
    private lateinit var customDialogHandler: CustomDialogHandler
    private lateinit var setDialogData: DialogDataModel
    private lateinit var mPref: AppSharedPreference
    private lateinit var mAdapter: CustomRecyclerAdapter

    private var listCollection = ListCollectionModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init
        appViewModel = ViewModelProvider(this)[AppViewModel::class.java]
        mPref = AppSharedPreference(this)

        runBlocking {
            when {
                mPref.getTokenCredential().isNullOrBlank() -> {
                    binding.mtvUser.visibility = View.GONE
                }
                else -> {
                    binding.mtvUser.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.t_user)
                    }
                }
            }
        }

        // observe register response
        appViewModel.registerResponse.observe(this, {
            when (it.isSuccess) {
                true -> {
                    Toast.makeText(this, it.getMessage, Toast.LENGTH_LONG)
                        .show()
                    customDialogHandler.dismiss()
                }
                else -> {
                    Toast.makeText(this, "Error Register", Toast.LENGTH_LONG)
                        .show()
                    customDialogHandler.dismiss()
                }
            }
        })

        // observe login response
        appViewModel.loginResponse.observe(this, {
            when {
                it.errorValue != null -> {
                    Toast.makeText(this, it.errorValue, Toast.LENGTH_LONG)
                        .show()
                    customDialogHandler.dismiss()
                }
                else -> {
                    when {
                        it.tokenValue != null -> {
                            mPref.saveTokenCredential(it.tokenValue)
                            binding.mtvUser.apply {
                                visibility = View.VISIBLE
                                text = getString(R.string.t_user)
                            }
                            customDialogHandler.dismiss()
                        }
                    }

                }
            }
        })

        // observe add stock response
        appViewModel.addStockResponse.observe(this, {
            when {
                it.errorValue != null -> {
                    Toast.makeText(this,
                        "Pemberitahuan: ${it.errorValue}\nSilahkan Login Kembali",
                        Toast.LENGTH_LONG)
                        .show()
                    customDialogHandler.dismiss()
                }
                else -> {
                    runBlocking {
                        appViewModel.saveStocksToRoomDb(
                            StocksTable(
                                idValue = it.idValue,
                                skuValue = it.skuValue,
                                productNameValue = it.productNameValue,
                                qtyValue = it.qtyValue,
                                priceValue = it.priceValue,
                                statusValue = it.statusValue,
                                unitValue = it.unitValue,
                                updateAtValue = it.updateAtValue,
                                createdAtValue = it.createdAtValue
                            )
                        )
                        launch {
                            Toast.makeText(this@MainActivity,
                                "SKU: ${it.skuValue} berhasil di simpan",
                                Toast.LENGTH_LONG)
                                .show()
                            customDialogHandler.dismiss()
                        }
                    }
                }
            }
        })

        // observe delete stock response
        appViewModel.deleteStockResponse.observe(this, {
            when {
                it.errorValue != null -> {
                    Toast.makeText(this,
                        "Pemberitahuan: ${it.errorValue}\nSilahkan Login Kembali",
                        Toast.LENGTH_LONG)
                        .show()
                    customDialogHandler.dismiss()
                }
                else -> {
                    runBlocking {
                        appViewModel.removeStocksInRoomDb(
                            StocksTable(
                                idValue = it.idValue,
                                skuValue = it.skuValue,
                                productNameValue = it.productNameValue,
                                qtyValue = it.qtyValue,
                                priceValue = it.priceValue,
                                statusValue = it.statusValue,
                                unitValue = it.unitValue,
                                updateAtValue = it.updateAtValue,
                                createdAtValue = it.createdAtValue
                            )
                        )
                        launch {
                            Toast.makeText(this@MainActivity,
                                "SKU: ${it.skuValue} berhasil di hapus",
                                Toast.LENGTH_LONG)
                                .show()
                            customDialogHandler.dismiss()
                        }
                    }
                }
            }
        })

        // observe update stock response
        appViewModel.updateStockResponse.observe(this, {
            when {
                it.errorValue != null -> {
                    Toast.makeText(this,
                        "Pemberitahuan: ${it.errorValue}\nSilahkan Login Kembali",
                        Toast.LENGTH_LONG)
                        .show()
                    customDialogHandler.dismiss()
                }
                else -> {
                    runBlocking {
                        appViewModel.updateStocksInRoomDb(
                            StocksTable(
                                idValue = it.idValue,
                                skuValue = it.skuValue,
                                productNameValue = it.productNameValue,
                                qtyValue = it.qtyValue,
                                priceValue = it.priceValue,
                                statusValue = it.statusValue,
                                unitValue = it.unitValue,
                                updateAtValue = it.updateAtValue,
                                createdAtValue = it.createdAtValue
                            )
                        )
                        launch {
                            Toast.makeText(this@MainActivity,
                                "SKU: ${it.skuValue} berhasil di update",
                                Toast.LENGTH_LONG)
                                .show()
                            customDialogHandler.dismiss()
                        }
                    }
                }
            }
        })

        // observe search stock response
        appViewModel.searchStockResponse.observe(this, {
            when {
                it.errorValue != null -> {
                    Toast.makeText(this,
                        "Pemberitahuan: ${it.errorValue}\nSilahkan Login Kembali",
                        Toast.LENGTH_LONG)
                        .show()
                    customDialogHandler.dismiss()
                }
                else -> {
                    when{
                        it.productNameValue != null -> {
                            // show dialog detail
                            showCustomDialog(
                                DialogDataModel(STOCK_DETAIL_DIALOG).apply {
                                    stockData = StockItemModel(
                                        skuValue = it.skuValue
                                    ).apply {
                                        productNameValue = it.productNameValue
                                        qtyValue = it.qtyValue
                                        priceValue = it.priceValue
                                        statusValue = it.statusValue
                                        unitValue = it.unitValue
                                    }
                                }
                            )
                            runBlocking {
                                Toast.makeText(this@MainActivity,
                                    "SKU: ${it.skuValue} berhasil ditemukan",
                                    Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        else -> Toast.makeText(this@MainActivity,
                            "Data tidak berhasil ditemukan",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })

        // observe list stock from room db
        appViewModel.loadStocks.observe(this, {
            when {
                it.isEmpty() -> {
                    // do something
                }
                else -> {
                    listCollection.listStock = it as ArrayList<StocksTable>
                    binding.rvStockItem.layoutManager = LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    mAdapter = CustomRecyclerAdapter(
                        this@MainActivity,
                        STOCK,
                        listCollection
                    )
                    mAdapter.notifyItemRangeChanged(0, listCollection.listStock.size)
                    binding.rvStockItem.adapter = mAdapter
                }
            }
        })

        // button register listener
        binding.mBtnRegister.setOnClickListener {
            // show register dialog
            showCustomDialog(
                DialogDataModel(REGISTER_DIALOG)
            )
        }

        // button login listener
        binding.mBtnLogin.setOnClickListener {
            // show login dialog
            showCustomDialog(
                DialogDataModel(LOGIN_DIALOG)
            )
        }

        // fab add stock listener
        binding.fabAdd.setOnClickListener {
            // show crud dialog
            showCustomDialog(
                DialogDataModel(CRUD_DIALOG).apply {
                    pathValue = ADD
                }
            )
        }

        // search view query listener
        binding.svMain.setOnQueryTextListener(this)

    }

    private fun showCustomDialog(
        inputDialogData: DialogDataModel,
    ) = runBlocking {
        setDialogData = inputDialogData.apply {
            when (inputDialogData.dialogTypeValue) {
                REGISTER_DIALOG -> {
                    titleValue = getString(R.string.t_register_account)
                    rightButtonTextValue = getString(R.string.t_register)
                }
                LOGIN_DIALOG -> {
                    titleValue = getString(R.string.t_login_account)
                    rightButtonTextValue = getString(R.string.t_login)
                }
                CRUD_DIALOG -> {
                    when (inputDialogData.pathValue) {
                        ADD -> {
                            titleValue = getString(R.string.t_add_stock_item)
                            rightButtonTextValue = getString(R.string.t_add)
                        }
                        UPDATE -> {
                            titleValue = getString(R.string.t_update_stock_item)
                            rightButtonTextValue = getString(R.string.t_update)
                        }
                    }
                }
                NOTICE_DIALOG -> {
                    titleValue = "PERHATIAN"
                    rightButtonTextValue = "Yakin"
                }
                STOCK_DETAIL_DIALOG -> {
                    rightButtonTextValue = null
                }
            }
            leftButtonTextValue = getString(R.string.t_close)
        }
        launch {
            customDialogHandler = CustomDialogHandler(
                inputDataDialog = setDialogData,
                inputListener = this@MainActivity
            ).apply {
                isCancelable = false
                show(supportFragmentManager, setDialogData.dialogTypeValue)
            }
        }
    }

    override fun onDialogObjectListener(
        inputView: View,
        inputAlertDialog: AlertDialog,
        inputDialogData: DialogDataModel,
    ) {
        when (inputView.id) {
            R.id.m_btn_close -> inputAlertDialog.dismiss()
            R.id.m_btn_process -> {
                val email = inputAlertDialog.findViewById<TextInputEditText>(R.id.et_email)
                    ?.let { it.text.toString() }
                val password =
                    inputAlertDialog.findViewById<TextInputEditText>(R.id.et_password)
                        ?.let { it.text.toString() }
                val emailLayout = inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_email)
                val passwordLayout =
                    inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_password)
                if (email != null && password != null) {
                    if (email.isEmpty() || password.isEmpty() || email.isBlank() || password.isBlank()) {
                        if ((email.isEmpty() || email.isBlank()) && (password.isEmpty() || password.isBlank())) {
                            emailLayout!!.error = getString(R.string.t_cannot_be_empty)
                            passwordLayout!!.error = getString(R.string.t_cannot_be_empty)
                        } else {
                            if (email.isEmpty() || email.isBlank()) {
                                emailLayout!!.error = getString(R.string.t_cannot_be_empty)
                                passwordLayout!!.error = null
                            } else if (password.isEmpty() || password.isBlank()) {
                                emailLayout!!.error = null
                                passwordLayout!!.error = getString(R.string.t_cannot_be_empty)
                            }
                        }
                    } else {
                        runBlocking {
                            emailLayout!!.error = null
                            passwordLayout!!.error = null
                            launch {
                                val userData = UserAccountDataModel(
                                    emailValue = email
                                ).apply {
                                    passwordValue = password
                                }
                                when (inputDialogData.dialogTypeValue) {
                                    REGISTER_DIALOG -> appViewModel.doRegisterAccount(userData)
                                    LOGIN_DIALOG -> appViewModel.doLoginAccount(userData)
                                }
                            }
                            launch {
                                inputAlertDialog.findViewById<LinearLayoutCompat>(R.id.lin_lay_credential)
                                    ?.apply {
                                        visibility = View.GONE
                                    }
                                inputAlertDialog.findViewById<ProgressBar>(R.id.pb_credential)
                                    ?.apply {
                                        visibility = View.VISIBLE
                                    }
                            }
                        }
                    }
                }

            }
            R.id.m_btn_crud_process -> {

                val productNameLayout =
                    inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_product_name)
                val qtyLayout = inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_qty)
                val priceLayout = inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_price)
                val unitLayout = inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_unit)

                val productName =
                    inputAlertDialog.findViewById<TextInputEditText>(R.id.et_product_name)?.text
                val qty = inputAlertDialog.findViewById<TextInputEditText>(R.id.et_qty)?.text
                val price = inputAlertDialog.findViewById<TextInputEditText>(R.id.et_price)?.text
                val unit = inputAlertDialog.findViewById<TextInputEditText>(R.id.et_unit)?.text

                if (productName.isNullOrBlank() or productName.isNullOrEmpty())
                    productNameLayout?.apply { error = getString(R.string.t_cannot_be_empty) }
                else
                    productNameLayout?.apply { error = null }

                if (qty.isNullOrBlank() or qty.isNullOrEmpty())
                    qtyLayout?.apply { error = getString(R.string.t_cannot_be_empty) }
                else
                    qtyLayout?.apply { error = null }

                if (price.isNullOrBlank() or price.isNullOrEmpty())
                    priceLayout?.apply { error = getString(R.string.t_cannot_be_empty) }
                else
                    priceLayout?.apply { error = null }

                if (unit.isNullOrBlank() or unit.isNullOrEmpty())
                    unitLayout?.apply { error = getString(R.string.t_cannot_be_empty) }
                else
                    unitLayout?.apply { error = null }

                when (inputDialogData.pathValue) {
                    ADD -> {
                        val skuLayout =
                            inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_sku)
                        val statusLayout =
                            inputAlertDialog.findViewById<TextInputLayout>(R.id.m_til_status)

                        val sku =
                            inputAlertDialog.findViewById<TextInputEditText>(R.id.et_sku)?.text
                        val status =
                            inputAlertDialog.findViewById<TextInputEditText>(R.id.et_status)?.text

                        if (sku.isNullOrBlank() or sku.isNullOrEmpty())
                            skuLayout?.apply { error = getString(R.string.t_cannot_be_empty) }
                        else
                            skuLayout?.apply { error = null }

                        if (status.isNullOrBlank() or status.isNullOrEmpty())
                            statusLayout?.apply { error = getString(R.string.t_cannot_be_empty) }
                        else
                            statusLayout?.apply { error = null }

                        runBlocking {

                            if (!(sku.isNullOrBlank() or sku.isNullOrEmpty())
                                && !(productName.isNullOrBlank() or productName.isNullOrEmpty())
                                && !(qty.isNullOrBlank() or qty.isNullOrEmpty())
                                && !(price.isNullOrBlank() or price.isNullOrEmpty())
                                && !(unit.isNullOrBlank() or unit.isNullOrEmpty())
                                && !(status.isNullOrBlank() or status.isNullOrEmpty())
                            ) {
                                when {
                                    inputDialogData.pathValue != null -> appViewModel.doCrudStockItem(
                                        inputContext = this@MainActivity,
                                        inputPath = inputDialogData.pathValue!!, // add stock
                                        inputStockItem = StockItemModel(
                                            skuValue = sku.toString()
                                        ).apply {
                                            productNameValue = productName.toString()
                                            qtyValue = qty.toString()
                                            statusValue = status.toString()
                                            priceValue = price.toString()
                                            unitValue = unit.toString()
                                        }
                                    )
                                }
                            }

                            launch {
                                inputAlertDialog.findViewById<LinearLayoutCompat>(R.id.lin_lay_crud)
                                    ?.apply {
                                        visibility = View.GONE
                                    }
                                inputAlertDialog.findViewById<ProgressBar>(R.id.pb_crud_stock_item)
                                    ?.apply {
                                        visibility = View.VISIBLE
                                    }
                            }
                        }

                    }
                    UPDATE -> {
                        runBlocking {

                            if (!(productName.isNullOrBlank() or productName.isNullOrEmpty())
                                && !(qty.isNullOrBlank() or qty.isNullOrEmpty())
                                && !(price.isNullOrBlank() or price.isNullOrEmpty())
                                && !(unit.isNullOrBlank() or unit.isNullOrEmpty())
                            ) {
                                when {
                                    inputDialogData.pathValue != null -> appViewModel.doCrudStockItem(
                                        inputContext = this@MainActivity,
                                        inputPath = inputDialogData.pathValue!!, // update stock
                                        inputStockItem = StockItemModel(
                                            skuValue = inputDialogData.stockData!!.skuValue
                                        ).apply {
                                            productNameValue = productName.toString()
                                            qtyValue = qty.toString()
                                            statusValue = inputDialogData.stockData!!.statusValue
                                            priceValue = price.toString()
                                            unitValue = unit.toString()
                                        }
                                    )
                                }
                            }

                            launch {
                                inputAlertDialog.findViewById<LinearLayoutCompat>(R.id.lin_lay_crud)
                                    ?.apply {
                                        visibility = View.GONE
                                    }
                                inputAlertDialog.findViewById<ProgressBar>(R.id.pb_crud_stock_item)
                                    ?.apply {
                                        visibility = View.VISIBLE
                                    }
                            }
                        }
                    }
                }

            }
            R.id.m_btn_notice_process -> {
                inputDialogData.stockData?.let {
                    appViewModel.doCrudStockItem(
                        inputContext = this,
                        inputPath = inputDialogData.pathValue.toString(),
                        inputStockItem = it
                    )
                }
            }
        }
    }

    override fun onRecyclerViewObjectListener(inputView: View, inputPosition: Int) {
        when (inputView.id) {
            R.id.siv_item_stock_delete -> showCustomDialog(
                DialogDataModel(NOTICE_DIALOG).apply {
                    bodyValue =
                        "Apakah anda yakin ingin menghapus \nstok ${listCollection.listStock[inputPosition].productNameValue}?"
                    stockData = StockItemModel(
                        skuValue = listCollection.listStock[inputPosition].skuValue
                    )
                }
            )
            R.id.siv_item_stock_edit -> showCustomDialog(
                DialogDataModel(CRUD_DIALOG).apply {
                    pathValue = UPDATE
                    stockData = StockItemModel(
                        skuValue = listCollection.listStock[inputPosition].skuValue
                    ).apply {
                        productNameValue = listCollection.listStock[inputPosition].productNameValue
                        qtyValue = listCollection.listStock[inputPosition].qtyValue
                        priceValue = listCollection.listStock[inputPosition].priceValue
                        statusValue = listCollection.listStock[inputPosition].statusValue
                        unitValue = listCollection.listStock[inputPosition].unitValue
                    }
                }
            )
            R.id.m_card_item_stock -> {
                appViewModel.doCrudStockItem(
                    inputContext = this@MainActivity,
                    inputPath = SEARCH, // search stock
                    inputStockItem = StockItemModel(
                        skuValue = listCollection.listStock[inputPosition].skuValue
                    )
                )
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        appViewModel.doCrudStockItem(
            inputContext = this@MainActivity,
            inputPath = SEARCH,
            inputStockItem = StockItemModel(query.orEmpty())
        )
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        appViewModel.loadStocks.observe(this, {
            when{
                it.isNotEmpty() -> {
                    when {
                        !newText.isNullOrBlank() -> {
                            appViewModel.searchStocksFromRoomDb(newText)
                                .observe(this@MainActivity, { list ->
                                    mAdapter.setSearchList(list as ArrayList<StocksTable>)
                                })
                        }
                    }
                }
            }
        })
        return true
    }

}
