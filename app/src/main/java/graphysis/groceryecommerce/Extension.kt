package graphysis.groceryecommerce

import android.content.Context
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





