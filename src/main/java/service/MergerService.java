package service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.garmin.xmlschemas.trainingcenterdatabase.v2.TrainingCenterDatabaseT;


public class MergerService {

	// TODO: make it Singleton.
	private XmlMapper mapper;

	public MergerService() {
		mapper = new XmlMapper();
		// mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(mapper.getTypeFactory());

		AnnotationIntrospector pair = AnnotationIntrospectorPair.create(primary, secondary);
		mapper.setAnnotationIntrospector(pair);
	}

	/**
	 * @param fileName - Assumed not null.
	 * @return TrainingCenterDatabaseT - Bean with the 
	 */
	public TrainingCenterDatabaseT deserializeTrackFile(String fileName) {
		TrainingCenterDatabaseT beanFromXml = null;
		try {
			beanFromXml = mapper.readValue(new File(fileName), TrainingCenterDatabaseT.class);

			System.out.println(beanFromXml.toString());
		} catch (JsonParseException jpe) {
			jpe.printStackTrace();

		} catch (JsonMappingException jme) {
			jme.printStackTrace();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return beanFromXml;
	}

	/**
	 * @param files - list of tcx files to merge. Assumed not null or empty.
	 */
	public void mergeTcxFiles(List<String> files) {
		
	}
}
