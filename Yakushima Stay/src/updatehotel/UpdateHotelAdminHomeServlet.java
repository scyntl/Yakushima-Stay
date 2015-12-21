package updatehotel;

import java.util.*;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.*;

public class UpdateHotelAdminHomeServlet extends HttpServlet {
	/**
	 * Only the administrator can create a new hotel. Takes hotelName, hotelName_j, and primary user ID number.
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=shift_JIS");
		PrintWriter pw = response.getWriter();
		String error = request.getParameter("error");
		/**Check for passed errors.**/
		if (error!=null){
			pw.println("There has been an error: "+error);
		}

		/*Check that user is an administrator.*/
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user==null)
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		else{
			String userID = user.getUserId();
			boolean isadmin=userService.isUserAdmin();
			if (userID.equals("104364285174483720328")|isadmin) {

				/*Build search menus.*/
				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				Query query2 = new Query("Hotel");
				List<String> hotelNames = new ArrayList<String>();
				List<String> hotelNames_j = new ArrayList<String>();
				List<String> hotelKeys = new ArrayList<String>();
				List<List<String>> managerLists = new ArrayList<List<String>>();
				PreparedQuery pq2 = datastore.prepare(query2);
				for (Entity result : pq2.asIterable()) 
				{hotelNames.add((String) result.getProperty("hotelName"));
				hotelNames_j.add((String) result.getProperty("hotelName_j"));
				Key tempkey=result.getKey();
				String tempstring = KeyFactory.keyToString(tempkey);
				hotelKeys.add((String) tempstring);
				}	

				/*Begin html*/
				pw.println("<HTML>");
				pw.println("<HEAD>");
				pw.println("<head>" +
						"<meta http-equiv='content-type' content='text/html;charset=shift_JIS'>" +
						"<link type='text/css' rel='stylesheet' 	href='/css/hotelsmain.css'>" +
						"<link href='http://fonts.googleapis.com/css?family=Marcellus' 	rel='stylesheet' type='text/css'>");
				pw.println("</head>");
				pw.println("<BODY>");
				pw.println("<div id='bigcontainer'>" +
						"		<div id='bigbanner' title='Comprehensive Listing of Hotels in Yakushima'>" +
						"			<table id=languageselection>" +
						"				<tr><td class=languagebutton><a href='/updatehotelhome'>English</a></td>" +
						"				<td class=languagebutton><a href='/updatehotelhomej'>日本語</a></td>" +
						"			</table>" +
						"		</div>" +
						"	<div id=searchmenu>"  +
						"		<form action=/updatehoteladmin method='get'>" +
						"			<dt>施設を選んで下さい。" +
						"					<dt><select name ='hotelKeyString'  required style='width:190px;' >" +
						"						<option value='Choose' disabled selected>Choose . . .</option>");
				for (int j=0; j<hotelNames_j.size(); j++){
					pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");
				}
				pw.println(	"						</select>" +
						"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
						"		</form>	" + 
						"		<form action=/updatehoteladmin method='get'>" +
						"			<dt>Choose an accomodation:" +
						"					<dt><select name ='hotelKeyString' required style='width:190px;' >" +
						"						<option value='Choose' disabled selected>Choose . . .</option>");
				for (int j=0; j<hotelNames.size(); j++){
					pw.println("<option value=" + hotelNames.get(j) + ">" + hotelNames.get(j) + "</option>");
				}
				pw.println(	"						</select>" +
						"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
						"		</form>	" + 
						"</div>" +
						"	<div id=centercontainer>" +
						"		<div id=results>" +
						"			<div id=dir>" +
						" 				<h2>Create a New Hotel:</h2>" +
						"				<form accept-charset='utf-16' id=dirform action='/updatedir' method='post'  style='height:110px;' >" +
						"					English Name: <input type=text name='hotelName' size= 30required>"  +
						"					<br>Japanese Name: <input type=text name='hotelName_j' size=30 required>"  +
						"					<br>Primary Manager ID: " +
						"					<input type=text name='newManagerID' size=30 required>"  + 
						"					<br><input type='submit' value='Create New Hotel' />" +
						"				</form>" +
						"			</div>" +
						"		</div>" +
						"	</div>" +
						"	<div id=basics></div>" +
						"</div>");
				pw.println("</BODY>");
				pw.println("</HTML>");
				pw.close();
			}
			else
				response.sendRedirect("/updatehotelhome?error=Not%20Administrator");
		}
	}
	private static String Filternulls(Entity hotel, String message) {
		String y = (String) hotel.getProperty(message);
		if (y == null | y == "null")
		{y="";}
		return y;
	}
}