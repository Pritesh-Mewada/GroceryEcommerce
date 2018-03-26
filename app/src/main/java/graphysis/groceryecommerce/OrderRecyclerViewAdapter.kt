package graphysis.groceryecommerce

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat

/**
 * Created by pritesh on 25/12/17.
 */
class OrderRecyclerViewAdapter(val orders:JSONArray,val context:Context): RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var item:JSONObject = orders.getJSONObject(position);

        var format:SimpleDateFormat = SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        var date = format.parse(item.get("date").toString())
        format = SimpleDateFormat("E, dd MMM yyyy ");

        if(item.get("easy_order").toString().equals("true")){
            holder?.productName?.text = "Easy Order"
            holder?.productBreed?.text = ""
            holder?.productQuantity?.text =""
            holder?.productPrice?.text ="";

            Picasso.with(context).load(item.get("image").toString()).into(holder?.productImage);
            holder?.productStatus?.text = item.get("status").toString().capitalize()
            holder?.productDate?.text = format.format(date);

            holder?.productImage?.setOnClickListener({
                var intent = Intent(context,FullScreenImage::class.java);
                intent.putExtra("url",item.get("image").toString());
                (context as FragmentActivity).startActivity(intent)
//            context.goToFullView(FullScreenImageFragment());

            })
        }else{
            holder?.productName?.text = item.get("name").toString().capitalize()
            holder?.productBreed?.text = item.get("type").toString().capitalize()
            holder?.productQuantity?.text =item.get("quantity").toString()+  " " +item.get("quantity_unit").toString().capitalize()
            holder?.productPrice?.text ="Rs: "+item.get("quantity").toString().toInt()* item.get("unit_price").toString().toInt();
            Picasso.with(context).load(item.get("img").toString()).into(holder?.productImage);
            holder?.productStatus?.text = item.get("status").toString().capitalize()
            holder?.productDate?.text = format.format(date);

            holder?.productImage?.setOnClickListener({
                var intent = Intent(context,FullScreenImage::class.java);
                intent.putExtra("url",item.get("img").toString());
                (context as FragmentActivity).startActivity(intent)
//            context.goToFullView(FullScreenImageFragment());

            })

        }









    }

    override fun getItemCount(): Int {
        return orders.length();
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var view :View = LayoutInflater.from(parent?.context).inflate(R.layout.orders_product_view,parent,false)
        return ViewHolder(view);
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView;
        val productBreed: TextView;
        val productQuantity: TextView;
        val productPrice: TextView;
        var productImage: ImageView;
        val productStatus:TextView;
        val productDate:TextView;

        init {
            productName = itemView?.findViewById(R.id.product_name)!!
            productBreed = itemView?.findViewById(R.id.product_breed)!!
            productQuantity = itemView?.findViewById(R.id.product_quantity)!!
            productPrice = itemView?.findViewById(R.id.product_price)!!
            productImage = itemView?.findViewById(R.id.product_image)!!
            productStatus = itemView?.findViewById(R.id.product_status)!!
            productDate = itemView?.findViewById(R.id.productDate)!!

        }
    }
}