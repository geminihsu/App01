package tw.com.geminihsu.app01;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import tw.com.geminihsu.app01.bean.LocationAddress;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.fragment.Fragment_TakeRide;


public class ClientTakeRideActivity extends FragmentActivity {


    public final static String BUNDLE_ORDER_TICKET_ID = "ticket_id";// from
    public final static String BUNDLE_ORDER_DRIVER_TYPE = "dtype";// from
    public final static String BUNDLE_ORDER_CARGO_TYPE = "cargo_type";// from

    //public final static String BUNDLE_ORDER_CUR_LONGITUDE = "dtype";// from
    //public final static String BUNDLE_ORDER_CUR_LATITUDE = "cargo_type";// from


    public final static String BUNDLE_ORDER_CUR_ADDRESS = "address";// from


    public final static String BUNDLE_KEEP_BOOMARK = "add_bookmark";// from



    final public static int TAKE_RIDE = 1;
    final public static int SEND_MERCHANDISE= 2;

    private int dType;//哪一種司機型態
    private int cargoType;//那一種訂單型態

    private Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private Constants.APP_REGISTER_ORDER_TYPE orderCargoType;
    private int option;
    private String currAddress = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_take_ride_activity);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                option = bundle.getInt(Constants.ARG_POSITION);
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

                option = bundle.getInt(Constants.NEW_ORDER_CARGO_TYPE);

            }else
            {
                //Error!!!!
            }
        }
        else
        {
            //Error!!!!
        }

        FragmentTransaction fragTran;
        Fragment fragment = new Fragment_TakeRide();
        fragTran = getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();

        b.putInt(Constants.ARG_POSITION, ClientTakeRideActivity.TAKE_RIDE);
        b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_DRIVER_TYPE, dataType.value());
        b.putInt(ClientTakeRideActivity.BUNDLE_ORDER_CARGO_TYPE, orderCargoType.value());
        b.putString(ClientTakeRideActivity.BUNDLE_ORDER_CUR_ADDRESS, currAddress);


        fragment.setArguments(b);

        fragTran.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        //fragTran.addToBackStack(fragment.getClass().getSimpleName());

        fragTran.commit();



    }



}
