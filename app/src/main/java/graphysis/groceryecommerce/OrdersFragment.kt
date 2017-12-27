package graphysis.groceryecommerce

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 25/12/17.
 */
class OrdersFragment: Fragment() {
    lateinit var orderRecycle: RecyclerView;
    lateinit var userInitial: TextView;
    lateinit var userName: TextView;
    lateinit var userEmail: TextView;
    lateinit var orderRecyclerAdapter: OrderRecyclerViewAdapter;
    lateinit var requestQueue: RequestQueue;
    var url:String;
    init {
        url="http://shopgondia.in/gondia_php/orders.php"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.orders_fragment_layout,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInitial = view.findViewById(R.id.user_initial);
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        orderRecycle = view.findViewById(R.id.orders_recyclerview);
        requestQueue = Volley.newRequestQueue(context)

        initTextView();
        initRecycle();
    }

    fun initTextView() {
        var sharedPrefrence: SharedPreferences = activity?.getSharedPreferences("User",Context.MODE_PRIVATE)!!;
        userInitial.text = sharedPrefrence.getString("Username", "Default").get(0).toString().toUpperCase();
        userName.text=sharedPrefrence.getString("Username", "Default").toString().capitalize();
        userEmail.text=sharedPrefrence.getString("Email", "Default").toString().capitalize()
    }


    fun initRecycle(){
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", "Orders")

                    Log.d("Google", response)
                    if(response !=null){
                        var json: JSONObject = JSONObject(response)
                        if(json.get("status").equals("Success")){
                            orderRecyclerAdapter = OrderRecyclerViewAdapter(json.get("data") as JSONArray,context!!);
                            orderRecycle.adapter = orderRecyclerAdapter;
                            orderRecycle.layoutManager = LinearLayoutManager(context);

                        }
                    }


                },
                Response.ErrorListener {
                    // error
                    Log.d("Google", "Error in custom login")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                Log.d("Google",getID());
                params.put("id",getID())
                return params
            }
        }

        requestQueue.add(postRequest)
    }

    fun getID():String{
        var sharedPref:SharedPreferences = context?.getSharedPreferences("User",Context.MODE_PRIVATE)!!;

        return sharedPref.getString("Id", "default_value").toString();
    }
}