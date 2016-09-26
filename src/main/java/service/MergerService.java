package service;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
		// mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(mapper.getTypeFactory());

		AnnotationIntrospector pair = AnnotationIntrospectorPair.create(primary, secondary);
		mapper.setAnnotationIntrospector(pair);
	}

	public TrainingCenterDatabaseT deserializeTrackFile(String fileName) {
		TrainingCenterDatabaseT beanFromXml = null;
		try {
			beanFromXml = mapper.readValue(new File(fileName), TrainingCenterDatabaseT.class);

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

		return beanFromXml;
	}
}
