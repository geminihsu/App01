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

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.delegate.Fragment_AccountDelegateBase;
import tw.com.geminihsu.app01.delegate.customer.Fragment_AccountDelegateCustomer;
import tw.com.geminihsu.app01.delegate.driver.Fragment_AccountDelegateDriver;


public class Fragment_Account extends Fragment {

    private ListView listView;
    //actionBar item Id
    private final int ACTIONBAR_MENU_ITEM_LOGOUT = 0x0001;

    private Fragment_AccountDelegateBase viewDelegateBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        //if (savedInstanceState != null) {
         //   mCurrentPosition = savedInstanceState.getInt(Constants.ARG_POSITION);
        //}


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Constants.Driver) {

            viewDelegateBase = new Fragment_AccountDelegateDriver(this);
        } else {

            viewDelegateBase = new Fragment_AccountDelegateCustomer(this);
        }
        this.findViews();
        this.setLister();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
    @Override
    public void onResume() {
        getActivity().setTitle(getString(R.string.myaccount_page_title));
        super.onResume();


    }
    
    @Override
	public void onStop() {
		super.onStop();

	}

	private void findViews()
    {
        listView = (ListView) getView().findViewById(R.id.listView1);


        String[] menu=viewDelegateBase.setListData();
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < menu.length; ++i) {
            list.add(menu[i]);
        }
        ListAdapter adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 ,menu);

        listView.setAdapter(adapter);

    }

    private void setLister(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                viewDelegateBase.listViewOnItemClickListener(parent,v,position,id);

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(Menu.NONE, ACTIONBAR_MENU_ITEM_LOGOUT, Menu.NONE, getString(R.string.btn_logout));
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
        item.setTitle(spanString);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case ACTIONBAR_MENU_ITEM_LOGOUT:
                //將表單資料送出後回到主畫面
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

	



}