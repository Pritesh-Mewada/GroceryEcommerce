package graphysis.groceryecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }
    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }
}