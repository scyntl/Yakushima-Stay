package updatehotel;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateBasicsServlet extends HttpServlet {
	/**
	 * Allow managers to update Basic Information for the hotel.
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		/*Get hotelKeyString and permissions(whether or not user expects to be returned to an admin or j page.*/
		String hotelKeyString = request.getParameter("hotelKeyString");
		String permissions = request.getParameter("permissions");
		if (permissions==null)
			permissions="";
		else
			permissions="admin";
		String language = request.getParameter("language");
		if (language==null)
			language="";
		else
			language="j";

		/*Check validity of hotelKeyString*/
		Key hotelKey=null; 
		if (hotelKeyString==null) 
		{response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=nokey");}
		else{
			if (hotelKeyString.equals("null"))
			{response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoKey");}
			else
			{hotelKey = KeyFactory.stringToKey(hotelKeyString);}
		}

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		UserService userService = UserServiceFactory.getUserService();

		/*Find the hotel with your hotel Key.*/
		Query query = new Query("Hotel", hotelKey).addSort("date",
				Query.SortDirection.DESCENDING);
		List<Entity> hotels = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(1));

		/*Check that user is logged in.*/
		User user = userService.getCurrentUser();
		if (user==null){
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}
		else{
			/*Check that user is in the hotel's manager list.*/
			String userID = user.getUserId();
			try {
				Entity hotel = datastore.get(hotelKey);
				List<String> managerList = new ArrayList<String>();
				managerList=(List<String>) hotel.getProperty("managerList");
				if (managerList==null|managerList.indexOf(userID)<0)
					response.sendRedirect("/updatehotelhome"+language+"?error=HotelManagerMismatch");

				/*Update hotel's published status.*/
				else{

					/*Update participations.
					 *(This is pretty long-winded, but it works for now. )
					 **/
					String touristassociation="";
					String travelporter="";
					String yesyakushima="";
					String participationcontent[] = request.getParameterValues("participation");
					List<String> participationtmp = new ArrayList<String>(Arrays.asList(participationcontent));
					if (participationtmp.contains("Tourist Association")){
						touristassociation="checked";
					} 
					if (participationtmp.contains("Travel Porter")){
						travelporter="checked";
					} 
					if (participationtmp.contains("YesYakushima")){
						yesyakushima="checked";
					} 
					hotel.setProperty("touristassociation", touristassociation);
					hotel.setProperty("travelporter", travelporter);
					hotel.setProperty("yesyakushima", yesyakushima);
					
					
					/*Update Location
					 *(This is pretty long-winded, but it works for now. )
					 **/
					String locationcontent = request.getParameter("location");
					String area="";
					hotel.setProperty("location", locationcontent);
					hotel.setProperty("Miyanoura", "");
					hotel.setProperty("Kusukawa", "");
					hotel.setProperty("Koseda", "");
					hotel.setProperty("Anbo", "");
					hotel.setProperty("Funayuki", "");
					hotel.setProperty("Hara", "");
					hotel.setProperty("Mugio", "");
					hotel.setProperty("Onoaida", "");
					hotel.setProperty("Koshima", "");
					hotel.setProperty("Hirauchi", "");
					hotel.setProperty("Yudomari", "");
					hotel.setProperty("Nakama", "");
					hotel.setProperty("Kurio", "");
					hotel.setProperty("Nagata", "");
					hotel.setProperty("Yoshida", "");
					hotel.setProperty("Issou", "");
					hotel.setProperty("Shitoko", "");

					if (locationcontent.equals("Miyanoura")){
						hotel.setProperty("Miyanoura", "selected");
						hotel.setProperty("location_j", "宮之浦");
						hotel.setProperty("area", "East");
						hotel.setProperty("town", "Miyanoura");
					}
					else if (locationcontent.equals("Kusukawa")){
						hotel.setProperty("Kusukawa", "selected");
						hotel.setProperty("location_j", "楠川");
						hotel.setProperty("area", "East");
						hotel.setProperty("town", "Kusukawa");
					}
					else if (locationcontent.equals("Koseda")){
						hotel.setProperty("Koseda", "selected");
						hotel.setProperty("location_j", "小瀬田");
						hotel.setProperty("area", "East");
						hotel.setProperty("town", "Koseda");
					}
					else if (locationcontent.equals("Funayuki")){
						hotel.setProperty("Funayuki", "selected");
						hotel.setProperty("location_j", "船行");
						hotel.setProperty("area", "East");
					}
					else if (locationcontent.equals("Anbo")){
						hotel.setProperty("Anbo", "selected");
						hotel.setProperty("location_j", "安房");
						hotel.setProperty("area", "East");
						hotel.setProperty("town", "Anbo");
					}
					else if (locationcontent.equals("Mugio")){
						hotel.setProperty("Mugio", "selected");
						hotel.setProperty("location_j", "麦生");
						hotel.setProperty("area", "South");
						hotel.setProperty("town", "Mugio");
					}
					else if (locationcontent.equals("Hara")){
						hotel.setProperty("Hara", "selected");
						hotel.setProperty("location_j", "原");
						hotel.setProperty("area", "South");
						hotel.setProperty("town", "Hara");
					}
					else if (locationcontent.equals("Onoaida")){
						hotel.setProperty("Onoaida", "selected");
						hotel.setProperty("location_j", "尾之間");
						hotel.setProperty("area", "South");
						hotel.setProperty("town", "Onoaida");
					}
					else if (locationcontent.equals("Koshima")){
						hotel.setProperty("Koshima", "selected");
						hotel.setProperty("location_j", "小島");
						hotel.setProperty("area", "South");
						hotel.setProperty("town", "Koshima");
					}
					else if (locationcontent.equals("Hirauchi")){
						hotel.setProperty("Hirauchi", "selected");
						hotel.setProperty("location_j", "平内");
						hotel.setProperty("area", "South");
						hotel.setProperty("town", "Hirauchi");
					}
					else if (locationcontent.equals("Yudomari")){
						hotel.setProperty("Yudomari", "selected");
						hotel.setProperty("location_j", "湯泊");
						hotel.setProperty("area", "South");
						hotel.setProperty("town", "Yudomari");
					}
					else if (locationcontent.equals("Kurio")){
						hotel.setProperty("Kurio", "selected");
						hotel.setProperty("location_j", "栗生");
						hotel.setProperty("area", "West");
						hotel.setProperty("town", "Kurio");
					}
					else if (locationcontent.equals("Nakama")){
						hotel.setProperty("Nakama", "selected");
						hotel.setProperty("location_j", "中間");
						hotel.setProperty("area", "West");
						hotel.setProperty("town", "Nakama");
					}
					else if (locationcontent.equals("Nagata")){
						hotel.setProperty("Nagata", "selected");
						hotel.setProperty("location_j", "永田");
						hotel.setProperty("area", "West");
						hotel.setProperty("town", "Nagata");
					}
					else if (locationcontent.equals("Issou")){
						hotel.setProperty("Issou", "selected");
						hotel.setProperty("location_j", "一奏");
						hotel.setProperty("area", "North");
						hotel.setProperty("town", "Issou");
					}
					else if (locationcontent.equals("Yoshida")){
						hotel.setProperty("Yoshida", "selected");
						hotel.setProperty("location_j", "吉田");
						hotel.setProperty("area", "North");
						hotel.setProperty("town", "Yoshida");
					}
					else if (locationcontent.equals("Shitoko")){
						hotel.setProperty("Shitoko", "selected");
						hotel.setProperty("location_j", "志戸子");
						hotel.setProperty("area", "North");
						hotel.setProperty("town", "Shitoko");
					}


					/*Update address, phone numbers, homepage, guestsmax and price range.*/
					String addresscontent = request.getParameter("address");
					hotel.setProperty("address", addresscontent);
					String address_jcontent = request.getParameter("address_j");
					hotel.setProperty("address_j", address_jcontent);
					String telephoneacontent = request.getParameter("telephonea");
					hotel.setProperty("telephonea", telephoneacontent);
					String telephonebcontent = request.getParameter("telephoneb");
					hotel.setProperty("telephoneb", telephonebcontent);
					String telephoneccontent = request.getParameter("telephonec");
					hotel.setProperty("telephonec", telephoneccontent);
					String telephonecontent="";
					if (telephonebcontent!=""){
						telephonecontent=(telephoneacontent+"-"+telephonebcontent+"-"+telephoneccontent);
						hotel.setProperty("telephone", telephonecontent);
					}else{

						hotel.setProperty("telephonea", "");
						hotel.setProperty("telephoneb", "");
						hotel.setProperty("telephonec", "");
						hotel.setProperty("telephone", "");
					}
					String mobileacontent = request.getParameter("mobilea");
					hotel.setProperty("mobilea", mobileacontent);
					String mobilebcontent = request.getParameter("mobileb");
					hotel.setProperty("mobileb", mobilebcontent);
					String mobileccontent = request.getParameter("mobilec");
					hotel.setProperty("mobilec", mobileccontent);
					String mobilecontent="";
					if (mobilebcontent!=""){
						mobilecontent=(mobileacontent+"-"+mobilebcontent+"-"+mobileccontent);
						hotel.setProperty("mobile", mobilecontent);
					}else{
						hotel.setProperty("mobilea", "");
						hotel.setProperty("mobileb", "");
						hotel.setProperty("mobilec", "");
						hotel.setProperty("mobile", "");
						hotel.setProperty("mobile", "");
					}

					String homepagecontent = request.getParameter("homepage");
					hotel.setProperty("homepage", homepagecontent);
					String homepagecontentlink = "http://" + homepagecontent;
					hotel.setProperty("homepagelink", homepagecontentlink);
					String pricemincontent = request.getParameter("pricemin");
					int pricemin=0;
					if (!(pricemincontent==null))
						pricemin =Integer.parseInt(pricemincontent);
					hotel.setProperty("pricemin", pricemin);
					int pricemax=0;
					String pricemaxcontent = request.getParameter("pricemax");
					if (!(pricemaxcontent==null))
						pricemax =Integer.parseInt(pricemaxcontent);
					hotel.setProperty("pricemax", pricemax);
					String lowprice="false";
					String midprice="false";
					String highprice="false";
					if (pricemin<5000)
						lowprice="true";
					else if (pricemin<10001)
						midprice="true";
					else
						highprice="true";
					if (pricemax>10000)
						highprice="true";
					else if (pricemax>5000)
						midprice="true";
					else
						lowprice="true";
					hotel.setProperty("lowprice", lowprice);
					hotel.setProperty("midprice", midprice);
					hotel.setProperty("highprice", highprice);
					
					String guestsmaxcontent = request.getParameter("guestsmax");
					hotel.setProperty("guestsmax", guestsmaxcontent);


					/*Update accommodation type.
					 * (This is also long-winded, but works for now.
					 * */
					String campsitecontent="";
					String cottagescontent="";
					String minshukucontent="";
					String businesscontent="";
					String hostelcontent="";
					String ryokancontent="";
					String fullhotelcontent="";
					String typecontent[] = request.getParameterValues("type");
					List<String> tmp = new ArrayList<String>(Arrays.asList(typecontent));
					if (tmp.contains("Campsite")){
						campsitecontent="checked";
					} 
					if (tmp.contains("Cottages")){
						cottagescontent="checked";
					} 
					if (tmp.contains("Minshuku")){
						minshukucontent="checked";
					}
					if (tmp.contains("Business Hotel")){
						businesscontent="checked";
					}
					if (tmp.contains("Hostel")){
						hostelcontent="checked";
					}
					if (tmp.contains("Japanese Inn")){
						ryokancontent="checked";
					}
					if (tmp.contains("Full Hotel")){
						fullhotelcontent="checked";
					}
					String typestring="";
					String typestring_j="";
					hotel.setProperty("type", Arrays.asList(typecontent));
					int j= typecontent.length;
					String othertypecontent = htmlFilter(request.getParameter("typesother"));
					String othertypecontent_j = htmlFilter(request.getParameter("typesother_j"));
					if (j>1 | (othertypecontent != "" && othertypecontent != null)){
						typestring="<ul>";
						typestring_j="<ul>";
						if (campsitecontent.equals("checked")){
							typestring=(typestring + "<li>Campsites");
							typestring_j=(typestring_j + "<li>キャンプ場");            		
						}            	if (cottagescontent.equals("checked")){
							typestring=(typestring + "<li>Cottages");
							typestring_j=(typestring_j + "<li>コテージ");            		
						}             	if (minshukucontent.equals("checked")){
							typestring=(typestring + "<li>Minshuku/Private Inn");
							typestring_j=(typestring_j + "<li>民宿");            		
						}            	if (businesscontent.equals("checked")){
							typestring=(typestring + "<li>Business Hotel");
							typestring_j=(typestring_j + "<li>ビズネスホテル");            		
						}            	if (hostelcontent.equals("checked")){
							typestring=(typestring + "<li>Hostel");
							typestring_j=(typestring_j + "<li>ホステル");            		
						}            	if (ryokancontent.equals("checked")){
							typestring=(typestring + "<li>Japanese-Style Inn");
							typestring_j=(typestring_j + "<li>旅館");            		
						}            	if (fullhotelcontent.equals("checked")){
							typestring=(typestring + "<li>Full Hotel");
							typestring_j=(typestring_j + "<li>ホテル");            		
						}  
						if (othertypecontent != "" && othertypecontent != null){
							if (othertypecontent.length()>2){
								typestring=(typestring + "<li>" + othertypecontent);
								typestring_j=(typestring_j + "<li>" + othertypecontent_j);
							}
						}
						typestring=(typestring + "</ul>");
						typestring_j=(typestring_j + "</ul>");   
					}

					hotel.setProperty("typestring", typestring);
					hotel.setProperty("typesother", othertypecontent);
					hotel.setProperty("typestring_j", typestring_j);
					hotel.setProperty("typesother_j", othertypecontent_j);
					hotel.setProperty("campsite", campsitecontent);
					hotel.setProperty("cottages", cottagescontent);
					hotel.setProperty("minshuku", minshukucontent);
					hotel.setProperty("business", businesscontent);
					hotel.setProperty("hostel", hostelcontent);
					hotel.setProperty("ryokan", ryokancontent);
					hotel.setProperty("fullhotel", fullhotelcontent);            

					/*Update Checkin/Checkout times.*/
					String checkincontent = request.getParameter("checkin");
					hotel.setProperty("checkin", checkincontent);
					String checkoutcontent = request.getParameter("checkout");
					hotel.setProperty("checkout", checkoutcontent);

					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {	
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+language+"?hotelKeyString="+hotelKeyString+"&msg=Payment%20options%20updated");
	}
	
private static String htmlFilter(String message) {
	if (message == null)
		return null;
	int len = message.length();
	StringBuffer result = new StringBuffer(len + 20);
	char aChar;

	for (int i = 0; i < len; ++i) {
		aChar = message.charAt(i);
		switch (aChar) {
		case '<':
			result.append("&lt;");
			break;
		case '>':
			result.append("&gt;");
			break;
		case '&':
			result.append("&amp;");
			break;
		case '"':
			result.append("&quot;");
			break;
		default:
			result.append(aChar);
		}
	}
	return (result.toString());
}
}