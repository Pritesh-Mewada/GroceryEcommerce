package graphysis.groceryecommerce

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import java.nio.file.Files.size
import android.support.v4.app.FragmentPagerAdapter



/**
 * Created by pritesh on 18/1/18.
 */
class FragmentPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0->{
                return CompletedFragment();
            }
            1->{
                return PendingFragment();
            }

        }

        return CompletedFragment()

    }

    override fun getCount(): Int {
        return 2
    }



    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0->{
                return "Completed";
            }
            1->{
                return "Pending";
            }

        }

        return "Completed"
    }
}