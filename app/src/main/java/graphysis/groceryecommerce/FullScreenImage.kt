package graphysis.groceryecommerce

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_screen_image.*

class FullScreenImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        Picasso.with(this).load(intent.getStringExtra("url")).into(imageZoom as ImageView);

        closeZoom.setOnClickListener({
            finish();
        })
    }

}
