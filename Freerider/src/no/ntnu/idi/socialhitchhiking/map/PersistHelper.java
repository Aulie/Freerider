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
package no.ntnu.idi.socialhitchhiking.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.freerider.model.MapLocation;
import android.app.Activity;
import android.content.Context;

class PersistHelper {
	static final String FILE_EXTENSION_AUTOCOMPLETE 	= "autocomplete";
	static final String FILE_EXTENSION_ROUTE_CACHE		= "routecache";
	static final String FILE_EXTENSION_ROUTE_FAVOURITES	= "routefav";
	 
	static final int MAX_ROUTES_CACHED = 50;
	static final int MAX_AUTOCOMPLETES_CACHED = 200;
	
	private static FancyInfoFile autoCompleteInfoFile;
	
	/**
	 * There is a maximum number of auto complete requests available, 
	 * so to avoid making the same requests more than once, 
	 * all the auto complete results are saved in this {@link Map}
	 */
	private static Map<String, List<String>> autoCompleteCache = new HashMap<String, List<String>>();

	private static RouteList routeCache = new RouteList(null, MAX_ROUTES_CACHED, FileType.ROUTE_CACHE);

	/**
	 * Saves the given object to the directory holding application files. 
	 * Runs in a {@link Thread}.
	 * 
	 * @param context The {@link Context} that contains the info of where to save the files (usually an instance of the {@link Activity}-class or a subclass).
	 * @param filename The filename that should be used when saving the file.
	 * @param objectToSave The {@link Object} to save.
	 */
	static void saveToFile(final Context context, final String filename, final Object objectToSave){
		new Thread(new Runnable() {
			@Override
			public void run() {
				filename.replace('/', ' ');
				FileOutputStream fos = null;
				ObjectOutputStream oos = null;
				try{
					fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
					oos = new ObjectOutputStream(fos);
					oos.writeObject(objectToSave);
					oos.close();
				}catch (Exception e) {
				}

			}
		}).start();
	}

	/**
	 * Save the {@link MapRoute} to the cache, to make it faster to draw it again. 
	 * Make sure the route cache is initialized ({@link #initRouteCache(Context)}).
	 * @param route The {@link MapRoute} to save.
	 */
	static void saveRouteToCache(MapRoute route){
		routeCache.addAndSave(route, generateFilename(route));
	}
	
	/**
	 * Saves a {@link MapRoute} to file. If you want to cache it, use {@link #saveRouteToCache(MapRoute)} instead.
	 */
	static SavedRoute saveRouteToFile(Context context, MapRoute route, String filename, FileType extension){
		SavedRoute savedRoute = new SavedRoute(route, filename.toLowerCase().trim(), extension.getExtension());
		saveToFile(context, filename.toLowerCase().trim()+"."+extension.getExtension(), savedRoute);
		return savedRoute;
	}

	/**
	 * Updates the file where info about e.g. cache is stored.
	 */
	static void updateInfoFile(Context context, FancyInfoFile infoFile){
		saveToFile(context, infoFile.getFileTypeOfFiles().getExtension()+"."+infoFile.getFileType().getExtension(), infoFile);
	}
	
	/**
	 * Deletes a saved file. 
	 */
	static void deleteFile(Context context, String filename, String fileExtension){
		String fName = filename+"."+fileExtension;
		deleteFile(context, fName);
	}
	
	/**
	 * Deletes a saved file.
	 */
	static void deleteFile(Context context, String filenameFull){
		File file = context.getFileStreamPath(filenameFull);
		// Make sure the file or directory exists and isn't write protected
	    if (!file.exists())
	      throw new IllegalArgumentException(
	          "Delete: no such file or directory: " + filenameFull);

	    if (!file.canWrite())
	      throw new IllegalArgumentException("Delete: write protected: "
	          + filenameFull);

	    // If it is a directory, make sure it is empty
	    if (file.isDirectory()) {
	      String[] files = file.list();
	      if (files.length > 0)
	        throw new IllegalArgumentException(
	            "Delete: directory not empty: " + filenameFull);
	    }

	    // Attempt to delete it
	    boolean success = file.delete();

	    if (!success)
	      throw new IllegalArgumentException("Delete: deletion failed");
	}
	
	/**
	 * Deletes a saved route from file.
	 */
	static void deleteRouteFromFile(Context context, SavedRoute savedRoute, FileType extension){
		deleteFile(context, savedRoute.getFilename(), extension.getExtension());
	}
	
	/**
	 * Loads a {@link FancyInfoFile}.
	 */
	static FancyInfoFile loadInfoFile(Context context, FileType infoAboutFileType, int maxFiles){
		FancyInfoFile infoFile = new FancyInfoFile(infoAboutFileType, maxFiles);
		String filename = infoAboutFileType.getExtension()+"."+FileType.INFO_FILE.getExtension();
		FileInputStream fis   = null;
		ObjectInputStream ois = null;
		
		try {
			fis = context.openFileInput(filename);
			ois = new ObjectInputStream(fis);
			infoFile = (FancyInfoFile) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			
		} catch (StreamCorruptedException e) {
			
		} catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			
		} catch (Exception e){
			
		}
		return infoFile;
	}
	
	/**
	 * Initializes the route cache. Should be called before e.g. {@link #saveRouteToCache(MapRoute)}.
	 */
	static void initRouteCache(final Context context){
		new Thread(new Runnable() {
			@Override
			public void run() {
				RouteList rl = PersistHelper.loadSavedRoutes(context, FileType.ROUTE_CACHE); 
				routeCache = rl;
			}
		}).start();
	}

	/**
	 * Finds out if there is a route between the two given {@link String}s saved in the cache.
	 */
	static boolean routeCacheContainsRoute(String from, String to){
		String filename  = (from+"-"+to).toLowerCase().trim();
		String filename2 = (to+"-"+from).toLowerCase().trim();
		if(routeCache != null) {
			if(routeCache.containsKey(filename) || routeCache.containsKey(filename2)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get a {@link MapRoute} from the route cache.
	 * @param list A list containing all the {@link MapLocation}s in the route (start point, driving through, driving through, ..., stop point).
	 * @return Returns the {@link MapRoute} from the route cache, if found.
	 */
	static MapRoute routeCacheGetRoute(List<MapLocation> list){
		String filename = "";
		String filenameReverse = "";
		int x = list.size()-1;
		for (int i = 0; i < list.size(); i++) {
			filename 		+= list.get(i).getAddress().toLowerCase().trim();
			filenameReverse += list.get(x).getAddress().toLowerCase().trim();
			if(i<list.size()-1) {
				filename 		+= "$";
				filenameReverse += "$";
			}
			x--;
		}
		return routeCacheGetRouteFromFilename(filename, filenameReverse);
	}
	
	/**
	 * Get a {@link MapRoute} between the two {@link String}s from the cache.
	 */
	static MapRoute routeCacheGetRoute(String from, String to){
		String filename  = (from+"$"+to).toLowerCase().trim();
		String filenameReverse = (to+"$"+from).toLowerCase().trim();
		return routeCacheGetRouteFromFilename(filename, filenameReverse);
	}
	
	/**
	 * Get a {@link MapRoute} from the route cache.
	 */
	static MapRoute routeCacheGetRouteFromFilename(String filename, String filenameReverse){
		if(routeCache != null) {
			if(routeCache.containsKey(filename)){
				return routeCache.getRoute(filename);
			}
			else if(routeCache.containsKey(filenameReverse)){
				//TODO: The route must be reverted... MapRoute.revert(Route ) ...
				return routeCache.getRoute(filenameReverse);
			}
		}
		return null;
	}
	
	/**
	 * Load all the saved routes.
	 */
	private static RouteList loadSavedRoutes(Context context, FileType fileType){
		RouteList list = new RouteList(context, PersistHelper.MAX_ROUTES_CACHED, fileType);
		
		String[] fileNames = context.getFilesDir().list(new FileExtensionFilter(fileType.getExtension()));
		FileInputStream fis   = null;
		ObjectInputStream ois = null;
		for (int i = 0; i < fileNames.length; i++) {
			try {
				fis = context.openFileInput(fileNames[i]);
				ois = new ObjectInputStream(fis);
				list.add((SavedRoute)ois.readObject());
				ois.close(); 
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * Load the auto complete cache.
	 */
	@SuppressWarnings("unchecked")
	static Map<String, List<String>> loadAutoCompleteCacheFromFile(Context context){
		autoCompleteInfoFile = loadInfoFile(context, FileType.AUTOCOMPLETE, MAX_AUTOCOMPLETES_CACHED);
		
		Map<String, List<String>> loadedMap = new HashMap<String, List<String>>();
		String[] fileNames = context.getFilesDir().list(new FileExtensionFilter(FILE_EXTENSION_AUTOCOMPLETE));
		FileInputStream fis   = null;
		ObjectInputStream ois = null;
		for (int i = 0; i < fileNames.length; i++) {
			try {
				fis = context.openFileInput(fileNames[i]);
				ois = new ObjectInputStream(fis);
				loadedMap.put(fileNames[i], (List<String>)ois.readObject());
				autoCompleteInfoFile.addFilename(fileNames[i]);
				ois.close(); 
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return loadedMap;
	}
	
	/**
	 * Adds a search string and it's results in the auto complete cache.
	 */
	static void addAutoCompleteCacheElement(final Context context, final String searchString, final List<String> resultList){
		String filename = (searchString+".autocomplete").toLowerCase().trim();
		autoCompleteCache.put(filename, resultList);
		autoCompleteInfoFile.addFilename(filename);
		saveToFile(context, filename, resultList);
		List<String> listToDelete = autoCompleteInfoFile.getLeastUsed();
		for (String deleteMe : listToDelete) {
			if(!filename.equals(deleteMe)){
				autoCompleteInfoFile.remove(deleteMe);
				deleteAutoCompleteFromFile(context, deleteMe);
			}
		}
		updateInfoFile(context, autoCompleteInfoFile);
	}
	
	/**
	 * Deletes an auto complete string from file.
	 */
	static void deleteAutoCompleteFromFile(Context context, String filename){
		deleteFile(context, filename);
		autoCompleteCache.remove(filename);
	}
	
	/**
	 * Initializes the auto complete cache.
	 */
	static synchronized void initAutoCompleteCache(final Context context){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Map<String, List<String>> loadedMap = PersistHelper.loadAutoCompleteCacheFromFile(context);
				autoCompleteCache.putAll(loadedMap);
			}
		}).start();
	}

	/**
	 * Finds out whether the given {@link String} is saved in the auto complete cache.
	 */
	static boolean autoCompleteCacheContainsKey(String key){
		String filename = key.toLowerCase().trim() + "." + FileType.AUTOCOMPLETE.getExtension();
		
		return autoCompleteCache.containsKey(filename);
	}
	
	/**
	 * Gets the auto complete results for the given {@link String}.
	 */
	static List<String> getAutoCompleteForKey(final Context context, String key){
		String filename = key.toLowerCase().trim() + "." + FileType.AUTOCOMPLETE.getExtension();
		autoCompleteInfoFile.onUsedAFile(filename);
		updateInfoFile(context, autoCompleteInfoFile);
		return autoCompleteCache.get(filename);
	}

	/**
	 * Generates a filename for the given {@link MapRoute}. <br><br>
	 * The format is the addresses of all the map points, separated by a "$". 
	 */
	public static String generateFilename(MapRoute mapRoute) {
		List<MapLocation> ml = mapRoute.getMapPoints();
		String filename = "";
		for (int i = 0; i < ml.size(); i++) {
			filename += ml.get(i).getAddress().toLowerCase().trim();
			if(i<ml.size()-1) {
				filename 		+= "$";
			}
		}
		return filename;
	}
}

/**
 * Used for filtering files with a specific file extension. 
 * Used by f.ex {@link #loadFilesWithExtension(Context, String)}, for loading files with a given extension.
 */
class FileExtensionFilter implements FilenameFilter{ 
	private String extension;

	/**
	 * @param extension The extension of the files to filter (e.g. "exe", "doc", "html" etc.)
	 */
	public FileExtensionFilter(String extension){
		this.extension = extension;
	}
	@Override
	public boolean accept(File dir, String filename) {
		return filename.endsWith("."+extension);
	}
}

enum FileType{
	ROUTE_CACHE (PersistHelper.FILE_EXTENSION_ROUTE_CACHE), 
	ROUTE_FAVS (PersistHelper.FILE_EXTENSION_ROUTE_FAVOURITES), 
	AUTOCOMPLETE (PersistHelper.FILE_EXTENSION_AUTOCOMPLETE),
	INFO_FILE ("infofile");
	
	private String extension;
	private FileType(String extension){
		this.extension= extension;
	}
	String getExtension(){
		return extension;
	}
}

/**
 * Used for storing info about all elements saved in the cache.
 */
class FileInfo implements Serializable, Comparable<FileInfo>{
	private static final long serialVersionUID = 7987921537266720262L;
	private String filename;
	private int timesUsed;
	private long lastTimeUsed;
	
	FileInfo(String filename){
		this.filename = filename;
		this.timesUsed = 0;
		this.lastTimeUsed = System.currentTimeMillis();
	}
	void usedTheFile(){
		timesUsed++;
		lastTimeUsed = System.currentTimeMillis();
	}
	int getFancyNumber(){
		return timesUsed;
	}
	long getLastTimeUsed(){
		return lastTimeUsed;
	}
	String getFilename(){
		return filename;
	}
	@Override
	public int compareTo(FileInfo another) {
		if(this.getFancyNumber() < another.getFancyNumber()){
			return 1;
		}else if(this.getFancyNumber() > another.getFancyNumber()){
			return -1;
		}
		
		//If "times used" is the same, compare on time since last use:
		if(this.getLastTimeUsed() < another.getLastTimeUsed()){
			return 1;
		}else if(this.getLastTimeUsed() > another.getLastTimeUsed()){
			return -1;
		}
		return 0;
	}
}

/**
 * Used for storing info about the route cache.
 */
class FancyInfoFile implements Serializable{
	private static final long serialVersionUID = -4917315832267837749L;
	private FileType fileType;
	private FileType fileTypeOfFiles;
	private int maximumNrOfFiles;
	private Map<String, FileInfo> routeInfoMap;
	private List<FileInfo> routeList;
	
	FancyInfoFile(FileType infoAbout, int maxFiles){
		this.fileType = FileType.INFO_FILE;
		this.fileTypeOfFiles = infoAbout;
		this.maximumNrOfFiles = maxFiles;
		routeInfoMap = new HashMap<String, FileInfo>();
		routeList = new ArrayList<FileInfo>();
	}
	void addFilename(String filename){
		if(!routeInfoMap.containsKey(filename)){
			FileInfo ri = new FileInfo(filename);
			routeInfoMap.put(filename, ri);
			routeList.add(ri);
		}
	}
	void remove(String filename){
		FileInfo ri = routeInfoMap.get(filename);
		routeInfoMap.remove(ri);
		routeList.remove(ri);
	}
	FileType getFileType(){
		return fileType;
	}
	FileType getFileTypeOfFiles(){
		return fileTypeOfFiles;
	}
	synchronized void onUsedAFile(String filename){
		routeInfoMap.get(filename).usedTheFile();
	}
	List<String> getLeastUsed(){
		Collections.sort(routeList);
		List<String> list = new ArrayList<String>();
		for (int i = maximumNrOfFiles; i < routeList.size(); i++) {
			list.add(routeList.get(i).getFilename());
		}
		return list;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (FileInfo routeInfo : routeList) {
			sb.append(routeInfo.getFilename());
			sb.append("\t ... ");
			sb.append(routeInfo.getFancyNumber());
			sb.append("\n");
		}
		return sb.toString().trim();
	}
}

/**
 * Contains {@link SavedRoute}s, and all the data and methods needed to cache {@link MapRoute}s easily.
 */
class RouteList {
	private Map<String, SavedRoute> routeMap;
	private List<SavedRoute> routeList;
	private int maximumElements;
	private Context context;
	private FileType fileType;
	private FancyInfoFile infoFile;
	
	RouteList(Context context, int maxElements, FileType ft){
		this.routeMap 	= new HashMap<String, SavedRoute>();
		this.routeList 	= new ArrayList<SavedRoute>(); 
		this.maximumElements = maxElements;
		this.context = context;
		this.fileType = ft;
		if(context != null){
			this.infoFile = PersistHelper.loadInfoFile(context, ft, maxElements);
		}
	}
	
	private List<SavedRoute> getLeastUsed(){
		List<String> stringList = infoFile.getLeastUsed();
		List<SavedRoute> list = new ArrayList<SavedRoute>();
		for (String filename : stringList) {
			list.add(routeMap.get(filename));
		}
		return list;
	}
	public boolean addAndSave(MapRoute object, String filename) {
		boolean b = false;
		if(context != null){
			b = add(PersistHelper.saveRouteToFile(context, object, filename, fileType));
			PersistHelper.updateInfoFile(context, infoFile);
		}
		return b;
	}
	public boolean add(SavedRoute object) {
		if(!containsKey(object.getFilename())){
			routeMap.put(object.getFilename(), object);
			routeList.add(object);
			infoFile.addFilename(object.getFilename());
			if(routeList.size() >= maximumElements){
				List<SavedRoute> toBeDeletedList = getLeastUsed();
				for (SavedRoute deleteMe : toBeDeletedList) {
					if(!object.equals(deleteMe)){
						remove(deleteMe);
					}
				}
			}
			return true;
		}else{
			return false;
		}
	}
	public void remove(SavedRoute object){
		routeList.remove(object);
		routeMap.remove(object.getFilename());
		infoFile.remove(object.getFilename());
		if(context != null){
			PersistHelper.deleteRouteFromFile(context, object, fileType);
			PersistHelper.updateInfoFile(context, infoFile);
		}
	}
	public boolean contains(SavedRoute object) {
		return routeList.contains(object);
	}
	public boolean containsKey(String filename) {
		return routeMap.containsKey(filename);
	}
	public SavedRoute getSavedRoute(String filename) {
		SavedRoute sr = routeMap.get(filename);
		infoFile.onUsedAFile(filename);
		if(context != null){
			PersistHelper.updateInfoFile(context, infoFile);
		}
		return sr;
	}
	public MapRoute getRoute(String filename){
		return getSavedRoute(filename).getRoute();
	}
	public boolean isEmpty() {
		return routeList.isEmpty();
	}
	public int size() {
		return routeList.size();
	}
}

/**
 * Contains a {@link MapRoute}, its filename and file extension. 
 * Is used for saving and loading routes easily.
 */
class SavedRoute implements Serializable{
	private static final long serialVersionUID = 2860196512753821255L;
	private MapRoute object;
	private String filename;
	private String fileExtension;
	
	SavedRoute(MapRoute route, String filename, String fileExtension) {
		this.object = route;
		this.filename = filename;
		this.fileExtension = fileExtension;
	}
	String getFilename(){
		return filename;
	}
	String getExtension(){
		return fileExtension;
	}
	MapRoute getRoute(){
		return object;
	}
}

