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
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.vertical_product_layout.*


/**
 * Created by pritesh on 17/12/17.
 */
class FruitScreenFragment: Fragment() {
    var fruitRecycle: RecyclerView?=null;
    lateinit var adapter:VerticalRecyclerViewAdapter;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fruitRecycle = view?.findViewById(R.id.product_view_vertical_recycle);

        adapter = VerticalRecyclerViewAdapter(DataStorageClass.fruits,context!!)
        fruitRecycle?.adapter=adapter;

        fruitRecycle?.layoutManager = LinearLayoutManager(context);

        product_checkout.setOnClickListener {
            goToFullView(CheckOutFragment())
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.vertical_product_layout,container,false);

    }

    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }

}