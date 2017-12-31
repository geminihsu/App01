package tw.com.geminihsu.app01.delegate.TwoRidePage;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.TakeRideMenuActivity;
import tw.com.geminihsu.app01.bean.RideType;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.TakeRideViewDelegateBase;
import tw.com.geminihsu.app01.fragment.Fragment_TakeRide;

/**
 * Created by geminih on 12/23/2017.
 */

public class TakeRideTrainDelegateBaseTwoRide extends TakeRideViewDelegateBase{


    public final static int TAKE_RIDE_TRAIN_DEPARTURE = 0;
    public final static int TAKE_RIDE_TRAIN_DESTINATION = 1;

    private Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private Constants.APP_REGISTER_ORDER_TYPE orderCargoType;
    private String currAddress;


    private ClientTakeRideTabAdapter mAdapter;
    private List<RideType> ridePage;

    public TakeRideTrainDelegateBaseTwoRide(TakeRideMenuActivity clientTakeRideActivity, Constants.APP_REGISTER_DRIVER_TYPE _dataType, Constants.APP_REGISTER_ORDER_TYPE _orderCargoType, String _currAddress, ViewPager _mPager) {
        super(clientTakeRideActivity,_mPager);
        this.dataType = _dataType;
        this.orderCargoType = _orderCargoType;
        this.currAddress = _currAddress;

    }

    @Override
    public void setInitialActivity(){

        clientTakeRideActivity.setTitle(R.string.client_train_pick_up);
        ridePage = new ArrayList<>();
        ridePage.add(new RideType(clientTakeRideActivity.getString(R.string.tab_send_train), TAKE_RIDE_TRAIN_DEPARTURE,dataType.value(),orderCargoType.value(),currAddress));
        ridePage.add(new RideType(clientTakeRideActivity.getString(R.string.tab_pick_up_train), TAKE_RIDE_TRAIN_DESTINATION,dataType.value(),orderCargoType.value(),currAddress));
        mAdapter = new ClientTakeRideTabAdapter(clientTakeRideActivity.getSupportFragmentManager(),ridePage);
        mPager.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshData()
    {
        mAdapter.notifyDataSetChanged();
    }

    public class ClientTakeRideTabAdapter extends FragmentPagerAdapter {



        public ClientTakeRideTabAdapter(FragmentManager fm, List<RideType> _ridePage) {
            super(fm);
            ridePage = _ridePage;
        }


        @Override
        public CharSequence getPageTitle(int position) {

            SpannableStringBuilder sb = new SpannableStringBuilder(ridePage.get(position).title); // space added before text for convenience

            //Here's an example that will look for all occurrences of a word (case insensitive), and color them red:

            sb.setSpan(new ForegroundColorSpan(Color.rgb(255, 255, 255)), 0, ridePage.get(position).title.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            return sb;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment_TakeRide.newInstance(ridePage.get(position));
        }

        @Override
        public int getCount() {
            return ridePage.size();

        }
    }
    }
