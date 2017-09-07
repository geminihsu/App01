/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tw.com.geminihsu.app01.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.ClientTakeRideActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.SupportAnswerActivity;
import tw.com.geminihsu.app01.adapter.BeginOrderListItem;
import tw.com.geminihsu.app01.adapter.BeginOrderListItemAdapter;
import tw.com.geminihsu.app01.adapter.OrderRecordListItem;
import tw.com.geminihsu.app01.adapter.OrderRecordListItemAdapter;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class Fragment_OrderRecord extends Fragment {

    private final String TAG = Fragment_OrderRecord.class.toString();
    private ListView listView;
    private final List<OrderRecordListItem> mRecordOrderListData = new ArrayList<OrderRecordListItem>();;
    private OrderRecordListItemAdapter listViewAdapter;


    private JsonPutsUtil sendDataRequest;
    private ArrayList<NormalOrder> orders;
    private ProgressDialog progressDialog_loading;
    private SwipeRefreshLayout loadOrderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        //if (savedInstanceState != null) {
         //   mCurrentPosition = savedInstanceState.getInt(Constants.ARG_POSITION);
        //}

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_record, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();
        this.findViews();
        setLister();
         //  getDataFromDB();
        //getOrderList();
        // 建立ListItemAdapter
        listViewAdapter = new OrderRecordListItemAdapter(getActivity(), 0, mRecordOrderListData);
        listView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();



    }

    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.order_record_page_title));
        super.onResume();
        if (progressDialog_loading==null) {
            progressDialog_loading = ProgressDialog.show(getActivity(), "",
                    "Loading. Please wait...", true);
        }
        //loadOrderList.setRefreshing(true);


        //sendDataRequest.queryRecommendOrderList(info.getAccountInfo());
        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
            @Override
            public void run() {
                if(getActivity()!=null) {
                    Utility info = new Utility(getActivity());
                    info.clearData(NormalOrder.class);
                    sendDataRequest.queryClientOrderList(info.getAccountInfo());
                }
            }
        }));

    }

    @Override
	public void onStop() {
		super.onStop();

	}


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
       // outState.putInt(Constants.ARG_POSITION, mCurrentPosition);
    }


    private void findViews() {

        listView = (ListView) getView().findViewById(R.id.listView1);
        loadOrderList = (SwipeRefreshLayout) getView().findViewById(R.id.refreshlayout);

        // 設定所有view 的font size
        // View main_layout = (View) getView().findViewById(R.id.main_layout);
        // DisplayUtil displayUtil = new DisplayUtil();
        // displayUtil.setFontSize(main_layout,
        // getResources().getDimension(R.dimen.default_text_size_px));
    }

    private void setLister()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final OrderRecordListItem order= mRecordOrderListData.get(position);


            }
        });

        loadOrderList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                /*progressDialog_loading = ProgressDialog.show(getActivity(), "",
                        "Loading. Please wait...", true);*/
                final Utility info = new Utility(getActivity());
                info.clearData(NormalOrder.class);
                //sendDataRequest.queryRecommendOrderList(info.getAccountInfo());
                ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable(){
                    @Override
                    public void run() {
                        if(getActivity()!=null) {
                            Utility info = new Utility(getActivity());
                            info.clearData(NormalOrder.class);
                            sendDataRequest.queryClientOrderList(info.getAccountInfo());
                        }
                    }
                }));

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.findViews();
        orders = new ArrayList<NormalOrder>();
        sendDataRequest = new JsonPutsUtil(getActivity());

        sendDataRequest.setClientQueryOrderListManagerCallBackFunction(new JsonPutsUtil.ClientQueryOrderListManagerCallBackFunction() {

            @Override
            public void getWaitOrderListSuccess(RealmResults<NormalOrder> orders) {
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading=null;
                }
                loadOrderList.setRefreshing(false);
                getOrderList(orders);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void getOrderListSuccess(RealmResults<NormalOrder> orders) {
                if(progressDialog_loading!=null) {
                    progressDialog_loading.dismiss();
                    progressDialog_loading=null;
                }
                //getDataFromServer(orders,option);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void getOrderListFail(boolean error,String message) {
                if(error)
                {
                    loadOrderList.setRefreshing(false);
                    if(progressDialog_loading!=null) {
                        progressDialog_loading.dismiss();
                        progressDialog_loading=null;
                    }
                    if(message.equals("836"))
                    {
                        Utility info = new Utility(getActivity());
                        if(info.getAccountInfo().getDriver_type().equals("0"))
                            Toast.makeText(getActivity(), "目前暫停營業中", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), "司機身份已送出審核", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

    }

    private void getOrderList(RealmResults<NormalOrder> orders) {
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_taxi);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_airport);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_local_shipping);


        mRecordOrderListData.clear();
        try {
            // GeoDeviceManagement.deviceList = new ArrayList<UpnpSearchResultBean>();
            // GeoDeviceManagement.deviceList.clear();
            for (NormalOrder order : orders) {


                //Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_online);
                OrderRecordListItem item = new OrderRecordListItem();
                Constants.APP_REGISTER_DRIVER_TYPE Dtype = Constants.conversion_register_driver_account_result(Integer.valueOf(order.getDtype()));
                Log.e(TAG,"type:"+Dtype.value());
                //Constants.APP_REGISTER_ORDER_TYPE order_type = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(order.getDtype()));


                if(Dtype.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TAXI)||Dtype.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_UBER)) {
                    item.image = bm1;
                    //item.car_status = "一般搭乘";
                    item.car_status_Visibility = View.VISIBLE;
                }else
                if(Dtype.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_CARGO)||Dtype.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRUCK)||Dtype.equals(Constants.APP_REGISTER_DRIVER_TYPE.K_REGISTER_DRIVER_TYPE_TRAILER))
                {
                    item.image = bm3;
                    item.pay_method = "$"+order.getPrice()+"元";
                    item.car_status = "";
                }
                if(order.getPrice().equals("0"))
                    item.pay_method = "跳錶收費";
                else
                    item.pay_method = "價錢 $" + order.getPrice() +"元,小費 $ " + order.getTip()+"元";


                if(order.getTicket_status().equals("0"))
                    item.order_status = "等待中";
                else
                if(order.getTicket_status().equals("1"))
                    item.order_status = "進行中";
                else
                if(order.getTicket_status().equals("100"))
                    item.order_status = "已結案";


                item.order_status_fontColor = getResources().getColor(R.color.btn_bouns_upgrade);
                if(order.getTimebegin().equals("0"))
                    item.time = "即時";
                else
                    item.time = order.getOrderdate();
                item.departure = "從:"+order.getBegin_address();
                item.destination = "到:"+order.getEnd_address();
                item.normalOrder = order;

                Constants.APP_REGISTER_ORDER_TYPE type = Constants.conversion_create_new_order_cargo_type_result(Integer.valueOf(order.getCargo_type()));
                if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_SEND_MERCHANDISE)) {
                    item.car_status = "貨物快送";
                } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_TAKE_RIDE)) {
                    item.car_status = "一般搭乘";
                } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_AIRPORT)) {
                    item.car_status = "機場接送";
                } else if (type.equals(Constants.APP_REGISTER_ORDER_TYPE.K_REGISTER_ORDER_TYPE_PICK_UP_TRAIN)) {
                    item.car_status = "車站接送";
                }
                //item.car_status = "";
                mRecordOrderListData.add(item);

            }

        } catch (Throwable t) {
            Toast.makeText(getActivity(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}