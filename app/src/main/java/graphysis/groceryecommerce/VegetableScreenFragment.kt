package graphysis.groceryecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by pritesh on 17/12/17.
 */
class VegetableScreenFragment:Fragment() {
    var fruitRecycle: RecyclerView?=null;
    lateinit var adapter:VerticalRecyclerViewAdapter;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.vertical_product_layout,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fruitRecycle = view?.findViewById(R.id.product_view_vertical_recycle);

        adapter = VerticalRecyclerViewAdapter(DataStorageClass.vegetable,context!!);

        fruitRecycle?.adapter=adapter;

        fruitRecycle?.layoutManager = LinearLayoutManager(context);


    }

}