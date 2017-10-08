package tw.com.geminihsu.app01;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import io.realm.RealmResults;
import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.bean.NormalOrder;
import tw.com.geminihsu.app01.bean.OrderLocationBean;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.serverbean.ServerSpecial;
import tw.com.geminihsu.app01.utils.DateTimeUtil;
import tw.com.geminihsu.app01.utils.JsonPutsUtil;
import tw.com.geminihsu.app01.utils.RealmUtil;
import tw.com.geminihsu.app01.utils.ThreadPoolUtil;
import tw.com.geminihsu.app01.utils.Utility;

public class SendMerchandiseActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;
    final public static int CLIENT_SEND_MERCHANDISE = 1;
    private LinearLayout target;

    private int choice = 0;
    private NormalOrder order;
    private JsonPutsUtil sendDataRequest;
    private ProgressDialog progressDialog_loading;
    private EditText client_name;
    private EditText client_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_merchandise_order);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        sendDataRequest = new JsonPutsUtil(SendMerchandiseActivity.this);
        sendDataRequest.setServerRequestOrderManagerCallBackFunction(new JsonPutsUtil.ServerRequestOrderManagerCallBackFunction() {

            @Override
            public void createNormalOrder(NormalOrder order) {

                /*Intent question = new Intent(SendMerchandiseActivity.this, ClientTakeRideSearchActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, ClientTakeRideSearchActivity.DRIVER_REPORT_PRICE);
                question.putExtras(b);
                startActivity(question);*/
                if(progressDialog_loading!=null)
                {
                    progressDialog_loading.cancel();
                    progressDialog_loading = null;
                }

                Intent intent = new Intent(getApplicationContext(), ClientTakeRideSearchActivity.class);

                Bundle b = new Bundle();
                b.putInt(Constants.ARG_POSITION, Integer.valueOf(order.getTicket_id()));
                intent.putExtras(b);
                startActivity(intent);
                //finish();

            }

            @Override
            public void cancelNormalOrder(NormalOrder order) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
                choice = bundle.getInt(Constants.ARG_POSITION);
                order = (NormalOrder)bundle.getSerializable(ClientTakeRideActivity.BUNDLE_ORDER_TICKET_ID);

                displayLayout();
            }else
            {
                //Error!!!!
            }
        }
        else
        {
            //Error!!!!
        }
        this.setLister();




    }

    private void findViews()
    {
        target = (LinearLayout) findViewById(R.id.txt_target);
        client_name = (EditText) findViewById(R.id.client_name);
        client_phone = (EditText) findViewById(R.id.client_phone);
        client_phone.addTextChangedListener(checkPhoneFormat);

    }

    private TextWatcher checkPhoneFormat= new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            if (s.length()<10 || s.length()>10) {
                client_phone.setError(getString(R.string.login_error_register_msg));
            }
        }
    };



    private void setLister()
    {

    }

    private void displayLayout() {
        /*if (choice == CLIENT_SEND_MERCHANDISE) {
            target.setVisibility(View.GONE);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.menu_take));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_SUMMIT:

                String name = client_name.getText().toString();
                String cellphone = client_phone.getText().toString();
                if(name.isEmpty()||cellphone.isEmpty())
                {
                    alert();
                }else {
                    Utility info = new Utility(this);
                    AccountInfo user = info.getAccountInfo();

                    String stop = "";
                    if (order.getStop_address() != null && !order.getStop_address().equals(""))
                        stop = "停：" + order.getStop_address() + "\n";
                    else
                        stop = "停：" + "\n";

                    String spec = "";

                    if (order.getCar_special() != null && !order.getCar_special().equals(""))
                    {
                        String[] list = order.getCar_special().split(",");

                        if(list.length == 0)
                        {
                            list = new String[1];
                            list[0] = order.getCar_special();
                        }
                        RealmUtil data = new RealmUtil(this);
                        RealmResults<ServerSpecial> specials= data.queryServerSpecial(Constants.SERVER_SPECIAL,"1");

                        for(int i = 0; i <specials.size(); i++)
                        {
                            ServerSpecial special = specials.get(i);
                            for(int j = 0; j < list.length; j++)
                            {
                                if(special.getId().equals(list[j]))
                                    spec+=special.getContent() +",";
                            }

                        }
                        spec = spec.substring(0,spec.length()-1);
                        spec = "特殊需求：" + spec + "\n";
                    }else
                        spec = "特殊需求：" + "\n";

                    String mark = "";

                    if (!client_name.getText().toString().equals("") && !client_phone.getText().toString().equals(""))
                        order.setRemark(order.getRemark() + "\n" + "收貨人姓名:" + client_name.getText().toString() + "\n" + "收貨人電話:" + client_phone.getText().toString() + "\n");
                    if (order.getRemark() != null && !order.getRemark().equals(""))
                        mark = "備註：" + order.getRemark() + "\n";
                    else
                        mark = "備註：" + "\n";


                    String data = "訂單類型:" + getString(R.string.client_merchanse_send_title) + "\n" + "客戶電話:" + user.getPhoneNumber() + "\n" + "時間：" + "即時" + "\n" + "從：" + order.getBegin_address() + "\n" + stop + "到：" + order.getEnd_address() + "\n" + spec + mark;

                    confirmAlert(data);
                }
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createMechardiseOrder(String target)
    {
        Utility info = new Utility(SendMerchandiseActivity.this);

        AccountInfo driver = info.getAccountInfo();
        OrderLocationBean begin_location = new OrderLocationBean();
        begin_location.setId(1);
        begin_location.setLatitude("24.09133");
        begin_location.setLongitude("120.540315");
        begin_location.setZipcode("404");
        begin_location.setAddress("台中市北區市政路172號");

        OrderLocationBean stop_location = new OrderLocationBean();
        stop_location.setId(2);
        stop_location.setLatitude("24.14411");
        stop_location.setLongitude("120.679567");
        stop_location.setZipcode("400");
        stop_location.setAddress("台中市中區柳川里成功路300號");


        OrderLocationBean end_location = new OrderLocationBean();
        end_location.setId(3);
        end_location.setLatitude("24.14411");
        end_location.setLongitude("120.679567");
        end_location.setZipcode("400");
        end_location.setAddress("台中市南區興大路145號");



        NormalOrder order = new NormalOrder();
        order.setUser_id(driver.getId());
        order.setUser_uid(driver.getUid());
        order.setUser_name(driver.getPhoneNumber());
        order.setAccesskey(driver.getAccessKey());
        order.setBegin(begin_location);
        order.setEnd(end_location);
        order.setDtype("4");
        order.setBegin_address(begin_location.getAddress());
        order.setStop_address(stop_location.getAddress());
        order.setEnd_address(end_location.getAddress());
        order.setPrice("1000");
        order.setTip("0");
        order.setTicket_status("0");
        //order.setOrderdate(time.getText().toString());
        order.setTarget(target);

        //sendDataRequest.putCreateNormalOrder(order);

    }

    private void confirmAlert(String content)
    {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(getString(R.string.menu_dialog_sure));

        // set dialog message
        alertDialogBuilder
                .setMessage(content)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.cancel_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(getString(R.string.sure_take_spec), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (progressDialog_loading == null) {
                            progressDialog_loading = ProgressDialog.show(SendMerchandiseActivity.this, "",
                                    "Loading. Please wait...", true);
                        }
                        ThreadPoolUtil.getThreadPoolExecutor().execute((new Runnable() {
                            @Override
                            public void run() {
                                sendDataRequest.putCreateNormalOrder(order);
                            }
                        }));

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle(getString(R.string.order));



        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.login_error_msg))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.dialog_get_on_car_comfirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
}
