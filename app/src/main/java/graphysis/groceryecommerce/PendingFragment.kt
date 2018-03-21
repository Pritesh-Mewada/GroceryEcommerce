package graphysis.groceryecommerce

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.pending_products_fragment.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 18/1/18.
 */
class PendingFragment:Fragment() {
    lateinit var requestQueue: RequestQueue;
    lateinit var orderRecyclerAdapter:OrderRecyclerViewAdapter;
    var url:String;
    init {
        url="http://shopgondia.in/gondia_php/orders_pending.php"

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pending_products_fragment,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestQueue = Volley.newRequestQueue(context)
        initRecycle();


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
                            pendingProducts.adapter = orderRecyclerAdapter;
                            pendingProducts.layoutManager = LinearLayoutManager(context);

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
        requestQueue.add(postRequest);

    }


    fun getID():String{
        var sharedPref: SharedPreferences = context?.getSharedPreferences("User", Context.MODE_PRIVATE)!!;
        return sharedPref.getString("Id", "default_value").toString();
    }

}