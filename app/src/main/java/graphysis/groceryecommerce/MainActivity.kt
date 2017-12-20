package graphysis.groceryecommerce


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
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
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


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        getDataInflate()

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
            R.id.action_settings -> return true
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
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment);
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

}
