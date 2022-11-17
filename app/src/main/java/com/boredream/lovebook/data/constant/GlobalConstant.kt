package com.boredream.lovebook.data.constant

import android.util.Log
import com.boredream.lovebook.data.User
import com.boredream.lovebook.utils.DataStoreUtils
import com.google.gson.Gson

object GlobalConstant {

    var token: String? = null
    var curUser: User? = null

    fun saveToken(token: String?) {
        DataStoreUtils.putSyncData(DataStoreKey.TOKEN, token)
        GlobalConstant.token = token
    }

    fun getLocalToken(): String? {
        if (GlobalConstant.token == null) {
            try {
                GlobalConstant.token = DataStoreUtils.getSyncData(DataStoreKey.TOKEN, "")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return GlobalConstant.token
    }

    fun saveUser(user: User?) {
        DataStoreUtils.putSyncData(DataStoreKey.USER, if(user == null) null else Gson().toJson(user))
        curUser = user
    }

    fun getLocalUser(): User? {
        if (curUser == null) {
            try {
                val userJson = DataStoreUtils.getSyncData(DataStoreKey.USER, "")
                val user = Gson().fromJson(userJson, User::class.java)
                curUser = user
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return curUser
    }

}