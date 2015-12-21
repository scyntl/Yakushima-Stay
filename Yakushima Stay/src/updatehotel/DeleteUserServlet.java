package updatehotel;

import com.google.appengine.api.datastore.DatastoreService;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
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

public class DeleteUserServlet extends HttpServlet {
	/**
	 * This code should only be accessible by administrative users.
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


			/*Get hotelKeyString and load data.*/
			else{
				String hotelKeyString = request.getParameter("hotelKeyString");
				if (hotelKeyString==null)
					response.sendRedirect("/updatehotel?error=No%20hotelKeyString.");
				else{
					Key hotelKey = KeyFactory.stringToKey(hotelKeyString);
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					String ipaddr = request.getRemoteAddr();
					Date date = new Date();
					String oldmanagerID = request.getParameter("oldmanagerID");
					String oldmanagerPin = request.getParameter("oldmanagerPin");
					String hotelName =null;
					String hotelName_j=null;


					//Delete User from hotel's managerList...
					try {					
						Entity hotel = datastore.get(hotelKey);
						List<String> managerList = new ArrayList<String>();
						List<String> pinList = new ArrayList<String>();
						managerList = (List<String>) hotel.getProperty("managerList");
						pinList = (List<String>) hotel.getProperty("pinList");
						hotelName=(String) hotel.getProperty("hotelName");
						hotelName_j=(String) hotel.getProperty("hotelName_j");

						//...assuming that the managerList and PINList stay coordinated.
						if (managerList!=null && pinList!=null){
							int j=managerList.indexOf(oldmanagerID);
							if (j>0){
								managerList.remove(j);
								pinList.remove(j);		
							}
							hotel.setProperty("oldmanager", oldmanagerID);
							hotel.setProperty("oldPin", oldmanagerPin);
							hotel.setProperty("managerList", managerList);
							hotel.setProperty("pinList", pinList);
						}
						else {
							List<String> newmanagerList = new ArrayList<String>();
							List<String> newpinList = new ArrayList<String>();
							newmanagerList.add("15336869913408271401");
							newpinList.add("9876");
							newmanagerList.add("12551072242172342374");
							newpinList.add("9876");
							hotel.setProperty("managerList", newmanagerList);
							hotel.setProperty("pinList", newpinList);
						}
						hotel.setProperty("ipaddr", ipaddr);
						hotel.setProperty("date", date);
						hotelKey = datastore.put(hotel);

					} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
						response.sendRedirect("/updatehoteladminhome&error=Hotel%20does%20not%20exist.");
					}

					//Delete hotel from manager's hotelList. 
					Key managerKey = KeyFactory.createKey("Manager", oldmanagerID);
					try {
						Entity manager = datastore.get(managerKey);
						manager.setProperty("date", date);
						manager.setProperty("ipaddr", ipaddr);
						List<String> hotelKeyStringList = new ArrayList<String>();
						List<String> hotelNameList = new ArrayList<String>();
						List<String> hotelName_jList = new ArrayList<String>();

						Query query2 = new Query("Manager").addFilter("managerID", FilterOperator.EQUAL, oldmanagerID);
						List<List<String>> managerLists = new ArrayList<List<String>>();
						PreparedQuery pq2 = datastore.prepare(query2);
						for (Entity result : pq2.asIterable()) 
						{
							hotelNameList = (List<String>) result.getProperty("hotelNameList");
							hotelName_jList = (List<String>) result.getProperty("hotelName_jList");
							hotelKeyStringList = (List<String>) result.getProperty("hotelKeyStringList");
						}
						if(hotelNameList!=null)
						{hotelNameList.remove(hotelName);}
						if(hotelName_jList!=null)
						{hotelName_jList.remove(hotelName_j); }
						if(hotelKeyStringList!=null)
						{hotelKeyStringList.remove(hotelKeyString); }				
						manager.setProperty("hotelKeyStringList", hotelKeyStringList);
						manager.setProperty("hotelNameList", hotelNameList);
						manager.setProperty("hotelName_jList", hotelName_jList);
						managerKey = datastore.put(manager);
					} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
						response.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString + "&error=No%20Hotel%20or%20manager%20in%20Database.");
					}
				}
				response.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString);
			}
		}
	}
}