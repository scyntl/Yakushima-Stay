package updatehotel;

import javax.servlet.*;
import javax.servlet.jsp.PageContext;
import java.util.*;
import java.io.PrintStream;

import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
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

public class UpdateHotelHomeJServlet extends HttpServlet {
	/**
	 * Only registered users can visit this page and select their hotel for editing. Requires log in.
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

		/*Check that user is logged in and registered.*/
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user==null){
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}
		else{
			String userID = user.getUserId();
			Key managerKey = KeyFactory.createKey("Manager", userID);
			try {
				Entity manager = datastore.get(managerKey);


				/*Build search menus.*/
				Query query2 = new Query("Hotel");
				List<String> hotelNames = new ArrayList<String>();
				List<String> hotelNames_j = new ArrayList<String>();
				List<String> hotelKeys = new ArrayList<String>();
				List<String> managerList = new ArrayList<String>();
				List<List<String>> managerLists = new ArrayList<List<String>>();
				PreparedQuery pq2 = datastore.prepare(query2);
				for (Entity result : pq2.asIterable()) 
				{hotelNames.add((String) result.getProperty("hotelName"));
				hotelNames_j.add((String) result.getProperty("hotelName_j"));
				Key tempkey=result.getKey();
				String tempstring = KeyFactory.keyToString(tempkey);
				hotelKeys.add((String) tempstring);
				managerList= (List<String>) result.getProperty("managerList");
				managerLists.add(managerList);				
				}	

				/*Begin HTML*/
				pw.println("<HTML>");
				pw.println("<HEAD>");
				pw.println("<head>" +
						"<meta http-equiv='content-type' content='text/html;charset=shift_JIS'>" +
						"<link type='text/css' rel='stylesheet' 	href='/css/hotelsmain.css'>" +
						"<link href='http://fonts.googleapis.com/css?family=Marcellus' 	rel='stylesheet' type='text/css'>");
				pw.println("</head>");
				pw.println("<BODY>");
				pw.println("<div id='bigcontainer'>" +
						"		<div id='bigbanner_j' title='屋久島の主の宿泊'>" +
						"			<table id=languageselection>" +
						"				<tr><td class=languagebutton><a href='/updatehotelhome'>English</a></td>" +
						"				<td class=languagebutton><a href='/updatehotelhomej'>日本語</a></td>" +
						"			</table>" +
						"		</div>" +
						"	<div id=searchmenu><span class=bold><center>宿泊検索</center></span>"  +
						"		<form action=/goupdatehotel method='post'>" +
						"			<input type='hidden' name=language value='Japanese' />" +
						"			<dt>貴方の施設を選んで下さい。" +
						"					<dt><select name ='hotelKeyString' required style='width:190px;' >" +
						"						<option value='Choose' disabled selected>Choose . . .</option>");
				for (int j=0; j<hotelKeys.size(); j++){
					if (managerLists.get(j).contains(userID))
					{pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");}
				}
				pw.println(	"						</select>" +
						"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
						"		</form>	" + 
						"		<form action=/goupdatehotel method='post'>" +
						"			<input type='hidden' name=language value='Japanese' />" +
						"			<dt>Choose your accomodation:" +
						"					<dt><select name ='hotelKeyString' required style='width:190px;' >" +
						"						<option value='Choose' disabled selected>Choose . . .</option>");
				for (int j=0; j<hotelKeys.size(); j++){
					if (managerLists.get(j).contains(userID))
					{pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames.get(j) + "</option>");}	
				}
				pw.println(	"						</select>" +
						"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
						"		</form>	" + 						
						"	</div>" +
						"	<div id=centercontainer>" +
						"		<div id=results>" +
						"			<div id=dir>" +
						"				<form>");

				pw.println("<p>" + user.getNickname() + "様<p>こんにちは！貴方のグーグルID番号は" + userID +
						"です。" +
						"</p>貴方の登録された宿泊を左のリストから洗濯して下さい。基本のデータ編集や写真のアップロードができます。" +
						"編集ができない場合、管理員までご連絡下さい。また、<a href=" + userService.createLogoutURL(request.getRequestURI())+ " >ログアウト</a>" +
						" すれば、他のユーザーで編集することができます。" +
						"<p><a href=/displayhotelj>宿泊の一覧</a>");

				pw.println(	"			</form>" +
						"			</div>" +
						"		</div>" +
						"	</div>" +
						"	<div id=basics>" +
						"		<form id=basicsform style='padding:5px 5px;'>" +
						"			<iframe width='196' height='196' frameborder='0' scrolling='no' marginheight='0' marginwidth='0' src='https://maps.google.co.jp/maps?q=yakushima&amp;client=ubuntu&amp;channel=cs&amp;aq=f&amp;ie=UTF8&amp;hl=en&amp;hq=&amp;hnear=Miyanoura-dake&amp;t=m&amp;brcurrent=3,0x353d3c13be0ddd69:0xa4ad6fac096ac880,0&amp;ll=30.334954,130.512085&amp;spn=0.464625,0.535583&amp;z=9&amp;iwloc=A&amp;output=embed'></iframe><br /><small><a href='https://maps.google.co.jp/maps?q=yakushima&amp;client=ubuntu&amp;channel=cs&amp;aq=f&amp;ie=UTF8&amp;hl=en&amp;hq=&amp;hnear=Miyanoura-dake&amp;t=m&amp;brcurrent=3,0x353d3c13be0ddd69:0xa4ad6fac096ac880,0&amp;ll=30.334954,130.512085&amp;spn=0.464625,0.535583&amp;z=9&amp;iwloc=A&amp;source=embed' style='color:#0000FF;text-align:left'>View Larger Map</a></small>" +
						"		</form>" +
						"	</div>" +
						"</div>");
				pw.println("</BODY>");
				pw.println("</HTML>");
				pw.close();
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
			}
		}

	}

	private static String Filternulls(Entity hotel, String message) {
		String y = (String) hotel.getProperty(message);
		if (y == null | y == "null")
		{y="";}
		return y;
	}
}