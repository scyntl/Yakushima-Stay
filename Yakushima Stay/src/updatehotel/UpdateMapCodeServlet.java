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

public class UpdateMapCodeServlet extends HttpServlet {
	/**
	 * This code should only be accessible by administrative users.
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		/*Check that user is an administrator.*/
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user==null)
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		else{
			boolean isadmin=userService.isUserAdmin();
			if (!isadmin)
				response.sendRedirect("/updatehotelhome?error=Not%20logged%20in%20as%20administrator.");

			/*Get hotelKeyString and load data.*/
			else{
				String hotelKeyString = request.getParameter("hotelKeyString");
				if (hotelKeyString==null)
					response.sendRedirect("/updatehoteladminhome?error=NoKey");
				else{
					Key hotelKey = KeyFactory.stringToKey(hotelKeyString);
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

					/*Update hotel's map code.*/
					try {
						Entity hotel = datastore.get(hotelKey);
						Text mapcodecontent = new Text(request.getParameter("mapcode"));
						hotel.setProperty("mapcode", mapcodecontent);
						Text oldmapcodecontent = new Text(request.getParameter("oldmapcode"));
						hotel.setProperty("oldmapcode", oldmapcodecontent);

						String ipaddr = request.getRemoteAddr();
						hotel.setProperty("ipaddr", ipaddr);
						Date date = new Date();
						hotel.setProperty("date", date);
						hotelKey = datastore.put(hotel);

					} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
						response.sendRedirect("/updatehoteladminhome?error=NoSuchHotel");}			
				}
				response.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString);
			}
		}
	}
}