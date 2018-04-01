package graphysis.groceryecommerce

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.net.NetworkInfo
import android.net.ConnectivityManager



/**
 * Created by pritesh on 21/3/18.
 */
class ListenNetworkChange : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("hello","I am network Broadcast");
        if(p0!=null){
            if(isNetworkAvailable(p0)==true){
                Log.d("hello","I am network Broadcast and i am online");
                //p0.ShowToast("I am network Broadcast and i am online");
            }else{
                Log.d("hello","I am network Broadcast and i am offline");
                //p0.ShowToast("I am network Broadcast and i am offline");
            }
        }

    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            // Network is present and connected
            isAvailable = true
        }
        return isAvailable
    }
}