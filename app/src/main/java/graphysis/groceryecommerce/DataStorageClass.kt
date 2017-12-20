package graphysis.groceryecommerce

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by pritesh on 20/12/17.
 */
class DataStorageClass {






    companion object {
        var fruits:JSONArray =JSONArray();
        var vegetable: JSONArray =JSONArray();
        var current:JSONObject = JSONObject();

        fun inflateData(data:JSONArray){

            for(i in 0..(data.length()-1)){
                val item = data.getJSONObject(i);
                if(item.get("category").equals("fruit")){
                    fruits.put(item);
                }else{
                    vegetable.put(item);
                }
            }

        }

        fun setItemForFullViewClass(data:JSONObject){
            current = data;
        }

    }

}

