package displayhotel;

import java.util.*;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.*;


public class SearchHotelServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html; charset=shift_JIS");
		PrintWriter pw = response.getWriter();

		String locationselection = request.getParameter("locationselection");
		String priceselection = request.getParameter("priceselection");
		String speakselection = request.getParameter("speakselection");
		String typeselectioncontent[] = request.getParameterValues("typeselection");
		String language = request.getParameter("language");

		List<String> typecollection = new ArrayList<String>(Arrays.asList(typeselectioncontent));  
		int g=typeselectioncontent.length;

		/*Build Search Results*/
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Hotel");
		if (priceselection.equals("price1")){
			q.addFilter("lowprice", Query.FilterOperator.EQUAL, "true");
		}
		else if (priceselection.equals("price2")){
			q.addFilter("midprice", Query.FilterOperator.EQUAL, "true");
		}
		else if (priceselection.equals("price3")){
			q.addFilter("highprice", Query.FilterOperator.EQUAL, "true");
		}

		if (locationselection!=null)
			if (!(locationselection.equals("All"))){
				if ((locationselection.equals("Anbo"))|(locationselection.equals("Miyanoura"))){
					q.addFilter("location", Query.FilterOperator.EQUAL, locationselection);
				}
				else
				q.addFilter("area", Query.FilterOperator.EQUAL, locationselection);
			}
		if (speakselection!=null)
			if (!(speakselection.equals("All")))
				q.addFilter("speaks", Query.FilterOperator.EQUAL, speakselection);
		q.addFilter("type", Query.FilterOperator.IN, typecollection);

		List<String> hotelNamesResults_j = new ArrayList<String>();
		List<String> hotelNamesResults = new ArrayList<String>();
		List<String> hotelKeysResults = new ArrayList<String>();
		List<String> hotelTagsResults = new ArrayList<String>();
		List<String> hotelPublishedStatesResults = new ArrayList<String>();
		PreparedQuery pqr = datastore.prepare(q);
		for (Entity resultr : pqr.asIterable()) 
		{hotelNamesResults.add((String) resultr.getProperty("hotelName"));
		hotelNamesResults_j.add((String) resultr.getProperty("hotelName_j"));
		hotelTagsResults.add((String) resultr.getProperty("tagline"));
		hotelPublishedStatesResults.add((String) Filternulls(resultr, "published"));
		Key tempkeyr=resultr.getKey();
		String tempstringr = KeyFactory.keyToString(tempkeyr);
		hotelKeysResults.add((String) tempstringr);
		}


		/*Build search menus.*/
		Query query2 = new Query("Hotel");
		List<String> hotelLocations = new ArrayList<String>();
		List<String> hotelNames_j = new ArrayList<String>();
		List<String> hotelNames = new ArrayList<String>();
		List<String> hotelKeys = new ArrayList<String>();
		List<String> hotelPublishedStates = new ArrayList<String>();
		PreparedQuery pq2 = datastore.prepare(query2);
		for (Entity result : pq2.asIterable()) 
		{hotelNames.add((String) result.getProperty("hotelName"));
		hotelNames_j.add((String) result.getProperty("hotelName_j"));
		hotelPublishedStates.add((String) Filternulls(result, "published"));
		Key tempkey=result.getKey();
		String tempstring = KeyFactory.keyToString(tempkey);
		hotelKeys.add((String) tempstring);
		}	



		pw.println("<HTML>");
		pw.println("<HEAD>");
		pw.println("<head>" +
				"<meta http-equiv='content-type' content='text/html;charset=shift_JIS'>" +
				"<link type='text/css' rel='stylesheet' 	href='/css/hotelsmain.css'>" +
				"<link href='http://fonts.googleapis.com/css?family=Marcellus' 	rel='stylesheet' type='text/css'>" +
				"</head>");

		pw.println("<BODY>");
		pw.println("<div id='bigcontainer'>" +
				"		<div id='bigbanner' title='Comprehensive Listing of Hotels in Yakushima'>" +
				"			<table id=languageselection>" +
				"				<tr><td class=languagebutton><a href='/displayhotelhome'>English</a></td>" +
				"				<td class=languagebutton><a href='/displayhotelhomej'>日本語</a></td>" +
				"			</table>" +
				"		</div>" +
				"	<div id=searchmenu>" +
				"		<form action=/displayhotel method='get'>" +
				"			<dt>Choose an accomodation:" +
				"					<dt><select name ='hotelKeyString' required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
		for (int j=0; j<hotelKeys.size(); j++){
			if (hotelPublishedStates.get(j).equals( "checked")){
				pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames.get(j) + "</option>");
			}
		}
		pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"		<form action=/displayhotelj method='get'>" +
				"			<dt>施設を選んで下さい。" +
				"					<dt><select name ='hotelKeyString'  required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
		for (int j=0; j<hotelKeys.size(); j++){
			if (hotelPublishedStates.get(j).equals( "checked")){
				pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");
			}
		}
		pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"" +
				"<form accept-charset='utf-16' id=searchform action='/searchhotel' method='get'>" +
				"" +
				"Or conduct a search:" +
				"				<dt>Location:" +
				"					<dd><select name ='locationselection'>" +
				"						<option value='All'>All</option>" +
				"						<option value='Miyanoura'>Miyanoura</option>" +
				"						<option value='Anbo' >Anbo</option>" +
				"						<option value='North' >North Area</option>" +
				"						<option value='South' >South Area</option>" +
				"						<option value='East' >East Area</option>" +
				"						<option value='West' >West Area</option>" +
				"						</select></dd>" +
				"" +				"				<dt>Accomodation Types:" +
				"					<dd><input type='hidden' name='typeselection' value='placeholder'> " +
				"						<input id='campsite' type='checkbox' name='typeselection' value='Campsite' checked >Campsite<br>" +
				"						<input id='cottages' type='checkbox' name='typeselection' value='Cottages' checked >Cottages<br>" +
				"						<input id='minshuku' type='checkbox' name='typeselection' value='Minshuku' checked >Minshuku<br>" +
				"						<input id='business' type='checkbox' name='typeselection' value='Business Hotel' checked >Business Hotel<br>" +
				"						<input id='hostel' type='checkbox' name='typeselection' value='Hostel' checked >Hostel<br>" +
				"						<input id='ryokan' type='checkbox' name='typeselection' value='Japanese Inn' checked >Japanese Inn<br>" +
				"						<input id='fullhotel' type='checkbox' name='typeselection' value='Full Hotel' checked 	>Full Hotel<br>" +
				"						<input id='o' type='checkbox' name='typeselection' value='Other' checked 	>Other<br>" +
				"</dd>" +
				"" +
				"					<dt>Price:" +
				"					<dd><select name ='priceselection'>" +
				"						<option value='All'>All</option>" +
				"						<option value='price1'>< &yen;5,000</option>" +
				"						<option value='price2' >&yen;5,000 ‾ &yen;10,000</option>" +
				"						<option value='price3' >&yen;10,000 < </option>" +
				"						</select></dd>" +
				"					<dt>Language:" +
				"					<dd><select name ='speakselection'>" +
				"						<option value='All'>All</option>" +
				"						<option value='Japanese'>Japanese</option>" +
				"						<option value='English'>English</option>" +
				"						<option value='French'>French</option>" +
				"						<option value='German'>German</option>" +	
				"						<option value='Chinese'>Chinese</option>" +
				"						<option value='Korean'>Korean</option>" +
				"						<option value='Russian'>Russian</option>" +
				"						</select></dd>" +
				"			<div style='text-align:right;'><input type='submit' value='Search' /></div>" +
				"</form>" +
				"" +
				"" +
				"" +
				"</div>	" +
				"	<div id=centercontainer>" +
				"		<div id=results>" +
				"			" +

				"			<div id=photobox2>" +
				"<h3>Search Parameters</h3>" +
				"<div id=searchparams>" +
				"Location: <i>" + locationselection + "</i><br>" +
				"Accommodation Type:<i> ");
		int tcsize=typecollection.size();
		for (int j=1; j<tcsize; j++){
			pw.println(typecollection.get(j));
			if (j<tcsize-1)
				pw.println(", ");
		}
		pw.println("<br></i>Price: <i>");
		if (priceselection.equals("price1"))
			pw.println("Less than &yen;5,000");
		else if (priceselection.equals("price2"))
			pw.println("&yen;5,000 ‾ &yen;10,000");
		else if (priceselection.equals("price3"))
			pw.println("Greater than &yen;10,000");
		else
			pw.println("Any");
		pw.println("<br></i>Languages: <i>" + speakselection);

		int i=0;
		int k=hotelKeysResults.size();
		for (int j = 0; j < k; j++) {
			if (hotelPublishedStatesResults.get(j).equals( "checked")){
				i++;
			}
		}
		pw.println("</i></div><p>Your search returned " + i + " result(s).");
		if (k>0){
			pw.println("<ol>");
			for (int j=0; j<hotelKeysResults.size(); j++){
				if (hotelPublishedStatesResults.get(j).equals( "checked")){
					pw.println("<li><a href='/displayhotel?hotelKeyString="+ hotelKeysResults.get(j) + "'> " + hotelNamesResults.get(j) + "</a> ("+ hotelNamesResults_j.get(j) + ")");
					if (!(hotelTagsResults.get(j)==null))
						pw.println(" - " + hotelTagsResults.get(j));
				}
			}
			pw.println("</ol>");
		}
		pw.println("			<div id=outline><br><br><br><br>" +
				"<div id=footer>"); try {
					request.getRequestDispatcher("/main/propertyfooter.html").include (request,response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} pw.println("</div>"  +
				"				</div>" +
				"			</div>" +
				"		</div>" +
				"	</div>" +
				"	<div id=basics>" +
				"		<iframe width='196' height='196' frameborder='0' scrolling='no' marginheight='0' marginwidth='0' src='https://maps.google.co.jp/?ie=UTF8&amp;t=m&amp;brcurrent=3,0x353d1c2bfe7ce839:0xf1bd53adb0401ba7,1&amp;ll=30.375245,130.550537&amp;spn=0.464434,0.535583&amp;z=9&amp;output=embed'></iframe><br>" +
				"		<script type='text/javascript' charset='utf-8' src='http://feed.tenki.jp/feed/blog/script/parts/point_clock/?map_point_id=1922&color=0&size=large'></script>" +
				"	</div>" +
				"</div>");
		pw.println("</BODY>");
		pw.println("</HTML>");
		pw.close();
	}
	
	private static String Filternulls(Entity hotel, String message) {
		String y = (String) hotel.getProperty(message);
		if (y == null)
			y="";
		else if (y.equals("null"))
			y="";
		return y;
	}
}

