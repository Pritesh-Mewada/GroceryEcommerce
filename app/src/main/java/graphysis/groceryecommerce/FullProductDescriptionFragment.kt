package graphysis.groceryecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 20/12/17.
 */
class FullProductDescriptionFragment: Fragment() {
    lateinit var  productName: TextView;
    lateinit var productBreed: TextView;
    lateinit var productQuantity: TextView;
    lateinit var productPrice: TextView;
    lateinit var productDescription: TextView;
    lateinit var productImage: ImageView;



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.full_product_description_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productName = view?.findViewById(R.id.product_name)!!;
        productBreed = view?.findViewById(R.id.product_breed)!!
        productQuantity = view?.findViewById(R.id.product_quantity)!!
        productPrice = view?.findViewById(R.id.product_price)!!
        productImage = view?.findViewById(R.id.product_image)!!
        productDescription = view?.findViewById(R.id.product_description)!!


        var dataObject =DataStorageClass.current;

        productName.text = dataObject.get("name").toString().capitalize();
        productBreed.text = dataObject.get("type").toString().capitalize();
        productQuantity.text ="1 "+dataObject.get("quantity_unit").toString().capitalize();
        productPrice.text ="Rs. " +dataObject.get("cost").toString().capitalize()
        productDescription.text = dataObject.get("description").toString().capitalize()

        Picasso.with(context).load(dataObject.get("img").toString()).into(productImage);



    }
}