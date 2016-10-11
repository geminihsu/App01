package tw.com.geminihsu.app01;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tw.com.geminihsu.app01.fragment.Fragment_About;
import tw.com.geminihsu.app01.fragment.Fragment_Account;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrder;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderInteractive;
import tw.com.geminihsu.app01.fragment.Fragment_OrderFilter;
import tw.com.geminihsu.app01.fragment.Fragment_OrderRecord;
import tw.com.geminihsu.app01.fragment.Fragment_Bouns;
import tw.com.geminihsu.app01.fragment.Fragment_Service;
import tw.com.geminihsu.app01.fragment.Fragment_Support;
import tw.com.geminihsu.app01.tw.com.geminihsu.app01.common.Constants;

public class MenuMainActivity extends AppCompatActivity implements Fragment_BeginOrder.TabLayoutSetupCallback,
        Fragment_BeginOrderInteractive.OnListItemClickListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView navigationView;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity_main);


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.findViews();
        this.setLister();

    }

    private void findViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        //calling sync state is necessary or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setCheckedItem(0);
    }

    private void setLister()
    {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_begin:
                        Fragment_BeginOrder begin = new Fragment_BeginOrder();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_begin = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_begin.replace(R.id.container, begin);
                        fragmentTransaction_begin.commit();

                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_order_filter:
                        Fragment_OrderFilter filter = new Fragment_OrderFilter();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_filter = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction_filter.replace(R.id.container, filter);
                        fragmentTransaction_filter.commit();

                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_wait:
                        // Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show();
                        Fragment_BeginOrderInteractive wait = new Fragment_BeginOrderInteractive();
                        android.support.v4.app.FragmentTransaction fragmentTransaction_wait = getSupportFragmentManager().beginTransaction();

                        Bundle data = new Bundle();
                        data.putBoolean(Constants.ARG_POSITION, true);
                        wait.setArguments(data);

                        fragmentTransaction_wait.replace(R.id.container, wait);
                        fragmentTransaction_wait.commit();

                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_order_past:

                        return true;

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.navigation_item_order:
                        // Toast.makeText(getApplicationContext(),"Inbox Selected",Toast.LENGTH_SHORT).show();
                        Fragment_Service fragment = new Fragment_Service();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container, fragment);
                        fragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.navigation_item_order_record:
                        //Toast.makeText(getApplicationContext(),"Stared Selected",Toast.LENGTH_SHORT).show();
                        Fragment_OrderRecord order_history = new Fragment_OrderRecord();
                        android.support.v4.app.FragmentTransaction order_historyTransaction = getSupportFragmentManager().beginTransaction();
                        order_historyTransaction.replace(R.id.container, order_history);
                        order_historyTransaction.commit();

                        return true;
                    case R.id.navigation_item_support:
                        Fragment_Support support = new Fragment_Support();
                        android.support.v4.app.FragmentTransaction support_historyTransaction = getSupportFragmentManager().beginTransaction();
                        support_historyTransaction.replace(R.id.container, support);
                        support_historyTransaction.commit();
                        return true;
                    case R.id.navigation_item_notification:
                        Toast.makeText(getApplicationContext(), "Drafts Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_item_about:
                        Fragment_About about = new Fragment_About();
                        android.support.v4.app.FragmentTransaction about_historyTransaction = getSupportFragmentManager().beginTransaction();
                        about_historyTransaction.replace(R.id.container, about);
                        about_historyTransaction.commit();
                        return true;
                    case R.id.navigation_item_share:

                        // MenuItem item = menuItem.getSubMenu().findItem(R.id.navigation_item_share);
                        // Fetch and store ShareActionProvider
                        ;


                        mShareActionProvider =(ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
                        setShareIntent(createShareIntent());


                       /* Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg"); // might be text, sound, whatever
                        //share.putExtra(Intent.EXTRA_STREAM, pathToPicture);
                        startActivity(Intent.createChooser(share, "share"));*/
                        return true;
                    case R.id.navigation_item_account:
                        Fragment_Account account = new Fragment_Account();
                        android.support.v4.app.FragmentTransaction account_historyTransaction = getSupportFragmentManager().beginTransaction();
                        account_historyTransaction.replace(R.id.container, account);
                        account_historyTransaction.commit();
                        return true;
                    case R.id.navigation_item_bouns:
                        Fragment_Bouns bouns = new Fragment_Bouns();
                        android.support.v4.app.FragmentTransaction bounsTransaction = getSupportFragmentManager().beginTransaction();
                        bounsTransaction.replace(R.id.container, bouns);
                        bounsTransaction.commit();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
    }
        @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "http://stackandroid.com");
        return shareIntent;
    }

    @Override
    public void setupTabLayout(ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onListItemClick(View v) {
        //Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        Button take_prize = (Button) v.findViewById(R.id.btn_take);

       /* take_prize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent question = new Intent(MenuMainActivity.this, BoundsRecordActivity.class);
                Bundle b = new Bundle();
                question.putExtras(b);
                startActivity(question);
            }
        });*/
       /* Intent question = new Intent(MenuMainActivity.this, MerchandiseOrderActivity.class);
        Bundle b = new Bundle();
        b.putInt(Constants.ARG_POSITION,Constants.QUERY_MERCHANDISE);
        question.putExtras(b);
        startActivity(question);*/

        Intent question = new Intent(MenuMainActivity.this, OrderProcesssActivity.class);

        startActivity(question);
    }
}