package tw.com.geminihsu.app01.delegate.OneRidePage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.TakeRideMenuActivity;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.TakeRideViewDelegateBase;
import tw.com.geminihsu.app01.fragment.Fragment_TakeRide;

/**
 * Created by geminih on 12/23/2017.
 */

public class TakeRideViewDelegateBaseOneRide extends TakeRideViewDelegateBase{

    public final static String BUNDLE_ORDER_DRIVER_TYPE = "dtype";// from
    public final static String BUNDLE_ORDER_CARGO_TYPE = "cargo_type";// from

    public final static String BUNDLE_ORDER_CUR_ADDRESS = "address";// from

    public final static String BUNDLE_ORDER_TICKET_ID = "ticket_id";// from
    public final static String BUNDLE_KEEP_BOOMARK = "add_bookmark";// from

    private Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private Constants.APP_REGISTER_ORDER_TYPE orderCargoType;
    private String currAddress;

    final public static int TAKE_RIDE = 1;
    final public static int SEND_MERCHANDISE= 2;
    public TakeRideViewDelegateBaseOneRide(TakeRideMenuActivity clientTakeRideActivity, Constants.APP_REGISTER_DRIVER_TYPE _dataType, Constants.APP_REGISTER_ORDER_TYPE _orderCargoType, String _currAddress, ViewPager _mPager) {
        super(clientTakeRideActivity,_mPager);
        this.dataType = _dataType;
        this.orderCargoType = _orderCargoType;
        this.currAddress = _currAddress;

    }

    @Override
    public void setInitialActivity(){
        if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE))
            clientTakeRideActivity.setTitle(clientTakeRideActivity.getString(R.string.client_take_ride_title));
        else if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE))
            clientTakeRideActivity.setTitle(clientTakeRideActivity.getString(R.string.client_send_merchandise_title));

        FragmentTransaction fragTran;
        Fragment fragment = new Fragment_TakeRide();
        fragTran = clientTakeRideActivity.getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();

        b.putInt(Constants.ARG_POSITION, TAKE_RIDE);
        b.putInt(BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
        b.putInt(BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
        b.putString(BUNDLE_ORDER_CUR_ADDRESS, currAddress);


        fragment.setArguments(b);

        fragTran.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        //fragTran.addToBackStack(fragment.getClass().getSimpleName());

        fragTran.commit();
    }
}
