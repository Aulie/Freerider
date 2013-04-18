package no.ntnu.idi.socialhitchhiking.inbox;

import no.ntnu.idi.socialhitchhiking.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ListNotifications extends TabActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_trips);
		TabHost tabHost = getTabHost();
		
		TabSpec unReadSpec = tabHost.newTabSpec("Unread");
		unReadSpec.setIndicator("Messages");
        Intent unReadIntent = new Intent(this, Inbox.class);
        unReadIntent.putExtra("history", false);
        unReadSpec.setContent(unReadIntent);
 

        TabSpec readSpec = tabHost.newTabSpec("Read");
        readSpec.setIndicator("Read");
        Intent readIntent = new Intent(this, Inbox.class);
        readIntent.putExtra("history", true);
        readSpec.setContent(readIntent);
 
        TabSpec requestSpec = tabHost.newTabSpec("Requests");
        requestSpec.setIndicator("Requests");
        Intent requestIntent = new Intent(this,Inbox.class);
        requestIntent.putExtra("request", true);
        requestSpec.setContent(requestIntent);
        
        tabHost.addTab(requestSpec);
        tabHost.addTab(unReadSpec);
        tabHost.addTab(readSpec);
	}
	
}
