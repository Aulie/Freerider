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
package no.ntnu.idi.socialhitchhiking.utility;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

public abstract class SectionedListViewAdapter extends BaseAdapter{
	protected abstract View getHeaderView(String caption, int index,View convertView, ViewGroup parent);

	private List<Section> sections;
	private static int TYPE_SECTION_HEADER = 1;

	public SectionedListViewAdapter(){
		super();

		sections = new ArrayList<Section>();
	}
	
	public void reset(){
		sections = new ArrayList<Section>();
		notifyDataSetChanged();
	}
	@SuppressWarnings("rawtypes")
	public void setSection(int i,String caption, ArrayAdapter adapter){
		sections.set(i,new Section(caption, adapter));
	}
	@SuppressWarnings("rawtypes")
	public void addSection(String caption, ArrayAdapter adapter) {
		sections.add(new Section(caption, adapter));
	}

	public boolean containsSection(String title){
		for (Section section : this.sections) {
			if(section.caption.equals(title))
				return true;
		}
		return false;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addObject(String title,Object o,ArrayAdapter ad){
		for (Section section : this.sections) {
			if(section.caption.equals(title)){
				section.adapter.add(o);
				section.adapter.notifyDataSetChanged();
				notifyDataSetChanged();
				return;
			}
		}
		sections.add(new Section(title, ad));
		
	}
	/**
	 * A method that removes an object from the adapter. Updates the view accordingly.
	 * If there is no objects left in the {@link Section} holding the object, then the
	 * section will be deleted from the adapter.
	 * 
	 * @param o - The object to be removed from the {@link SectionedListViewAdapter}
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String removeObject(Object o){
		
		for (Section section : this.sections) {
			if(section.adapter.getPosition(o) != -1){
				section.adapter.remove(o);
				if(section.adapter.getCount() == 0){
					sections.remove(section);
				}				
				if(getCount() == 1){
					Object obj = getItem(0);
					if(obj instanceof Section){
						sections = new ArrayList<Section>();
					}
				}
				
				section.adapter.notifyDataSetChanged();
				notifyDataSetChanged();
				return section.caption;
			}
		}
		
		return null;
	}

	public Object getItem(int position) {
		for (Section section : this.sections) {
			if (position == 0) {
				return (section);
			}
			int size = section.adapter.getCount() + 1;
			if (position < size) {
				return (section.adapter.getItem(position - 1));
			}
			position -= size;
		}
		return (null);
	}


	public int getCount() {
		int total = 0;
		for (Section section : this.sections) {
			total += section.adapter.getCount() + 1; // add one for header
		}
		return (total);
	}


	public int getViewTypeCount() {
		int total = 1; // one for the header, plus those from sections


		for (Section section : this.sections) {
			total += section.adapter.getViewTypeCount();
		}
		return (total);
	}


	public int getItemViewType(int position) {
		int typeOffset = TYPE_SECTION_HEADER + 1; // start counting from here


		for (Section section : this.sections) {
			if (position == 0) {
				return (TYPE_SECTION_HEADER);
			}

			int size = section.adapter.getCount() + 1;
			if (position < size) {
				return (typeOffset + section.adapter
						.getItemViewType(position));
			}
			position -= size;
			typeOffset += section.adapter.getViewTypeCount();
		}
		return (-1);
	}


	public boolean areAllItemsSelectable() {
		return (false);
	}


	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionIndex = 0;


		for (Section section : this.sections) {
			if (position == 0) {
				return (getHeaderView(section.caption, sectionIndex,
						convertView, parent));
			}

			int size = section.adapter.getCount() + 1;

			if (position < size) {
				return (section.adapter.getView(position - 1, convertView,
						parent));
			}

			position -= size;
			sectionIndex++;
		}


		return (null);
	}

	public void updateDataSet(){
		for(Section s : sections){
			s.adapter.notifyDataSetChanged();
		}
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return (position);
	}
	
	@SuppressWarnings("rawtypes")
	private class Section {
		String caption;
		
		ArrayAdapter adapter;

		Section(String caption, ArrayAdapter adapter) {
			this.caption = caption;
			this.adapter = adapter;
		}
	}
	
}

