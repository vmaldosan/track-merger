import java.io.File;
import java.io.IOException;

import pojos.TrainingCenterDatabase;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


public class Main {

	public static void main(String[] args) {
		XmlMapper mapper = new XmlMapper();

		try {
			TrainingCenterDatabase beanFromXml = mapper.readValue(new File(
					"src/test/resources/test.tcx"),
					TrainingCenterDatabase.class);

			System.out.println(beanFromXml.toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
