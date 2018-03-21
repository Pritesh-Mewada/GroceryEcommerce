package graphysis.groceryecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.checkout_fragment_layout.*

/**
 * Created by pritesh on 25/12/17.
 */
class CheckOutFragment: Fragment() {
    lateinit var checkoutRecycle:RecyclerView;
    lateinit var checkoutRecycleAdapter:CheckoutRecyclerViewAdapter;
    lateinit var totalPrice:TextView;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.checkout_fragment_layout,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalPrice = view.findViewById(R.id.total_price);
        checkoutRecycleAdapter = CheckoutRecyclerViewAdapter(context = context,jsonFruit =DataStorageClass.fruits ,jsonVegetable =DataStorageClass.vegetable,totalprice = totalPrice);
        checkoutRecycle = view.findViewById(R.id.checkout_recycle);

        checkoutRecycle.layoutManager = LinearLayoutManager(context);

        checkoutRecycle.adapter=checkoutRecycleAdapter

        checkoutFragmentButton.setOnClickListener {
            var mAddressDetailFragment:AddressDetailFragment = AddressDetailFragment();

            var bundle:Bundle = Bundle();
            bundle.putString("price",totalPrice.text.toString());
            mAddressDetailFragment.arguments = bundle;
            goToFullView(mAddressDetailFragment);
        }
    }

    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }

}