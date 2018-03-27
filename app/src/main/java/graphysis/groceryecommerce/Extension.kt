package graphysis.groceryecommerce

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * Created by pritesh on 6/1/18.
 */
fun Context.ShowToast(text:String){
    Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
}
fun Context.goToFullView(fragment: Fragment){
    var fragmentManager =( this  as FragmentActivity).supportFragmentManager;
    var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
    fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
    fragmentTransaction?.commit();
}

fun Context.CheckOnline ():Boolean{
    var manager :ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;

    if(manager !=null){
        var netinfo:NetworkInfo  = manager.activeNetworkInfo;
        return (netinfo!=null && netinfo.isConnected);
    }else{
        return false
    }
}

fun Context.IncrementCart(icon:LayerDrawable,count:String){
    var badge:BadgeDrawable;

    // Reuse drawable if possible
    var  reuse:Drawable = icon.findDrawableByLayerId(R.id.ic_badge);
    if (reuse != null && reuse is BadgeDrawable) {
        badge =  reuse as BadgeDrawable;
    } else {
        badge = BadgeDrawable(this);
    }

    badge.setCount(count);
    icon.mutate();
    icon.setDrawableByLayerId(R.id.ic_badge, badge);
}




