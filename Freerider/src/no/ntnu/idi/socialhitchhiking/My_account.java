package no.ntnu.idi.socialhitchhiking;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * This class makes the tab view for my account and adds three tabs on it.
 * @author Made ziius
 *
 */
@SuppressWarnings("deprecation")
public class My_account extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account);
		TabHost tabHost = getTabHost();
		   
	    // setting Title and Icon for the Me Tab
	    TabSpec mespec = tabHost.newTabSpec("Me");
	    mespec.setIndicator("ME");
	    Intent meIntent = new Intent(this, MyAccountMeActivity.class);
	    mespec.setContent(meIntent);

	    // Tab for My car
	    TabSpec mycarspec = tabHost.newTabSpec("MyCar");
	    mycarspec.setIndicator("MY CAR");
	    Intent mycarIntent = new Intent(this, MyAccountCar.class);
	    mycarspec.setContent(mycarIntent);

	    // Tab for Preferences
	    TabSpec preferencesspec = tabHost.newTabSpec("Preferances");
	    preferencesspec.setIndicator("PREFERENCES");
	    Intent preferancesIntent = new Intent(this, MyAccountPreferences.class);
	    preferencesspec.setContent(preferancesIntent);

	    // Adding all TabSpec to TabHost
	    tabHost.addTab(preferencesspec); 
	    tabHost.addTab(mespec); 
	    tabHost.addTab(mycarspec);
	    
	    //custom size to the preference tab
	    tabHost.getTabWidget().getChildAt(0).getLayoutParams().width =45;
	    /*if(getIntent().getBooleanExtra("fromDialog", false)){
	    	tabHost.setCurrentTab(1);
	    }*/
	}

}