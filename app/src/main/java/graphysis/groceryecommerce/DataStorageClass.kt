package graphysis.groceryecommerce

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by pritesh on 20/12/17.
 */
class DataStorageClass(val context: Context?, val name:String, val version:Int) : SQLiteOpenHelper(context,name,null,version) {


    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = (" CREATE TABLE IF NOT EXISTS Orders ( id TEXT UNIQUE ) ");

        p0?.execSQL(CREATE_TABLE);
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

        p0?.execSQL("DROP TABLE IF EXISTS Orders");

        // Create tables again
        onCreate(p0);
    }

    fun AddOrderID(id:String){
        var db:SQLiteDatabase = this.writableDatabase;
        var contentvalues:ContentValues = ContentValues();
        contentvalues.put("id",id);

        db.insert("Orders",null,contentvalues);
        db.close()
    }


    fun RemoveOrder(id:String){
        var db:SQLiteDatabase = this.writableDatabase;

        db.execSQL("delete from Orders where id = "+id);

    }

    fun getAllOrder():ArrayList<String>{
        var array:ArrayList<String> = ArrayList();
        var db:SQLiteDatabase = this.readableDatabase;

        val select = "SELECT * FROM Orders"
        var cursor: Cursor = db.rawQuery(select,null);

        if(cursor.moveToFirst()){
            do {
                array.add(cursor.getString(0).toString());

            }while (cursor.moveToNext())
        }

        Log.d("Google",array.toString())
        return array;

    }


    companion object {
        var fruits:JSONArray =JSONArray();
        var vegetable: JSONArray =JSONArray();
        var current:JSONObject = JSONObject();
        var checkout:ArrayList<JSONObject> = ArrayList();

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

        fun setDataForCheckout(data:ArrayList<JSONObject>){
            checkout = data;
        }

        fun getDataForCheckout():JSONArray{
            var dataReturn=JSONArray();

            for(i in 0..(checkout.size-1)){

                var orderObject = JSONObject();
                var tempObject = checkout.get(i) as JSONObject;
                orderObject.put("product_id",tempObject.get("keyword").toString());
                orderObject.put("order_quantity",tempObject.get("order_quantity").toString());
                orderObject.put("unit_price",tempObject.get("cost").toString());

                dataReturn.put(orderObject);
            }

            return dataReturn;
        }



    }

}

