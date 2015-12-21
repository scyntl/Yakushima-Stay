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
import javax.servlet.http.*;

import java.io.*;


public class SearchHotelJServlet extends HttpServlet {
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
			if (!(locationselection.equals("All")))
				q.addFilter("area", Query.FilterOperator.EQUAL, locationselection);
		if (speakselection!=null)
			if (!(speakselection.equals("All")))
				q.addFilter("speaks", Query.FilterOperator.EQUAL, speakselection);
		q.addFilter("type", Query.FilterOperator.IN, typecollection);

		List<String> hotelNamesResults_j = new ArrayList<String>();
		List<String> hotelNamesResults = new ArrayList<String>();
		List<String> hotelKeysResults = new ArrayList<String>();
		List<String> hotelTagsResults = new ArrayList<String>();
		PreparedQuery pqr = datastore.prepare(q);
		for (Entity resultr : pqr.asIterable()) 
		{hotelNamesResults.add((String) resultr.getProperty("hotelName"));
		hotelNamesResults_j.add((String) resultr.getProperty("hotelName_j"));
		hotelTagsResults.add((String) resultr.getProperty("tagline_j"));
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
		PreparedQuery pq2 = datastore.prepare(query2);
		for (Entity result : pq2.asIterable()) 
		{hotelNames.add((String) result.getProperty("hotelName"));
		hotelNames_j.add((String) result.getProperty("hotelName_j"));
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
				"				<td class=languagebutton><a href='/displayhotelhomej'>���{��</a></td>" +
				"			</table>" +
				"		</div>" +
				"	<div id=searchmenu>" +
				"		<form action=/displayhotelj method='get'>" +
				"			<dt>�{�݂�I��ŉ������B" +
				"					<dt><select name ='hotelKeyString' size=3 required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
		for (int j=0; j<hotelKeys.size(); j++){
			pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");
		}
		pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"		<form action=/displayhotelj method='get'>" +
				"			<dt>Choose an accomodation:" +
				"					<dt><select name ='hotelKeyString' size=3 required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
		for (int j=0; j<hotelKeys.size(); j++){
			pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames.get(j) + "</option>");
		}
		pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"" +				"<form accept-charset='utf-16' id=searchform action='/searchhotelj' method='get'>" +
				"" +
				"�󌏂Ō���:" +
				"				<dt>�n��:" +
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

				"			<div id=photobox>" +
				"<h3>Search Parameters</h3>" +
				"<div id=searchparams>" +
				"�n��F<i>" + locationselection + "</i><br>" +
				"�h�����t�F<i> ");
		int tcsize=typecollection.size();
		for (int j=1; j<tcsize-1; j++){
			pw.println(typecollection.get(j));
			if (j<tcsize-2)
				pw.println(", ");
		}
		pw.println("<br></i>����: <i>");
		if (priceselection.equals("price1"))
			pw.println("&yen;5,000�ȉ�");
		else if (priceselection.equals("price2"))
			pw.println("&yen;5,000 ~ &yen;10,000");
		else
			pw.println("&yen;10,000�ȏ�");
		pw.println("<br></i>����: <i>" + speakselection);


		int k=hotelKeysResults.size();
		pw.println("</i></div><p>���̌����󌏂𖞂���{�� " + k + " ��������܂�.");
		if (k>0){
			pw.println("<ol>");
			for (int j=0; j<hotelKeysResults.size(); j++){
				pw.println("<li><a href='/displayhotelj?hotelKeyString="+ hotelKeysResults.get(j) + "'> " + hotelNamesResults_j.get(j) + "</a>");
				if (!(hotelTagsResults.get(j)==null))
					pw.println(" - " + hotelTagsResults.get(j));
			}
			pw.println("</ol>");
		}
		pw.println("			<div id=outline><br><br><br><br>" +
				"			<div id=footer>" +
				"�{�T�C�g�Ɍf�ڂ����e�L�X�g�A�ʐ^�̒��쌠�͊e�{�݂ɋA�����Ă��܂��B�g�b�v�y�[�W�̎ʐ^�Ȃǂ̒��쌠��" +
				"�W�F�j�[��<a href=http://www.yakushimalife.com>Yakushima Life</a>�ɋA�����Ă��܂��B�h���{�݂�" +
				"�ւ��鎿�₪����ꍇ�A���ꂼ��̎{�݂ɒ��ڂ��A���������B����ӊO�̃R�����g�̓W�F���[�܂ŘA���������B" +
				"			</div>" +
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
}


