import java.awt.Color;


public interface SHPRCConstants {
	public static final int N_SUID_DIGITS = 7;
	public static final Color LIGHT_GREY = new Color(153, 153, 153);
	public static final Color[] COLORS = {Color.BLACK, new Color(204, 255, 254),
			new Color(204, 204, 255), new Color(244, 204, 244), 
			new Color(255, 204, 222), new Color(204, 255, 204)};

	public String[] AFFILIATIONS = {"-Select-","Frosh", "Soph", "Junior", 
			"Senior", "Co-term", "Grad", "Other"}; //TODO make customizable

}
