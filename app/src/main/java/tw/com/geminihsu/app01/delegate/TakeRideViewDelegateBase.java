package tw.com.geminihsu.app01.delegate;

import android.support.v4.view.ViewPager;

import tw.com.geminihsu.app01.TakeRideMenuActivity;

/**
 * Created by geminih on 12/23/2017.
 */

public class TakeRideViewDelegateBase {

    protected TakeRideMenuActivity clientTakeRideActivity;
    protected ViewPager mPager;

    public TakeRideViewDelegateBase(TakeRideMenuActivity _clientTakeRideActivity, ViewPager _mPager)
    {
        clientTakeRideActivity = _clientTakeRideActivity;
        mPager = _mPager;


    }

    public void setInitialActivity() {

    }
    public void refreshData()
    {

    }
}
