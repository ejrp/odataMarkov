package org.openmarkov.io.odata.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.cud.ODataDeleteRequest;
import org.apache.olingo.client.api.communication.request.cud.ODataEntityCreateRequest;
import org.apache.olingo.client.api.communication.request.cud.ODataEntityUpdateRequest;
import org.apache.olingo.client.api.communication.request.cud.UpdateType;
import org.apache.olingo.client.api.communication.request.retrieve.EdmMetadataRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntityRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetIteratorRequest;
import org.apache.olingo.client.api.communication.response.ODataDeleteResponse;
import org.apache.olingo.client.api.communication.response.ODataEntityCreateResponse;
import org.apache.olingo.client.api.communication.response.ODataEntityUpdateResponse;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientCollectionValue;
import org.apache.olingo.client.api.domain.ClientComplexValue;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.api.domain.ClientEnumValue;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.api.domain.ClientValue;
import org.apache.olingo.client.api.serialization.ODataDeserializerException;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.client.core.http.BasicAuthHttpClientFactory;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.format.ContentType;

public class OdataClientHandler {

	private String serviceUrl;
	private String serviceUser;
	private String servicePassword;
	private static ODataClient client;
	private static OdataClientHandler defaultHandler;

	public OdataClientHandler(boolean authenticationEnabled, String aServiceUrl, String aUsername, String aPassword) {

		serviceUrl = aServiceUrl;
		serviceUser = aUsername;
		servicePassword = aPassword;

		client = ODataClientFactory.getClient();

		// add the configuration here
		if (authenticationEnabled) {
			client.getConfiguration().setHttpClientFactory(new BasicAuthHttpClientFactory(aUsername, aPassword));
		}else {
			//client.getConfiguration().setHttpClientFactory(new BasicAuthHttpClientFactory(aUsername, aPassword));
		}
	}


	/**
	 * An alternative starting point for this demo, to also allow running this
	 * applet as an application.
	 * @param authenticationEnabled 
	 *
	 * @param args
	 *            ignored.
	 * @throws Exception 
	 */
	public static OdataClientHandler getClientHandler(boolean authenticationEnabled, String serviceUrl, String username, String password) throws Exception {
		// JGraphAdapterDemo applet = new JGraphAdapterDemo();
		// applet.init();
//		String serviceUrl = "https://api.devpersistent.iot-accelerator.ericsson.net/occhub/proxy/appiot/odatav2/34bb4075-753b-4209-a79d-ca347b615c63";
//		String username = "AnalyticsApplication@AnalyticsUser";
//		String password = "9AS+kRXd9K3oupdISlAoGAI4uY%asR-E";

		// OClientBehavior basicAuth = OClientBehaviors.basicAuth("user", "password");
		// ODataConsumer c =
		// ODataJerseyConsumer.newBuilder(serviceUri).setClientBehaviors(basicAuth).build();

		if (client == null) {
			defaultHandler = new OdataClientHandler(authenticationEnabled,serviceUrl, username, password);			
		}
		return defaultHandler;

//		ODataServiceDocumentRequest odClientReq = getClient().getRetrieveRequestFactory()
//				.getServiceDocumentRequest(serviceUrl);
//	
//		c.perform(serviceUrl);
	}
	
	


	private static void print(String content) {
		System.out.println(content);
	}

	private static void print(String content, List<?> list) {
		System.out.println(content);
		for (Object o : list) {
			System.out.println("    " + o);
		}
		System.out.println();
	}

	private static String prettyPrint(Map<String, Object> properties, int level) {
		StringBuilder b = new StringBuilder();
		Set<Entry<String, Object>> entries = properties.entrySet();

		for (Entry<String, Object> entry : entries) {
			intend(b, level);
			b.append(entry.getKey()).append(": ");
			Object value = entry.getValue();
			if (value instanceof Map) {
				value = prettyPrint((Map<String, Object>) value, level + 1);
			} else if (value instanceof Calendar) {
				Calendar cal = (Calendar) value;
				value = SimpleDateFormat.getInstance().format(cal.getTime());
			}
			b.append(value).append("\n");
		}
		// remove last line break
		b.deleteCharAt(b.length() - 1);
		return b.toString();
	}

	private static String prettyPrint(Collection<ClientProperty> properties, int level) {
		StringBuilder b = new StringBuilder();

		for (ClientProperty entry : properties) {
			intend(b, level);
			ClientValue value = entry.getValue();
			if (value.isCollection()) {
				ClientCollectionValue cclvalue = value.asCollection();
				b.append(prettyPrint(cclvalue.asJavaCollection(), level + 1));
			} else if (value.isComplex()) {
				ClientComplexValue cpxvalue = value.asComplex();
				b.append(prettyPrint(cpxvalue.asJavaMap(), level + 1));
			} else if (value.isEnum()) {
				ClientEnumValue cnmvalue = value.asEnum();
				b.append(entry.getName()).append(": ");
				b.append(cnmvalue.getValue()).append("\n");
			} else if (value.isPrimitive()) {
				b.append(entry.getName()).append(": ");
				b.append(entry.getValue()).append("\n");
			}
		}
		return b.toString();
	}

	private static void intend(StringBuilder builder, int intendLevel) {
		for (int i = 0; i < intendLevel; i++) {
			builder.append("  ");
		}
	}

	public  Edm readEdm() throws IOException {
		EdmMetadataRequest request = client.getRetrieveRequestFactory().getMetadataRequest(serviceUrl);
		ODataRetrieveResponse<Edm> response = request.execute();
		return response.getBody();
	}

	public ClientEntitySetIterator<ClientEntitySet, ClientEntity> readEntities(Edm edm,	String entitySetName) {
		URI absoluteUri = client.newURIBuilder(serviceUrl).appendEntitySetSegment(entitySetName).build();
		return readEntities(edm, absoluteUri);
	}

	public ClientEntitySetIterator<ClientEntitySet, ClientEntity> readEntitiesWithFilter(Edm edm, String serviceUri,
			String entitySetName, String filterName) {
		URI absoluteUri = client.newURIBuilder(serviceUri).appendEntitySetSegment(entitySetName).filter(filterName)
				.build();
		return readEntities(edm, absoluteUri);
	}

	private ClientEntitySetIterator<ClientEntitySet, ClientEntity> readEntities(Edm edm, URI absoluteUri) {
		System.out.println("URI = " + absoluteUri);
		ODataEntitySetIteratorRequest<ClientEntitySet, ClientEntity> request = client.getRetrieveRequestFactory()
				.getEntitySetIteratorRequest(absoluteUri);
		// odata4 sample/server limitation not handling metadata=full
		request.setAccept("application/json");
		
		ODataRetrieveResponse<ClientEntitySetIterator<ClientEntitySet, ClientEntity>> response = request.execute();

		return response.getBody();
	}

	public ClientEntity readEntityWithKey(Edm edm, String serviceUri, String entitySetName, Object keyValue) {
		URI absoluteUri = client.newURIBuilder(serviceUri).appendEntitySetSegment(entitySetName)
				.appendKeySegment(keyValue).build();
		return readEntity(edm, absoluteUri);
	}

	public ClientEntity readEntityWithKeyExpand(Edm edm, String serviceUri, String entitySetName, Object keyValue,
			String expandRelationName) {
		URI absoluteUri = client.newURIBuilder(serviceUri).appendEntitySetSegment(entitySetName)
				.appendKeySegment(keyValue).expand(expandRelationName).build();
		return readEntity(edm, absoluteUri);
	}
	
	public ClientEntity readEntityWithSelect(Edm edm, String entitySetName, 
			List<String> selectProperties) {
		URI absoluteUri = client.newURIBuilder(serviceUrl).appendEntitySetSegment(entitySetName)
				.select(StringUtils.join(selectProperties, ',')).build();
		return readEntity(edm, absoluteUri);
	}
	
	public ClientEntitySetIterator<ClientEntitySet, ClientEntity> readEntitiesWithSelect(Edm edm,	String entitySetName,List<String> selectProperties) {
		String[] selectString = selectProperties.toArray(new String[0]);
		URI absoluteUri = client.newURIBuilder(serviceUrl).appendEntitySetSegment(entitySetName).select(selectString).build();
		return readEntities(edm, absoluteUri);
	}
	
	public ClientEntitySetIterator<ClientEntitySet, ClientEntity> readEntitiesWithExpand(Edm edm,	String entitySetName, String expandItem,List<String> selectProperties) {
		String[] selectString = selectProperties.toArray(new String[0]);
		URI absoluteUri = client.newURIBuilder(serviceUrl).appendEntitySetSegment(entitySetName).expandWithSelect(expandItem, selectString).build();
		return readEntities(edm, absoluteUri);
	}
	public ClientEntitySetIterator<ClientEntitySet, ClientEntity> readEntitiesWithSelectAndExpand(Edm edm,	String entitySetName,List<String> selectProperties, String expandItem,List<String> expandSelectProperties) {
		String[] selectString = selectProperties.toArray(new String[0]);
		// URI absoluteUri = client.newURIBuilder(serviceUrl).appendEntitySetSegment(entitySetName).select(selectString).build();
		URI absoluteUri = client.newURIBuilder(serviceUrl).appendEntitySetSegment(entitySetName).select(selectString).expandWithSelect(expandItem, expandSelectProperties.toArray(new String[0])).build();
	//	absoluteUri = client.newURIBuilder(absoluteUri.toString()).appendValueSegment().expandWithSelect(expandItem,expandSelectProperties.toArray(new String[0])).build(); //      appendEntitySetSegment(entitySetName).expandWithSelect(expandItem, selectString).build();
		return readEntities(edm, absoluteUri);
	}

	private ClientEntity readEntity(Edm edm, URI absoluteUri) {
		ODataEntityRequest<ClientEntity> request = client.getRetrieveRequestFactory().getEntityRequest(absoluteUri);
		// odata4 sample/server limitation not handling metadata=full
		request.setAccept("application/json;odata.metadata=minimal");
		ODataRetrieveResponse<ClientEntity> response = request.execute();

		return response.getBody();
	}

	private ClientEntity loadEntity(String path) throws ODataDeserializerException {
		InputStream input = getClass().getResourceAsStream(path);
		return client.getBinder().getODataEntity(client.getDeserializer(ContentType.APPLICATION_JSON).toEntity(input));
	}

	public ClientEntity createEntity(Edm edm, String serviceUri, String entitySetName, ClientEntity ce) {
		URI absoluteUri = client.newURIBuilder(serviceUri).appendEntitySetSegment(entitySetName).build();
		return createEntity(edm, absoluteUri, ce);
	}

	private ClientEntity createEntity(Edm edm, URI absoluteUri, ClientEntity ce) {
		ODataEntityCreateRequest<ClientEntity> request = client.getCUDRequestFactory()
				.getEntityCreateRequest(absoluteUri, ce);
		// odata4 sample/server limitation not handling metadata=full
		request.setAccept("application/json;odata.metadata=minimal");
		ODataEntityCreateResponse<ClientEntity> response = request.execute();

		return response.getBody();
	}

	public int updateEntity(Edm edm, String serviceUri, String entityName, Object keyValue, ClientEntity ce) {
		URI absoluteUri = client.newURIBuilder(serviceUri).appendEntitySetSegment(entityName).appendKeySegment(keyValue)
				.build();
		ODataEntityUpdateRequest<ClientEntity> request = client.getCUDRequestFactory()
				.getEntityUpdateRequest(absoluteUri, UpdateType.PATCH, ce);
		// odata4 sample/server limitation not handling metadata=full
		request.setAccept("application/json;odata.metadata=minimal");
		ODataEntityUpdateResponse<ClientEntity> response = request.execute();
		return response.getStatusCode();
	}

	public int deleteEntity(String serviceUri, String entityName, Object keyValue) throws IOException {
		URI absoluteUri = client.newURIBuilder(serviceUri).appendEntitySetSegment(entityName).appendKeySegment(keyValue)
				.build();
		ODataDeleteRequest request = client.getCUDRequestFactory().getDeleteRequest(absoluteUri);
		// odata4 sample/server limitation not handling metadata=full
		request.setAccept("application/json;odata.metadata=minimal");
		ODataDeleteResponse response = request.execute();
		return response.getStatusCode();
	}
	/**
	 * Add to the HTTP request basic server authorization.
	 *
	 * @param req the request to add.
	 * @return
	 */
	private ODataEntityRequest<ClientEntity> setServerAuth(ODataEntityRequest<ClientEntity> req) {
	  String authorization = "Basic ";

	  authorization += new String(Base64.encodeBase64((serviceUser + ":" + servicePassword).getBytes()));
	  req.addCustomHeader("Authorization", authorization);

	  return req;
	}
	 


//	 void perform(String serviceUrl) throws Exception {
//		// Add AuthCache to the execution context
//
//		 
//		print("\n----- Read Edm ------------------------------");
//		Edm edm = readEdm(serviceUrl);
//		List<FullQualifiedName> ctFqns = new ArrayList<FullQualifiedName>();
//		List<FullQualifiedName> etFqns = new ArrayList<FullQualifiedName>();
//		for (EdmSchema schema : edm.getSchemas()) {
//			for (EdmComplexType complexType : schema.getComplexTypes()) {
//				ctFqns.add(complexType.getFullQualifiedName());
//			}
//			for (EdmEntityType entityType : schema.getEntityTypes()) {
//				etFqns.add(entityType.getFullQualifiedName());
//			}
//		}
//		print("Found ComplexTypes", ctFqns);
//		print("Found EntityTypes", etFqns);
//
//		print("\n----- Inspect each property and its type of the first entity: " + etFqns.get(0) + "----");
//		EdmEntityType etype = edm.getEntityType(etFqns.get(0));
//		for (String propertyName : etype.getPropertyNames()) {
//			EdmProperty property = etype.getStructuralProperty(propertyName);
//			FullQualifiedName typeName = property.getType().getFullQualifiedName();
//			print("property '" + propertyName + "' " + typeName);
//		}
//
//		print("\n----- Read Entities ------------------------------");
//		ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = readEntities(edm, serviceUrl,
//				"DimDevice");
//
//		while (iterator.hasNext()) {
//			ClientEntity ce = iterator.next();
//			print("Entry:\n" + prettyPrint(ce.getProperties(), 0));
//		}

//		print("\n----- Read Entry ------------------------------");
//		ClientEntity entry = readEntityWithKey(edm, serviceUrl, "DimDevice", 1);
//		print("Single Entry:\n" + prettyPrint(entry.getProperties(), 0));

		//
//		print("\n----- Read Entity with $expand  ------------------------------");
//		entry = readEntityWithKeyExpand(edm, serviceUrl, "DimDevice", 1, "Cars");
//		print("Single Entry with expanded Cars relation:\n" + prettyPrint(entry.getProperties(), 0));
//
//		//
//		print("\n----- Read Entities with $filter  ------------------------------");
//		iterator = readEntitiesWithFilter(edm, serviceUrl, "Manufacturers", "Name eq 'Horse Powered Racing'");
//		while (iterator.hasNext()) {
//			ClientEntity ce = iterator.next();
//			print("Entry:\n" + prettyPrint(ce.getProperties(), 0));
//		}

		// System.out.println(c.getOdataConsumer().getEntitySets()
		// entity.getProperty("Name").getValue().toString());
		// EdmDataServices entitySets = c.getOdataConsumer().getMetadata();
		// reportMetadata(entitySets);

//		System.out.println(c.getOdataConsumer().getEntitiesCount("DimDate").execute());
		// for(EdmEntityType entity : entitySets.getEntityTypes()){
		// System.out.println(entity.getName());
		// }
		// }

		// for(OEntity entity :
		// c.getOdataConsumer().getEntities("EntityName").execute()){
		// System.out.println(entity.getProperty("Name").getValue().toString());
		// }

//		JFrame frame = new JFrame();
//		// 5tframe.getContentPane().add(applet);
//		frame.setTitle("Connecting to Odata Source");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();
//		frame.setVisible(true);

//	}	
	
}
