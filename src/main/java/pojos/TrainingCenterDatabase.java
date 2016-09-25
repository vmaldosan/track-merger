package pojos;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TrainingCenterDatabase implements java.io.Serializable {

	private static final long serialVersionUID = -7475253057320637502L;

	@JacksonXmlProperty(localName = "Activities")
	Activity[] activities;

	public Activity[] getActivities() {
		return activities;
	}

	public void setActivities(Activity[] activities) {
		this.activities = activities;
	}

	@Override
	public String toString() {
		if (activities == null) {
			return null;
		}
		String result = "";
		for (Activity activity : activities) {
			result += "Activity id: "+ activity.getId() 
					+ "\nSport: " + activity.getSport();
		}
		return result;
	}
}
