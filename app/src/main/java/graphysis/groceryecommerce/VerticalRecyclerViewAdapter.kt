package graphysis.groceryecommerce

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 17/12/17.
 */
class VerticalRecyclerViewAdapter(val data:JSONArray, val context: Context,val type:String) : RecyclerView.Adapter<VerticalRecyclerViewAdapter.ViewHolder>() {
    var dataStorageClass:DataStorageClass ;
    var json:JSONArray;

    init {
        dataStorageClass = DataStorageClass(context,"Order",3);
        json=data;
    }

    fun setData(data:JSONArray){
        this.json=data
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var item: JSONObject = json.get(position) as JSONObject;
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

        holder?.productAdd?.setOnClickListener(View.OnClickListener {
            dataStorageClass.AddOrderID(keyword = item.get("keyword").toString(),cost=item.get("cost").toString(),quantity = "1");

            holder?.productAdd.visibility=View.GONE;
            holder?.showIncrement.visibility=View.VISIBLE;
        })

        holder?.productIncrement?.setOnClickListener(View.OnClickListener {
            var number = holder.productNumbers.text.toString().toInt();
            var max = item.get("quantity").toString().toInt();

            if(number==max){
                Toast.makeText(context,"Maximum Available Quantity Reached", Toast.LENGTH_LONG).show();
            }else{
                number++
                dataStorageClass.UpdateOrderID(keyword = item.get("keyword").toString(),cost = item.get("cost").toString(),quantity = number.toString());
                holder.productNumbers.text = number.toString()
                holder?.productPrice?.text ="Rs: "+(number*item.get("cost").toString().toInt()).toString()
            }
        })

        holder?.productDecrement?.setOnClickListener(View.OnClickListener {
            var number = holder.productNumbers.text.toString().toInt();
            var max = 1;
            if(number==max){
                holder?.productAdd.visibility=View.VISIBLE;
                holder?.showIncrement.visibility=View.GONE;
                dataStorageClass.RemoveOrder(item.get("keyword").toString())
            }else{
                number--;
                dataStorageClass.UpdateOrderID(keyword = item.get("keyword").toString(),cost = item.get("cost").toString(),quantity = number.toString());
                holder.productNumbers.text = number.toString()
                holder?.productPrice?.text ="Rs: "+(number*item.get("cost").toString().toInt()).toString()
            }
        })

    }

    override fun getItemCount(): Int {
        return json.length();
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(parent?.context).inflate(R.layout.product_view_horizontal,parent,false)
        if(type.equals("fruits")){
            view = LayoutInflater.from(parent?.context).inflate(R.layout.product_view_horizontal_yellow,parent,false)
        }
        return ViewHolder(view);
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val productName:TextView;
        val productBreed:TextView;
        val productQuantity:TextView;
        val productPrice:TextView;
        var productImage:ImageView;
        var productAdd:Button;
        var cardview :CardView;
        var productNumbers:TextView;
        var productIncrement: ImageButton;
        var productDecrement: ImageButton;
        var showIncrement:LinearLayout;
        init {
            productName = itemView?.findViewById(R.id.product_name)!!;
            productBreed = itemView?.findViewById(R.id.product_breed)!!
            productQuantity = itemView?.findViewById(R.id.product_quantity)!!
            productPrice = itemView?.findViewById(R.id.product_price)!!
            productImage = itemView?.findViewById(R.id.product_image)!!
            productAdd = itemView?.findViewById(R.id.product_button)!!
            productNumbers = itemView?.findViewById(R.id.quantity)!!
            cardview = itemView?.findViewById(R.id.cardview)!!
            productIncrement = itemView?.findViewById(R.id.increment_quantity)!!
            productDecrement = itemView?.findViewById(R.id.decrement_quantity)!!
            showIncrement = itemView?.findViewById(R.id.ShowIncrement)!!

        }
    }

    fun goToFullView(fragment: Fragment){
        var fragmentManager = (context as FragmentActivity).supportFragmentManager;
        var fragmentTransaction: FragmentTransaction? = fragmentManager.beginTransaction();
        fragmentTransaction?.replace(R.id.show_all_fragments,fragment)?.addToBackStack(null);
        fragmentTransaction?.commit();
    }
}