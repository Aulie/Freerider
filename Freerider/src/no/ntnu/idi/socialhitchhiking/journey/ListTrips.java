package no.ntnu.idi.socialhitchhiking.journey;

import no.ntnu.idi.socialhitchhiking.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ListTrips extends TabActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_trips);
		TabHost tabHost = getTabHost();
		 

        TabSpec ownedSpec = tabHost.newTabSpec("Owned");
        ownedSpec.setIndicator("Owned");
        Intent ownedIntent = new Intent(this, ListJourneys.class);
        ownedIntent.putExtra("owned", true);
        ownedSpec.setContent(ownedIntent);
 

        TabSpec hitchedSpec = tabHost.newTabSpec("Hitched");
        hitchedSpec.setIndicator("Hitched");
        Intent hitchedIntent = new Intent(this, ListJourneys.class);
        hitchedIntent.putExtra("owned", false);
        hitchedSpec.setContent(hitchedIntent);
 
        tabHost.addTab(ownedSpec);
        tabHost.addTab(hitchedSpec);
	}
	
}
