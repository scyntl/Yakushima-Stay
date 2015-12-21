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

public class UpdateCloseServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		/*Get hotelKeyString and permissions(whether or not user expects to be returned to an admin page.*/
		String hotelKeyString = request.getParameter("hotelKeyString");
		String permissions = request.getParameter("permissions");
		if (permissions==null)
		{permissions="";}
		else
		{permissions="admin";}

		/*Check validity of hotelKeyString*/
		Key hotelKey=null; 
		if (hotelKeyString==null) 
		{response.sendRedirect("/updatehotel"+permissions+"home?error=nokey");}
		else{
			if (hotelKeyString.equals("null"))
			{response.sendRedirect("/updatehotel"+permissions+"home?error=NoKey");}
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
					response.sendRedirect("/updatehotelhome?error=HotelManagerMismatch");

				/*Update hotel's vicinity data.*/
				else{
					String closecontent[] = request.getParameterValues("close");
					String busstopname = htmlFilter(request.getParameter("busstopname"));
					String busstopname_j = htmlFilter(request.getParameter("busstopname_j"));
					hotel.setProperty("close", Arrays.asList(closecontent));
					List<String> tmp = new ArrayList<String>(Arrays.asList(closecontent));

					String checkport =checklistfor(tmp, "Port");
					String checkportAnbo =checklistfor(tmp, "PortAnbo");
					String checkairport =checklistfor(tmp, "Airport");
					String checkbusstop =checklistfor(tmp, "Bus Stop");
					String checksupermarket =checklistfor(tmp, "Supermarket");
					String checkconveniencestore =checklistfor(tmp, "Convenience Store");
					String checkrestaurant =checklistfor(tmp, "Restaurant/Cafe");
					String checkpost =checklistfor(tmp, "Post Office (with ATM)");

					hotel.setProperty("closeport", checkport);
					hotel.setProperty("closeportAnbo", checkportAnbo);
					hotel.setProperty("closeairport", checkairport);
					hotel.setProperty("closebus", checkbusstop);
					hotel.setProperty("busstopname", busstopname);
					hotel.setProperty("busstopname_j", busstopname_j);
					hotel.setProperty("closesuper", checksupermarket);
					hotel.setProperty("closeconvenience", checkconveniencestore);
					hotel.setProperty("closerestaurant", checkrestaurant);
					hotel.setProperty("closepost", checkpost);

					String time0j = "目の前です！";
					String time1j = "5分以下";
					String time2j = "5分~10分";
					String time3j = "10分~15分";
					String time4j = "10分~20分";
					String porttime_j ="";
					String porttimeAnbo_j ="";
					String airporttime_j ="";
					String busstoptime_j ="";
					String supermarkettime_j ="";
					String conveniencestoretime_j ="";
					String restauranttime_j ="";
					String posttime_j ="";

					String porttime = request.getParameter("porttime");
					hotel.setProperty("porttime", porttime);
					if (porttime.equals("under 5 min")) {
						hotel.setProperty("porttime5", "selected"); 
						hotel.setProperty("porttime10", "");
						hotel.setProperty("porttime10", "");
						porttime_j=time1j;
					}
					else if (porttime.equals("5~10 min")) {
						hotel.setProperty("porttime10", "selected"); 
						hotel.setProperty("porttime5", "");
						hotel.setProperty("porttime15", "");
						porttime_j=time2j;
					}
					else if (porttime.equals("10~20 min")) {
						hotel.setProperty("porttime15", "selected"); 
						hotel.setProperty("porttime5", "");
						hotel.setProperty("porttime10", "");
						porttime_j=time4j;
					}
					
					String porttimeAnbo = request.getParameter("porttimeAnbo");
					hotel.setProperty("porttimeAnbo", porttimeAnbo);
					if (porttimeAnbo.equals("under 5 min")) {
						hotel.setProperty("porttimeAnbo5", "selected"); 
						hotel.setProperty("porttimeAnbo10", "");
						hotel.setProperty("porttimeAnbo10", "");
						porttimeAnbo_j=time1j;
					}
					else if (porttimeAnbo.equals("5~10 min")) {
						hotel.setProperty("porttimeAnbo10", "selected"); 
						hotel.setProperty("porttimeAnbo5", "");
						hotel.setProperty("porttimeAnbo15", "");
						porttimeAnbo_j=time2j;
					}
					else if (porttimeAnbo.equals("10~20 min")) {
						hotel.setProperty("porttimeAnbo15", "selected"); 
						hotel.setProperty("porttimeAnbo5", "");
						hotel.setProperty("porttimeAnbo10", "");
						porttimeAnbo_j=time4j;
					}

					String airporttime = request.getParameter("airporttime");
					hotel.setProperty("airporttime", airporttime);
					if (airporttime.equals("under 5 min")) {
						hotel.setProperty("airporttime5", "selected"); 
						hotel.setProperty("airporttime10", "");
						hotel.setProperty("airporttime10", "");
						airporttime_j=time1j;
					}
					else if (airporttime.equals("5~10 min")) {
						hotel.setProperty("airporttime10", "selected"); 
						hotel.setProperty("airporttime5", "");
						hotel.setProperty("airporttime15", "");
						airporttime_j=time2j;
					}
					else if (airporttime.equals("10~15 min")) {
						hotel.setProperty("airporttime15", "selected"); 
						hotel.setProperty("airporttime5", "");
						hotel.setProperty("airporttime10", "");
						airporttime_j=time3j;
					}
					String busstoptime = request.getParameter("busstoptime");
					hotel.setProperty("busstoptime", busstoptime);
					if (busstoptime.equals("right there!")) {
						hotel.setProperty("busstoptime0", "selected"); 
						hotel.setProperty("busstoptime5", ""); 
						hotel.setProperty("busstoptime10", "");
						hotel.setProperty("busstoptime15", "");
						busstoptime_j=time0j;
					}
					if (busstoptime.equals("under 5 min")) {
						hotel.setProperty("busstoptime0", "");
						hotel.setProperty("busstoptime5", "selected"); 
						hotel.setProperty("busstoptime10", "");
						hotel.setProperty("busstoptime15", "");
						busstoptime_j=time1j;
					}
					else if (busstoptime.equals("5~10 min")) {
						hotel.setProperty("busstoptime0", "");
						hotel.setProperty("busstoptime5", "");
						hotel.setProperty("busstoptime10", "selected"); 
						hotel.setProperty("busstoptime15", "");
						busstoptime_j=time2j;;
					}
					else if (busstoptime.equals("10~15 min")) {
						hotel.setProperty("busstoptime0", "");
						hotel.setProperty("busstoptime5", "");
						hotel.setProperty("busstoptime10", "");
						hotel.setProperty("busstoptime15", "selected"); 
						busstoptime_j=time3j;
					}
					String supermarkettime = request.getParameter("supermarkettime");
					hotel.setProperty("supermarkettime", supermarkettime);
					if (supermarkettime.equals("right here!")) {
						hotel.setProperty("supermarkettime0", "selected"); 
						hotel.setProperty("supermarkettime5", ""); 
						hotel.setProperty("supermarkettime10", "");
						hotel.setProperty("supermarkettime15", "");
						supermarkettime_j=time0j;
					}
					else if (supermarkettime.equals("under 5 min")) {
						hotel.setProperty("supermarkettime0", ""); 
						hotel.setProperty("supermarkettime5", "selected"); 
						hotel.setProperty("supermarkettime10", "");
						hotel.setProperty("supermarkettime15", "");
						supermarkettime_j=time1j;
					}
					else if (supermarkettime.equals("5~10 min")) {
						hotel.setProperty("supermarkettime0", ""); 
						hotel.setProperty("supermarkettime5", "");
						hotel.setProperty("supermarkettime10", "selected"); 
						hotel.setProperty("supermarkettime15", "");
						supermarkettime_j=time2j;
					}
					else if (supermarkettime.equals("10~15 min")) {
						hotel.setProperty("supermarkettime0", ""); 
						hotel.setProperty("supermarkettime5", "");
						hotel.setProperty("supermarkettime10", "");
						hotel.setProperty("supermarkettime15", "selected"); 
						supermarkettime_j=time3j;
					}
					String conveniencestoretime = request.getParameter("conveniencestoretime");
					hotel.setProperty("conveniencestoretime", conveniencestoretime);
					if (conveniencestoretime.equals("right here!")) {
						hotel.setProperty("conveniencestoretime0", "selected"); 
						hotel.setProperty("conveniencestoretime5", ""); 
						hotel.setProperty("conveniencestoretime10", "");
						hotel.setProperty("conveniencestoretime15", "");
						conveniencestoretime_j=time0j;
					}
					else if (conveniencestoretime.equals("under 5 min")) {
						hotel.setProperty("conveniencestoretime0", ""); 
						hotel.setProperty("conveniencestoretime5", "selected"); 
						hotel.setProperty("conveniencestoretime10", "");
						hotel.setProperty("conveniencestoretime15", "");
						conveniencestoretime_j=time1j;
					}
					else if (conveniencestoretime.equals("5~10 min")) {
						hotel.setProperty("conveniencestoretime0", ""); 
						hotel.setProperty("conveniencestoretime5", "");
						hotel.setProperty("conveniencestoretime10", "selected"); 
						hotel.setProperty("conveniencestoretime15", "");
						conveniencestoretime_j=time2j;
					}
					else if (conveniencestoretime.equals("10~15 min")) {
						hotel.setProperty("conveniencestoretime0", "");
						hotel.setProperty("conveniencestoretime5", "");
						hotel.setProperty("conveniencestoretime10", "");
						hotel.setProperty("conveniencestoretime15", "selected"); 
						conveniencestoretime_j=time3j;
					}
					String restauranttime = request.getParameter("restauranttime");
					hotel.setProperty("restauranttime", restauranttime);
					if (restauranttime.equals("right there!")) {
						hotel.setProperty("restauranttime0", "selected"); 
						hotel.setProperty("restauranttime5", ""); 
						hotel.setProperty("restauranttime10", "");
						hotel.setProperty("restauranttime15", "");
						restauranttime_j=time0j;
					}
					else if (restauranttime.equals("under 5 min")) {
						hotel.setProperty("restauranttime0", ""); 
						hotel.setProperty("restauranttime5", "selected"); 
						hotel.setProperty("restauranttime10", "");
						hotel.setProperty("restauranttime15", "");
						restauranttime_j=time1j;
					}
					else if (restauranttime.equals("5~10 min")) {
						hotel.setProperty("restauranttime0", ""); 
						hotel.setProperty("restauranttime5", "");
						hotel.setProperty("restauranttime10", "selected"); 
						hotel.setProperty("restauranttime15", "");
						restauranttime_j=time2j;
					}
					else if (restauranttime.equals("10~15 min")) {
						hotel.setProperty("restauranttime0", ""); 
						hotel.setProperty("restauranttime5", "");
						hotel.setProperty("restauranttime10", "");
						hotel.setProperty("restauranttime15", "selected"); 
						restauranttime_j=time3j;
					}

					String posttime = request.getParameter("posttime");
					hotel.setProperty("posttime", posttime);
					if (posttime.equals("right here!")) {
						hotel.setProperty("posttime0", "selected"); 
						hotel.setProperty("posttime5", ""); 
						hotel.setProperty("posttime10", "");
						hotel.setProperty("posttime15", "");
						posttime_j=time0j;
					}
					else if (posttime.equals("under 5 min")) {
						hotel.setProperty("posttime0", ""); 
						hotel.setProperty("posttime5", "selected"); 
						hotel.setProperty("posttime10", "");
						hotel.setProperty("posttime15", "");
						posttime_j=time1j;
					}
					else if (posttime.equals("5~10 min")) {
						hotel.setProperty("posttime0", ""); 
						hotel.setProperty("posttime5", "");
						hotel.setProperty("posttime10", "selected"); 
						hotel.setProperty("posttime15", "");
						posttime_j=time2j;
					}
					else if (posttime.equals("10~15 min")) {
						hotel.setProperty("posttime0", ""); 
						hotel.setProperty("posttime5", "");
						hotel.setProperty("posttime10", "");
						hotel.setProperty("posttime15", "selected"); 
						posttime_j=time3j;
					}            

					String closestring="";
					String closestring_j="";
					int j = closecontent.length;
					if (j>1){
						closestring="<ul>";
						closestring_j="<ul>";
						if (checkport.equals("checked")){
							closestring=(closestring + "<li>the port (Miyanoura, " + porttime + ")");
							closestring_j=(closestring_j + "<li>港 (宮之浦、" + porttime_j + ")");
						}
						if (checkportAnbo.equals("checked")){
							closestring=(closestring + "<li>the port (Anbo, " + porttimeAnbo + ")");
							closestring_j=(closestring_j + "<li>港 (安房、" + porttimeAnbo_j + ")");
						}
						if (checkairport.equals("checked")){
							closestring=(closestring + "<li>the airport (" + airporttime + ")");
							closestring_j=(closestring_j + "<li>空港 (" + airporttime_j + ")");
						}
						if (checkbusstop.equals("checked")){
							closestring=(closestring + "<li>a bus stop (" + busstopname + ", " + busstoptime + ")");
							closestring_j=(closestring_j + "<li>バス停 (" + busstopname_j + ", " + busstoptime_j + ")");
						}
						if (checksupermarket.equals("checked")){
							closestring=(closestring + "<li>a supermarket (" + supermarkettime + ")");
							closestring_j=(closestring_j + "<li>スーパー (" + supermarkettime_j + ")");
						}
						if (checkconveniencestore.equals("checked")){
							closestring=(closestring + "<li>a convenience store (" + conveniencestoretime + ")");
							closestring_j=(closestring_j + "<li>コンビニかストア (" + conveniencestoretime_j + ")");
						} 
						if (checkrestaurant.equals("checked")){
							closestring=(closestring + "<li>a restaurant or cafe (" + restauranttime + ")");
							closestring_j=(closestring_j + "<li>飲食店かカフェ (" + restauranttime_j + ")");
						}
						if (checkpost.equals("checked")){
							closestring=(closestring + "<li>a post office (with ATM) (" + posttime + ")");
							closestring_j=(closestring_j + "<li>（ATMのある）郵便局 (" + posttime_j + ")");
						}

						closestring=(closestring + "</ul>");
						closestring_j=(closestring_j + "</ul>");
					}

					hotel.setProperty("closestring", closestring);
					hotel.setProperty("closestring_j", closestring_j);        


					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);

				} 
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {	
				response.sendRedirect("/updatehotel"+permissions+"home?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+"?hotelKeyString="+hotelKeyString+"&msg=Payment%20options%20updated");
	}

	private static String checklistfor(List<String> amenitylist, String amenity) {
		if (amenitylist.contains(amenity)){
			return ("checked");
		}else{
			return ("");
		}
	}

	private static String htmlFilter(String message) {
		if (message == null) return null;
		int len = message.length();
		StringBuffer result = new StringBuffer(len + 20);
		char aChar;
		for (int i = 0; i < len; ++i) {
			aChar = message.charAt(i);
			switch (aChar) {
			case '<': result.append("&lt;"); break;
			case '>': result.append("&gt;"); break;
			case '&': result.append("&amp;"); break;
			case '"': result.append("&quot;"); break;
			default: result.append(aChar);
			}
		}
		return (result.toString());
	}



}