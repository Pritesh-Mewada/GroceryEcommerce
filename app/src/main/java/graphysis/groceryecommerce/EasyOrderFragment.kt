package graphysis.groceryecommerce

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.easy_order_fragment.*
import java.io.ByteArrayOutputStream
import android.R.attr.data
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.Volley
import org.json.JSONObject




/**
 * Created by pritesh on 17/1/18.
 */
class EasyOrderFragment:Fragment() {
    lateinit var bitmap:Bitmap;
    var uploadurl:String;
    lateinit var requestQueue:RequestQueue
    init {
        uploadurl="http://shopgondia.in/gondia_php/easy_order.php?apicall=uploadpic";
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view :View = inflater.inflate(R.layout.easy_order_fragment,container,false);
        requestQueue=Volley.newRequestQueue(context)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyPic.setOnClickListener {
            var intent:Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,1234);
        }



        initEditText();

        orderProceed.setOnClickListener {
            var fields = AddressLine.text.isEmpty() || LandMark.text.isEmpty() || contactNumber.text.isEmpty() || alternateNumber.text.isEmpty();
            if(!fields){
                uploadAll(bitmap);
            }else{
                context?.ShowToast("Fill all the fields");
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==1234 && resultCode==RESULT_OK){
            var bitmap:Bitmap = data?.extras?.get("data") as Bitmap;
            this.bitmap = bitmap
            easyPic.setImageBitmap(bitmap);
        }
    }

    fun StoreID(){
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        var  editor: SharedPreferences.Editor = sharedPref.edit();
        editor.putString("address",AddressLine.text.toString());
        editor.putString("landmark",LandMark.text.toString());
        editor.putString("contact",contactNumber.text.toString());
        editor.putString("alternate",alternateNumber.text.toString());
        editor.commit();
    }

    fun initEditText(){
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        if (!sharedPref.getString("address", "default_value").equals("default_value")){
            AddressLine.setText(sharedPref.getString("address", "default_value").toString())
            LandMark.setText(sharedPref.getString("landmark", "default_value").toString())
            contactNumber.setText(sharedPref.getString("contact", "default_value").toString())
            alternateNumber.setText(sharedPref.getString("alternate", "default_value").toString())
        }

    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun uploadAll(bitmap: Bitmap){
        ProgressBar.visibility = View.VISIBLE;
        var volleyRequest:VolleyMultipartRequest = object:VolleyMultipartRequest(Request.Method.POST,uploadurl, object:Response.Listener<NetworkResponse>{
            override fun onResponse(response: NetworkResponse?) {
                ProgressBar.visibility = View.INVISIBLE;
                if(response !=null){
                    val obj = JSONObject(String(response.data));
                    Log.d("Google",obj.toString());
                    context?.ShowToast("Order Placed Successfully");
                    startActivity(Intent(context,MainActivity::class.java));
                    (context as FragmentActivity).finish();
                }
            }


        },object :Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                context?.ShowToast("Some error occurred please try again later")
                ProgressBar.visibility = View.INVISIBLE;
            }
        }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("tags","Hello beta")
                params.put("name",getUserName());
                params.put("customer_id", getID())
                params.put("location",AddressLine.text.toString()+"Near to:"+LandMark.text.toString())
                params.put("contact",contactNumber.text.toString())
                params.put("alt_contact",alternateNumber.text.toString());
                Log.d("hello",params.toString());
                return params
            }

            override fun getByteData():Map<String, DataPart> {
                var  params:HashMap<String, DataPart> = HashMap<String, DataPart>();
                var imagename: Long = System.currentTimeMillis();
                params.put("pic", DataPart(imagename.toString() + ".png",getFileDataFromDrawable(bitmap)))
                return params;

            }
        }
        requestQueue.add(volleyRequest);
    }

    fun getID():String{
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPref.getString("Id", "default_value");

    }

    fun getUserName():String{
        var sharedPref: SharedPreferences = (context as FragmentActivity).getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPref.getString("Username", "default_value");
    }

}