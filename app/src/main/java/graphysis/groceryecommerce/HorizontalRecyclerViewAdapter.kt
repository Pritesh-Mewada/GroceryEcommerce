package graphysis.groceryecommerce

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 17/12/17.
 */
class HorizontalRecyclerViewAdapter(val json:JSONArray,val context:Context) : RecyclerView.Adapter<HorizontalRecyclerViewAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var item:JSONObject = json.get(position) as JSONObject;

        //setting views

        holder?.productName?.text = item.get("name").toString().capitalize()
        holder?.productBreed?.text = item.get("type").toString().capitalize()
        holder?.productQuantity?.text = "1 "+item.get("quantity_unit").toString().capitalize()
        holder?.productPrice?.text ="Rs: "+item.get("cost").toString()
        Picasso.with(context).load(item.get("img").toString()).into(holder?.productImage);

        holder?.cardview?.setOnClickListener(View.OnClickListener {
            DataStorageClass.setItemForFullViewClass(json.get(position) as JSONObject);
            goToFullView(FullProductDescriptionFragment())
        })




    }

    override fun getItemCount(): Int {
        return json.length();
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(parent?.context).inflate(R.layout.product_view_mainscreen,parent,false)

        return ViewHolder(view);
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val productName:TextView;
        val productBreed:TextView;
        val productQuantity:TextView;
        val productPrice:TextView;
        var productImage:ImageView;
        var productAdd:Button;
        var cardview:CardView;

        init {
            productName = itemView?.findViewById(R.id.product_name)!!
            productBreed = itemView?.findViewById(R.id.product_breed)!!
            productQuantity = itemView?.findViewById(R.id.product_quantity)!!
            productPrice = itemView?.findViewById(R.id.product_price)!!
            productImage = itemView?.findViewById(R.id.product_image)!!
            productAdd = itemView?.findViewById(R.id.product_button)!!
            cardview = itemView?.findViewById(R.id.cardview)!!

        }

    }

    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }
}