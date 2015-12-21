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

public class UpdateMealsServlet extends HttpServlet {
	/**
	 * Allow managers and administrators to update Meal plans.
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

				/*Update hotel's Meals.*/
				else{
					String mealsstring=" ";
					String mealsstring_j=" ";
					String mealscontent[] = request.getParameterValues("meals");
					hotel.setProperty("meals", Arrays.asList(mealscontent));
					String mealsothercontent = htmlFilter(request.getParameter("mealsother"));
					String mealsothercontent_j = htmlFilter(request.getParameter("mealsother_j"));
					List<String> tmp = new ArrayList<String>(Arrays.asList(mealscontent)); 
					String otherchecked="";
					if (mealsothercontent.length()>0)
						otherchecked="checked";

					String[][] mealsdata = new String[][] {
							{"mealbreakfast",	checklistfor(tmp, "Breakfast"),	"Breakfast, ", "朝食、" },
							{"mealdinner", 	checklistfor(tmp, "Dinner"),		"Dinner, ", "夕食、" },
							{"mealstayonly", checklistfor(tmp, "Stay-Only"),     "Stay with No Meals, ", "素泊まり、" },
							{"mealvegetarian", checklistfor(tmp, "Vegetarian Options"),     "Vegetarian Options, ", "ベジタリアン料理可、" },
							{"meaalallergen", checklistfor(tmp, "Allergen"),     "NonAllergenic Options, ", "アレルギー物資を使わない料理可、" },
							{"mealother", otherchecked,     (mealsothercontent + ", "), (mealsothercontent_j + "、") }
					};

					for (String[] row : mealsdata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							mealsstring=(mealsstring + row[2]);
							mealsstring_j=(mealsstring_j + row[3]);
						}
					}
					int j=mealsstring.length();
					if (j>1){
//						mealsstring=("This accomodation offers:<br>" + mealsstring);
//						mealsstring_j=("食事について以下のオプションがあります。<br>"+ mealsstring_j);
						mealsstring=mealsstring.substring(0, j-2);
						mealsstring_j=mealsstring_j.substring(0, mealsstring_j.length()-1);
					}

					hotel.setProperty("mealsstring", mealsstring);
					hotel.setProperty("mealsstring_j", mealsstring_j);
					hotel.setProperty("mealsother", mealsothercontent);
					hotel.setProperty("mealsother_j", mealsothercontent_j);
					Date date = new Date();
					hotel.setProperty("date", date);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotel"+permissions+"home"+language+"?error=NoSuchHotel");}			
		}
		response.sendRedirect("/updatehotel"+permissions+language+"?hotelKeyString="+hotelKeyString+"&msg=Meals%20updated");
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