package tw.com.geminihsu.app01.delegate.driver;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import tw.com.geminihsu.app01.MenuMainActivity;
import tw.com.geminihsu.app01.R;
import tw.com.geminihsu.app01.fragment.Fragment_BeginOrder;
import tw.com.geminihsu.app01.delegate.MenuMainViewDelegateBase;

public class MenuMainViewDelegateDriver extends MenuMainViewDelegateBase {
	private final String TAG= this.getClass().getSimpleName();

	public MenuMainViewDelegateDriver(MenuMainActivity mainActivity) {
		super(mainActivity);

	}

	@Override
	public void setContentLayoutFragment() {
        // Instantiate a new fragment.
		Fragment newFragment = new Fragment_BeginOrder();

		FragmentTransaction ft = mainActivity.getSupportFragmentManager().beginTransaction();
		//if (mainActivity.getSupportFragmentManager().findFragmentByTag(MultiLiveView.PARENT_FRAGMENT_TAG_ID) == null) {
			ft.add(R.id.container, newFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			// ft.addToBackStack(Fragment_Liveview.class.getSimpleName());
			ft.commit();
		//}

	}

	@Override
	public void setNavigationItemOnClick_pastOrder() {
         super.setNavigationItemOnClick_pastOrder();
	}

	@Override
	public void setNavigationItem(Menu menu) {
		//先全部隱藏
		super.setNavigationItem(menu);
		menu.findItem(R.id.navigation_item_order).setVisible(false);
		//menu.findItem(R.id.navigation_item_order_record).setVisible(true);
		menu.findItem(R.id.navigation_item_begin).setVisible(true);
		//menu.findItem(R.id.navigation_item_order_filter).setVisible(true);
		menu.findItem(R.id.navigation_item_wait).setVisible(true);
		//menu.findItem(R.id.navigation_item_order_past).setVisible(true);
		menu.findItem(R.id.navigation_item_logOut).setVisible(true);

	}

}
