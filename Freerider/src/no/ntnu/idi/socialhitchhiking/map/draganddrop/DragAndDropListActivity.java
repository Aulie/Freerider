/**
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @version: 		1.0
 *
 * Copyright (C) 2012 Freerider Team.
 *
 * Licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 */
package no.ntnu.idi.socialhitchhiking.map.draganddrop;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.freerider.model.MapLocation;
import no.ntnu.idi.socialhitchhiking.R;
import no.ntnu.idi.socialhitchhiking.SocialHitchhikingApplication;
import no.ntnu.idi.socialhitchhiking.map.MapRoute;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DragAndDropListActivity extends ListActivity {

	List<MapLocation> mapLocationList = new ArrayList<MapLocation>();
	boolean inEditMode = false;
	int positionOfRoute = 0;
	MapRoute oldRoute = null;
	SocialHitchhikingApplication app;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dragndroplistview);
        app = (SocialHitchhikingApplication) getApplication();
        
        Bundle extras = getIntent().getExtras(); 
        boolean addPoint = false;
        if(extras != null){
        	inEditMode 	= extras.getBoolean("editMode");
        	positionOfRoute = extras.getInt("routePosition");
        	String type = extras.getString("type");
        	oldRoute = app.getSelectedMapRoute();
        	
        	if(type.equals("addPoint")) addPoint = true;
        	mapLocationList = oldRoute.getMapPoints();
        }
        
        ColorStateList cl = null;
        try {
        	XmlResourceParser xpp = getResources().getXml(R.color.dragndrop_list_colors);
        	cl = ColorStateList.createFromXml(getResources(), xpp);
        } catch (Exception e) {}
        int[] itemLayouts = new int[]{R.layout.dragitem};
        int[] itemIDs = new int[]{R.id.DragAndDropTextView01, R.id.DragAndDropDeleteButton};
        DragAndDropAdapter adapter = new DragAndDropAdapter(this, itemLayouts, itemIDs, mapLocationList);
        if(addPoint){
        	adapter.setLastLocationAdded(mapLocationList.get(mapLocationList.size()-1));
        }else{
        	adapter.setLastLocationAdded(null);
        }
        
        adapter.setColorStateList(cl);
        setListAdapter(adapter);
        ListView listView = getListView();
        
        if (listView instanceof DragAndDropListView) {
        	((DragAndDropListView) listView).setDropListener(mDropListener);
        	((DragAndDropListView) listView).setRemoveListener(mRemoveListener);
        	((DragAndDropListView) listView).setDragListener(mDragListener);
        }
        
        ((Button)findViewById(R.id.btnDragNDropDone)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<MapLocation> mapLocations = new ArrayList<MapLocation>();
				for (int i = 0; i < getListAdapter().getCount(); i++) {
					mapLocations.add(((DragAndDropAdapter) getListAdapter()).getMapLocation(i));
				}
				Intent mapViewIntent = new Intent(DragAndDropListActivity.this, no.ntnu.idi.socialhitchhiking.map.MapActivityCreateOrEditRoute.class);
				mapViewIntent.putExtra("editMode", inEditMode);
				mapViewIntent.putExtra("routePosition", positionOfRoute);
				app.setSelectedMapRoute(oldRoute);
				startActivity(mapViewIntent);
				finish(); 
			}
		});
    }

	private DropListener mDropListener = 
		new DropListener() {
        @Override
		public void onDrop(int from, int to) {
        	ListAdapter adapter = getListAdapter();
        	if (adapter instanceof DragAndDropAdapter) {
        		((DragAndDropAdapter)adapter).onDrop(from, to);
        		getListView().invalidateViews();
        	}
        }
    };
    
    public void removeListItem(MapLocation ml){
    	int index = -1;
    	for (int i = 0; i < ((DragAndDropAdapter)getListAdapter()).getCount(); i++) {
    		if(((DragAndDropAdapter)getListAdapter()).getMapLocation(i).equals(ml)){
    			index = i;
    			break;
    		}
		}
    	if(index > -1) mRemoveListener.onRemove(index);
    	
    	Intent intent = getIntent();
    	finish();
    	
    	//removes "slide-in" animations:
    	overridePendingTransition(0, 0); 
    	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    	
    	startActivity(intent);
    }
    
    private RemoveListener mRemoveListener =
        new RemoveListener() {
        @Override
		public void onRemove(int which) {
        	ListAdapter adapter = getListAdapter();
        	if (adapter instanceof DragAndDropAdapter) {
        		((DragAndDropAdapter)adapter).onRemove(which);
        		getListView().invalidateViews();
        	}
        }
    };
    
    private DragListener mDragListener =
    	new DragListener() {

    	int backgroundColor = 0xe0103010;
    	int defaultBackgroundColor;
    	
			@Override
			public void onDrag(int x, int y, ListView listView) {
			}

			@Override
			public void onStartDrag(View itemView) {
				itemView.setVisibility(View.INVISIBLE);
				defaultBackgroundColor = itemView.getDrawingCacheBackgroundColor();
				itemView.setBackgroundColor(backgroundColor);
				ImageView iv = (ImageView)itemView.findViewById(R.id.DragAndDropImageView01);
				if (iv != null) iv.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onStopDrag(View itemView) {
				itemView.setVisibility(View.VISIBLE);
				itemView.setBackgroundColor(defaultBackgroundColor);
				ImageView iv = (ImageView)itemView.findViewById(R.id.DragAndDropImageView01);
				if (iv != null) iv.setVisibility(View.VISIBLE);
			}
    	
    };
}