package graphysis.groceryecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import kotlinx.android.synthetic.main.vertical_product_layout.*
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by pritesh on 17/12/17.
 */
class FruitScreenFragment: Fragment() {
    lateinit var adapter:VerticalRecyclerViewAdapter;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = VerticalRecyclerViewAdapter(DataStorageClass.fruits,context!!,"fruits")

        product_view_vertical_recycle.adapter=adapter;
        product_view_vertical_recycle.layoutManager = LinearLayoutManager(context);

        product_checkout.setOnClickListener {
            goToFullView(CheckOutFragment())
        }

        searchText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().equals("")){
                    adapter.setData(DataStorageClass.fruits)
                    adapter.notifyDataSetChanged()
                }else{
                    var items =DataStorageClass.fruits;
                    var products= JSONArray();
                    for(i in 0..(items.length()-1)){
                        var item = items.get(i) as JSONObject;
                        if(item.get("name").toString().contains(p0.toString())){
                            products.put(item)
                        }
                    }
                    adapter.setData(products)
                    adapter.notifyDataSetChanged()
                }
            }

        })


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