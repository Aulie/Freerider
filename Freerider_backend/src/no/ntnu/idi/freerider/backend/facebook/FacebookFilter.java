package no.ntnu.idi.freerider.backend.facebook;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import no.ntnu.idi.freerider.backend.SocialNetwork;
import no.ntnu.idi.freerider.model.Journey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A SocialNetwork implementation that queries Facebook's OpenGraph API over HTTP. */
public class FacebookFilter implements SocialNetwork{
	private static final String URL_PREFIX = "https://graph.facebook.com/";
	private static final String ACCESTOKEN_POSTFIX = "?access_token=";
	private static final String FRIENDS_OF_FRIENDS_MIDFIX = "/mutualfriends&user=";
	private static final String FRIENDSEARCH_URL_MIDFIX = "/friends" + ACCESTOKEN_POSTFIX;
	private static final String ID_IDENTIFIER = "\"id\":\"";
	private static final char ID_CLOSER = '\"';
	private static Logger logger = LoggerFactory.getLogger(FacebookFilter.class);


	@Override
	/** Filter a list of Journeys to remove any not visible to the given searcher ID,
	 * using the given access token to get permission for queries from Facebook.  */
	public void filterSearch(List<Journey> journeys, String searcherID, String accesstoken) {
		List<String> friends = new ArrayList<String>();
		try {
			friends = getFriends(searcherID, accesstoken);
		} catch (IOException e) {
			logger.warn("Error retrieving friends list.",e);
		}
		for (Iterator<Journey> it = journeys.iterator(); it.hasNext();) {
			Journey journey = it.next();
			String ownerID = journey.getRoute().getOwner().getID();
			switch(journey.getVisibility()){
			case FRIENDS:
				if(!friends.contains(ownerID)) it.remove();
				break;
			case FRIENDS_OF_FRIENDS:
				if(friends.contains(ownerID)) break;
				if(!isFriendOfFriend(searcherID, ownerID,accesstoken)) it.remove();
				break;
			case PUBLIC:
				//The journey is public. Leave it in.
			}
		}
	}

	/** Check whether two IDs belong to users who have mutual friends.  */
	private static boolean isFriendOfFriend(String id, String otherID, String accessToken){
		try{
			URL url = new URL(URL_PREFIX + id + FRIENDS_OF_FRIENDS_MIDFIX + otherID + ACCESTOKEN_POSTFIX + accessToken);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			InputStreamReader reader = new InputStreamReader(conn.getInputStream());

			int matchingpositions = 0;
			int read;
			//If ID_IDENTIFIER exists in the output JSON, then the two IDs have mutual friends.
			//Search for this string in the input stream and return whether it was found.
			while((read = reader.read())!=-1 && matchingpositions < ID_IDENTIFIER.length()){
				if( read == ID_IDENTIFIER.charAt(matchingpositions)){
					matchingpositions++;
				}else{
					matchingpositions = 0;
				}
			}
			return matchingpositions > 0;
		} catch(IOException e){
			logger.warn("Error finding mutual friends.",e);
			return false;
		}
	}

	/** Get the list of IDs which are friends of a given ID. Requires an access token for permission. */
	private static List<String> getFriends(String id,String accessToken) throws IOException{
		URL url = new URL(URL_PREFIX + id + FRIENDSEARCH_URL_MIDFIX + accessToken);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		InputStreamReader reader = new InputStreamReader(conn.getInputStream());
		return parseFriendsList(reader);

	}

	/** Reads the values of all "id":"value" pairs out of an InputStream(Reader) containing JSON. */
	private static List<String> parseFriendsList(InputStreamReader reader)throws IOException {
		List<String> ret = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		int read;
		int matchingpositions = 0;
		boolean inID = false;
		while((read = reader.read())!=-1){
			if(!inID){
				if(read == ID_IDENTIFIER.charAt(matchingpositions)){
					matchingpositions++;
					if(matchingpositions==ID_IDENTIFIER.length()){
						inID = true;
						matchingpositions = 0;
					}
				}else{
					matchingpositions = 0;
				}
			}else{
				if(read == ID_CLOSER){
					ret.add(builder.toString());
					builder.delete(0, builder.length());
					inID = false;
				}else{
					builder.append((char) read);
				}
			}
		}
		return ret;
	}
}
