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


public class UpdateInRoomAmenitiesServlet extends HttpServlet {
	/**
	 * Allow managers and administrators to update inRoomAmenities.
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

				/*Update hotel's InRoom Amenities.*/
				else{
					String roomamenitiesstring=" ";
					String roomamenitiesstring_j=" ";
					String roomamenitiescontent[] = request.getParameterValues("roomamenities");
					hotel.setProperty("roomamenities", Arrays.asList(roomamenitiescontent));
					String roomotheramenitiescontent = htmlFilter(request.getParameter("roomotheramenities"));
					String roomotheramenitiescontent_j = htmlFilter(request.getParameter("roomotheramenities_j"));
					List<String> tmp = new ArrayList<String>(Arrays.asList(roomamenitiescontent)); 
					String otherchecked="";
					if (roomotheramenitiescontent.length()>0)
						otherchecked="checked";

					String[][] roomdata = new String[][] {
							{"roomtoilet",	checklistfor(tmp, "Toilet"),	"Toilet, ", "トイレ、" },
							{"roombath", 	checklistfor(tmp, "Bath/Shower"),		"Bath, ", "お風呂、" },
							{"roomkitchen", checklistfor(tmp, "Kitchen/Kitchenette"),     "Kitchen/Kitchenette, ", "キッチン、" },
							{"roommicrowave", checklistfor(tmp, "Microwave"),     "Microwave, ", "電気レンジ、" },
							{"roomfridge", checklistfor(tmp, "Fridge"),     "Fridge, ", "冷蔵庫、" },
							{"roomheater", checklistfor(tmp, "Heater"),     "Heater, ", "ヘアドライヤー、" },
							{"roomac", checklistfor(tmp, "A/C"),     "A/C, ", "エアコン、" },
							{"roomtv", checklistfor(tmp, "Local TV"),     "Local TV, ", "テレビ、" },
							{"roomcable", checklistfor(tmp, "Cable or Satelite"),     "Cable or Satelite, ", "ケーブルまたはサテライトテレビ、" },
							{"roomphone", checklistfor(tmp, "Telephone"),     "Telephone, ", "電話	、" },
							{"roomwifi", checklistfor(tmp, "Wifi"),     "Wifi, ", "無線LAN、" },
							{"roomother", otherchecked,     (roomotheramenitiescontent + ", "), (roomotheramenitiescontent_j + "、") }
					};

					for (String[] row : roomdata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							roomamenitiesstring=(roomamenitiesstring + row[2]);
							roomamenitiesstring_j=(roomamenitiesstring_j + row[3]);
						}
					}
					int j=roomamenitiesstring.length();
					if (j>1)
						roomamenitiesstring=(roomamenitiesstring.substring(0, j-2)+ ".");
					j=roomamenitiesstring_j.length();
					if (j>1)
						roomamenitiesstring_j=(roomamenitiesstring_j.substring(0, j-1)+ "");

					hotel.setProperty("roomamenitiesstring", roomamenitiesstring);
					hotel.setProperty("roomamenitiesstring_j", roomamenitiesstring_j);
					hotel.setProperty("roomotheramenities", roomotheramenitiescontent);
					hotel.setProperty("roomotheramenities_j", roomotheramenitiescontent_j);
					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+language+"?hotelKeyString="+hotelKeyString+"&msg=Room%20Amenities%20updated");
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

	private static String checklistfor(List<String> amenitylist, String amenity) {
		if (amenitylist.contains(amenity)){
			return ("checked");
		}else{
			return ("");
		}
	}
}