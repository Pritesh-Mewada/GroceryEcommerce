package graphysis.groceryecommerce

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 25/12/17.
 */
class CheckoutRecyclerViewAdapter(val context:Context?,val jsonFruit:JSONArray,val jsonVegetable:JSONArray,val totalprice:TextView): RecyclerView.Adapter<CheckoutRecyclerViewAdapter.ViewHolder>() {
    var products:ArrayList<String>
    var dataStorage:DataStorageClass;
    var retrieveJson :ArrayList<JSONObject>;
    init {
        dataStorage = DataStorageClass(context = context, name = "Order", version = 2);
        products = dataStorage.getAllOrder();
        retrieveJson = ArrayList();
        DataStorageClass.setDataForCheckout(retrieveJson)

        for (j in 0..(products.size-1)){
            for (i in 0..(jsonFruit.length()-1)){
                var product = jsonFruit.get(i) as JSONObject;

                if(product.get("id").equals(products.get(j))){
                    product.put("order_quantity",1)
                    retrieveJson.add(product)
                    totalprice.text = (totalprice.text.toString().toInt()+product.get("cost").toString().toInt()).toString()
                }


            }
            for (i in 0..(jsonVegetable.length()-1)){
                var product = jsonVegetable.get(i) as JSONObject;

                if(product.get("id").equals(products.get(j))){
                    product.put("order_quantity",1)
                    retrieveJson.add(product)
                    totalprice.text = (totalprice.text.toString().toInt()+product.get("cost").toString().toInt()).toString()
                }
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var item: JSONObject = retrieveJson.get(position) as JSONObject;



        holder?.productName?.text = item.get("name").toString().capitalize()
        holder?.productBreed?.text = item.get("type").toString().capitalize()
        holder?.productQuantity?.text = "1 "+item.get("quantity_unit").toString().capitalize()
        holder?.productPrice?.text ="Rs: "+item.get("cost").toString()
        Picasso.with(context).load(item.get("img").toString()).into(holder?.productImage);

        holder?.productIncrement?.setOnClickListener(View.OnClickListener {
            var number = holder.productNumbers.text.toString().toInt();
            var max = item.get("quantity").toString().toInt();

            if(number==max){
                Toast.makeText(context,"Maximum Available Quantity Reached",Toast.LENGTH_LONG).show();
            }else{
                number++
                totalprice.text = (totalprice.text.toString().toInt()+item.get("cost").toString().toInt()).toString();
                holder.productNumbers.text = number.toString()
                holder?.productPrice?.text ="Rs: "+(number*item.get("cost").toString().toInt()).toString()
                item.put("order_quantity",number);
                DataStorageClass.setDataForCheckout(retrieveJson)
            }
        })

        holder?.productDecrement?.setOnClickListener(View.OnClickListener {
            var number = holder.productNumbers.text.toString().toInt();
            var max = 1;
            if(number==max){
                Toast.makeText(context,"Minimum Quantity Reached",Toast.LENGTH_LONG).show();
            }else{
                number--;
                totalprice.text = (totalprice.text.toString().toInt()-item.get("cost").toString().toInt()).toString()
                holder.productNumbers.text = number.toString()
                holder?.productPrice?.text ="Rs: "+(number*item.get("cost").toString().toInt()).toString()
                item.put("order_quantity",number);
                DataStorageClass.setDataForCheckout(retrieveJson)
            }
        })


        holder?.productRemove?.setOnClickListener {
            dataStorage.RemoveOrder(item.get("id").toString());
            var number:Int = item.get("order_quantity").toString().toInt();

            totalprice.text = (totalprice.text.toString().toInt()-number*item.get("cost").toString().toInt()).toString()

            retrieveJson.remove(item);
            notifyDataSetChanged();
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var view :View = LayoutInflater.from(parent?.context).inflate(R.layout.checkout_product_view,parent,false)
        return ViewHolder(view);
    }

    override fun getItemCount(): Int {
        return retrieveJson.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView;
        val productBreed: TextView;
        val productQuantity: TextView;
        val productPrice: TextView;
        var productImage: ImageView;
        var productRemove: Button;
        var productNumbers:TextView;
        var productIncrement:ImageButton;
        var productDecrement:ImageButton;

        init {
            productName = itemView?.findViewById(R.id.product_name)!!
            productBreed = itemView?.findViewById(R.id.product_breed)!!
            productQuantity = itemView?.findViewById(R.id.product_quantity)!!
            productPrice = itemView?.findViewById(R.id.product_price)!!
            productImage = itemView?.findViewById(R.id.product_image)!!
            productRemove = itemView?.findViewById(R.id.product_remove)!!
            productNumbers = itemView?.findViewById(R.id.quantity)!!
            productIncrement = itemView?.findViewById(R.id.increment_quantity)!!
            productDecrement = itemView?.findViewById(R.id.decrement_quantity)!!



        }
    }
}