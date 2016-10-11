package tw.com.geminihsu.app01.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.R;

public class Fragment_BeginOrder extends Fragment {

    private TabLayoutSetupCallback mToolbarSetupCallback;
    private List<String> mTabNamesList;
    private final int ACTIONBAR_MENU_ITEM_FIILTER = 0x0001;

    public Fragment_BeginOrder() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MenuMainActivity) {
            mToolbarSetupCallback = (TabLayoutSetupCallback) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement TabLayoutSetupCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mTabNamesList = new ArrayList<>();
        mTabNamesList.add(getString(R.string.tab_real_time));
        mTabNamesList.add(getString(R.string.tab_reservation));
        mTabNamesList.add(getString(R.string.tab_company));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beginorder, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ItemsPagerAdapter(getChildFragmentManager(), mTabNamesList));
        mToolbarSetupCallback.setupTabLayout(viewPager);

        return view;
    }
    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.begin_order_page_title));
        super.onResume();


    }

    public static class ItemsPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> mTabs = new ArrayList<>();

        public ItemsPagerAdapter(FragmentManager fm, List<String> tabNames) {
            super(fm);

            mTabs = tabNames;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment_BeginOrderInteractive.newInstance();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position);
        }
    }

    public interface TabLayoutSetupCallback {
        void setupTabLayout(ViewPager viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_FIILTER, Menu.NONE, getString(R.string.order_filter_page_title));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_FIILTER:
                //將表單資料送出後回到主畫面
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
