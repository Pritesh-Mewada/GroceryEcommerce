package graphysis.groceryecommerce

import android.os.Bundle
import android.support.v4.app.*
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.main_screen_fragment.*
import org.json.JSONException



/**
 * Created by pritesh on 17/12/17.
 */
class MainScreenFragment: Fragment() {
    var vegetableRecycle:RecyclerView?=null;
    var fruitRecycle:RecyclerView?=null;
    lateinit var adapterFruit:HorizontalRecyclerViewAdapter;
    lateinit var adapterVegetable:HorizontalRecyclerViewAdapter;
    lateinit var fruitViewAll: Button;
    lateinit var vegetableViewAll:Button;
    lateinit var request:RequestQueue;
    var url:String;

    init {
        url="http://shopgondia.in/gondia_php/products.php"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.main_screen_fragment,container,false);

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        request = Volley.newRequestQueue(context);


        vegetableRecycle = view?.findViewById(R.id.vegetable_recyclerview);
        fruitRecycle= view?.findViewById(R.id.fruits_recyclerview);

        fruitViewAll = view?.findViewById(R.id.fruits_view_all);
        vegetableViewAll = view?.findViewById(R.id.vegetable_view_all);

        fruitViewAll.setOnClickListener(View.OnClickListener {
            goToFullView(FruitScreenFragment())
        });

        slideShow.isAutoStart =true
        slideShow.setFlipInterval(5000)
        slideShow.setInAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))
        slideShow.setOutAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_out))
        slideShow.startFlipping()

        vegetableViewAll.setOnClickListener(View.OnClickListener {
            goToFullView(VegetableScreenFragment())
        });

        vegetableRecycle?.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        fruitRecycle?.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        initAdapter()


    }



    fun initAdapter(){
        adapterFruit = HorizontalRecyclerViewAdapter(DataStorageClass.fruits, context!!,"fruits");
        adapterVegetable = HorizontalRecyclerViewAdapter(DataStorageClass.vegetable, context!!,"vegetables");
        vegetableRecycle?.adapter=adapterVegetable;
        fruitRecycle?.adapter=adapterFruit;

    }

    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }




}