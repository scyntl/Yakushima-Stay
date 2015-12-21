package updatehotel;

import com.google.appengine.api.datastore.DatastoreService;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
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

public class UpdateHotelNameServlet extends HttpServlet {
	/**
	 * This code should only be accessible by administrative users. 
	 * It updates hotelName, hotelName_j, date, and ipaddr.
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		/*Check that user is an administrator.*/
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user==null){
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}
		else{
			boolean isadmin=userService.isUserAdmin();
			if (!isadmin)
				response.sendRedirect("/updatehotelhome?error=Not%20logged%20in%20as%20administrator.");

			/*Get hotelKeyString and load data.*/
			else{
				String hotelKeyString = request.getParameter("hotelKeyString");
				Key hotelKey = KeyFactory.stringToKey(hotelKeyString);
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				try {
					Entity hotel = datastore.get(hotelKey);
					String hotelName=null;
					String hotelName_j=null;
					String oldhotelName=null;
					String oldhotelName_j=null;
					hotelName = request.getParameter("newhotelName");
					hotelName_j = request.getParameter("newhotelName_j");
					oldhotelName = request.getParameter("oldhotelName");
					oldhotelName_j = request.getParameter("oldhotelName_j");
					if (hotelName == null)
						hotelName = htmlFilter(Filternulls(hotel, "hotelName"));
					if (hotelName_j == null)
						hotelName_j = htmlFilter(Filternulls(hotel, "hotelName_j"));

					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("hotelName", hotelName);
					hotel.setProperty("hotelName_j", hotelName_j);
					hotel.setProperty("oldhotelName", oldhotelName);
					hotel.setProperty("oldhotelName_j", oldhotelName_j);
					hotel.setProperty("ipaddr", ipaddr);
					Date date = new Date();
					hotel.setProperty("date", date);
					hotelKey = datastore.put(hotel);


				} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
					response.sendRedirect("/updatehoteladminhome?error=No%such%20hotel");
				}
				response.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString);
			}
		}
	}
	private static String Filternulls(Entity hotel, String message) {
		String y = (String) hotel.getProperty(message);
		if (y == null | y == "null")
		{y="";}
		return y;
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