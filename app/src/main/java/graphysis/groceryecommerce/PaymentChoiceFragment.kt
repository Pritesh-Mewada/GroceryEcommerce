package graphysis.groceryecommerce

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.payment_choice_fragment.*
import org.json.JSONObject

/**
 * Created by pritesh on 27/12/17.
 */
class PaymentChoiceFragment:Fragment() {
    lateinit var requestQueue:RequestQueue;
    var url:String
    init {
        url="http://shopgondia.in/gondia_php/place_order.php"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.payment_choice_fragment,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestQueue = Volley.newRequestQueue(context)

        var bundle:Bundle = arguments as Bundle;

        if(bundle !=null){
            amountPayable.text="Rs. " +bundle.get("price");
        }

        payViaCOD.setOnClickListener {
            postOrders()
        }

    }

    fun postOrders(){
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", response)
                    if(response !=null){
                        var json: JSONObject = JSONObject(response)
                        if( json.get("status").equals("Success")){
                            startActivity(Intent(context,MainActivity::class.java));
                            (context as FragmentActivity).finish();
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
                params.put("data",DataStorageClass.getDataForCheckout().toString())
                params.put("customer_id", getID())
                return params
            }
        }

        requestQueue.add(postRequest);
    }

    fun getID():String{
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);

        return sharedPref.getString("Id", "default_value");
    }
}