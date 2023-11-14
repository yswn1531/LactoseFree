package com.sesac.lactosefree.common

import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val IS_LOCATION = "is_location"

class PermissionManager {
    companion object {
        private lateinit var manager: PermissionManager
        private lateinit var sp: SharedPreferences
        private lateinit var spEditor: SharedPreferences.Editor


        fun getInstance(): PermissionManager {
            if (this::manager.isInitialized) {
                return manager
            } else {
                sp = PreferenceManager.getDefaultSharedPreferences(
                    DefaultApplication.applicationContext())
                spEditor = sp.edit()
                manager = PermissionManager()
            }
            return manager
        }
    }

    /**
     * 본 앱의 퍼미션 체크 여부
     */
    var isPermission : Boolean
        get() = sp.getBoolean(IS_LOCATION, false)
        set(permissionCheck) {
            with(spEditor){
                putBoolean(IS_LOCATION, permissionCheck).apply()
            }
        }
}