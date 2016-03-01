package com.taksila.veda.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.taksila.veda.model.api.base.v1_0.Err;
import com.taksila.veda.model.api.base.v1_0.ErrorInfo;


public class CommonUtils 
{
	static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();		
	static Logger logger = LogManager.getLogger(CommonUtils.class.getName());
	
	static final BigDecimal mileUnit = new BigDecimal("0.621371");
	public static final String BROWSER_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
			
	public static String readFile(String fileName)
	{
		try 
		{
			ClassLoader classLoader = CommonUtils.class.getClassLoader();	
			InputStream fileInputStream = classLoader.getResourceAsStream(fileName);	
			StringWriter writer = new StringWriter();
			IOUtils.copy(fileInputStream, writer,"UTF-8");			
			fileInputStream.close();
			return writer.toString();
		} 
		catch (Exception e) 
		{	
			e.printStackTrace();
			return new String("{\"error\":\"error occured ="+e.getMessage()+"}\"");
		}
	 
	}
	
	public static byte[] readExcelFile(String fileName)
	{
		try 
		{
			ClassLoader classLoader = CommonUtils.class.getClassLoader();	
			InputStream fileInputStream = classLoader.getResourceAsStream(fileName);	
			byte[] imageBytes = IOUtils.toByteArray(fileInputStream);
			fileInputStream.close();
			return imageBytes;
		} 
		catch (Exception e) 
		{	
			e.printStackTrace();			
		}
		return null;
	}
	
	public static byte[] readImageFile(String fileName)
	{
		try 
		{
			logger.trace(" inside readImageFile for file = "+fileName);
			ClassLoader classLoader = CommonUtils.class.getClassLoader();	
			InputStream fileInputStream = classLoader.getResourceAsStream(fileName);
			byte[] imageBytes = IOUtils.toByteArray(fileInputStream);
			fileInputStream.close();
			return imageBytes;
		} 
		catch (Exception e) 
		{	
			e.printStackTrace();			
		}
		return null;
	 
	}
	
	public static String getFormParamString(String parmname, MultivaluedMap<String, String> formParams) 
	{
		if (formParams == null || formParams.isEmpty() || !formParams.containsKey(parmname))
			return "";
		
		if (formParams.get(parmname) == null || formParams.get(parmname).isEmpty())
			return "";
		else			
			return formParams.get(parmname).get(0);
	}
	
	 public static XMLGregorianCalendar getXMLGregorianCalendarNow() 	            
	    {
	        XMLGregorianCalendar now = null;
			try {
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
				now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return now;
	    }
	
	public static String getNowDateTime(String format)
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    	    
	    return sdf.format(cal.getTime());
	}
	 
	public static String toJson(Object modelObj) 
	{
		return gson.toJson(modelObj);
	}
	
	public static String toJson(Object modelObj, String rootId) 
	{
		JsonElement jsEl = gson.toJsonTree(modelObj);
	    JsonObject jsObj = new JsonObject();
	    jsObj.add(rootId, jsEl);
		
		return gson.toJson(jsObj);
	}

	public static JsonObject toJSONObject(String jSON) 
	{
		JsonParser jsonParser = new JsonParser();
		
		return (JsonObject)jsonParser.parse(jSON);
	}
	
	public static JsonArray toJSONArray(String jSON) 
	{
		JsonParser jsonParser = new JsonParser();
		
		return (JsonArray)jsonParser.parse(jSON);
	}
	
	public static String getNotNullString(String str,String delimiter)
	{
		if (StringUtils.isNotBlank(str))
			return str + delimiter;
		else
			return "";
	}
	
	public static String getNotNullString(String str)
	{
		if (StringUtils.isNotBlank(str))
			return str;
		else
			return "";
	}
	
//	public static Object fromJsonToJaxbObj(String json)
//	{
//		Map<String, Object> properties = new HashMap<String, Object>(2);
//        properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
//        properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
//		JSONJAXBContext jc = JAXBContext.newInstance(new Class[] {Foo.class}, properties);
//
//        Unmarshaller unmarshaller = jc.createUnmarshaller();
//        StreamSource json = new StreamSource("src/forum15728638/input.json");
//        List<Foo> foos = (List<Foo>) unmarshaller.unmarshal(json, Foo.class).getValue();
//
//        Marshaller marshaller = jc.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        marshaller.marshal(foos, System.out);
//	}
	
	
	
	public static String getSubDomain(UriInfo uriInfo)
	 {	      
		 return getSubDomain(uriInfo.getRequestUri());
	      
	 }
	
	 public static String getSubDomain(URI url)
	 {	      
		 return getSubDomain(url.toString());
	      
	 }
	 
	 
	 
	 
	 public static String getSubDomain(String url)
	 {	      
		 logger.trace(" getSubDomain = "+url);  
        String host = StringUtils.removeStartIgnoreCase( url,"https://");
        host = StringUtils.removeStartIgnoreCase(host,"http://");
                        
        logger.trace(" request host for split = "+host);
        if (host == null)
        	return null;            
        String[] p = host.split("\\.");       
        logger.trace(" host variable split by period len = "+p.length);       
        String tenantDomain = null;
        for(int i=0;i<p.length;i++)
        {
	        if ("www".equals(p[i]) ||  /* ignore www */
	        	"dev".equals(p[i]) ||   /* ignore dev / stag / test region domain route */
	        	"test".equals(p[i]) ||	        	
	        	"stag".equals(p[i]))
	        	continue;
	        else if ("127".equals(p[i]) && StringUtils.contains(host,"127.0.0.1"))
	        {
	        	tenantDomain = "cloud"; /* to facilitate development on localhost */
	        	break;
	        }	
	        else
	        {
	        	tenantDomain = p[i];
	        	break;
	        }
        }
        
        logger.trace(" Domain = "+tenantDomain);  
        
        return tenantDomain;
	      
	 }
	 
	 
	 public static String getDomainUrl(String url)
	 {	      
		 if(isNotEmpty(url)) {
			 return url.substring(0, url.indexOf("/", url.indexOf("//") + 3));
		 }
	      
		 return "";
	 }
	 public static Err buildErr(String field, String msg)
	 {
		Err err = new Err();	
		err.setErrorFieldId(field);
		err.setErrorMsg(msg);	
		
		return err;
	 }
	 
	 public static ErrorInfo buildErrorInfo(Exception e) 
	 {
		 
		return buildErrorInfo("Exception", e.getMessage()+","+e.getLocalizedMessage());
	 }
	 
	 public static ErrorInfo buildErrorInfo(String field, String msg)
	 {
		ErrorInfo errInfo = new ErrorInfo();	
		errInfo.getErrors().add(buildErr(field, msg));
		
		return errInfo;
	 }
	 
		
	public static <T> T fromJson(String jsonString, Class<T> destclass)
	{
		if (jsonString != null && jsonString.length() > 0)
		{						
			return gson.fromJson(jsonString, destclass);
		}
		return null;
	}
	
	public static String newUUIDString() {
        final String tmp = UUID.randomUUID().toString();
        return tmp.replaceAll("-", "");
    }
			
	public static boolean isEmpty(Collection<?> collection){
		if(collection == null || collection.isEmpty()){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(Collection<?> collection){
		return !isEmpty(collection);
	}
	
	public static boolean isEmpty(String str){
		if(StringUtils.isBlank(str)){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	public static boolean isEmpty(Object[] objectArr){
		if(objectArr == null || objectArr.length == 0){
			return true;
		}
		return false;
	}
	
	public static boolean isNotEmpty(Object[] objectArr){
		return !isEmpty(objectArr);
	}
	
	public static <T extends Object> T performWebServiceCall(String restUrl, Map<String,String> urlParams, String basicAuthId, String basicAuthPwd, Class<T> returnEntityClass) 
	{
		long startTime = System.currentTimeMillis();
		
		 StringBuilder sb = new StringBuilder();
	     for (Entry<String, String> entry : urlParams.entrySet()) 
	     {
	    	 if (sb.length() > 0) 
	    	 {
	    		 sb.append("&");
	    	 }
	         
	    	 String pathParamKey = "{"+entry.getKey().toString().trim()+"}";
	    	 
	    	 if (StringUtils.contains(restUrl, pathParamKey))
	    		 restUrl = restUrl.replace(pathParamKey, entry.getValue().toString());
	    	 else
	    		 sb.append(String.format("%s=%s",  urlEncodeUTF8(entry.getKey().toString()), urlEncodeUTF8(entry.getValue().toString())));
	     }	            
		
	    if (StringUtils.isNotBlank(sb.toString()))
	    	restUrl = restUrl+"?"+sb.toString();
		logger.trace("----------------------------------------ABOUT TO PERFORM WEB SERVICE CALL ------------------------------------------------------------------------------------");
		logger.trace("URL = "+restUrl);
		
		final Client client = ClientBuilder.newClient();
		HttpAuthenticationFeature httpFeature = HttpAuthenticationFeature.basic(basicAuthId,basicAuthPwd);	
		client.register(httpFeature);				
		WebTarget target = client.target(restUrl);	
		Response webServiceRsponse = target.request().accept(MediaType.APPLICATION_JSON).get();
		T responseEntity = webServiceRsponse.readEntity(returnEntityClass);
		long endTime = (System.currentTimeMillis() - startTime)/1000;
		logger.trace("response = "+String.valueOf(responseEntity));
		logger.trace("---------------------------------------- WEBSERVICE CALL COMPLETE IN "+endTime+" secs----------------------------------------------------------");
		
		return responseEntity;
	}
	
	public static String urlEncodeUTF8(String s) 
	{
        try 
        {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
	
	/*
	 * string compare
	 */
	public static double compareStrings(String str1, String str2)
	{
		int str1Count = (str1 == null ? 0: str1.length());
		int str2Count = (str2 == null ? 0: str2.length());		
		int lfd = StringUtils.getLevenshteinDistance(str1, str2);
		double ratio = ((double) lfd) / (Math.max(str1Count,str2Count));				
		return ratio;
		
	}
	
	/*
	
	
	/*
	 * read a properties file as a hashmap
	 */
	public static Properties getProperties(String propertiesFileName) throws Exception
	{		
		Properties properties = new Properties();	
		try
		{
			properties = loadPropertiesFromFile(propertiesFileName);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return properties;
	}
	
	private static Properties loadPropertiesFromFile(String propertyFile){
		InputStream is =null;
		try {
			is= CommonUtils.class.getClassLoader().getResourceAsStream(propertyFile);
			// InputStream is = new
			
			Properties props = new Properties() {
				private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();
	
				public Enumeration<Object> keys() {
					return Collections.<Object> enumeration(keys);
				}
	
				public Object put(Object key, Object value) {
					keys.add(key);
					return super.put(key, value);
				}
			};
			props.load(is);
			return props;
		}catch(Exception e){
			logger.error("Could Not Read Property File : " + propertyFile + ", Error : " + e.getMessage());
			e.printStackTrace();
		}finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return null;
		
		
	}
	/*
	 * read properties file as properties
	 */
	public static HashMap<String,String> getPropertiesMap(String propertiesFileName) throws Exception
	{
		HashMap<String, String> propertiesMap = new HashMap<String, String>();		 
		try
		{
			Properties properties = getProperties(propertiesFileName);
			
			for (String key : properties.stringPropertyNames()) 
			{
			    String value = properties.getProperty(key);
			    propertiesMap.put(key, value);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
		return propertiesMap;
	}
	
	/*
	 * get JSON object from json
	 */
	public static JsonObject buildJsonObject(String JsonString)
	{
		JsonParser parser = new JsonParser();
		JsonObject jsonObject = null;
		if (StringUtils.isNotBlank(JsonString))
			jsonObject = parser.parse(JsonString).getAsJsonObject();
		else;
		
		return jsonObject;
	}
	
	
	public static boolean isNumeric(String str) {
		boolean isNumeric = false;
		if(isNotEmpty(str) && str.matches("^-?[0-9]\\d*(\\.\\d+)?$")) {
			isNumeric = true;
		}
		return isNumeric;
	}
	
	
	public static String encodeBase64(String str)
	{
		// encode data on your side using BASE64
		byte[]   bytesEncoded = Base64.encodeBase64(str.getBytes());
		return new String(bytesEncoded);
	}
	
	public static String encodeBase64UrlSafe(String str)
	{
		byte[]   bytesEncoded = Base64.encodeBase64URLSafe(str.getBytes());
		return new String(bytesEncoded);
	}
	
	public static byte[] decodeBase64(String base64String)
	{
		logger.trace(" Decoding the base 64 string to byte stream ");
		return Base64.decodeBase64(base64String);
	}
	
	public static String decodeBase64ToString(String base64String)
	{
		logger.trace(" Decoding the base 64 string to byte stream ");
		return new String(Base64.decodeBase64(base64String));
	}
	
	public static Date getDateAfterDays(int numberOfDays) {
		GregorianCalendar gregCalendar = new GregorianCalendar();
		gregCalendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
		return gregCalendar.getTime();
	}
	
	public static long findDifferenceInDates(Date date1, Date date2) {
		long diff = date2.getTime() - date1.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		return days;
	}

	public static void logEyeCatchingMessage(String msg, boolean isError) 
	{
		String msgHead = isError ? "ERROR":"WARNING";		
					
		logger.trace("*****************************************"+msgHead+"***************************************************");
		logger.trace("*                                                                                                     *");
		logger.trace("*                                                                                                     *");
		logger.trace("*  "+msg+"                                                                                            *");
		logger.trace("*                                                                                                     *");
		logger.trace("*                                                                                                     *");
		logger.trace("*************************************"+msgHead+" "+" END "+"*******************************************");
		
	}
	
	
	public static <E> E fromJsonFile(String jsonFilePath, Class<E> destClass) {
		String jsonFile = readFile(jsonFilePath);
		E resultObj = fromJson(jsonFile, destClass);
		return resultObj;
	}
	
	public static BigDecimal convertToMilesFroKM(String valueInKM) {
		if(isNotEmpty(valueInKM)) {
			BigDecimal valueInB = new BigDecimal(valueInKM);
			valueInB = valueInB.multiply(mileUnit);
			return valueInB;
		}
		
		return null;
	}
	
	public static BigDecimal roundDecimals(BigDecimal value, int noOfDigits) {
		if(value != null) {
			value = value.setScale(noOfDigits, RoundingMode.CEILING);
			return value;
		}
		
		return null;
	}
	
	public static Set<JsonElement> setOfElements(JsonArray arr) 
	{ 
        Set<JsonElement> set = new HashSet<JsonElement>(); 
        for (JsonElement j: arr) { 
            set.add(j); 
        } 
        return set; 
    } 
	
	public static boolean isEquals(JsonArray obj1, JsonArray obj2)
	{
		if (obj1 == null && obj2 == null) return true; 
		if (obj1 == null || obj2 == null) return false;   
		Set<JsonElement> arr1elems =  setOfElements(obj1); 
		Set<JsonElement> arr2elems =  setOfElements(obj2); 
		return arr1elems.equals(arr2elems);
	}
	
	public static String removeUnicode(String input){
//	    StringBuffer buffer = new StringBuffer(input.length());
//	    for (int i =0; i < input.length(); i++){
//	        if ((int)input.charAt(i) > 256){
//	        buffer.append("\\u").append(Integer.toHexString((int)input.charAt(i)));
//	        } else {
//	            if ( input.charAt(i) == '\n'){
//	                buffer.append("\\n");
//	            } else {
//	                buffer.append(input.charAt(i));
//	            }
//	        }
//	    }
//	    return buffer.toString();
		
		return input.replaceAll("\\p{C}", "?");
		
	}
	
	
	public static float round(float d, int decimalPlace) 
	{
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
	
	

	 public static Map<String,String> jsonToMap(Object obj)
	 {
		 String json = toJson(obj);
		 Type t = new TypeToken<Map<String, String>>(){}.getType();
		 Gson gson = new GsonBuilder().registerTypeAdapter(t, new FlattenDeserializer()).create();
		 Map<String, String> map = gson.fromJson(json, t);
	     
		 System.out.print(" About to deserialize = "+json);
		 
		 System.out.println(CommonUtils.toJson(map));
		 System.out.println("-------------------");
		 
		 return map;
	 	
	 }

	
	 
	 
		
}

