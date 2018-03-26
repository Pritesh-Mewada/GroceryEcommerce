package graphysis.groceryecommerce


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.pending_completed_fragment.*
import org.json.JSONException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var request: RequestQueue;
    var url:String;

    init {
        url="http://shopgondia.in/gondia_php/products.php"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        request = Volley.newRequestQueue(applicationContext);

        Fab.setOnClickListener(View.OnClickListener { view ->
            var intent = Intent()
            intent.action = Intent.ACTION_DIAL;
            intent.data= Uri.parse("tel:9172977934");
            startActivity(intent)
        })


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        nav_view.setNavigationItemSelectedListener(this)

        getDataInflate()
        setNavView(nav_view)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.sign_out ->{
                clearData();
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragmentManager =supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        var fragment: Fragment =MainScreenFragment();
        when (item.itemId) {
            R.id.home -> {
                // Handle the camera action
                fragment = MainScreenFragment();
            }
            R.id.fruit -> {
                fragment = FruitScreenFragment();
            }
            R.id.vegetable -> {
                fragment = VegetableScreenFragment();
            }
            R.id.user_profile -> {
                fragment = ProfileScreenFragment();
            }
            R.id.user_orders -> {
                fragment = CheckOutFragment();
            }
            R.id.easy_orders -> {
                fragment = EasyOrderFragment();
            }
            R.id.recent_orders->{
                fragment =PendingCompletedFragment()
            }
            R.id.feedback->{
                sendEmail()
                return true
            }
        }
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)
        fragmentTransaction?.commit();

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun getMainScreen(){
        var fragmentManager =supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        var fragment: Fragment =MainScreenFragment();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment);
        fragmentTransaction?.commit();
    }


    fun getDataInflate(){

        if(DataStorageClass.fruits.length() != 0 && DataStorageClass.vegetable.length() != 0 ){
            getMainScreen()
            return
        }

        val jsonArrayRequest = JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->
                    try {
                        Log.d("Google",response.toString());
                        if(response!=null){
                            DataStorageClass.inflateData(response);
                            getMainScreen()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    // Do something when error occurred
                    Toast.makeText(applicationContext,"Error...", Toast.LENGTH_LONG).show();
                }
        )

        request.add(jsonArrayRequest);

    }

    fun clearData(){

        if(LoginManager.getInstance()!=null){
            LoginManager.getInstance().logOut();
            changeScreen()
            return
        }else if(GoogleSignIn.getLastSignedInAccount(this) !=null){
            var googleSignInClient :GoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);

            googleSignInClient.signOut().addOnCompleteListener(this, object:OnCompleteListener<Void>{
                override fun onComplete(p0: Task<Void>) {
                    changeScreen()
                }
            })

            return
        }

        changeScreen();

    }

    fun changeScreen(){
        var sharedPref: SharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        var  editor: SharedPreferences.Editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        var intent:Intent = Intent(applicationContext,SignInActivity::class.java)
        startActivity(intent)
        finish()
    }


    fun setNavView(nav:NavigationView){

        var header:View = nav.getHeaderView(0);
        var sharedPrefrence: SharedPreferences = getSharedPreferences("User",Context.MODE_PRIVATE)!!;
        header.findViewById<TextView>(R.id.name_initial_nav).text  = sharedPrefrence.getString("Username", "Default").get(0).toString().toUpperCase();
        header.findViewById<TextView>(R.id.user_name_nav).text=sharedPrefrence.getString("Username", "Default").toString().capitalize();
        header.findViewById<TextView>(R.id.user_email_nav).text=sharedPrefrence.getString("Email", "Default").toString().capitalize()

    }

    protected fun sendEmail() {
        val TO = arrayOf("tarun.manuja@gmail.com")
        val CC = arrayOf("")
        val emailIntent = Intent(Intent.ACTION_SEND)

        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
        emailIntent.putExtra(Intent.EXTRA_CC, CC)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for shopGondia app")

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            finish()
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this@MainActivity, "There is no email client installed.", Toast.LENGTH_SHORT).show()
        }

    }

}
