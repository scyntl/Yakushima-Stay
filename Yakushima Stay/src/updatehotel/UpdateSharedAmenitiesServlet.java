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

public class UpdateSharedAmenitiesServlet extends HttpServlet {
	/**
	 * Allow managers and administrators to update sharedAmenities.
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
			response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=nokey");
		else{
			if (hotelKeyString.equals("null"))
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoKey");
			else
				hotelKey = KeyFactory.stringToKey(hotelKeyString);
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
		if (user==null)
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		else{
			/*Check that user is in the hotel's manager list.*/
			String userID = user.getUserId();
			try {
				Entity hotel = datastore.get(hotelKey);
				List<String> managerList = new ArrayList<String>();
				managerList=(List<String>) hotel.getProperty("managerList");
				if (managerList==null|managerList.indexOf(userID)<0)
					response.sendRedirect("/updatehotelhome"+language+"?error=HotelManagerMismatch");

				/*Update hotel's Shared Amenities.*/
				else{
					String sharedamenitiesstring=" ";
					String sharedamenitiesstring_j=" ";
					String sharedamenitiescontent[] = request.getParameterValues("sharedamenities");
					hotel.setProperty("sharedamenities", Arrays.asList(sharedamenitiescontent));
					String sharedotheramenitiescontent = htmlFilter(request.getParameter("sharedotheramenities"));
					String sharedotheramenitiescontent_j = htmlFilter(request.getParameter("sharedotheramenities_j"));
					List<String> tmp = new ArrayList<String>(Arrays.asList(sharedamenitiescontent)); 
					String otherchecked="";
					if (sharedotheramenitiescontent.length()>0)
						otherchecked="checked";

					String[][] shareddata = new String[][] {
							{"sharedtoilet",	checklistfor(tmp, "Toilet"),	"Toilet, ", "トイレ、" },
							{"sharedshower", 	checklistfor(tmp, "Shower"),		"Shower, ", "シャワー、" },
							{"sharedindoorbath", checklistfor(tmp, "Indoor Bath"),     "Indoor Bath, ", "お風呂、" },
							{"sharedpublicbath", checklistfor(tmp, "Public Bath"),     "Public Bath, ", "共同浴場、" },
							{"sharedoutdoorbath", checklistfor(tmp, "Outdoor Bath"),     "Outdoor Bath, ", "露天風呂、" },
							{"sharedsauna", checklistfor(tmp, "Sauna"),     "Sauna, ", "サウナ、" },
							{"sharedspa", checklistfor(tmp, "Spa"),     "Spa, ", "スパ、" },
							{"sharedpool", checklistfor(tmp, "Pool"),     "Pool, ", "プール、" },
							{"sharedkitchen", checklistfor(tmp, "Kitchen/Kitchenette"),     "Kitchen/Kitchenette, ", "キッチン、" },
							{"sharedmicrowave", checklistfor(tmp, "Microwave"),     "Microwave", "電子レンジ、" },
							{"sharedfridge", checklistfor(tmp, "Fridge"),     "Fridge, ", "冷蔵庫、" },
							{"sharedac", checklistfor(tmp, "A/C"),     "Central Airconditioning ", "エアコン、" },
							{"sharedheater", checklistfor(tmp, "Heater"),     "Central Heating, ", "ヒーター、" },
							{"sharedtv", checklistfor(tmp, "Television"),     "Television, ", "テレビ、" },
							{"shareddvd", checklistfor(tmp, "DVD Player"),     "DVD Player, ", "DVDプレイヤー、" },
							{"sharedcomputer", checklistfor(tmp, "Computer"),     "Computer, ", "パソコン、" },
							{"sharedsmokingarea", checklistfor(tmp, "Smoking Area"),     "Smoking Area, ", "喫煙コーナー、" },
							{"sharedgiftshop", checklistfor(tmp, "Gift Shop"),     "Gift Shop, ", "ギフトショップ、" },
							{"sharedother", otherchecked,     (sharedotheramenitiescontent + ", "), (sharedotheramenitiescontent_j + "、") }
					};

					for (String[] row : shareddata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							sharedamenitiesstring=(sharedamenitiesstring + row[2]);
							sharedamenitiesstring_j=(sharedamenitiesstring_j + row[3]);
						}
					}
					int j=sharedamenitiesstring.length();
					if (j>1)
						sharedamenitiesstring=(sharedamenitiesstring.substring(0, j-2)+ ".");
					j=sharedamenitiesstring_j.length();
					if (j>1)
						sharedamenitiesstring_j=(sharedamenitiesstring_j.substring(0, j-1)+ "");

					hotel.setProperty("sharedamenitiesstring", sharedamenitiesstring);
					hotel.setProperty("sharedamenitiesstring_j", sharedamenitiesstring_j);
					hotel.setProperty("sharedotheramenities", sharedotheramenitiescontent);
					hotel.setProperty("sharedotheramenities_j", sharedotheramenitiescontent_j);
					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+language+"?hotelKeyString="+hotelKeyString+"&msg=Shared%20amenities%20updated");
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