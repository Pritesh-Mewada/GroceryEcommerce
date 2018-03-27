package graphysis.groceryecommerce

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.drawable.LayerDrawable
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject


/**
 * Created by pritesh on 20/12/17.
 */
class DataStorageClass(val context: Context?, val name:String, val version:Int) : SQLiteOpenHelper(context,name,null,version) {


    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = (" CREATE TABLE IF NOT EXISTS Orders ( keyword TEXT UNIQUE , quantity TEXT , cost TEXT ) ");
        p0?.execSQL(CREATE_TABLE);
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS Orders");
        // Create tables again
        onCreate(p0);
    }

    fun Increment(){
        var db:SQLiteDatabase = this.readableDatabase;

        val select = "SELECT * FROM Orders"
        var cursor: Cursor = db.rawQuery(select,null);

        context?.IncrementCart(DataStorageClass.icon,cursor.count.toString())
    }
    fun AddOrderID(keyword:String,cost:String,quantity:String){

        Log.d("hello",keyword+"  "+cost+"  "+quantity)
        var db:SQLiteDatabase = this.writableDatabase;


        var contentvalues:ContentValues = ContentValues();
        contentvalues.put("keyword",keyword);
        contentvalues.put("quantity",quantity);
        contentvalues.put("cost",cost);
        db.insert("Orders",null,contentvalues);
        db.close()
        Increment()
    }

    fun UpdateOrderID(keyword:String,cost:String,quantity:String){
        var db:SQLiteDatabase = this.writableDatabase;
        var contentvalues:ContentValues = ContentValues();
        contentvalues.put("keyword",keyword);
        contentvalues.put("quantity",quantity);
        contentvalues.put("cost",cost);
        val where = "keyword=?"
        val whereArgs = arrayOf<String>(keyword)
        db.update("Orders",contentvalues,where,whereArgs);
        db.close()
    }

    fun RemoveOrder(id:String){
        var db:SQLiteDatabase = this.writableDatabase;
        db.execSQL("delete from Orders where keyword='"+id+"'")
        db.close()
        Increment()
    }

    fun deleteAllOrder(){
        var db:SQLiteDatabase = this.writableDatabase;
        db.execSQL("delete from Orders");
        Increment()
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

        return array;

    }

    fun getDataForCheckout():JSONArray{
        var db:SQLiteDatabase = this.readableDatabase;
        val select = "SELECT * FROM Orders"
        var cursor: Cursor = db.rawQuery(select,null);
        var dataReturn = JSONArray();
        if(cursor.moveToFirst()){
            do {
                var orderObject = JSONObject();
                orderObject.put("product_id",cursor.getString(0).toString());
                orderObject.put("order_quantity",cursor.getString(1).toString());
                orderObject.put("unit_price",cursor.getString(2).toString());
                dataReturn.put(orderObject);

            }while (cursor.moveToNext())
        }

        return dataReturn

    }


    companion object {
        var fruits:JSONArray =JSONArray();
        var vegetable: JSONArray =JSONArray();
        var current:JSONObject = JSONObject();
        var checkout:ArrayList<JSONObject> = ArrayList()
        lateinit  var icon:LayerDrawable;

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





    }

}

