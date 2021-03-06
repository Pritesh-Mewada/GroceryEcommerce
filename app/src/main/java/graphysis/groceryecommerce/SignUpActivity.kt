package graphysis.groceryecommerce


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject


class SignUpActivity : AppCompatActivity() {
    lateinit var requestQueue:RequestQueue;
    var url:String;
    init {
        url="http://shopgondia.in/gondia_php/custom_signup.php"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        requestQueue = Volley.newRequestQueue(applicationContext);
        signup.setOnClickListener(View.OnClickListener {

            var validity : Boolean = password.text.toString().isEmpty() && display_name.text.toString().isEmpty();

            var match :Boolean = password.text.toString().equals(rpassword.text.toString());
            if(verifyEmail(email_id.text.toString()) && !validity && match){
                signUp(url,display_name.text.toString(),email_id.text.toString(),password.text.toString());
            }


        })

    }
    fun verifyEmail(email:String):Boolean{
        if (getIndex(email,".")>2 && getIndex(email,"@")>5) return true else return false
    }
    fun getIndex(email:String,str:String):Int{
        return (email.length - email.lastIndexOf(str));
    }

    fun signUp(url:String,user:String,email:String,password:String){
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", response)
                    if(response !=null){
                        var json:JSONObject = JSONObject(response)
                        if( json.get("status").equals("Success")){
                            var intent =Intent(applicationContext,MainActivity::class.java);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            startActivity(intent);
                            StoreID(JSONObject(json.get("data").toString()).get("id").toString(),email,user);
                            finish()
                        }else if(json.get("status").equals("Error")){
                            ShowToast(json.get("reason").toString());
                        }
                    }

                },
                Response.ErrorListener {
                    ShowToast("Unfortunately Some Error Ocurred")
                    Log.d("Google", "Error in custom login")
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("name",user)
                params.put("password",password)
                params.put("email", email)

                return params
            }
        }

        requestQueue.add(postRequest);
    }

    fun StoreID(id:String,email: String,username: String){
        var sharedPref:SharedPreferences = getSharedPreferences("User",Context.MODE_PRIVATE);
        var  editor: SharedPreferences.Editor = sharedPref.edit();
        editor.putString("Id",id);
        editor.putString("Email",email);
        editor.putString("Username",username);
        editor.commit();
    }


}