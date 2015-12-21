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

public class UpdateSpeaksServlet extends HttpServlet {
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

				/*Update hotel's Shared Amenities.*/
				else{
					String speaksstring=" ";
					String speaksstring_j=" ";
					String speakscontent[] = request.getParameterValues("speaks");
					hotel.setProperty("speaks", Arrays.asList(speakscontent));
					String otherspeakscontent = htmlFilter(request.getParameter("speaksothers"));
					String otherspeakscontent_j = htmlFilter(request.getParameter("speaksothers_j"));
					List<String> tmp = new ArrayList<String>(Arrays.asList(speakscontent)); 
					String otherchecked="";
					if (otherspeakscontent.length()>1)
						otherchecked="checked";

					String[][] speaksdata = new String[][] {
							{"speaksjapanese",	checklistfor(tmp, "Japanese"),	"Japanese, ", "日本語、" },
							{"speaksenglish", 	checklistfor(tmp, "English"),		"English, ", "英語、" },
							{"speaksfrench", checklistfor(tmp, "French"),     "French, ", "フランス語、" },
							{"speaksgerman", checklistfor(tmp, "German"),     "German, ", "ドイツ語、" },
							{"speakschinese", checklistfor(tmp, "Chinese"),     "Chinese, ", "中国語、" },
							{"speakskorean", checklistfor(tmp, "Korean"),     "Korean, ", "韓国語、" },
							{"speaksrussian", checklistfor(tmp, "Russian"),     "Russian, ", "ロシア語、" },
							{"speaksothers", otherchecked,     (otherspeakscontent + ", "), (otherspeakscontent_j + "、") }
					};

					for (String[] row : speaksdata) {
						hotel.setProperty(row[0], row[1]);
						if (row[1].equals("checked")){
							speaksstring=(speaksstring + row[2]);
							speaksstring_j=(speaksstring_j + row[3]);
						}
					}
					int j=speaksstring.length();
					if (j>1)
						speaksstring=(speaksstring.substring(0, j-2)+ ".");
					j=speaksstring_j.length();
					if (j>1)
						speaksstring_j=(speaksstring_j.substring(0, j-1)+ "");

					hotel.setProperty("speaksstring", speaksstring);
					hotel.setProperty("speaksstring_j", speaksstring_j);
					hotel.setProperty("speaksothers", otherspeakscontent);
					hotel.setProperty("speaksothers_j", otherspeakscontent_j);
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
