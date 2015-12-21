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

public class UpdateAcceptsServlet extends HttpServlet {
	/**
	 * Allow managers to update payment options (accepts) for the hotel.
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
					String acceptsstring=" ";
					String acceptsstring_j=" ";
					String acceptscontent[] = request.getParameterValues("accepts");
					hotel.setProperty("accepts", Arrays.asList(acceptscontent));
					String acceptsotherscontent = htmlFilter(request.getParameter("acceptsothers"));
					String acceptsotherscontent_j = htmlFilter(request.getParameter("acceptsothers_j"));
					List<String> tmp = new ArrayList<String>(Arrays.asList(acceptscontent)); 
					String otherchecked="";
					if (acceptsotherscontent.length()>0)
						otherchecked="checked";

					hotel.setProperty("acceptsvisa", checklistfor(tmp, "Visa"));
					hotel.setProperty("acceptsmastercard", checklistfor(tmp, "Mastercard"));
					hotel.setProperty("acceptsdiscover", checklistfor(tmp, "Discover"));
					hotel.setProperty("acceptsjcb", checklistfor(tmp, "JCB"));
					hotel.setProperty("acceptsamericanexpress", checklistfor(tmp, "American Express"));
					hotel.setProperty("acceptssaison", checklistfor(tmp, "Saison"));
					hotel.setProperty("acceptsdc", checklistfor(tmp, "DC"));
					hotel.setProperty("acceptsuc", checklistfor(tmp, "UC"));
					hotel.setProperty("acceptstravelerschecks", checklistfor(tmp, "Travelers Checks"));
					String[][] acceptsdata = new String[][] {
							{"acceptsvisa",	checklistfor(tmp, "Visa"),	"Visa, ", "ビザ、" },
							{"acceptsmastercard", 	checklistfor(tmp, "Mastercard"),		"Mastercard, ", "マスターカード、" },
							{"acceptsdiscover", checklistfor(tmp, "Discover"),     "Discover, ", "ディスカバーカード、" },
							{"acceptsjcb", checklistfor(tmp, "JCB"),     "JCB, ", "JCB、" },
							{"acceptsamericanexpress", checklistfor(tmp, "American Express"),     "American Express, ", "アメリカンエキスプレス、" },
							{"acceptssaison", checklistfor(tmp, "Saison"),     "Saison, ", "Saison、" },
							{"acceptsdc", checklistfor(tmp, "DC"),     "DC, ", "DC、" },
							{"acceptsuc", checklistfor(tmp, "UC"),     "UC, ", "UC、" },
							{"acceptstravelerschecks", checklistfor(tmp, "Travelers Checks"),     "Travelers Checks, ", "トラベラーズチェック、" },
							{"acceptstransfer", checklistfor(tmp, "bank transfers"),     "bank transfers, ", "振込、" },
							{"acceptscash", checklistfor(tmp, "cash"),     "cash, ", "現金、" },
							{"acceptsother", otherchecked,     (acceptsotherscontent + ", "), (acceptsotherscontent_j + "、") }
					};

					for (String[] row : acceptsdata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							acceptsstring=(acceptsstring + row[2]);
							acceptsstring_j=(acceptsstring_j + row[3]);
						}
					}
					int j=acceptsstring.length();
					if (j>1)
						acceptsstring=(acceptsstring.substring(0, j-2)+ ".");
					j=acceptsstring_j.length();
					if (j>1)
						acceptsstring_j=(acceptsstring_j.substring(0, j-1)+ "");
					hotel.setProperty("acceptsstring", acceptsstring);
					hotel.setProperty("acceptsstring_j", acceptsstring_j);
					hotel.setProperty("acceptsothers", acceptsotherscontent);
					hotel.setProperty("acceptsothers_j", acceptsotherscontent_j);
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

private static String checklistfor(List<String> amenitylist, String amenity) {
	if (amenitylist.contains(amenity)) {
		return ("checked");
	} else {
		return ("");
	}
}

}