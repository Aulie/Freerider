/*******************************************************************************
 * @contributor(s): Freerider Team (Group 4, IT2901 Fall 2012, NTNU)
 * @contributor(s): Freerider Team 2 (Group 3, IT2901 Spring 2013, NTNU)
 * @version: 2.0
 * 
 * Copyright 2013 Freerider Team 2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

import java.util.List;

import no.ntnu.idi.freerider.model.MapLocation;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public final class DragAndDropAdapter extends BaseAdapter implements RemoveListener, DropListener{

	private int[] mIds;
    private int[] mLayouts;
    private LayoutInflater mInflater;
    private List<MapLocation> mContent;
    private MapLocation lastLocationAdded = null;
    private ColorStateList defaultColorStateList;
    private DragAndDropListActivity activity;
    
    public DragAndDropAdapter(DragAndDropListActivity activity, List<MapLocation> content) {
        init(activity,new int[]{android.R.layout.simple_list_item_1},new int[]{android.R.id.text1}, content);
    }
    
    public DragAndDropAdapter(DragAndDropListActivity activity, int[] itemLayouts, int[] itemIDs, List<MapLocation> content) {
    	init(activity,itemLayouts,itemIDs, content);
    }

    private void init(DragAndDropListActivity activity, int[] layouts, int[] ids, List<MapLocation> content) {
    	mInflater = LayoutInflater.from(activity);
    	mIds = ids;
    	mLayouts = layouts;
    	mContent = content;
    	this.activity = activity;
    }
    
    @Override
	public int getCount() {
        return mContent.size();
    }

    @Override
	public String getItem(int position) {
        return mContent.get(position).toString();
    }
    
    
    public MapLocation getMapLocation(int position){
    	return mContent.get(position);
    }
    
    @Override
	public long getItemId(int position) {
        return position;
    }

    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayouts[0], null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(mIds[0]);
            convertView.setTag(holder);
            
            ((ImageButton)convertView.findViewById(mIds[1])).setOnClickListener(new DeleteButtonOnClickListener(mContent.get(position)));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(mContent.get(position).toString());
        
        if(defaultColorStateList != null){
        	holder.text.setTextColor(defaultColorStateList);
        	if(lastLocationAdded != null){
        		if(holder.text.getText().equals(lastLocationAdded.toString())){
        			holder.text.setTextColor(Color.BLUE);
        		}
        	}
        }
        
        return convertView;
    }
    
    void setColorStateList(ColorStateList csl){
    	defaultColorStateList = csl;
    }
    void setLastLocationAdded(MapLocation loc){
    	lastLocationAdded = loc;
    }
    
    static class ViewHolder {
        TextView text;
    }

	@Override
	public void onRemove(int which) {
		if (which < 0 || which > mContent.size()) return;		
		mContent.remove(which);
	}

	@Override
	public void onDrop(int from, int to) {
		MapLocation temp = mContent.get(from);
		mContent.remove(from);
		mContent.add(to,temp);
	}

	class DeleteButtonOnClickListener implements OnClickListener{

		private MapLocation mapLocation;

		public DeleteButtonOnClickListener(MapLocation loc){
			mapLocation = loc; 
		}
		
		@Override
		public void onClick(View arg0) {
			activity.removeListItem(mapLocation);
		}
		
	}
}

