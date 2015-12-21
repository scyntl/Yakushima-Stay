package updatehotel;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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

public class ChangePinServlet extends HttpServlet {
	/**
	 * This code should only be accessible by administrative users.
	 * It requires managerID, newPin, and hotelKey.
	 * The pin is updated for the current hotelKey and also in the manager list,
	 * but not for other hotels.
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		//Check that user is an administrator.
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user==null){
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}
		else{
			boolean isadmin=userService.isUserAdmin();
			if (!isadmin)
				response.sendRedirect("/updatehotelhome?error=Not%20logged%20in%20as%20administrator.");
			
			//Check the hotelKey and load data.
			else{
				String hotelKeyString = request.getParameter("hotelKeyString");
				if (hotelKeyString==null){
					response.sendRedirect("/updatehotel?error=No%20hotelKeyString.");
				}
				else{
					Key hotelKey = KeyFactory.stringToKey(hotelKeyString);
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					String hotelName =null;
					String hotelName_j=null;
					String managerID = request.getParameter("managerID");
					String newPin = request.getParameter("newPin");
					String ipaddr = request.getRemoteAddr();
					Date date = new Date();
					
					//Update pin in the hotel's pinList.
					try {
						Entity hotel = datastore.get(hotelKey);
						String oldmanagerPin = null;
						List<String> managerList = new ArrayList<String>();
						List<String> pinList = new ArrayList<String>();
						managerList = (List<String>) hotel.getProperty("managerList");
						pinList = (List<String>) hotel.getProperty("pinList");
						hotelName=(String) hotel.getProperty("hotelName");
						hotelName_j=(String) hotel.getProperty("hotelName_j");
						if (managerList!=null && pinList!=null){
							int i=managerList.indexOf(managerID);
							if (i>-1){
								oldmanagerPin=pinList.get(i);
								pinList.remove(i);
								pinList.add(i, newPin);
							}
							hotel.setProperty("oldPin", oldmanagerPin);
							hotel.setProperty("managerList", managerList);
							hotel.setProperty("pinList", pinList);
						}
						else {
							List<String> newmanagerList = new ArrayList<String>();
							List<String> newpinList = new ArrayList<String>();
							newmanagerList.add("104364285174483720328");
							newpinList.add("9876");
							hotel.setProperty("managerList", newmanagerList);
							hotel.setProperty("pinList", newpinList);
						}
						hotel.setProperty("ipaddr", ipaddr);
						hotel.setProperty("date", date);
						hotelKey = datastore.put(hotel);
					} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
					}

					//Update pin in the manager entity. (Not so important. Shall I delete this?)
					Key managerKey = KeyFactory.createKey("Manager", managerID);
					try {
						Entity manager = datastore.get(managerKey);
						manager.setProperty("PIN", newPin);
						manager.setProperty("date", date);
						manager.setProperty("ipaddr", ipaddr);
						managerKey = datastore.put(manager);
					} catch (com.google.appengine.api.datastore.EntityNotFoundException e) { 
						Entity manager = new Entity("Manager", managerID);
						manager.setProperty("date", date);
						manager.setProperty("ipaddr", ipaddr);
						manager.setProperty("managerID", managerID);
						manager.setProperty("PIN", newPin);
						List<String> hotelKeyStringList = new ArrayList<String>();
						List<String> hotelNameList = new ArrayList<String>();
						List<String> hotelName_jList = new ArrayList<String>();
						hotelKeyStringList.add(hotelKeyString);
						if (hotelName.equals(null))
						{hotelName="";}
						if (hotelName_j.equals(null))
						{hotelName_j="";}
						hotelNameList.add(hotelName);
						hotelName_jList.add(hotelName_j);						
						manager.setProperty("hotelKeyStringList", hotelKeyStringList);
						manager.setProperty("hotelNameList", hotelNameList);
						manager.setProperty("hotelName_jList", hotelName_jList);
						managerKey = datastore.put(manager);
					}
				}
				response.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString);
			}
		}
	}
}