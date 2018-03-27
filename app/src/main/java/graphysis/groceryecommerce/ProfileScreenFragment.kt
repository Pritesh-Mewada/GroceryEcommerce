package graphysis.groceryecommerce

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.profile_screen_fragment.*

/**
 * Created by pritesh on 30/1/18.
 */
class ProfileScreenFragment :Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_screen_fragment,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        goToEdit.setOnClickListener(View.OnClickListener {
            goToFullView(OrdersFragment())
        })

        initText()
    }
    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }


    fun initText(){
        var sharedPref: SharedPreferences = activity?.getSharedPreferences("User",Context.MODE_PRIVATE)!!;
        user_initial.text = sharedPref.getString("Username", "Default").get(0).toString().toUpperCase();
        user_name.text=sharedPref.getString("Username", "Default").toString().capitalize();
        user_email.text=sharedPref.getString("Email", "Default").toString().capitalize()
        if (!sharedPref.getString("landmark", "default_value").equals("default_value")){
            addressProfile.setText(sharedPref.getString("address", "default_value").toString()+"Near to: "+sharedPref.getString("landmark", "default_value").toString())

        }
        if(!sharedPref.getString("contact", "default_value").equals("default_value")){
            numberProfile.setText(sharedPref.getString("contact", "default_value").toString())
        }
    }
}