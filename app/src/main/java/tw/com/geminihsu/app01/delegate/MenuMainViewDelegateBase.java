package tw.com.geminihsu.app01.delegate;

import tw.com.geminihsu.app01.DriverIdentityActivity;
import tw.com.geminihsu.app01.MainActivity;
import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.fragment.Fragment_About;
import tw.com.geminihsu.app01.fragment.Fragment_Account;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrderList;
import tw.com.geminihsu.app01.fragment.Fragment_Bouns;
import tw.com.geminihsu.app01.fragment.Fragment_Client_Service;
import tw.com.geminihsu.app01.fragment.Fragment_Client_SubService;
import tw.com.geminihsu.app01.fragment.Fragment_NotifyList;
import tw.com.geminihsu.app01.fragment.Fragment_OrderFilter;
import tw.com.geminihsu.app01.fragment.Fragment_OrderRecord;
import tw.com.geminihsu.app01.common.Constants;
import tw.com.geminihsu.app01.fragment.Fragment_Support;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class MenuMainViewDelegateBase extends BaseViewDelegate{
	protected MenuMainActivity mainActivity;

	public MenuMainViewDelegateBase(MenuMainActivity _mainActivity) {
		mainActivity = _mainActivity;
	}

	public void setContentLayoutFragment() {

	}


	public void setNavigationItemOnClick_beginOrder() {
		FragmentTransaction fragTran;
		Fragment_BeginOrderList frag2 = new Fragment_BeginOrderList();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_BeginOrderList.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_BeginOrderList.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_orderFilter() {
		FragmentTransaction fragTran;
		Fragment_OrderFilter frag2 = new Fragment_OrderFilter();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_OrderFilter.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_OrderFilter.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_waitOrder() {
		FragmentTransaction fragTran;
		Fragment_BeginOrderList frag2 = new Fragment_BeginOrderList();
		Bundle args2 = new Bundle();
		args2.putBoolean(Constants.ARG_POSITION, true);
		frag2.setArguments(args2);
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_BeginOrderList.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_BeginOrderList.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_pastOrder() {
		clearSubService();

		FragmentTransaction fragTran;
		Fragment_OrderRecord frag2 = new Fragment_OrderRecord();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_OrderRecord.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_OrderRecord.class.getSimpleName());
		fragTran.commit();
	}


	public void setNavigationItemOnClick_service() {


		//clearSubService();

		if(mainActivity != null) {
			if (mainActivity.getSupportFragmentManager().getBackStackEntryCount() != 0) {
				FragmentManager.BackStackEntry backEntry = mainActivity.getSupportFragmentManager().getBackStackEntryAt(mainActivity.getSupportFragmentManager().getBackStackEntryCount() - 1);
				String str = backEntry.getName();

				if (str != null) {
					if (str.equals(Fragment_Client_SubService.class.getSimpleName()))
						mainActivity.getSupportFragmentManager().popBackStack();
				}

			}
			int cnt = mainActivity.getSupportFragmentManager().getBackStackEntryCount();
			while (cnt > 0) {
				mainActivity.getSupportFragmentManager().popBackStack();
				cnt--;

			}

			FragmentTransaction fragTran;
			Fragment_Client_Service frag2 = new Fragment_Client_Service();
			fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
			fragTran.replace(R.id.container, frag2, Fragment_Client_Service.class.getSimpleName());
			fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragTran.addToBackStack(Fragment_Client_Service.class.getSimpleName());
			fragTran.commit();
		}

	}

	public void setNavigationItemOnClick_support() {


		clearSubService();

		FragmentTransaction fragTran;
		Fragment_Support frag2 = new Fragment_Support();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Support.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Support.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_notify() {
		clearSubService();

		FragmentTransaction fragTran;
		Fragment_NotifyList frag2 = new Fragment_NotifyList();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_NotifyList.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_NotifyList.class.getSimpleName());
		fragTran.commit();
	}
	
	public void setNavigationItemOnClick_about() {

		clearSubService();
		FragmentTransaction fragTran;
		Fragment_About frag2 = new Fragment_About();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_About.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_About.class.getSimpleName());
		fragTran.commit();
	}
    public void setNavigationItemOnClick__logOut() {
		SharedPreferences configSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);

		configSharedPreferences.edit().putString(mainActivity.getString(R.string.config_login_phone_number_key), "").commit();
		configSharedPreferences.edit().putString(mainActivity.getString(R.string.config_login_password_key), "").commit();

		Intent i = new Intent();
		i.setClass(mainActivity, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mainActivity.startActivity(i);
	}


	public void setNavigationItemOnClick_account() {
		clearSubService();
		FragmentTransaction fragTran;
		Fragment_Account frag2 = new Fragment_Account();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Account.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Account.class.getSimpleName());
		fragTran.commit();
	}

	public void setNavigationItemOnClick_wallet() {


	}
	public void setNavigationItemOnClick_bounds() {
		clearSubService();
		FragmentTransaction fragTran;
		Fragment_Bouns frag2 = new Fragment_Bouns();
		fragTran = mainActivity.getSupportFragmentManager().beginTransaction();
		fragTran.replace(R.id.container, frag2, Fragment_Bouns.class.getSimpleName());
		fragTran.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragTran.addToBackStack(Fragment_Bouns.class.getSimpleName());
		fragTran.commit();

	}

	public void setNavigationItemOnClick_driver() {
		Intent question = new Intent(mainActivity, DriverIdentityActivity.class);
		//question.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mainActivity.startActivity(question);

	}


	/*更新actionbar 的item*/
	@Override
	public void setNavigationItem(Menu menu) {
		//先全部隱藏
		super.setNavigationItem(menu);

	}


    //Pop all Fragment_Client_SubService stack
	public void clearSubService()
	{
		if (mainActivity.getSupportFragmentManager().getBackStackEntryCount() !=0)
		{
			FragmentManager.BackStackEntry backEntry=mainActivity.getSupportFragmentManager().getBackStackEntryAt(mainActivity.getSupportFragmentManager().getBackStackEntryCount()-1);
			String str=backEntry.getName();

			if (str.equals(Fragment_Client_SubService.class.getSimpleName()))
				mainActivity.getSupportFragmentManager().popBackStack();

		}

	}
}
