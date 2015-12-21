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

public class UpdateWelcomingServlet extends HttpServlet {
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

				/*Update hotel's Welcoming List.*/
				else{
					String welcomingstring=" ";
					String welcomingstring_j=" ";
					String welcontent[] = request.getParameterValues("welcoming");
					hotel.setProperty("welcoming", Arrays.asList(welcontent));
					String otherwelcomingcontent = "";
					String otherwelcomingcontent_j = "";
					List<String> tmp = new ArrayList<String>(Arrays.asList(welcontent)); 
					String otherchecked="";
					if (otherwelcomingcontent.length()>0)
						otherchecked="checked";

					String[][] weldata = new String[][] {
							{"welpet",	checklistfor(tmp, "pets"),	"pets, ", "ペットをお連れのお客様、" },
							{"welkid", 	checklistfor(tmp, "children"),		"children, ", "お子様、" },
							{"welhandicapped", 	checklistfor(tmp, "handicapped"),		"guests with physical disabilities, ", "体の不自由の方、" },
							{"welgroups", 	checklistfor(tmp, "large groups"),		"large groups, ", "団体のお客様、" },
							{"welforeigner", checklistfor(tmp, "foreigners"),     "foreigners, ", "外国人のお客様、" },
							{"welcouple", checklistfor(tmp, "couples"),     "couples, ", "カップル、" },
							{"welbackpackers", checklistfor(tmp, "backpackers"),     "backpackers, ", "バックパッカー、" },
							{"welother", otherchecked,     (otherwelcomingcontent + ", "), (otherwelcomingcontent_j + "、") }
					};
					for (String[] row : weldata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							welcomingstring=(welcomingstring + row[2]);
							welcomingstring_j=(welcomingstring_j + row[3]);
						}
					}
					int j=welcomingstring.length();
					if (j>1){
						welcomingstring=("<p>This accommodation is proud to welcome " + welcomingstring);
						welcomingstring=(welcomingstring.substring(0, j+40)+ ".");
						welcomingstring_j=(welcomingstring_j.substring(0, welcomingstring_j.length()-1)+ "の皆様も大歓迎です。");
					}

					String welnoanimalsstring="";
					String welnoanimalscontent[] = request.getParameterValues("welnoanimals");
					hotel.setProperty("welnoanimals", Arrays.asList(welnoanimalscontent));
					if (welnoanimalscontent.length > 1)
					{welnoanimalsstring = " No animals are kept on the premises.";}
					hotel.setProperty("welnoanimalsstring", welnoanimalsstring);
					tmp = new ArrayList<String>(Arrays.asList(welnoanimalscontent));
					hotel.setProperty("welnoanimals", checklistfor(tmp, " No animals are kept on the premises."));
					if (tmp.contains( " No animals are kept on the premises.")){
						welcomingstring= (welcomingstring + "<br>No animals are kept on the premises.");
						welcomingstring_j= (welcomingstring_j + "<br>この施設は動物は一切飼っておりません。");
					}
					
					String childrenspricingstring="";
					String childrenspricingcontent[] = request.getParameterValues("childrenspricing");
					hotel.setProperty("childrenspricing", Arrays.asList(childrenspricingcontent));
					if (childrenspricingcontent.length > 1)
					{childrenspricingstring = "* Special prices for children.";}
					hotel.setProperty("childrenspricingstring", childrenspricingstring);
					tmp = new ArrayList<String>(Arrays.asList(childrenspricingcontent));
					hotel.setProperty("childrenspricing", checklistfor(tmp, "* Special prices for children."));
					if (tmp.contains( "* Special prices for children.")){
						welcomingstring= (welcomingstring + "<br>* Special prices for children.");
						welcomingstring_j= (welcomingstring_j + "<br>※お子様の割引あります！");
					}

					hotel.setProperty("welcomingstring", welcomingstring);
					hotel.setProperty("welcomingstring_j", welcomingstring_j);
					hotel.setProperty("otherwel", otherwelcomingcontent);

					/*Update hotel's Smoking Info.*/
					String smokingstring="";
					String smokingstring_j=" ";
					String smokingyes=" ";
					String smokingno=" ";
					String smokingboth=" ";
					String smokingcontent[] = request.getParameterValues("smoking");
					List<String> tmp2 = new ArrayList<String>(Arrays.asList(smokingcontent)); 
					hotel.setProperty("smoking", Arrays.asList(smokingcontent));

					if (tmp2.contains("NonSmoking Rooms Only")){
						smokingstring="NonSmoking rooms only.";
						smokingstring_j="全室は禁煙";
						smokingno="checked";
					}
					if (tmp2.contains("Smoking Rooms Only")){
						smokingstring="Smoking allowed in all rooms.";
						smokingstring_j="全室は喫煙可";
						smokingyes="checked";
					}
					if (tmp2.contains("Smoking and Nonsmoking Rooms")){
						smokingstring="Smoking and nonsmoking rooms available.";
						smokingstring_j="喫煙と禁煙の部屋はあります。";
						smokingboth="checked";
					}
					hotel.setProperty("smokingstring", smokingstring);
					hotel.setProperty("smokingstring_j", smokingstring_j);
					hotel.setProperty("smokingyes", smokingyes);
					hotel.setProperty("smokingno", smokingno);
					hotel.setProperty("smokingboth", smokingboth);
					
					
					

					/*Update room types.*/
					String roomtypesstring=" ";
					String roomtypesstring_j=" ";
					String roomtypescontent[] = request.getParameterValues("roomtypes");
					hotel.setProperty("roomtypes", Arrays.asList(roomtypescontent));
					String otherroomtypescontent = htmlFilter(request.getParameter("otherroomtypes"));
					String otherroomtypescontent_j = htmlFilter(request.getParameter("otherroomtypes_j"));
					List<String> tmp3 = new ArrayList<String>(Arrays.asList(roomtypescontent)); 
					String otherroomtypeschecked="";
					if (otherroomtypescontent.length()>0)
						otherroomtypeschecked="checked";

					String[][] roomtypesdata = new String[][] {
							{"roomtypesjapanese",	checklistfor(tmp3, "Japanese Rooms"),	"Japanese Rooms, ", "和室、" },
							{"roomtypeswestern", 	checklistfor(tmp3, "Western Rooms"),		"Western Rooms, ", "洋室、" },
							{"roomtypessingle", 	checklistfor(tmp3, "Single Rooms"),		"Single Rooms, ", "シングルルーム、" },
							{"roomtypestwin", 	checklistfor(tmp3, "Twin Rooms"),		"Twin Rooms, ", "ツインルーム、" },
							{"roomtypesdouble", checklistfor(tmp3, "Double Rooms"),     "Double Rooms, ", "ダブルルーム、" },
							{"roomtypesdormitories", checklistfor(tmp3, "Dormitories"),     "Dormitories, ", "ドミトリー、" },
							{"roomtypesstandalone", checklistfor(tmp3, "Stand-Alone Rooms"),     "Stand-Alone Rooms, ", "一軒家、" },
							{"roomtypestatamibanquetspace", checklistfor(tmp3, "Tatami Banquet Space"),     "Tatami Banquet Space, ", "宴会場、" },
							{"roomtypesmeetingbanquethall", checklistfor(tmp3, "Meeting-Banquet Hall"),     "Meeting-Banquet Hall, ", "会議室・バンケットホール、" },
							{"roomtypeswesterntoilets", checklistfor(tmp3, "Western Toilets"),     "Western Toilets, ", "様式のトイレ、" },
							{"roomtypessquattoilets", checklistfor(tmp3, "Squat Toilets"),     "Squat Toilets, ", "和式のトイレ、" },
							{"roomtypesother", otherroomtypeschecked,     (otherroomtypescontent + ", "), (otherroomtypescontent_j + "、") },
					};
					for (String[] row : roomtypesdata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							roomtypesstring=(roomtypesstring + row[2]);
							roomtypesstring_j=(roomtypesstring_j + row[3]);
						}
					}

					hotel.setProperty("otherroomtypes", otherroomtypescontent);
					hotel.setProperty("otherroomtypes_j", otherroomtypescontent_j);
					
					int k=roomtypesstring.length();
					if (k>1){
						roomtypesstring=("This accommodation offers " + roomtypesstring);
						roomtypesstring=(roomtypesstring.substring(0, k+24)+ ".");
						roomtypesstring_j=(roomtypesstring_j.substring(0, roomtypesstring_j.length()-1)+ "が用意されています。");
					}


					hotel.setProperty("roomtypesstring", roomtypesstring);
					hotel.setProperty("roomtypesstring_j", roomtypesstring_j);
					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
					
					
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+language+"?hotelKeyString="+hotelKeyString+"&msg=Welcoming%20List%20updated");
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