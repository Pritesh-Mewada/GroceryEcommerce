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
import kotlinx.android.synthetic.main.orders_fragment_layout.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 25/12/17.
 */
class OrdersFragment: Fragment() {
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
        requestQueue = Volley.newRequestQueue(context)

        initTextView();
        //initRecycle();
        Save.setOnClickListener {
            var fields = AddressLine.text.isEmpty() || LandMark.text.isEmpty() || contactNumber.text.isEmpty() || alternateNumber.text.isEmpty();

            if(fields){
                context?.ShowToast("Fill all the fields")
            }else{
                StoreID()
                context?.ShowToast("Details saved successfully")

            }

        }
    }


    fun initTextView() {

        var sharedPref: SharedPreferences = activity?.getSharedPreferences("User",Context.MODE_PRIVATE)!!;
        user_initial.text = sharedPref.getString("Username", "Default").get(0).toString().toUpperCase();
        user_name.text=sharedPref.getString("Username", "Default").toString().capitalize();
        user_email.text=sharedPref.getString("Email", "Default").toString().capitalize()
        if (!sharedPref.getString("landmark", "default_value").equals("default_value")){
            AddressLine.setText(sharedPref.getString("address", "default_value").toString())
            LandMark.setText(sharedPref.getString("landmark", "default_value").toString())
            contactNumber.setText(sharedPref.getString("contact", "default_value").toString())
            alternateNumber.setText(sharedPref.getString("alternate", "default_value").toString())
        }

        if(!sharedPref.getString("contact", "default_value").equals("default_value")){
            contactNumber.setText(sharedPref.getString("contact", "default_value").toString())
        }

    }


    fun StoreID(){
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        var  editor: SharedPreferences.Editor = sharedPref.edit();
        editor.putString("address", AddressLine.text.toString());
        editor.putString("landmark",LandMark.text.toString());
        editor.putString("contact",contactNumber.text.toString());
        editor.putString("alternate",alternateNumber.text.toString());
        editor.commit();
    }


//    fun initRecycle(){
//        val postRequest = object : StringRequest(Request.Method.POST, url,
//                Response.Listener { response ->
//                    // response
//                    Log.d("Google", "Orders")
//
//                    Log.d("Google", response)
//                    if(response !=null){
//                        var json: JSONObject = JSONObject(response)
//                        if(json.get("status").equals("Success")){
//                            orderRecyclerAdapter = OrderRecyclerViewAdapter(json.get("data") as JSONArray,context!!);
//                            orders_recyclerview.adapter = orderRecyclerAdapter;
//                            orders_recyclerview.layoutManager = LinearLayoutManager(context);
//
//                        }
//                    }
//
//
//                },
//                Response.ErrorListener {
//                    // error
//                    Log.d("Google", "Error in custom login")
//                }
//        ) {
//            override fun getParams(): Map<String, String> {
//                val params = HashMap<String, String>()
//                Log.d("Google",getID());
//                params.put("id",getID())
//                return params
//            }
//        }
//
//        requestQueue.add(postRequest)
//    }

    fun getID():String{
        var sharedPref:SharedPreferences = context?.getSharedPreferences("User",Context.MODE_PRIVATE)!!;
        return sharedPref.getString("Id", "default_value").toString();
    }
}