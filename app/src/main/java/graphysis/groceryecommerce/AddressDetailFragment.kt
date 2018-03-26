package graphysis.groceryecommerce

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlinx.android.synthetic.main.address_detail_fragment.*;

/**
 * Created by pritesh on 16/1/18.
 */
class AddressDetailFragment :Fragment() {
    val url:String ;
    val otpurl:String;
    var otpNumber = "";
    lateinit var requestQueue:RequestQueue;
    lateinit var progressDialog:ProgressDialog;

    init {
        url = "http://www.shopgondia.in/gondia_php/sendotp.php"
        otpurl ="http://www.shopgondia.in/gondia_php/verifyotp.php"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view :View = inflater.inflate(R.layout.address_detail_fragment,container,false)
        return  view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestQueue = Volley.newRequestQueue(context);
        initEditText();
        orderProceed.setOnClickListener {
            var fields = orderName.text.isEmpty() || AddressLine.text.isEmpty() || LandMark.text.isEmpty() || contactNumber.text.isEmpty() || alternateNumber.text.isEmpty();
            var bundle:Bundle = arguments as Bundle;
            var price:String?=null
            if(bundle !=null){
                price=bundle.get("price").toString();
            }

            if(!fields){
                var mPaymentChoiceFragment:PaymentChoiceFragment = PaymentChoiceFragment();
                bundle = Bundle();
                if(price !=null){
                    bundle.putString("price",price.toString());
                }
                bundle.putString("name",orderName.text.toString());
                bundle.putString("address",AddressLine.text.toString());
                bundle.putString("landmark",LandMark.text.toString());
                bundle.putString("contact",contactNumber.text.toString());
                bundle.putString("alternate",alternateNumber.text.toString());
                StoreID();
                mPaymentChoiceFragment.arguments = bundle;
                if(otpNumber.equals(contactNumber.text.toString())){
                    goToFullView(mPaymentChoiceFragment)
                }else{
                    sendOtp(contactNumber.text.toString());
                }
            }else{
                context?.ShowToast("Fill all the fields");
            }

        }

        VerifyOtp.setOnClickListener {
            if(OtpNumber.text.toString().length<4){
                context?.ShowToast("Please Enter a Valid Otp")
                return@setOnClickListener;
            }

            ProgressBar.visibility = View.VISIBLE;
            val postRequest = object : StringRequest(Request.Method.POST, otpurl,
                    Response.Listener { response ->
                        // response
                        Log.d("Google", response)
                        if(response !=null){
                            var json: JSONObject = JSONObject(response)
                            if( json.get("status").equals("Success")){
                                context?.ShowToast("Otp Verified Please Proceed");
                                orderProceed.text = "Proceed for payment"
                                otpNumber=contactNumber.text.toString()
                            }
                        }

                        ProgressBar.visibility = View.INVISIBLE;
                    },
                    Response.ErrorListener {
                        // error
                        Log.d("Google", "Error in custom login")
                        ProgressBar.visibility = View.INVISIBLE;
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params.put("otp",OtpNumber.text.toString())
                    params.put("contact",contactNumber.text.toString())
                    return params
                }
            }

            requestQueue.add(postRequest)

        }

    }

    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }

    fun StoreID(){
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        var  editor: SharedPreferences.Editor = sharedPref.edit();
        editor.putString("name",orderName.text.toString());
        editor.putString("address",AddressLine.text.toString());
        editor.putString("landmark",LandMark.text.toString());
        editor.putString("contact",contactNumber.text.toString());
        editor.putString("alternate",alternateNumber.text.toString());
        editor.commit();
    }

    fun initEditText(){
        var sharedPref:SharedPreferences = (context as FragmentActivity).getSharedPreferences("User",Context.MODE_PRIVATE);
        if (!sharedPref.getString("address", "default_value").equals("default_value")){
            orderName.setText(sharedPref.getString("Username", "default_value").toString());
            AddressLine.setText(sharedPref.getString("address", "default_value").toString())
            LandMark.setText(sharedPref.getString("landmark", "default_value").toString())
            contactNumber.setText(sharedPref.getString("contact", "default_value").toString())
            alternateNumber.setText(sharedPref.getString("alternate", "default_value").toString())
        }

    }

    fun sendOtp(number:String){
        ProgressBar.visibility = View.VISIBLE;
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", response)
                    if(response !=null){
                        var json: JSONObject = JSONObject(response)
                        if( json.get("status").equals("Success")){
                            context?.ShowToast("Otp Sent");
                            OtpLayout.visibility = View.VISIBLE;

                        }
                    }

                    ProgressBar.visibility = View.INVISIBLE;

                },
                Response.ErrorListener {
                    // error
                    Log.d("Google", "Error in custom login")
                    ProgressBar.visibility = View.INVISIBLE;
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params.put("customer_id", getID())
                params.put("contact",number)

                return params
            }
        }

        requestQueue.add(postRequest)

    }
    fun getID():String{
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPref.getString("Id", "default_value");
    }
}