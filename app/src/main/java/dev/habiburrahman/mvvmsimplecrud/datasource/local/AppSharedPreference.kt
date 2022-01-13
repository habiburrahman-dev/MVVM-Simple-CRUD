package dev.habiburrahman.mvvmsimplecrud.datasource.local

import android.content.Context
import dev.habiburrahman.mvvmsimplecrud.BuildConfig
import dev.habiburrahman.mvvmsimplecrud.utils.Constant.TOKEN

class AppSharedPreference(inputContext: Context) {

    private val prefs = inputContext.getSharedPreferences(BuildConfig.PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val edit = prefs.edit()

    fun saveTokenCredential(
        inputToken: String
    ){
        edit.putString(TOKEN, inputToken)
            .apply()
    }

    fun getTokenCredential(): String? {
        return prefs.getString(TOKEN, null)
    }

    private fun removeTokenCredential(){
        edit.remove(TOKEN)
            .commit()
    }

    fun clearPref(){
        removeTokenCredential()
        edit.clear()
            .commit()
    }
}