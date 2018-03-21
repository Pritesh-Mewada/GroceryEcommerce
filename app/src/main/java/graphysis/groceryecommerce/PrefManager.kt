package graphysis.groceryecommerce

import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences



/**
 * Created by pritesh on 17/1/18.
 */
class PrefManager(context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var _context: Context
    // shared pref mode
    var PRIVATE_MODE = 0
    // Shared preferences file name
    private val PREF_NAME = "grocery-welcome"

    private val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"

    init{
        this._context = context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }





    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor.commit()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }
}