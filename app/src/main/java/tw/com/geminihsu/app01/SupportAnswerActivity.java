package tw.com.geminihsu.app01;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.webview.*;

public class SupportAnswerActivity extends Activity {

    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_SUMMIT = 0x0001;

    private WebView browser;
    private LinearLayout linearLayout_form;

    final public static int QUESTION = 0;

    final public static int CLAUSE = 1;
    final public static int SUGGESTION= 2;

    private int choice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_answer_activity);
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
                choice = bundle.getInt(Constants.ARG_POSITION);
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
        browser = (WebView) findViewById(R.id.webview);
        browser.setWebViewClient(new MyBrowser("www.google.com"));
        linearLayout_form = (LinearLayout) findViewById(R.id.form);

        /*String url = "www.google.com";

        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.loadUrl(url);*/
    }



    private void displayLayout()
    {
        if(choice == SupportAnswerActivity.SUGGESTION)
        {
            getActionBar().setTitle(getString(R.string.suggestion_page_title));
            linearLayout_form.setVisibility(View.VISIBLE);
        }
        else if(choice == SupportAnswerActivity.CLAUSE)
            getActionBar().setTitle(getString(R.string.clause_page_title));
        else {
            getActionBar().setTitle(getString(R.string.question_page_title));
            browser.setVisibility(View.VISIBLE);
        }
    }

    private void setLister()
    {
        browser.loadUrl("http://www.tutorialspoint.com");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(choice== SupportAnswerActivity.SUGGESTION){
            MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_SUMMIT, Menu.NONE, getString(R.string.btn_send));
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
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
