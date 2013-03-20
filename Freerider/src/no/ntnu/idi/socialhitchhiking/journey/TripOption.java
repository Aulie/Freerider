package no.ntnu.idi.socialhitchhiking.journey;

/**
 * @author Jose Luis Trigo
 */

public class TripOption {
	public int icon;
    public String title;
    public String subtitle;
    
    public TripOption(){
        super();
    }
    
    public TripOption(int icon, String title, String subtitle) {
        super();
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
    }
}
