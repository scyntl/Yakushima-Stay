package updatehotel;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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

public class PublishHotelServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		/*Get hotelKeyString and permissions(whether or not user expects to be returned to an admin page.*/
		String hotelKeyString = request.getParameter("hotelKeyString");
		String permissions = request.getParameter("permissions");
		if (permissions==null)
		{permissions="";}
		else
		{permissions="admin";}
		
		/*Check validity of hotelKeyString*/
		Key hotelKey=null;
		if (hotelKeyString==null) 
		{response.sendRedirect("/updatehotel"+permissions+"home?error=nokey&redirectpage=NoKey");}
		else{
			if (hotelKeyString.equals("null"))
			{response.sendRedirect("/updatehotel"+permissions+"home?error=NoKey");}
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
				{response.sendRedirect("/updatehotelhome?error=HotelManagerMismatch");}
				
				/*Update hotel's published status.*/
				else{
					String published=" ";
					String tmpcontent[] = request.getParameterValues("publish");
					if (tmpcontent!=null){
						if(tmpcontent.length>0){
							published="checked";
						}
					}
					hotel.setProperty("published", published);
					String ipaddr = request.getRemoteAddr();
					hotel.setProperty("ipaddr", ipaddr);
					Date date = new Date();   
					hotel.setProperty("date", date);
					hotelKey = datastore.put(hotel);
					response.sendRedirect("/updatehotel"+permissions+"?hotelKeyString=" + hotelKeyString);       
				}

			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotel"+permissions+"?hotelKeyString=" + hotelKeyString + "&error=NoHotelEntity");       
			}
		}
	}
}