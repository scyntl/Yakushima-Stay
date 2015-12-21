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

public class CopyOfUpdateDirServlet extends HttpServlet {
	/**
	 * Create a new hotel listing, input hotelName, hotelName_j, and newUserID
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String hotelName = req.getParameter("hotelName");
		String hotelName_j = req.getParameter("hotelName_j");
		String newUserID = req.getParameter("newUserID");
		if (hotelName==null){
			resp.sendRedirect("/updatehoteladminhome?error=NohotelName");
		}
		if (hotelName==""){
			resp.sendRedirect("/updatehoteladminhome?error=NohotelName");
		} 
		if (hotelName_j==null){
			resp.sendRedirect("/updatehoteladminhome?error=NohotelName_j");
		}
		if (hotelName_j==""){
			resp.sendRedirect("/updatehoteladminhome?error=NohotelName_j");
		}
		Key hotelKey = KeyFactory.createKey("Hotel", hotelName);
		String hotelKeyString = KeyFactory.keyToString(hotelKey);
		Date date = new Date();       
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		try {
			Entity hotel = datastore.get(hotelKey);
			resp.sendRedirect("/updatehoteladminhome?error="+ hotelName+"AreadyExists");
		} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
			Entity hotel = new Entity("Hotel", hotelName);
			hotel.setProperty("date", date);
			String ipaddr = req.getRemoteAddr();
			hotel.setProperty("ipaddr", ipaddr);
			hotel.setProperty("hotelName_j", hotelName_j);
			hotel.setProperty("hotelName", hotelName);


			List<String> userList = new ArrayList<String>();
			List<String> pinList = new ArrayList<String>();
			userList.add("15336869913408271401");
			pinList.add("9876");
			userList.add("12551072242172342374");
			pinList.add("9876");
			userList.add(newUserID);
			pinList.add("9876");


			hotel.setProperty("userList", userList);	
			hotel.setProperty("pinList", pinList);	

			hotelKey = datastore.put(hotel);
		}
		Key userDataKey=null;
		Query query2 = new Query("UserData");
		List<Key> userDataKeys = new ArrayList<Key>();
		PreparedQuery pq2 = datastore.prepare(query2);
		int count=0;
		for (Entity result : pq2.asIterable()) 
		{
			userDataKey=result.getKey();
			userDataKeys.add((Key) userDataKey);
			count++;
		}	
		List<String> userIDs = new ArrayList<String>();
		if (userDataKeys!=null&&count>0){
			try{

				Entity userList = datastore.get(userDataKey);
				userIDs = (List<String>) userList.getProperty("userIDs");
				userIDs.add(newUserID);          
				userDataKey = datastore.put(userList);
			} 
			catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				Entity userList = new Entity("UserData", "userList");
				userIDs.add("15336869913408271401");
				userIDs.add("12551072242172342374");
				userIDs.add(newUserID);     	
				userList.setProperty("userIDs", userIDs);
				userDataKey = datastore.put(userList);
			}
		}
		else{
			Entity userList = new Entity("UserData", "userList");
			userIDs.add("15336869913408271401");
			userIDs.add("12551072242172342374");
			userIDs.add(newUserID);     	
			userList.setProperty("userIDs", userIDs);
			userDataKey = datastore.put(userList);
		}

		resp.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString);
	}

}

