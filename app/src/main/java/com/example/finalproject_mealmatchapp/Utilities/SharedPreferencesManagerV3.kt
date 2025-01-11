package com.example.finalproject_mealmatchapp.Utilities

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferencesManagerV3 private constructor(context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("SP_FILE", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: SharedPreferencesManagerV3? = null

        fun init(context: Context): SharedPreferencesManagerV3 {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesManagerV3(context).also { instance = it }
            }
        }

        fun getInstance(): SharedPreferencesManagerV3 {
            return instance
                ?: throw IllegalStateException("SharedPreferencesManagerV3 must be initialized by calling init(context) before use.")
        }
    }

    fun putString(key: String, value: String) {
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun deleteString(key: String) {
        with(sharedPref.edit()) {
            remove(key)
            apply()
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        with(sharedPref.edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun editString(key: String, newValue: String) {
        if (sharedPref.contains(key)) {
            with(sharedPref.edit()) {
                putString(key, newValue)
                apply()
            }
        } else {
            throw IllegalArgumentException("Key '$key' does not exist in SharedPreferences.")
        }
    }

    fun getValue(key: String): String {
        return when {
            sharedPref.contains(key) -> {
                val allEntries = sharedPref.all
                allEntries[key].toString()
            }
            else -> throw IllegalArgumentException("Key '$key' does not exist in SharedPreferences.")
        }
    }

    fun getBooleanOrLog(key: String, defaultValue: Boolean): Boolean {
        return if (sharedPref.contains(key)) {
            sharedPref.getBoolean(key, defaultValue)
        } else {
            Log.d("SharedPreferencesManagerV3", "D not found")
            defaultValue
        }
    }
}
