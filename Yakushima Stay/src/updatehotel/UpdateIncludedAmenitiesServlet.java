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

public class UpdateIncludedAmenitiesServlet extends HttpServlet {
	/**
	 * Allow managers and administrators to update includedAmenities.
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

				/*Update hotel's Included Amenities.*/
				else{
					String includedamenitiesstring=" ";
					String includedamenitiesstring_j=" ";
					String includedamenitiescontent[] = request.getParameterValues("includedamenities");
					hotel.setProperty("includedamenities", Arrays.asList(includedamenitiescontent));
					String includedotheramenitiescontent = htmlFilter(request.getParameter("includedotheramenities"));
					String includedotheramenitiescontent_j = htmlFilter(request.getParameter("includedotheramenities_j"));
					List<String> tmp = new ArrayList<String>(Arrays.asList(includedamenitiescontent)); 
					String otherchecked="";
					if (includedotheramenitiescontent.length()>0)
						otherchecked="checked";

					String[][] incdata = new String[][] {
							{"incwasher",	checklistfor(tmp, "Washer"),	"Washer, ", "洗濯機、" },
							{"incdryer", 	checklistfor(tmp, "Dryer"),		"Dryer, ", "乾燥機、" },
							{"inclaundry", checklistfor(tmp, "Laundry Service"),     "Laundry Service, ", "ランドリーサービス、" },
							{"incbodysoap", checklistfor(tmp, "Body Soap"),     "Body Soap, ", "ボディソープ" },
							{"incshampoo", checklistfor(tmp, "Shampoo"),     "Shampoo, ", "シャンプー" },
							{"incconditioner", checklistfor(tmp, "Conditioner"),     "Conditioner, ", "コンディショナー" },
							{"incrazor", checklistfor(tmp, "Razor"),     "Razor, ", "カミソリ" },
							{"inctoothbrush", checklistfor(tmp, "Toothbrush"),     "Toothbrush, ", "歯ブラシ" },
							{"incpajamas", checklistfor(tmp, "Pajamas/Yukata"),     "Pajamas/Yukata, ", "パジャマ・夕方" },
							{"inctowel", checklistfor(tmp, "Towel"),     "Towel, ", "タオル、" },
							{"inciron", checklistfor(tmp, "Iron"),     "Iron, ", "アイアン、" },
							{"inchairdryer", checklistfor(tmp, "Hair Dryer"),     "Hair Dryer, ", "ヘアドライヤー、" },
							{"incdvd", checklistfor(tmp, "DVD"),     "DVD, ", "DVDプレーヤー、" },
							{"incwifi", checklistfor(tmp, "Wifi"),     "Wifi, ", "無線LAN、" },
							{"inccomputer", checklistfor(tmp, "Computer"),     "Computer/Internet, ", "パソコン・ネット、" },
							{"inclocker", checklistfor(tmp, "Locker"),     "Locker, ", "ロッカー、" },
							{"incbicycle", checklistfor(tmp, "Bicycles"),     "Bicyces, ", "自転車、" },
							{"inccar", checklistfor(tmp, "Car"),     "Car, ", "レンタカー、" },
							{"incluggage", checklistfor(tmp, "Luggage Holding"),     "Luggage Holding, ", "手荷物預かり、" },
							{"incbus", checklistfor(tmp, "Welcome Bus"),     "One-time pickup/drop-off, ", "送迎" },
							{"incrazor", checklistfor(tmp, "Razor"),     "Razor, ", "カミソリ" },
							{"inctoothbrush", checklistfor(tmp, "Toothbrush"),     "Toothbrush, ", "歯ブラシ" },
							{"incpajamas", checklistfor(tmp, "Pajamas/Yukata"),     "Pajamas/Yukata, ", "パジャマ・夕方" },
							{"incparkinglot", checklistfor(tmp, "Parking Lot"),     "Parking Lot, ", "駐車場" },
							{"inctents", checklistfor(tmp, "Tents"),     "Tents, ", "テント、" },
							{"incsleepingbags", checklistfor(tmp, "Sleeping Bags"),     "Sleeping Bags, ", "シラフ、" },
							{"inchikinggear", checklistfor(tmp, "Hiking Gear"),     "Hiking Gear, ", "登山用品、" },
							{"incbbqgrill", checklistfor(tmp, "BBQ Grill"),     "BBQ Grill, ", "BBQ施設、" },
							{"incother", otherchecked,     (includedotheramenitiescontent + ", "), (includedotheramenitiescontent_j + "、") }
					};

					for (String[] row : incdata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							includedamenitiesstring=(includedamenitiesstring + row[2]);
							includedamenitiesstring_j=(includedamenitiesstring_j + row[3]);
						}
					}
					int j=includedamenitiesstring.length();
					if (j>1)
						includedamenitiesstring=(includedamenitiesstring.substring(0, j-2)+ ".");
					j=includedamenitiesstring_j.length();
					if (j>1)
						includedamenitiesstring_j=(includedamenitiesstring_j.substring(0, j-1)+ "");

					hotel.setProperty("includedamenitiesstring", includedamenitiesstring);
					hotel.setProperty("includedamenitiesstring_j", includedamenitiesstring_j);
					hotel.setProperty("includedotheramenities", includedotheramenitiescontent);
					hotel.setProperty("includedotheramenities_j", includedotheramenitiescontent_j);
					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+language+"?hotelKeyString="+hotelKeyString+"&msg=Included%20amenities%20updated");
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
		if (amenitylist.contains(amenity))
			return ("checked");
		else
			return ("");
	}
}