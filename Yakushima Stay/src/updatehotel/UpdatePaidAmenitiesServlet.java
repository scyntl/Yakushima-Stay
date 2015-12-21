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

public class UpdatePaidAmenitiesServlet extends HttpServlet {
	/**
	 * 
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

				/*Update hotel's Paid Amenities.*/
				else{
					String paidamenitiesstring=" ";
					String paidamenitiesstring_j=" ";
					String paidamenitiescontent[] = request.getParameterValues("paidamenities");
					hotel.setProperty("paidamenities", Arrays.asList(paidamenitiescontent));
					String paidotheramenitiescontent = htmlFilter(request.getParameter("paidotheramenities"));
					String paidotheramenitiescontent_j = htmlFilter(request.getParameter("paidotheramenities_j"));
					List<String> tmp = new ArrayList<String>(Arrays.asList(paidamenitiescontent)); 

					String otherchecked="";
					if (paidotheramenitiescontent.length()>0)
						otherchecked="checked";

					String[][] paiddata = new String[][] {
							{"paidwasher",	checklistfor(tmp, "Washer"),	"Washer, ", "洗濯機、" },
							{"paiddryer", 	checklistfor(tmp, "Dryer"),		"Dryer, ", "乾燥機、" },
							{"paidlaundry", checklistfor(tmp, "Laundry Service"),     "Laundry Service, ", "ランドリーサービス、" },
							{"paidtowel", checklistfor(tmp, "Towel"),     "Towel, ", "タオル、" },
							{"paidiron", checklistfor(tmp, "Iron"),     "Iron, ", "アイアン、" },
							{"paidhairdryer", checklistfor(tmp, "Hair Dryer"),     "Hair Dryer, ", "ヘアドライヤー、" },
							{"paiddvd", checklistfor(tmp, "DVD"),     "DVD, ", "DVDプレーヤー、" },
							{"paidwifi", checklistfor(tmp, "Wifi"),     "Wifi, ", "無線LAN、" },
							{"paidcomputer", checklistfor(tmp, "Computer"),     "Computer, ", "パソコン、" },
							{"paidlocker", checklistfor(tmp, "Locker"),     "Locker, ", "ロッカー、" },
							{"paidbicycle", checklistfor(tmp, "Bicycles"),     "Bicyces, ", "自転車、" },
							{"paidcar", checklistfor(tmp, "Car"),     "Car, ", "レンタカー、" },
							{"paidluggage", checklistfor(tmp, "Luggage Holding"),     "Luggage Holding, ", "手荷物預かり、" },
							{"paidbus", checklistfor(tmp, "Welcome Bus"),     "Welcome Bus, ", "送迎シャトルバス、" },
							{"paidtents", checklistfor(tmp, "Tents"),     "Tents, ", "テント、" },
							{"paidsleepingbags", checklistfor(tmp, "Sleeping Bags"),     "Sleeping Bags, ", "シラフ、" },
							{"paidhikinggear", checklistfor(tmp, "Hiking Gear"),     "Hiking Gear, ", "登山用品、" },
							{"paidbbqgrill", checklistfor(tmp, "BBQ Grill"),     "BBQ Grill, ", "BBQ施設、" },
							{"paidmassage", checklistfor(tmp, "Massage Service"),     "Massage Service, ", "マッサージ、" },
							{"paidother", otherchecked,     (paidotheramenitiescontent + ", "), (paidotheramenitiescontent_j + "、") }
					};

					for (String[] row : paiddata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							paidamenitiesstring=(paidamenitiesstring + row[2]);
							paidamenitiesstring_j=(paidamenitiesstring_j + row[3]);
						}
					}
					
					int j=paidamenitiesstring.length();
					if (j>1)
						paidamenitiesstring=(paidamenitiesstring.substring(0, j-2)+ ".");
					j=paidamenitiesstring_j.length();
					if (j>1)
						paidamenitiesstring_j=(paidamenitiesstring_j.substring(0, j-1)+ "");

					hotel.setProperty("paidamenitiesstring", paidamenitiesstring);
					hotel.setProperty("paidamenitiesstring_j", paidamenitiesstring_j);
					hotel.setProperty("paidotheramenities", paidotheramenitiescontent);
					hotel.setProperty("paidotheramenities_j", paidotheramenitiescontent_j);
					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+language+"?hotelKeyString="+hotelKeyString+"&msg=Paid%20Amenities%20updated");
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