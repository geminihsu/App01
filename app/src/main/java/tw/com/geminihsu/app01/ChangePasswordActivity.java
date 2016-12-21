package tw.com.geminihsu.app01;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import tw.com.geminihsu.app01.bean.AccountInfo;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.utils.Utility;

public class ChangePasswordActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private EditText name_edit;
    private EditText id_edit;
    private EditText phone_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constants.ARG_POSITION)){
               // choice = bundle.getInt(Constants.ARG_POSITION);
                findViews();
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
        //linearLayout_form = (LinearLayout) findViewById(R.id.form);

        /*String url = "www.google.com";

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(url);*/
        name_edit = (EditText) findViewById(R.id.name_edit);
        id_edit = (EditText) findViewById(R.id.id_edit);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        Utility account = new Utility(this);
        AccountInfo accountInfo = account.getAccountInfo();
        name_edit.setText(accountInfo.getName());
        name_edit.setEnabled(false);
        id_edit.setText(accountInfo.getIdentify());
        id_edit.setEnabled(false);
        phone_edit.setText(accountInfo.getPhoneNumber());
        phone_edit.setEnabled(false);



    }





    private void setLister()
    {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.driver_account_save));
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
                //將表單資料送出後回到主畫面
                this.finish();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
