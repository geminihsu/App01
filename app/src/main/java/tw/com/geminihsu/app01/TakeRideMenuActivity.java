package tw.com.geminihsu.app01;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.bean.RideType;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.OneRidePage.TakeRideViewDelegateBaseOneRide;
import tw.com.geminihsu.app01.delegate.TakeRideViewDelegateBase;
import tw.com.geminihsu.app01.delegate.TwoRidePage.TakeRideAirPlaneDelegateBaseTwoRide;
import tw.com.geminihsu.app01.delegate.TwoRidePage.TakeRideTrainDelegateBaseTwoRide;
import tw.com.geminihsu.app01.fragment.Fragment_TakeRide;


public class TakeRideMenuActivity extends FragmentActivity implements Fragment_TakeRide.OnFragmentInteractionListener{

    public final static String BUNDLE_ORDER_CUR_ADDRESS = "address";// from
    public final static int TAKE_RIDE_TRAIN_DEPARTURE = 0;
    public final static int TAKE_RIDE_TRAIN_DESTINATION = 1;
    private List<RideType> mDemoData = new ArrayList<RideType>();


    private ViewPager mPager;


    private int dType;//哪一種司機型態
    private int cargoType;//那一種訂單型態

    private static Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private static Constants.APP_REGISTER_ORDER_TYPE orderCargoType;
    private static String currAddress = "";

    private LinearLayout linearLayout_tab;
    private FrameLayout frameLayout;

    private TakeRideViewDelegateBase takeRideViewDelegateBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        setTitle(getString(R.string.client_train_pick_up));
        setContentView(R.layout.client_take_ride_train_activity);

        linearLayout_tab = (LinearLayout) findViewById(R.id.linearLayout_tab);
        frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        mPager = (ViewPager) findViewById(R.id.viewPager);



        Bundle bundle = this.getIntent().getExtras();
        if (bundle.containsKey(Constants.NEW_ORDER_DTYPE)) {
            dType = bundle.getInt(Constants.NEW_ORDER_DTYPE);
            dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(dType));
        }
        if (bundle.containsKey(Constants.NEW_ORDER_CARGO_TYPE)) {
            cargoType = bundle.getInt(Constants.NEW_ORDER_CARGO_TYPE);
            orderCargoType = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargoType));
        }

        if (bundle.containsKey(BUNDLE_ORDER_CUR_ADDRESS)) {
            currAddress = bundle.getString(BUNDLE_ORDER_CUR_ADDRESS);

        }


        if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE) || orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE))
        {
            takeRideViewDelegateBase = new TakeRideViewDelegateBaseOneRide(this,dataType,orderCargoType,currAddress,mPager);
        }else  if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN))
        {
            frameLayout.setVisibility(View.GONE);
            linearLayout_tab.setVisibility(View.VISIBLE);
            takeRideViewDelegateBase = new TakeRideTrainDelegateBaseTwoRide(this,dataType,orderCargoType,currAddress,mPager);

        }else  if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT))
        {
            frameLayout.setVisibility(View.GONE);
            linearLayout_tab.setVisibility(View.VISIBLE);
            takeRideViewDelegateBase = new TakeRideAirPlaneDelegateBaseTwoRide(this,dataType,orderCargoType,currAddress,mPager);

        }
        takeRideViewDelegateBase.setInitialActivity();

    }

    @Override
    public void onFragmentInteraction(int currentDataPosition) {

    }

    @Override
    public void onFragmentCreated(Fragment_TakeRide demoFragment) {

    }

    @Override
    public void onFragmentResumed(Fragment_TakeRide demoFragment) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
