package no.ntnu.idi.socialhitchhiking.journey;

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
