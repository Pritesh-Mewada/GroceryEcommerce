package graphysis.groceryecommerce

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_welcome.view.*
import kotlinx.android.synthetic.main.pending_completed_fragment.*

/**
 * Created by pritesh on 18/1/18.
 */

class PendingCompletedFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pending_completed_fragment,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var viewpagerAdapter:FragmentPagerAdapter = FragmentPagerAdapter((context as FragmentActivity).supportFragmentManager);
        orderViewPager.adapter= viewpagerAdapter;
        (ordersTab as SlidingTabLayout).setViewPager(orderViewPager)
        (ordersTab as SlidingTabLayout).setDistributeEvenly(true);
        (ordersTab as SlidingTabLayout).setSelectedIndicatorColors(R.color.colorAccent);

    }

}