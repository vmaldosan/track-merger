package pojos;

import java.util.Date;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Activity implements java.io.Serializable {
	
	private static final long serialVersionUID = -3938271623199064411L;

	@JacksonXmlProperty(isAttribute = true, localName = "Sport")
	String sport;
	
	@JacksonXmlProperty(localName = "Id")
	Date id;

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public Date getId() {
		return id;
	}

	public void setId(Date id) {
		this.id = id;
	}
	
	
}
