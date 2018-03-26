package graphysis.groceryecommerce


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import android.R.attr.data
import android.content.Context
import android.content.SharedPreferences
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject


class SignInActivity : AppCompatActivity() {

    lateinit var gso:GoogleSignInOptions;
    lateinit var mGoogleSignInClient:GoogleSignInClient;
    lateinit var callbackManager :CallbackManager;
    lateinit var loginButton:LoginButton;
    lateinit var requestQueue:RequestQueue;

    var RC_SIGN_IN:Int;
    val TAG:String;
    val url:String;
    val otpurl:String;
    val social_url :String;

    init {
        RC_SIGN_IN=91;
        TAG ="Google"
        //url="http://shopgondia.in/gondia_php/custom_login.php"
        social_url = "http://shopgondia.in/gondia_php/social_login.php"
        url = "http://www.shopgondia.in/gondia_php/newlogin.php"
        otpurl ="http://www.shopgondia.in/gondia_php/verify_user.php"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if(getID()==true){
            startActivity(Intent(applicationContext,MainActivity::class.java));
            finish()
        }

        requestQueue = Volley.newRequestQueue(this)

        VerifyOtp.setOnClickListener {
            if(OtpNumber.text.toString().length<4){
                baseContext.ShowToast("Please Enter a Valid Otp")
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
                                baseContext.ShowToast("Otp Verified Please Proceed");
                                //var intent =Intent(applicationContext,MainActivity::class.java);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                //startActivity(intent);
                                StoreID(id=JSONObject(json.get("data").toString()).get("id").toString(),email=email.text.toString(),number = number.text.toString(),name=name.text.toString());
                                //finish()
                            }
                        }
                        ProgressBar.visibility = View.INVISIBLE;
                    },
                    Response.ErrorListener {
                        // error
                        Log.d("Google", "Error in custom login")
                        baseContext.ShowToast("Some error occurres please try again later")
                        ProgressBar.visibility = View.INVISIBLE;
                    }
            ) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params.put("otp",OtpNumber.text.toString())
                    params.put("contact",number.text.toString())
                    params.put("name",name.text.toString())
                    params.put("email",email.text.toString())

                    return params
                }
            }
            requestQueue.add(postRequest)

        }


//        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//

//        google_sign_in.setOnClickListener(View.OnClickListener {
//            var  signInIntent = mGoogleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, RC_SIGN_IN);
//        })

        //callbackManager = CallbackManager.Factory.create();

//        signup_text.setOnClickListener {
//            var intent:Intent = Intent(applicationContext,SignUpActivity::class.java);
//            startActivity(intent)
//            finish()
//        }


        //loginButton = findViewById<View>(R.id.login_button) as LoginButton
        //login_button.setReadPermissions("email")
//        LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult> {
//                    override fun onSuccess(loginResult: LoginResult) {
//                        // App code
//                        var accessToken: AccessToken =loginResult.accessToken;
//                        var profile: Profile = Profile.getCurrentProfile();
//                        Log.d(TAG, profile.name);
//
//                        var request :GraphRequest = GraphRequest.newMeRequest(accessToken,object :GraphRequest.GraphJSONObjectCallback{
//                            override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
//                                Log.d(TAG, `object`?.get("email") as String?);
//                                socialLogin(social_url,username = profile.name.toString(),email = (`object`?.get("email") as String?).toString())
//                            }
//
//                        })
//                        var parameters:Bundle = Bundle();
//                        parameters.putString("fields","email");
//                        request.parameters = parameters;
//                        request.executeAsync();
//                    }
//
//                    override fun onCancel() {
//                        // App code
//                    }
//
//                    override fun onError(exception: FacebookException) {
//                        // App code
//                        ShowToast("Unfortunately Some Error Ocurred")
//
//                    }
//                });

        custom_login.setOnClickListener {
//            var validity:Boolean = email_id.text.toString().isEmpty() && password.text.toString().isEmpty()
//
//            if(!validity && verifyEmail(email_id.text.toString())){
//                customLogin(url,email_id.text.toString(),password.text.toString());
//            }


            var fields = name.text.isEmpty() && number.text.isEmpty() && email.text.isEmpty()

            if(fields==true){
                baseContext.ShowToast("Fill all the fields")
            }else{
                if(verifyEmail(email = email.text.toString())==false){
                    baseContext.ShowToast("Please enter valid email")

                }else{
                    if(number.length()!=10){
                        baseContext.ShowToast("Please enter 10 digit mobile number")
                    }else{
                        sendOtp(number =number.text.toString())
                    }
                }

            }

        }

    }

    fun verifyEmail(email:String):Boolean{
        if (getIndex(email,".")>2 && getIndex(email,"@")>5) return true else return false
    }
    fun getIndex(email:String,str:String):Int{
        if(email.lastIndexOf(str)<0){
            return 0
        }
        return (email.length - email.lastIndexOf(str));
    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode === RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//
//    }



    fun sendOtp(number:String){
        ProgressBar.visibility = View.VISIBLE;
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", response)
                    if(response !=null){
                        var json: JSONObject = JSONObject(response)
                        if( json.get("status").equals("Success")){
                            baseContext.ShowToast("Otp Sent");
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
                params.put("contact",number)
                return params
            }
        }

        requestQueue.add(postRequest)

    }

//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//
//            // Signed in successfully, show authenticated UI.
//            Log.d(TAG,account.email);
//            Log.d(TAG,account.displayName);
//            socialLogin(social_url,username = account.displayName.toString(),email = account.email.toString())
//
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//
//        }
//
//    }

//    fun customLogin(url:String,email:String,password:String){
//        val postRequest = object : StringRequest(Request.Method.POST, url,
//                Response.Listener { response ->
//                    // response
//                    Log.d("Google", response)
//                    if(response !=null){
//                        var json:JSONObject = JSONObject(response)
//                        if( json.get("status").equals("Success")){
//                            var dataObject = JSONObject(json.get("data").toString());
//                            startActivity(Intent(applicationContext,MainActivity::class.java));
//                            StoreID(dataObject.get("id").toString(),email,dataObject.get("name").toString());
//                            finish()
//                        }else if(json.get("status").equals("Error")){
//                            ShowToast(json.get("reason").toString());
//                        }
//                    }
//
//
//                },
//                Response.ErrorListener {
//                    // error
//                    ShowToast("Unfortunately Some Error Ocurred")
//                    Log.d("Google", "Error in custom login")
//                }
//        ) {
//            override fun getParams(): Map<String, String> {
//                val params = HashMap<String, String>()
//                params.put("password",password)
//                params.put("email", email)
//                return params
//            }
//        }
//
//        requestQueue.add(postRequest);
//    }

//    fun socialLogin(url:String,username:String,email:String){
//        val postRequest = object : StringRequest(Request.Method.POST, url,
//                Response.Listener { response ->
//                    // response
//                    Log.d("Google", response)
//                    if(response !=null){
//                        var json:JSONObject = JSONObject(response)
//                        if(json.get("status").equals("Success")){
//                            startActivity(Intent(applicationContext,MainActivity::class.java));
//                            StoreID(JSONObject(json.get("data").toString()).get("id").toString(),email,username);
//                            finish()
//                        }else if(json.get("status").equals("Error")){
//                            ShowToast(json.get("reason").toString())
//                        }
//                    }
//
//
//                },
//                Response.ErrorListener {
//                    // error
//                    ShowToast("Unfortunately Some Error Ocurred")
//                    Log.d("Google", "Error in social login")
//                }
//        ) {
//            override fun getParams(): Map<String, String> {
//                val params = HashMap<String, String>()
//                params.put("username",username)
//                params.put("email", email)
//                return params
//            }
//
//
//        }
//
//        requestQueue.add(postRequest);
//    }


    fun StoreID(id:String,email: String,name: String,number:String){
        var sharedPref:SharedPreferences = getSharedPreferences("User",Context.MODE_PRIVATE);
        var  editor:SharedPreferences.Editor = sharedPref.edit();
        editor.putString("Id",id);
        editor.putString("Email",email);
        editor.putString("Username",name);
        editor.putString("number",number);
        Log.d("Google","Id insert:  "+id);
        editor.commit();
    }


    fun getID():Boolean{
        var sharedPref:SharedPreferences = getSharedPreferences("User",Context.MODE_PRIVATE);
        if (sharedPref.getString("Id", "default_value").equals("default_value")) return false else return true;
    }

}