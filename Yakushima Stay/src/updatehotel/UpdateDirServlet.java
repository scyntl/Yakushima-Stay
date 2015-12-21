package updatehotel;

import com.google.appengine.api.datastore.DatastoreService;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
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

public class UpdateDirServlet extends HttpServlet {
	/**
	 * Create a new hotel listing, input hotelName, hotelName_j, and newManagerID
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
				/*Check for required input fields.*/
				String hotelName = request.getParameter("hotelName");
				String hotelName_j = request.getParameter("hotelName_j");
				String newManagerID = request.getParameter("newManagerID");
				int pricemin = 0;
				int pricemax = 0;
				if (hotelName==null)
				{response.sendRedirect("/updatehoteladminhome?error=NohotelName");}
				if (hotelName.equals(""))
				{response.sendRedirect("/updatehoteladminhome?error=NohotelName");} 
				if (hotelName_j==null)
				{response.sendRedirect("/updatehoteladminhome?error=NohotelName_j");}
				if (hotelName_j.equals(""))
				{response.sendRedirect("/updatehoteladminhome?error=NohotelName_j");}

				/*Create entity for the new hotel*/
				Key hotelKey = KeyFactory.createKey("Hotel", hotelName);
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				String hotelKeyString = KeyFactory.keyToString(hotelKey);
				Date date = new Date();
				String ipaddr = request.getRemoteAddr();
				try {
					Entity hotel = datastore.get(hotelKey);
					response.sendRedirect("/updatehoteladminhome?error="+ hotelName+"%20already%20exists");
				} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
					Entity hotel = new Entity("Hotel", hotelName);
					hotel.setProperty("hotelName_j", hotelName_j);
					hotel.setProperty("hotelName", hotelName);
					hotel.setProperty("pricemin", pricemin);
					hotel.setProperty("pricemax", pricemax);

					/*Register managers and PIN numbers as managers of this hotel's data.*/
					List<String> managerList = new ArrayList<String>();
					List<String> pinList = new ArrayList<String>();
					managerList.add("104364285174483720328");
					pinList.add("9876");
					managerList.add(newManagerID);
					pinList.add("0000");
					hotel.setProperty("managerList", managerList);	
					hotel.setProperty("pinList", pinList);	

					hotel.setProperty("date", date);
					hotel.setProperty("ipaddr", ipaddr);
					hotelKey = datastore.put(hotel);
				}

				/*Create Entity for the manager and one administrator.*/
				List<String> managers = new ArrayList<String>();
				managers.add(newManagerID);
				managers.add("104364285174483720328");
				for (int i=0; i<managers.size();i++){
					Key managerKey = KeyFactory.createKey("Manager", managers.get(i));
					try {
						Entity manager = datastore.get(managerKey);
						List<String> hotelKeyStringList = new ArrayList<String>();
						hotelKeyStringList = (List<String>) manager.getProperty("hotelKeyStringList");
						if(hotelKeyStringList.indexOf(hotelKeyString)==-1){
							hotelKeyStringList=addstring(hotelKeyStringList, hotelKeyString);
							manager.setProperty("hotelKeyStringList", hotelKeyStringList);
						}
						List<String> hotelNameList = new ArrayList<String>();
						List<String> hotelName_jList = new ArrayList<String>();
						hotelNameList = (List<String>) manager.getProperty("hotelNameList");
						hotelName_jList = (List<String>) manager.getProperty("hotelNameList");
						hotelNameList=addstring(hotelNameList, hotelName);
						hotelName_jList=addstring(hotelName_jList, hotelName_j);
						manager.setProperty("hotelNameList", hotelNameList);
						manager.setProperty("hotelName_jList", hotelName_jList);

						hotelKey = datastore.put(manager);
					} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
						Entity manager = new Entity("Manager", managers.get(i));
						manager.setProperty("date", date);
						manager.setProperty("ipaddr", ipaddr);
						manager.setProperty("managerID", managers.get(i));
						if (i>0)
						{manager.setProperty("PIN", "9876");}
						else
						{manager.setProperty("PIN", "0000");}
						List<String> hotelKeyStringList = new ArrayList<String>();
						List<String> hotelNameList = new ArrayList<String>();
						List<String> hotelName_jList = new ArrayList<String>();
						hotelKeyStringList.add(hotelKeyString);
						hotelNameList.add(hotelName);
						hotelName_jList.add(hotelName_j);
						manager.setProperty("hotelKeyStringList", hotelKeyStringList);
						manager.setProperty("hotelNameList", hotelNameList);
						manager.setProperty("hotelName_jList", hotelName_jList);

						hotelKey = datastore.put(manager);
					}
				}
				response.sendRedirect("/updatehoteladmin?hotelKeyString="+hotelKeyString);
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
