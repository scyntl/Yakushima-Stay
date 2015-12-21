package updatehotel;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateOutlineServlet extends HttpServlet {
	/**
	 * Allow managers and administrators to update hotel's Outline.
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

				/*Update hotel's Outline.*/
				else{
					Text outline = new Text(request.getParameter("outline"));
					Text outline_j = new Text(request.getParameter("outline_j"));

					hotel.setProperty("outline", outline);
					hotel.setProperty("outline_j", outline_j);
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
}