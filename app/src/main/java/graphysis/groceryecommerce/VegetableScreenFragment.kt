package graphysis.groceryecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.vertical_product_layout.*

/**
 * Created by pritesh on 17/12/17.
 */
class VegetableScreenFragment:Fragment() {
    lateinit var adapter:VerticalRecyclerViewAdapter;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.vertical_product_layout,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = VerticalRecyclerViewAdapter(DataStorageClass.vegetable,context!!);

        product_view_vertical_recycle.adapter=adapter;
        product_view_vertical_recycle.layoutManager = LinearLayoutManager(context);

        product_checkout.setOnClickListener {
            goToFullView(CheckOutFragment())
        }


    }

    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }

}