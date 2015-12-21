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

public class AddUserServlet extends HttpServlet {
	/**
	 * This code should only be accessible by administrative users.
	 * It requires managerID, newPin, and hotelKey.
	 * The pin is updated for the current hotelKey and also in the manager list,
	 * but not for other hotels.
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
				if (hotelKeyString==null)
					response.sendRedirect("/updatehotel?error=No%20hotelKeyString.");
				else{
					Key hotelKey = KeyFactory.stringToKey(hotelKeyString);
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					String hotelName =null;
					String hotelName_j=null;
					String newmanagerID = request.getParameter("newmanagerID");
					String newmanagerPin = request.getParameter("newmanagerPin");
					String ipaddr = request.getRemoteAddr();
					Date date = new Date();

					/*Add manager to the hotel's managerList.*/
					try {
						Entity hotel = datastore.get(hotelKey);
						hotelName=(String) hotel.getProperty("hotelName");
						hotelName_j=(String) hotel.getProperty("hotelName_j");
						List<String> managerList = new ArrayList<String>();
						List<String> pinList = new ArrayList<String>();
						managerList = (List<String>) hotel.getProperty("managerList");
						pinList = (List<String>) hotel.getProperty("pinList");
						if (managerList!=null && pinList!=null){
							if (managerList==null|managerList.indexOf(newmanagerID)==-1){
								managerList.add(newmanagerID);
								pinList.add(newmanagerPin);
								hotel.setProperty("managerList", managerList);
								hotel.setProperty("pinList", pinList);
							}
							else{
								response.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString + "&error=manager%20Already%20Exists");
							}
						}
						/*If no managerList exists for the current hotel, create one.*/
						else {
							List<String> newmanagerList = new ArrayList<String>();
							List<String> newpinList = new ArrayList<String>();
							newmanagerList.add("104364285174483720328");
							newpinList.add("9876");
							newmanagerList.add(newmanagerID);
							newpinList.add(newmanagerPin);
							hotel.setProperty("managerList", newmanagerList);
							hotel.setProperty("pinList", newpinList);
						}
						hotel.setProperty("ipaddr", ipaddr);
						hotel.setProperty("date", date);
						hotelKey = datastore.put(hotel);
					} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
						response.sendRedirect("/updatehoteladminhome&error=Hotel%20does%20not%20exist.");
					}

						/*Add the current hotel (key and names) to the manager's hotel lists.*/
						Key managerKey = KeyFactory.createKey("Manager", newmanagerID);
						try {
							Entity manager = datastore.get(managerKey);
							List<String> hotelKeyStringList = new ArrayList<String>();
							List<String> hotelNameList = new ArrayList<String>();
							List<String> hotelName_jList = new ArrayList<String>();
							hotelKeyStringList = (List<String>) manager.getProperty("hotelKeyStringList");
							hotelNameList = (List<String>) manager.getProperty("hotelNameList");
							hotelName_jList = (List<String>) manager.getProperty("hotelNameList");
							if(hotelKeyStringList==null|hotelKeyStringList.indexOf(hotelKeyString)==-1){
								hotelKeyStringList=addstring(hotelKeyStringList, hotelKeyString);
								manager.setProperty("hotelKeyStringList", hotelKeyStringList);
								hotelNameList=addstring(hotelNameList, hotelName);
								hotelName_jList=addstring(hotelName_jList, hotelName_j);
								manager.setProperty("hotelNameList", hotelNameList);
								manager.setProperty("hotelName_jList", hotelName_jList);
								manager.setProperty("date", date);
								manager.setProperty("ipaddr", ipaddr);
								managerKey = datastore.put(manager);
							}
						} 
						/*If no entity exists for the manager, create one.*/
						catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
							Entity manager = new Entity("Manager", newmanagerID);
							manager.setProperty("date", date);
							manager.setProperty("ipaddr", ipaddr);
							manager.setProperty("managerID", newmanagerID);
							manager.setProperty("PIN", newmanagerPin);
							List<String> hotelKeyStringList = new ArrayList<String>();
							List<String> hotelNameList = new ArrayList<String>();
							List<String> hotelName_jList = new ArrayList<String>();
							hotelKeyStringList=addstring(hotelNameList, hotelKeyString);
							hotelNameList=addstring(hotelNameList, hotelName);
							hotelName_jList=addstring(hotelNameList, hotelName_j);
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

	/*This function adds a string to a list, or creates a new list with the string as the 
	 * first element if the list is null.*/
	private static List<String> addstring(List<String> alist, String message) {
		if (alist!=null){
			alist.add(message);
			return alist;
		}
		else{
			List<String> blankString = new ArrayList<String>();
			blankString.add(message);
			return blankString;
		}
	}
}