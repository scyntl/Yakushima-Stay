package displayhotel;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.io.PrintWriter;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;


public class DisplayHotelHomeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html; charset=shift_JIS");
		PrintWriter pw = response.getWriter();

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

		int hotelcount=hotelKeys.size();

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
				"				<td class=languagebutton><a href='/displayhotelhomej'>���{��</a></td>" +
				"			</table>" +
				"		</div>" +
				"	<div id=searchmenu>" +
				"		<form action=/displayhotel method='get'>" +
				"			<dt>�{�݂�I��ŉ������B" +
				"					<dt><select name ='hotelKeyString' size=3 required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
		for (int j=0; j<hotelKeys.size(); j++){
			pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");
		}
		pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"		<form action=/displayhotel method='get'>" +
				"			<dt>Choose an accomodation:" +
				"					<dt><select name ='hotelKeyString' size=3 required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
		for (int j=0; j<hotelKeys.size(); j++){
			pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames.get(j) + "</option>");
		}
		pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"" +
				"<form accept-charset='utf-16' id=searchform action='/searchhotel' method='get'>" +
				"" +
				"�����Ō���:" +
				"				<dt>Location:" +
				"					<dd><select name ='locationselection'>" +
				"						<option value='All'>���ׂ�</option>" +
				"						<option value='Miyanoura'>�{�V�Y</option>" +
				"						<option value='Anbo' >���[</option>" +
				"						<option value='North' >�k�̒n��</option>" +
				"						<option value='South' >��̒n��/option>" +
				"						<option value='East' >���̒n��</option>" +
				"						<option value='West' >���̒n��</option>" +
				"						</select></dd>" +
				"" +				"				<dt>�h���̃^�C�v:" +
				"					<dd><input type='hidden' name='typeselection' value='placeholder'> " +
				"						<input id='campsite' type='checkbox' name='typeselection' value='Campsite' checked >�L�����v��<br>" +
				"						<input id='minshuku' type='checkbox' name='typeselection' value='Minshuku' checked >���h<br>" +
				"						<input id='business' type='checkbox' name='typeselection' value='Business Hotel' checked >�r�W�l�X�E�z�e��<br>" +
				"						<input id='hostel' type='checkbox' name='typeselection' value='Hostel' checked >�z�X�e��<br>" +
				"						<input id='ryokan' type='checkbox' name='typeselection' value='Japanese Inn' checked >����<br>" +
				"						<input id='fullhotel' type='checkbox' name='typeselection' value='Full Hotel' checked 	>�z�e��<br>" +
				"						<input id='o' type='checkbox' name='typeselection' value='Other' checked 	>��<br>" +
				"</dd>" +
				"" +
				"					<dt>�l�i:" +
				"					<dd><select name ='priceselection'>" +
				"						<option value='All'>���ׂ�</option>" +
				"						<option value='price1'>< &yen;5,000</option>" +
				"						<option value='price2' >&yen;5,000 ~ &yen;10,000</option>" +
				"						<option value='price3' >&yen;10,000 < </option>" +
				"						</select></dd>" +
				"					<dt>Language:" +
				"					<dd><select name ='speakselection'>" +
				"						<option value='All'>���ׂ�</option>" +
				"						<option value='Japanese'>���{��</option>" +
				"						<option value='English'>�p��</option>" +
				"						<option value='French'>�t�����X��</option>" +
				"						<option value='German'>�h�C�c��</option>" +	
				"						<option value='Chinese'>������</option>" +
				"						<option value='Korean'>�؍���</option>" +
				"						<option value='Russian'>���V�A��</option>" +
				"						</select></dd>" +
				"			<div style='text-align:right;'><input type='submit' value='����' /></div>" +
				"</form>" +
				"" +
				"" +
				"" +
				"</div>	" +
				"	<div id=centercontainer>" +
				"		<div id=results>" +
				"			" +
				"				<form><center style='font-size:1.2em;'>Currently posting information for " + hotelcount + " accomodations" +
						" in Yakushima!</center></form>" +
				"			" +
				"			<img src=/images/cover.jpg style='border-radius:25px;box-shadow: 8px 8px 4px #888888;' class=picture width=540><br><br>" +
				"			<div id=photobox>" +			
				"<br>" +
				"<p>Still haven't found what your looking for? Why not try the following two sites?" +
				"<p><img src=/images/yta_banner.jpg> The official Yakushima Tourism Association (Japanese only)" +
				"<p><br><img src=/images/YesYakushima.jpg height=80> A private company offering help in English" +
				"			</div>" +
				"			<div id=amenities>" +

				"				<form class=amenitiesform></form>" +
				"			</div>" +
				"			<div id=outline>" +
				"<div id=footer>" +
				"This website is maintained by Jenny at <a href=http://www.yakushimalife.com>Yakushima Life</a>, created in 2013." +
				"Pictures and text appearing on individual accommodation pages are property of their respectful owners. Other pictures are" +
				"property of Jenny. If you have questions about a particular accommodation please contact the accommodation. Inquiries about accomodations with the " +
				"YES!Yakushima logo can be addressed to <a href='http://www.yesyakushima.com'>YES!Yakushima</a>. If you have comments about" +
				" this website, please contact Jenny.</div>"  +
				"			</div>" +
				"		</div>" +
				"	</div>" +
				"	<div id=basics>" +
				"		<iframe width='196' height='196' frameborder='0' scrolling='no' marginheight='0' marginwidth='0' src='https://maps.google.co.jp/?ie=UTF8&amp;t=m&amp;brcurrent=3,0x353d1c2bfe7ce839:0xf1bd53adb0401ba7,1&amp;ll=30.375245,130.550537&amp;spn=0.464434,0.535583&amp;z=9&amp;output=embed'></iframe><br>" +

				"<script type='text/javascript' charset='utf-8' src='http://feed.tenki.jp/feed/blog/script/parts/point_clock/?map_point_id=1922&color=0&size=large'></script>" +
				"	</div>" +
				"</div>");
		pw.println("</BODY>");
		pw.println("</HTML>");
		pw.close();
	}
}

