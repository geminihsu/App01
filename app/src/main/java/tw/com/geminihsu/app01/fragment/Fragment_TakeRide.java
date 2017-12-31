package tw.com.geminihsu.app01.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.BookmarksMapListActivity;
import tw.com.geminihsu.app01.ClientTakeRideSearchActivity;
import tw.com.geminihsu.app01.MainActivity;
import tw.com.geminihsu.app01.OrderMapActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.SendMerchandiseActivity;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItem;
import tw.com.geminihsu.app01.adapter.ClientTakeRideSelectSpecListItemAdapter;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.LocationAddress;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.bean.OrderLocationBean;
import tw.com.geminihsu.app01.bean.RideType;
import tw.com.geminihsu.app01.bean.USerBookmark;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.TwoRidePage.TakeRideAirPlaneDelegateBaseTwoRide;
import tw.com.geminihsu.app01.delegate.TwoRidePage.TakeRideTrainDelegateBaseTwoRide;
import tw.com.geminihsu.app01.serverbean.ServerBookmark;
import tw.com.geminihsu.app01.serverbean.ServerSpecial;
import tw.com.geminihsu.app01.utils.DateTimeUtil;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class Fragment_TakeRide extends Fragment {
    private final String TAG = Fragment_TakeRide.class.toString();
    public final static String BUNDLE_ORDER_TICKET_ID = "ticket_id";// from
    public final static String BUNDLE_ORDER_DRIVER_TYPE = "dtype";// from
    public final static String BUNDLE_ORDER_CARGO_TYPE = "cargo_type";// from

    //public final static String BUNDLE_ORDER_CUR_LONGITUDE = "dtype";// from
    //public final static String BUNDLE_ORDER_CUR_LATITUDE = "cargo_type";// from


    public final static String BUNDLE_ORDER_CUR_ADDRESS = "address";// from


    public final static String BUNDLE_KEEP_BOOMARK = "add_bookmark";// from

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;


    final public static int TAKE_RIDE = 1;
    final public static int SEND_MERCHANDISE= 2;


    private LinearLayout linearLayout_date_picker;
    private LinearLayout linearLayout_merchandise_info;

    private LinearLayout taxi_take_ride_departure_linearLayout;
    private LinearLayout taxi_take_ride_destination_linearLayout;


    private LinearLayout train_take_ride_departure_linearLayout;
    private LinearLayout train_take_ride_departure_station_linearLayout;
    private LinearLayout train_take_ride_destination_linearLayout;
    private LinearLayout train_take_ride_destination_station_linearLayout;
    private ImageButton btn_datePicker;
    private ImageButton btn_timerPicker;
    private ImageButton departure;
    private ImageButton stop;
    private ImageButton destination;
    private ImageButton spec;
    private RadioGroup radioGroup_type;
    private RadioButton realtime;
    private RadioButton reservation;
    private TextView show_title;
    private EditText spec_value;
    private EditText date;
    private EditText time;
    private EditText departure_address;
    private EditText stop_address;
    private EditText destination_address;
    private EditText remark;
    private EditText merchandise_content;
    private EditText merchandise_weight;

    private TextView departure_location;
    private TextView destination_location;
    private TextView merchandise_unit;



    private RadioGroup radioGroup_departure_location;
    private RadioGroup radioGroup_destination_location;
    private RadioButton departure_tra_train;
    private RadioButton departure_thsr_train;

    private RadioButton destination_tra_train;
    private RadioButton destination_thsr_train;

    private Spinner spinner_departure;
    private Spinner spinner_destination;

    private int option;

    private final List<ClientTakeRideSelectSpecListItem> mCommentListData = new ArrayList<ClientTakeRideSelectSpecListItem>();;
    private ClientTakeRideSelectSpecListItemAdapter listViewAdapter;

    private DatePickerDialog.OnDateSetListener datePicker;
    private TimePickerDialog.OnTimeSetListener timePicker;

    private Calendar calendar;

    private JsonPutsUtil sendDataRequest;
    private AccountInfo driver;

    private LocationAddress departure_detail;
    private LocationAddress stop_detail;
    private LocationAddress destination_detail;

    private int dType;//哪一種司機型態
    private int cargoType;//那一種訂單型態

    private Constants.APP_REGISTER_DRIVER_TYPE dataType;
    private Constants.APP_REGISTER_ORDER_TYPE orderCargoType;



    private long order_timeStamp;
    private ArrayList<ClientTakeRideSelectSpecListItem> spec_list;

    private ProgressDialog progressDialog_loading;

    private Dialog dialog;
    private AlertDialog enter_alertDialog;
    private AlertDialog choiceSpecAlertDialog;

    private String currAddress = "";
    private String departure_info = "";
    private String destination_info = "";
    private String stop_info = "";


    private static final String ARG_DATA = "data";
    private RideType rideType;

    private OnFragmentInteractionListener mListener;

    private ArrayList<ServerBookmark> trainStationList;
    private ServerBookmark currentLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_take_ride, container, false);

        if(rideType != null) {


            if(mListener != null) {
                mListener.onFragmentCreated(this);
            }
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onFragmentResumed(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        sendDataRequest = new JsonPutsUtil(getActivity());
        sendDataRequest.setServerRequestOrderManagerCallBackFunction(new JsonPutsUtil.ServerRequestOrderManagerCallBackFunction() {

            @Override
            public void createNormalOrder(NormalOrder order) {

                if(progressDialog_loading!=null)
                {
                    progressDialog_loading.cancel();
                    progressDialog_loading = null;
                }

                Intent intent = new Intent(getActivity(), ClientTakeRideSearchActivity.class);

                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Integer.valueOf(order.getTicket_id()));
                intent.putExtras(b);
                startActivity(intent);
                //finish();
            }

            @Override
            public void cancelNormalOrder(NormalOrder order) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public RideType getData() {
        return rideType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = this.getArguments();
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
                    departure_info = currAddress;

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

        if (getArguments() != null) {
            rideType = (RideType) getArguments().getParcelable(ARG_DATA);
            if(rideType != null)
            {
                option = rideType.option;
                dType = rideType.dtype;
                cargoType = rideType.cargoType;
                currAddress = rideType.curAddress;
                departure_info = currAddress;
            }
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.findViews();
        this.setLister();


        //departure_address.setText(departure_info);
        displayLayout();
    }


    @Override
    public void onStart() {
        super.onStart();



    }


    public static Fragment newInstance(RideType rideType) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, rideType);

        Fragment f = new Fragment_TakeRide();
        f.setArguments(args);
        return f;
    }
    private void findViews()
    {
        spec_list = new ArrayList<ClientTakeRideSelectSpecListItem>();
        linearLayout_merchandise_info = (LinearLayout) getView().findViewById (R.id.merchandise_content);
        linearLayout_date_picker = (LinearLayout) getView().findViewById(R.id.date_layout);
        taxi_take_ride_departure_linearLayout = (LinearLayout) getView().findViewById(R.id.taxi_take_ride_departure_linearLayout);
        taxi_take_ride_destination_linearLayout = (LinearLayout) getView().findViewById(R.id.taxi_take_ride_destination_linearLayout);

        train_take_ride_departure_linearLayout = (LinearLayout) getView().findViewById(R.id.train_take_ride_departure_linearLayout);
        train_take_ride_departure_station_linearLayout = (LinearLayout) getView() .findViewById(R.id.train_take_ride_departure_station_linearLayout);

        train_take_ride_destination_linearLayout = (LinearLayout) getView().findViewById(R.id.train_take_ride_destination_linearLayout);
        train_take_ride_destination_station_linearLayout = (LinearLayout) getView() .findViewById(R.id.train_take_ride_destination_station_linearLayout);


        btn_datePicker = (ImageButton) getView().findViewById(R.id.date_picker);
        btn_timerPicker = (ImageButton) getView().findViewById(R.id.time_picker);
        departure = (ImageButton) getView().findViewById(R.id.departure);
        stop = (ImageButton) getView().findViewById(R.id.stop);
        destination = (ImageButton) getView().findViewById(R.id.destination);
        spec = (ImageButton) getView().findViewById(R.id.spec_option);

        departure_location = (TextView) getView().findViewById(R.id.departure_location_txt);
        destination_location =  (TextView) getView().findViewById(R.id.destination_location_txt);

        show_title = (TextView) getView().findViewById(R.id.txt_info);
        spec_value = (EditText) getView().findViewById(R.id.passenger_spec_value);
        merchandise_unit = (TextView) getView().findViewById(R.id.merchandise_unit);


        radioGroup_type = (RadioGroup) getView().findViewById(R.id.source);
        realtime = (RadioButton)getView().findViewById(R.id.real_radio);
        reservation = (RadioButton) getView().findViewById(R.id.reservation_radio);

        date = (EditText) getView().findViewById(R.id.date_info);
        time = (EditText) getView().findViewById(R.id.time_info);
        departure_address = (EditText) getView().findViewById(R.id.departure_address);
        departure_address.setText(currAddress);
        stop_address = (EditText) getView().findViewById(R.id.stop_address);
        remark = (EditText) getView().findViewById(R.id.spec_info);
        destination_address = (EditText) getView().findViewById(R.id.destination_address);
        merchandise_content = (EditText) getView().findViewById(R.id.merchandise_content_info);
        merchandise_weight = (EditText) getView().findViewById(R.id.merchandise_weight);

        radioGroup_departure_location = (RadioGroup) getView().findViewById(R.id.departure_train);
        radioGroup_destination_location = (RadioGroup) getView().findViewById(R.id.destination_train);

        departure_tra_train = (RadioButton) getView().findViewById(R.id.departure_tra_train);
        departure_thsr_train = (RadioButton) getView().findViewById(R.id.departure_thsr_train);


        destination_tra_train = (RadioButton) getView().findViewById(R.id.destination_tra_train);
        destination_thsr_train = (RadioButton) getView().findViewById(R.id.destination_thsr_train);

        spinner_departure = (Spinner) getView().findViewById(R.id.train_departure_spinner);
        spinner_destination = (Spinner) getView().findViewById(R.id.train_destination_spinner);

        Date dateIfo=new Date();
        time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateIfo));
        //Log.e(TAG,new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(date));

    }


    private void displayLayout() {
        dataType = Constants.conversion_register_driver_account_result(Integer.valueOf(dType));
        orderCargoType =  Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(cargoType));

        //display cargo form
        if (orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)) {
            //getActionBar().setTitle(getString(R.string.client_take_ride_title));
            linearLayout_merchandise_info.setVisibility(View.VISIBLE);
        }else
            linearLayout_merchandise_info.setVisibility(View.GONE);

        //display the first title
        if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT) || orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN))
        {
            show_title.setVisibility(View.GONE);
        }

        if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN))
        {
            getLocationFromDB("高鐵");

            if(option == TakeRideTrainDelegateBaseTwoRide.TAKE_RIDE_TRAIN_DEPARTURE)
            {
                train_take_ride_destination_linearLayout.setVisibility(View.VISIBLE);
                train_take_ride_destination_station_linearLayout.setVisibility(View.VISIBLE);
                taxi_take_ride_destination_linearLayout.setVisibility(View.GONE);

            }else  if(option == TakeRideTrainDelegateBaseTwoRide.TAKE_RIDE_TRAIN_DESTINATION)
            {

                train_take_ride_departure_linearLayout.setVisibility(View.VISIBLE);
                train_take_ride_departure_station_linearLayout.setVisibility(View.VISIBLE);
                taxi_take_ride_departure_linearLayout.setVisibility(View.GONE);
            }

        }

        if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT))
        {
            getLocationFromDB("機場");

            if(option == TakeRideAirPlaneDelegateBaseTwoRide.TAKE_RIDE_AIRPORT_DEPARTURE)
            {
                destination_location.setText(getString(R.string.txt_take_destination));
                train_take_ride_destination_station_linearLayout.setVisibility(View.VISIBLE);
                taxi_take_ride_destination_linearLayout.setVisibility(View.GONE);

            }else  if(option == TakeRideAirPlaneDelegateBaseTwoRide.TAKE_RIDE_AIRPORT_DESTINATION)
            {

                departure_location.setText(getString(R.string.txt_take_departure));
                train_take_ride_departure_station_linearLayout.setVisibility(View.VISIBLE);
                taxi_take_ride_departure_linearLayout.setVisibility(View.GONE);
            }

        }

    }
    private void setLister()
    {

        calendar = Calendar.getInstance();
        timePicker=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {
                time.setText(hourOfDay+ ":" +minute);
            }
        };
        btn_timerPicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),
                        timePicker,
                        24,
                        59,
                        true).show();
            }
        });

        datePicker=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(dayOfMonth+ "/" + (month+1) + "/" + year);
            }

        };
        btn_datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), datePicker,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        radioGroup_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Date dateIfo=new Date();
                    if (checkedId == reservation.getId()) {
                        linearLayout_date_picker.setVisibility(View.VISIBLE);

                        String schedule = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateIfo);
                        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(dateIfo));
                        time.setText(new SimpleDateFormat("HH:mm:ss").format(dateIfo));
                        order_timeStamp = DateTimeUtil.convertString_yyyymmddToMillisecondsTime(schedule);
                    } else {
                        linearLayout_date_picker.setVisibility(View.GONE);
                        time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateIfo));
                        order_timeStamp = 0;

                    }
          }
        });

        departure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pop_map_option1));
                arrayAdapter.add(getString(R.string.pop_map_option2));

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);

                                switch (which){
                                    case 0:
                                        /*Intent map = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);*/
                                        Intent map = new Intent(getActivity(), OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(getActivity(), BookmarksMapListActivity.class);
                                        Bundle flag = new Bundle();
                                        flag.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_BOOKMARK);
                                        page.putExtras(flag);
                                        startActivityForResult(page,Constants.DEPARTURE_QUERY_BOOKMARK);

                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pop_map_option1));
                arrayAdapter.add(getString(R.string.pop_map_option2));

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);

                                switch (which){
                                    case 0:
                                        /*Intent map = new Intent(ClientTakeRideActivity.this, MapsActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DEPARTURE_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DEPARTURE_QUERY_GPS);*/
                                        Intent map = new Intent(getActivity(), OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.STOP_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.STOP_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(getActivity(), BookmarksMapListActivity.class);
                                        Bundle flag = new Bundle();
                                        flag.putInt(Constants.ARG_POSITION, Constants.STOP_QUERY_BOOKMARK);
                                        page.putExtras(flag);
                                        startActivityForResult(page,Constants.STOP_QUERY_BOOKMARK);

                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });

        destination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.select_dialog_item);
                arrayAdapter.add(getString(R.string.pop_map_option1));
                arrayAdapter.add(getString(R.string.pop_map_option2));

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);

                                switch (which){
                                    case 0:
                                        Intent map = new Intent(getActivity(), OrderMapActivity.class);
                                        Bundle b = new Bundle();
                                        b.putInt(Constants.ARG_POSITION, Constants.DESTINATION_QUERY_GPS);
                                        map.putExtras(b);
                                        startActivityForResult(map,Constants.DESTINATION_QUERY_GPS);
                                        break;
                                    case 1:
                                        Intent page = new Intent(getActivity(), BookmarksMapListActivity.class);
                                        Bundle flag = new Bundle();
                                        flag.putInt(Constants.ARG_POSITION, Constants.DESTINATION_QUERY_BOOKMARK);
                                        page.putExtras(flag);
                                        startActivityForResult(page,Constants.DESTINATION_QUERY_BOOKMARK);

                                        break;
                                }
                            }
                        });
                builderSingle.show();
            }
        });



        spec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choiceSpecFilter();
            }
        });

        spec_value.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choiceSpecFilter();
            }
        });


        merchandise_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateMerchandiseUnit();
            }
        });



        spinner_departure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                currentLocation = trainStationList.get(position);
                departure_detail = new LocationAddress();
                departure_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                departure_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                departure_detail.setAddress(currentLocation.getLocation()+"("+currentLocation.getStreetAddress().substring(3,currentLocation.getStreetAddress().length())+")");
                departure_detail.setLocation(currentLocation.getLocation());
                //departure_detail.setZipCode(currentLocation.getStreetAddress().substring(0,3));
                String zipCode = (getTrainStationZip(currentLocation.getLocation()));
                departure_detail.setZipCode(zipCode);
                departure_detail.setCountryName("Taiwan");
                departure_detail.setLocality(currentLocation.getStreetAddress());
                departure_address.setText(departure_detail.getAddress());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spinner_destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                currentLocation = trainStationList.get(position);
                destination_detail = new LocationAddress();
                destination_detail.setLongitude(Double.parseDouble(currentLocation.getLng()));
                destination_detail.setLatitude(Double.parseDouble(currentLocation.getLat()));
                destination_detail.setAddress(currentLocation.getLocation()+"("+currentLocation.getStreetAddress().substring(3,currentLocation.getStreetAddress().length())+")");

                destination_detail.setLocation(currentLocation.getLocation());
                String zipCpde = (getTrainStationZip(currentLocation.getLocation()));
                destination_detail.setZipCode(zipCpde);
                destination_detail.setCountryName("Taiwan");
                destination_detail.setLocality(currentLocation.getStreetAddress());
                destination_address.setText(destination_detail.getAddress());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        radioGroup_departure_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == departure_thsr_train.getId()) {
                    getLocationFromDB("高鐵");
                } else {

                    getLocationFromDB("火車");
                }
            }
        });

        radioGroup_destination_location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == destination_thsr_train.getId()) {
                    getLocationFromDB("高鐵");
                } else {

                    getLocationFromDB("火車");
                }
            }
        });
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = null;

        if(orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE))
            item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.sure_take_spec));
        else
            item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.menu_take));

        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:

                if(departure_address.getText().toString().isEmpty() || (dataType.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER) && destination_address.getText().toString().isEmpty()))
                    alert();
               else
                    sendOrder();
               /*if(!departure_address.getText().toString().isEmpty() && option != Fragment_TakeRide.SEND_MERCHANDISE)
                     sendOrder();
               else if(option == Fragment_TakeRide.SEND_MERCHANDISE && !departure_address.getText().toString().isEmpty() && !destination_address.getText().toString().isEmpty())
                     sendOrder();
               else
                   alert();*/


                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* 從 xml 取得 OrderRecord 清單 */
    private void getDataFromDB() {

        mCommentListData.clear();
        //Resources res =getResources();
        //String[] opt=res.getStringArray(R.array.client_take_ride_requirement);
        RealmUtil data = new RealmUtil(getActivity());
        RealmResults<ServerSpecial> specials= data.queryServerSpecial(Constants.SERVER_SPECIAL,"1");
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (int i = 0; i < specials.size(); i++) {
                // for listview 要用的資料
                ClientTakeRideSelectSpecListItem item = new ClientTakeRideSelectSpecListItem();


                item.check=false;
                //if(specials.get(i).getDtype().equals(dType))
                item.spec_id = specials.get(i).getId();
                item.book_title =specials.get(i).getContent();
                mCommentListData.add(item);


            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }



    private void createTaxiOrder(String target,String price,String tip)
    {

        Utility info = new Utility(getActivity());
        driver = info.getAccountInfo();
        OrderLocationBean begin_location = new OrderLocationBean();
        begin_location.setId(1);
        begin_location.setLatitude(""+departure_detail.getLatitude());
        begin_location.setLongitude(""+departure_detail.getLongitude());
        //String zip = departure_detail.getAddress().substring(0,3);
        begin_location.setZipcode(departure_detail.getZipCode());
        begin_location.setAddress(departure_detail.getAddress());

        OrderLocationBean stop_location = new OrderLocationBean();
        stop_location.setId(2);
        stop_location.setLatitude(""+stop_detail.getLatitude());
        stop_location.setLongitude(""+stop_detail.getLongitude());
        stop_location.setZipcode(stop_detail.getZipCode());
        stop_location.setAddress(stop_detail.getAddress());


        OrderLocationBean end_location = new OrderLocationBean();
        end_location.setId(3);
        end_location.setLatitude(""+destination_detail.getLatitude());
        end_location.setLongitude(""+destination_detail.getLongitude());
        //String zip1 = destination_detail.getAddress().substring(0,3);
        end_location.setZipcode(destination_detail.getZipCode());
        end_location.setAddress(destination_detail.getAddress());





        //long unixTime = System.currentTimeMillis() / 1000L;


        final NormalOrder order = new NormalOrder();
        order.setUser_id(driver.getId());
        order.setUser_uid(driver.getUid());
        order.setUser_name(driver.getPhoneNumber());
        order.setAccesskey(driver.getAccessKey());

        Log.e(TAG,"time stamp:"+time.getText().toString());
        order_timeStamp = DateTimeUtil.convertString_yyyymmddToMillisecondsTime(time.getText().toString());

        order.setTimebegin(""+order_timeStamp);
        order.setDtype(""+dataType.value());
        order.setCargo_type(""+orderCargoType.value());
        order.setBegin(begin_location);
        order.setEnd(end_location);
        order.setStop(stop_location);
        order.setBegin_address(begin_location.getAddress());
        order.setStop_address(stop_location.getAddress());
        order.setEnd_address(end_location.getAddress());
        order.setTicket_status("0");
        order.setOrderdate(time.getText().toString());
        order.setTarget(target);

        if(!remark.getText().toString().equals(""))
            order.setRemark(remark.getText().toString());

        if(price.equals(""))
            price = "0";
        if(tip.equals(""))
            tip = "0";
        order.setPrice(price);
        order.setTip(tip);

        if (orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE) && !merchandise_content.getText().toString().equals("") && !merchandise_unit.getText().toString().equals(""))
        {
            order.setRemark("\n"+getString(R.string.merchandise_content)+merchandise_content.getText().toString()+"\n"+getString(R.string.merchandise_weight)+merchandise_weight.getText().toString()+"\n"+getString(R.string.merchandise_unit)+merchandise_unit.getText().toString());
        }

        String spec="";
       if(!spec_list.isEmpty())
        {
            for(ClientTakeRideSelectSpecListItem item:spec_list){
                spec+=item.spec_id+",";
            }
            spec = spec.substring(0,spec.length()-1);
            Log.e(TAG,"spec car:"+spec);
            order.setCar_special(spec);
        }


        if (option == Fragment_TakeRide.SEND_MERCHANDISE) {
            //貨物快遞需填寫收件人資訊
            Intent question = new Intent(getActivity(), SendMerchandiseActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.ARG_POSITION, SendMerchandiseActivity.CLIENT_SEND_MERCHANDISE);
            b.putSerializable(Fragment_TakeRide.BUNDLE_ORDER_TICKET_ID, order);
            question.putExtras(b);
            startActivity(question);


        }else {
            //sendDataRequest.putCreateQuickTaxiOrder(order);
            if (progressDialog_loading == null) {
                progressDialog_loading = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);
            }
            ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable() {
                @Override
                public void run() {
                    sendDataRequest.putCreateNormalOrder(order);
                }
            }));

        }
    }






    private void provideOrderPrice()
    {

        if(dialog==null) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_enter_order_price_layout);
                dialog.setCancelable(false);
                dialog.setTitle(getString(R.string.order_change_fee));
                TextView content = (TextView) dialog.findViewById(R.id.txt_msg);
                final EditText enter = (EditText) dialog.findViewById(R.id.editText_password);
                enter.setText("");
                final EditText tip = (EditText) dialog.findViewById(R.id.editText_tip);
                tip.setText("");
                Button sure = (Button) dialog.findViewById(R.id.sure_action);
                sure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        //if (option == TAKE_RIDE)
                        //    if (departure_detail != null)
                        //if (departure_detail.getLongitude()==0)

                            parserAddressToGPS();
                            createTaxiOrder("" + option, enter.getText().toString(), tip.getText().toString());
                            //     else
                            //         createTempTaxiOrder("" + option);
                            // else
                            //    createMechardiseOrder("" + option);

                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel_action);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        dialog = null;
                    }
                });

                dialog.show();

        }
    }




    private void addUserLocationToBookMark(LocationAddress location)
    {
        String[] addressInfo = location.getLocation().split("\\(");
        USerBookmark item = new USerBookmark();
        item.setLat(""+location.getLatitude());
        item.setLng(""+location.getLongitude());
        item.setLocation(addressInfo[0]);
        item.setLocality(location.getLocality());
        item.setCountryName(location.getCountryName());
        item.setZipCode(location.getZipCode());
        item.setStreetAddress(addressInfo[1].substring(0,addressInfo[1].length()-1));

        //新增地點到資料庫
        RealmUtil database = new RealmUtil(getActivity());
        database.addUserBookMark(item);

    }



    @Override
    public void onStop() {
        super.onStop();
        if(dialog!=null)
        {
            dialog.dismiss();
            dialog = null;
        }

        if(progressDialog_loading!=null)
        {
            progressDialog_loading.cancel();
            progressDialog_loading = null;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog!=null)
        {
            dialog.dismiss();
            dialog = null;
        }

        if(progressDialog_loading!=null)
        {
            progressDialog_loading.cancel();
            progressDialog_loading = null;
        }
    }

    private void parserAddressToGPS() {

        Geocoder fwdGeocoder = new Geocoder(getActivity());

        if(departure_detail==null) {
        String departure = departure_address.getText().toString();


        List<Address> departure_locations = null;
        try {
            departure_locations = fwdGeocoder.getFromLocationName(departure, 10);
        } catch (IOException e) {
        }



            departure_detail = new LocationAddress();
            if (departure_locations.size() > 0) {
                departure_detail.setLongitude(departure_locations.get(0).getLongitude());
                departure_detail.setLatitude(departure_locations.get(0).getLatitude());
                departure_detail.setAddress(departure);
                departure_detail.setLocation(departure);
                departure_detail.setCountryName(departure_locations.get(0).getCountryName());
                departure_detail.setLocality(departure_locations.get(0).getLocality());
                departure_detail.setZipCode(departure_locations.get(0).getPostalCode());
            }
        }
        String stop = stop_address.getText().toString();


        if(stop_detail==null) {
            List<Address> stop_locations = null;
            try {
                stop_locations = fwdGeocoder.getFromLocationName(stop, 10);
            } catch (IOException e) {
            }


            //Log.e(TAG, "Stop zipCode:" + stop_locations.get(0).getPostalCode());
            stop_detail = new LocationAddress();
            if(stop_locations!=null) {
                if (stop_locations.size() > 0) {
                    stop_detail.setLongitude(stop_locations.get(0).getLongitude());
                    stop_detail.setLatitude(stop_locations.get(0).getLatitude());
                    stop_detail.setAddress(stop);
                    stop_detail.setLocation(stop);
                    stop_detail.setCountryName(stop_locations.get(0).getCountryName());
                    stop_detail.setLocality(stop_locations.get(0).getLocality());
                    stop_detail.setZipCode(stop_locations.get(0).getPostalCode());
                }
            }
        }
        String destination = destination_address.getText().toString();


        if(destination_detail==null) {
            List<Address> destination_locations = null;
            try {
                destination_locations = fwdGeocoder.getFromLocationName(destination, 10);
            } catch (IOException e) {
            }


            destination_detail = new LocationAddress();
            if(destination_locations != null) {
                if (destination_locations.size() > 0) {
                    destination_detail.setLongitude(destination_locations.get(0).getLongitude());
                    destination_detail.setLatitude(destination_locations.get(0).getLatitude());
                    destination_detail.setAddress(destination);
                    destination_detail.setLocation(destination);
                    destination_detail.setCountryName(destination_locations.get(0).getCountryName());
                    destination_detail.setLocality(destination_locations.get(0).getLocality());
                    destination_detail.setZipCode(destination_locations.get(0).getPostalCode());
                }
            }
        }


    }

    private void sendOrder(){

        String departure_address_info;
        if(departure_detail!=null)
            departure_address_info="從："+departure_detail.getAddress()+"\n";
        else
            departure_address_info = "從："+departure_address.getText().toString()+"\n";
        String stop_address_info;
        if(stop_detail!=null)
            stop_address_info="停："+stop_detail.getAddress()+"\n";
        else
            stop_address_info = "停："+stop_address.getText().toString()+"\n";

        String destination_address_info;
        if(destination_detail!=null)
            destination_address_info="到："+destination_detail.getAddress()+"\n";
        else
            destination_address_info = "到："+destination_address.getText().toString()+"\n";


        String type = "";
        if (orderCargoType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE)
            type = getString(R.string.client_take_ride_title);
        else if (orderCargoType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)
            type = getString(R.string.client_train_pick_up);
        else if (orderCargoType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)
            type = getString(R.string.client_airplane_pick_up);
        else if (orderCargoType == Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)
            type = getString(R.string.client_merchanse_send_title);

        String orderType = "";
        if(realtime.isChecked())
            orderType = "時間：即時"+"\n";
        else
            orderType = "時間："+time.getText().toString()+"\n";

        String spec = "";

        if(!spec_list.isEmpty())
        {
            spec = "特殊需求：";
            for(ClientTakeRideSelectSpecListItem item:spec_list){
                spec+=item.book_title+",";
            }
            spec = spec.substring(0,spec.length()-1)+"\n";


        }else
            spec = "特殊需求："+"\n";

        String markdetail="";
        //if(!remark.getText().toString().equals(""))
            markdetail = "備註："+remark.getText().toString();
        Utility info = new Utility(getActivity());
        AccountInfo user = info.getAccountInfo();

        String merchandiseInfo = "";
        if (orderCargoType.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE) && !merchandise_content.getText().toString().equals("") && !merchandise_unit.getText().toString().equals(""))
        {
            merchandiseInfo = "\n"+getString(R.string.merchandise_content)+merchandise_content.getText().toString()+"\n"+getString(R.string.merchandise_weight)+merchandise_weight.getText().toString()+"\n"+getString(R.string.merchandise_unit)+merchandise_unit.getText().toString();
        }
        String data = "訂單類型:"+type+"\n"+"客戶電話:"+user.getPhoneNumber()+"\n"+orderType+departure_address_info+stop_address_info+destination_address_info+spec+markdetail+merchandiseInfo;
       /* if (option == Fragment_TakeRide.SEND_MERCHANDISE) {
          if(dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI) {
                    parserAddressToGPS();
                    createTaxiOrder("" + option, "0", "0");
                }else
                    provideOrderPrice();
        }else
            confirmAlert(data);*/
        confirmAlert(data);

    }

    private String getTrainStationZip(String address) {

        Geocoder fwdGeocoder = new Geocoder(getActivity());

        String zipCode = "";
        String departure = address;


        List<Address> departure_locations = null;
        try {
            departure_locations = fwdGeocoder.getFromLocationName(departure, 10);
        } catch (IOException e) {
        }


        zipCode = departure_locations.get(0).getPostalCode();



        return zipCode;
    }
    private void choiceSpecFilter() {
        //specData.clear();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View login_view = inflater.inflate(R.layout.client_take_ride_spec_item, null);
        ListView lv = (ListView) login_view.findViewById(R.id.listviewicon);
        Button btn_ok = (Button) login_view.findViewById(R.id.button_category_ok);
        Button btn_cancel = (Button) login_view.findViewById(R.id.button_category_cancel);
        btn_ok.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        if (choiceSpecAlertDialog == null) {
            choiceSpecAlertDialog = new AlertDialog.Builder(getActivity()).create();

            //Resources res = getResources();
            //final String[] status = res.getStringArray(R.array.client_take_ride_requirement);
            // Change MyActivity.this and myListOfItems to your own values
            if (listViewAdapter == null) {
                getDataFromDB();
                listViewAdapter = new ClientTakeRideSelectSpecListItemAdapter(getActivity(), 0, mCommentListData);
                lv.setAdapter(listViewAdapter);
            } else {
                lv.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    ClientTakeRideSelectSpecListItem clientTakeRideSelectSpecListItem = mCommentListData.get(position);
                    clientTakeRideSelectSpecListItem.check = !clientTakeRideSelectSpecListItem.check;
                    if (clientTakeRideSelectSpecListItem.check) {
                        if(!spec_list.contains(clientTakeRideSelectSpecListItem))
                        {
                            spec_list.add(clientTakeRideSelectSpecListItem);
                        }
                       // status_selectedItem = position;
                    }else
                        spec_list.remove(clientTakeRideSelectSpecListItem);

                    mCommentListData.set(position, clientTakeRideSelectSpecListItem);
                    /*for (int i = 0; i < mCommentListData.size(); i++) {
                        if (i != position) {

                            ClientTakeRideSelectSpecListItem clientTakeRideSelectSpecListItemOther = mCommentListData.get(i);

                            clientTakeRideSelectSpecListItemOther.check = false;
                            mCommentListData.set(i, clientTakeRideSelectSpecListItemOther);
                        }

                    }*/
                    listViewAdapter.notifyDataSetChanged();
                }
            });
            choiceSpecAlertDialog.setView(login_view);

            btn_ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //show_target_event_status.setText(status[status_selectedItem]);

                    String content = "";
                    for(int i = 0; i < spec_list.size(); i++)
                    {
                            content+=spec_list.get(i).book_title;
                            if(i != spec_list.size() -1)
                            content += ",";
                    }

                    Log.e(TAG,"Spec:"+content);
                    spec_value.setText(content);
                    choiceSpecAlertDialog.dismiss();
                    choiceSpecAlertDialog = null;
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    for(int i = 0; i < mCommentListData.size(); i++)
                    {
                        ClientTakeRideSelectSpecListItem clientTakeRideSelectSpecListItem = mCommentListData.get(i);
                        clientTakeRideSelectSpecListItem.check = false;
                        mCommentListData.set(i, clientTakeRideSelectSpecListItem);

                    }
                    listViewAdapter.notifyDataSetChanged();
                    choiceSpecAlertDialog.dismiss();
                    choiceSpecAlertDialog = null;
                }
            });

            choiceSpecAlertDialog.setCancelable(false);
            choiceSpecAlertDialog.show();
        }

    }


    private void confirmAlert(String content)
    {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

        // set dialog message
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enter_alertDialog.cancel();
                        enter_alertDialog = null;
                    }
                })
                .setNegativeButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (dataType == Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI) {
                            //if (option == TAKE_RIDE) {
                            //if (departure_detail.getLongitude()==0)
                            enter_alertDialog.cancel();
                            enter_alertDialog = null;
                            parserAddressToGPS();
                            createTaxiOrder("" + option, "0", "0");
                            //}
                            //else
                            //   createMechardiseOrder("" + option);
                        }else
                        {

                            enter_alertDialog.cancel();
                            enter_alertDialog = null;
                            if(dialog != null)
                            {
                                dialog.cancel();
                                dialog = null;
                            }
                            //讓使用者填入價錢和小費
                            provideOrderPrice();
                        }

                    }
                });

        // create alert dialog
        if(enter_alertDialog == null) {
            enter_alertDialog = alertDialogBuilder.create();
            // show it
            enter_alertDialog.show();
        }

    }
    private void alert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set title
        alertDialogBuilder.setTitle(getString(R.string.order));

            alertDialogBuilder
                    .setMessage(getString(R.string.login_error_register_msg))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                              if(destination_address.getText().toString().isEmpty())
                              {
                                  destination_address.setHint(getString(R.string.order_destination_alert));
                                  destination_address.setHintTextColor(Color.RED);
                                  destination_address.requestFocus();


                              }

                              if(departure_address.getText().toString().isEmpty())
                              {
                                  departure_address.setHint(getString(R.string.order_departure_alert));
                                  departure_address.setHintTextColor(Color.RED);
                                  departure_address.requestFocus();
                              }
                        }
                    });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.DEPARTURE_QUERY_GPS:
                ArrayList<Address> locationInfo = null;
                if (data != null) {
                    //departure_detail = new LocationAddress();
                    departure_detail = (LocationAddress) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    //String add_bookmark = data.getStringExtra(BUNDLE_KEEP_BOOMARK);
                    //String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    //String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    //departure_detail.setLatitude(location.getLatitude());
                    //departure_detail.setLongitude(location.getLongitude());
                    boolean isBookMark = data.getBooleanExtra(BUNDLE_KEEP_BOOMARK, false);
                    if (isBookMark) {
                        addUserLocationToBookMark(departure_detail);
                    }
                    departure_info = departure_detail.getAddress();
                    departure_address.setText(departure_info);
                    //departure_detail.setLocation(location.getAddress());
                    //departure_detail.setAddress(location.getAddress());
                    /*if(add_bookmark.equals(true))
                    {
                        //新增到資料庫的BookMark
                    }*/
                    //showAddressList((ArrayList<Address>)locationInfo,departure_address,departure_detail);
                }
                break;
            case Constants.DESTINATION_QUERY_GPS:
                ArrayList<Address> locationInfo2 = null;
                if (data != null) {
                    //destination_detail = new LocationAddress();
                    destination_detail = (LocationAddress) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    //String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    //String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    //destination_detail.setLatitude(Double.valueOf(latitude));
                    //destination_detail.setLongitude(Double.valueOf(longitude));
                    boolean isBookMark = data.getBooleanExtra(BUNDLE_KEEP_BOOMARK, false);
                    if (isBookMark) {
                        addUserLocationToBookMark(destination_detail);
                    }

                    destination_info = destination_detail.getAddress();
                    destination_address.setText(destination_info);
                    //destination_detail.setAddress(locationInfo2.get(0).getAddressLine(0));
                    //destination_detail.setLocation(locationInfo2.get(0).getAddressLine(0));
                    //showAddressList(locationInfo2,destination_address,destination_detail);
                }
                break;

            case Constants.STOP_QUERY_GPS:
                ArrayList<Address> locationInfo3 = null;
                if (data != null) {
                    //destination_detail = new LocationAddress();
                    stop_detail = (LocationAddress) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    //String latitude = data.getStringExtra(Constants.BUNDLE_MAP_LATITUDE);
                    //String longitude = data.getStringExtra(Constants.BUNDLE_MAP_LONGITUDE);
                    //destination_detail.setLatitude(Double.valueOf(latitude));
                    //destination_detail.setLongitude(Double.valueOf(longitude));
                    boolean isBookMark = data.getBooleanExtra(BUNDLE_KEEP_BOOMARK, false);
                    if (isBookMark) {
                        addUserLocationToBookMark(stop_detail);
                    }
                    stop_info = stop_detail.getAddress();
                    stop_address.setText(stop_info);
                    //destination_detail.setAddress(locationInfo2.get(0).getAddressLine(0));
                    //destination_detail.setLocation(locationInfo2.get(0).getAddressLine(0));
                    //showAddressList(locationInfo2,destination_address,destination_detail);
                }
                break;

            case Constants.DEPARTURE_QUERY_BOOKMARK:
                if (data != null) {
                    departure_detail = new LocationAddress();
                    USerBookmark uSerBookmark = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    departure_address.setText(uSerBookmark.getStreetAddress());
                    departure_detail.setLongitude(Double.parseDouble(uSerBookmark.getLng()));
                    departure_detail.setLatitude(Double.parseDouble(uSerBookmark.getLat()));
                    departure_detail.setAddress(uSerBookmark.getStreetAddress());
                    departure_detail.setLocation(uSerBookmark.getLocation());
                    departure_detail.setCountryName(uSerBookmark.getCountryName());
                    departure_detail.setLocality(uSerBookmark.getLocality());
                    departure_detail.setZipCode(uSerBookmark.getZipCode());
                    departure_info = uSerBookmark.getStreetAddress();
                }
                break;
            case Constants.STOP_QUERY_BOOKMARK:
                if (data != null) {
                    stop_detail = new LocationAddress();
                    USerBookmark uSerBookmark = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    stop_address.setText(uSerBookmark.getStreetAddress());
                    stop_detail.setLongitude(Double.parseDouble(uSerBookmark.getLng()));
                    stop_detail.setLatitude(Double.parseDouble(uSerBookmark.getLat()));
                    stop_detail.setAddress(uSerBookmark.getStreetAddress());
                    stop_detail.setLocation(uSerBookmark.getLocation());
                    stop_detail.setCountryName(uSerBookmark.getCountryName());
                    stop_detail.setLocality(uSerBookmark.getLocality());
                    stop_detail.setZipCode(uSerBookmark.getZipCode());
                    stop_info = uSerBookmark.getStreetAddress();

                }
                break;
            case Constants.DESTINATION_QUERY_BOOKMARK:
                if (data != null) {
                    destination_detail = new LocationAddress();
                    USerBookmark uSerBookmark1 = (USerBookmark) data.getSerializableExtra(Constants.BUNDLE_LOCATION);
                    destination_address.setText(uSerBookmark1.getStreetAddress());
                    destination_detail.setLongitude(Double.parseDouble(uSerBookmark1.getLng()));
                    destination_detail.setLatitude(Double.parseDouble(uSerBookmark1.getLat()));
                    destination_detail.setAddress(uSerBookmark1.getStreetAddress());
                    destination_detail.setLocation(uSerBookmark1.getLocation());
                    destination_detail.setCountryName(uSerBookmark1.getCountryName());
                    destination_detail.setLocality(uSerBookmark1.getLocality());
                    destination_detail.setZipCode(uSerBookmark1.getZipCode());
                    destination_info = uSerBookmark1.getStreetAddress();

                }
                break;
        }
    }

    //顯示車站資料
    private void getLocationFromDB(String filterName) {
        trainStationList  = new ArrayList<ServerBookmark>();
        ArrayList<String> stationInfo = new ArrayList<String>();
        RealmUtil database = new RealmUtil(getActivity());
        RealmResults<ServerBookmark> tainList= database.queryServerBookmark();
        for (ServerBookmark station:tainList)
        {
            if(station.getLocation().contains(filterName)) {
                stationInfo.add(station.getLocation());
                trainStationList.add(station);
            }
        }


        ArrayAdapter arrayAdapter_location = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, stationInfo);
        ArrayAdapter arrayAdapter_departure= new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, stationInfo);
        spinner_departure.setAdapter(arrayAdapter_location);
        spinner_destination.setAdapter(arrayAdapter_departure);
    }

    private void calculateMerchandiseUnit()
    {

        if(dialog==null) {
            dialog = new Dialog(getActivity());
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.setTitle(getString(R.string.unit_title));
            dialog.setContentView(R.layout.dialog_enter_merchandise_unit_layout);

            //TextView content = (TextView) dialog.findViewById(R.id.txt_msg);
            final EditText unitLength = (EditText) dialog.findViewById(R.id.merchandise_content_length);

            final EditText unitWidth = (EditText) dialog.findViewById(R.id.merchandise_content_width);
            final EditText unitHigh = (EditText) dialog.findViewById(R.id.merchandise_content_high);


            Button sure = (Button) dialog.findViewById(R.id.sure_action);
            sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    long sum = 0l;
                    if(!(unitLength.getText().toString().equals("") && unitLength.getText().toString().equals("") && unitLength.getText().toString().equals("")))
                    {
                        long len = Long.parseLong(unitLength.getText().toString());
                        long width = Long.parseLong(unitWidth.getText().toString());
                        long high = Long.parseLong(unitHigh.getText().toString());
                       sum = len * width * high / 28317;
                       Log.e(TAG,String.valueOf(Math.round(sum)));
                       merchandise_unit.setText(String.valueOf(Math.round(sum)) + getActivity().getString(R.string.unit_total_count));

                    }else
                        merchandise_unit.setText("0才");

                         dialog.cancel();
                    dialog = null;
                }
            });

            Button cancel = (Button) dialog.findViewById(R.id.cancel_action);
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

        }
        dialog.show();
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int currentDataPosition);
        void onFragmentCreated(Fragment_TakeRide demoFragment);
        void onFragmentResumed(Fragment_TakeRide demoFragment);
    }
}
