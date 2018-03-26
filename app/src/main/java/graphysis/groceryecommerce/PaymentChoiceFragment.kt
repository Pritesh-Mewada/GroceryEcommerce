package graphysis.groceryecommerce

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
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
    lateinit var dataStorageClass:DataStorageClass;
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
        dataStorageClass = DataStorageClass(context,"Order",3);

        if(bundle !=null){
            amountPayable.text="Rs. " +bundle.get("price");
        }

        payViaCOD.setOnClickListener {
            postOrders()
        }

        payOnline.setOnClickListener {
            val intent = context?.packageManager?.getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user");

            val list = context?.packageManager?.getInstalledApplications(PackageManager.GET_META_DATA) as List<ApplicationInfo>;

            for(x:ApplicationInfo in list){
                Log.d("hello",x.packageName);
            }
            if(intent!=null){

                //intent.data = Uri.parse("upi://pay?pa=rohitkawale@ybl&pn=Rohit Kawale&tid=422d97c1-f0fc-4bea-b24a-511ffa85e86f&am=200.87&tn=Test%transaction")

                val UPI = "upi://pay?pa=kghormade@ybl" + "&pn=rohit" + "&" + "&am=1"
                intent.action = Intent.ACTION_VIEW
                intent.data = Uri.parse(UPI)
                val chooser = Intent.createChooser(intent, "Pay with...")
                //starting activity for result
                startActivityForResult(chooser, 1, null)


            }else{
                context?.ShowToast("App not installed")
            }

        }

    }

    fun postOrders(){
        ProgressBar.visibility = View.VISIBLE;
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", response)
                    if(response !=null){
                        var json: JSONObject = JSONObject(response)
                        if( json.get("status").equals("Success")){
                            context?.ShowToast("Order Placed Successfully");
                            ProgressBar.visibility = View.INVISIBLE;
                            dataStorageClass.deleteAllOrder();
                            startActivity(Intent(context,MainActivity::class.java));
                            (context as FragmentActivity).finish();
                        }
                    }else{
                        ProgressBar.visibility = View.INVISIBLE;
                    }


                },
                Response.ErrorListener {
                    // error
                    Log.d("Google", "Error in Placing order")
                    ProgressBar.visibility = View.INVISIBLE;
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                var bundle:Bundle = arguments as Bundle;
                Log.d("hello",dataStorageClass.getDataForCheckout().toString());
                params.put("data",dataStorageClass.getDataForCheckout().toString())
                params.put("customer_id", getID())
                params.put("name",bundle.get("name").toString())
                params.put("location",bundle.get("address").toString()+"Near to:"+bundle.get("landmark").toString())
                params.put("contact",bundle.get("contact").toString())
                params.put("alt_contact",bundle.get("alternate").toString())
                params.put("paymentMode","COD")


                return params
            }
        }

        requestQueue.add(postRequest);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getID():String{
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPref.getString("Id", "default_value");
    }
}