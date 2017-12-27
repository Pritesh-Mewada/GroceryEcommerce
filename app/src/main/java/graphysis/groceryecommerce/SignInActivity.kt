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
    lateinit var googleSignInButton:SignInButton;
    lateinit var callbackManager :CallbackManager;
    lateinit var loginButton:LoginButton;
    lateinit var signUpText:TextView;
    lateinit var requestQueue:RequestQueue;

    lateinit var email:TextInputEditText;
    lateinit var password:TextInputEditText;
    lateinit var loginCustom:Button;
    var RC_SIGN_IN:Int;
    val TAG:String;
    val url:String;
    val social_url :String;

    init {
        RC_SIGN_IN=91;
        TAG ="Google"
        url="http://shopgondia.in/gondia_php/custom_login.php"
        social_url = "http://shopgondia.in/gondia_php/social_login.php"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if(getID()==true){
            startActivity(Intent(applicationContext,MainActivity::class.java));
            finish()
        }

        requestQueue = Volley.newRequestQueue(this)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton = findViewById(R.id.google_sign_in) ;

        googleSignInButton.setOnClickListener(View.OnClickListener {
            var  signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        })

        callbackManager = CallbackManager.Factory.create();

        signUpText = findViewById(R.id.signup_text);

        signUpText.setOnClickListener {
            var intent:Intent = Intent(applicationContext,SignUpActivity::class.java);
            startActivity(intent)
        }


        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        loginButton.setReadPermissions("email")
        // If using in a fragment



        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // App code
                        var accessToken: AccessToken =loginResult.accessToken;
                        var profile: Profile = Profile.getCurrentProfile();
                        Log.d(TAG, profile.name);

                        var request :GraphRequest = GraphRequest.newMeRequest(accessToken,object :GraphRequest.GraphJSONObjectCallback{
                            override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                                Log.d(TAG, `object`?.get("email") as String?);
                                socialLogin(social_url,username = profile.name.toString(),email = (`object`?.get("email") as String?).toString())
                            }

                        })
                        var parameters:Bundle = Bundle();
                        parameters.putString("fields","email");
                        request.parameters = parameters;
                        request.executeAsync();
                    }

                    override fun onCancel() {
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                    }
                });


        email = findViewById(R.id.email_id) ;
        password = findViewById(R.id.password)
        loginCustom=findViewById(R.id.custom_login);

        loginCustom.setOnClickListener {
            var validity:Boolean = email.text.toString().isEmpty() && password.text.toString().isEmpty()

            if(!validity && verifyEmail(email.text.toString())){
                customLogin(url,email.text.toString(),password.text.toString());
            }

        }

    }

    fun verifyEmail(email:String):Boolean{
        if (getIndex(email,".")>2 && getIndex(email,"@")>5) return true else return false
    }
    fun getIndex(email:String,str:String):Int{
        return (email.length - email.lastIndexOf(str));
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Log.d(TAG,account.email);
            Log.d(TAG,account.displayName);
            socialLogin(social_url,username = account.displayName.toString(),email = account.email.toString())

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.


        }

    }

    fun customLogin(url:String,email:String,password:String){
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", response)
                    if(response !=null){
                        var json:JSONObject = JSONObject(response)
                        if( json.get("status").equals("Success")){
                            startActivity(Intent(applicationContext,MainActivity::class.java));
                            var dataObject = JSONObject(json.get("data").toString());
                            StoreID(dataObject.get("id").toString(),email,dataObject.get("name").toString());
                            finish()
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
                params.put("password",password)
                params.put("email", email)
                return params
            }
        }

        requestQueue.add(postRequest);
    }

    fun socialLogin(url:String,username:String,email:String){
        val postRequest = object : StringRequest(Request.Method.POST, url,
                Response.Listener { response ->
                    // response
                    Log.d("Google", response)
                    if(response !=null){
                        var json:JSONObject = JSONObject(response)
                        if(json.get("status").equals("Success")){
                            startActivity(Intent(applicationContext,MainActivity::class.java));
                            StoreID(JSONObject(json.get("data").toString()).get("id").toString(),email,username);
                            finish()
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
                params.put("username",username)
                params.put("email", email)
                return params
            }
        }

        requestQueue.add(postRequest);
    }


    fun StoreID(id:String,email: String,username: String){
        var sharedPref:SharedPreferences = getSharedPreferences("User",Context.MODE_PRIVATE);
        var  editor:SharedPreferences.Editor = sharedPref.edit();
        editor.putString("Id",id);
        editor.putString("Email",email);
        editor.putString("Username",username);
        Log.d("Google","Id insert:  "+id);
        editor.commit();
    }


    fun getID():Boolean{
        var sharedPref:SharedPreferences = getSharedPreferences("User",Context.MODE_PRIVATE);

        if (sharedPref.getString("Id", "default_value").equals("default_value")) return false else return true;
    }

}