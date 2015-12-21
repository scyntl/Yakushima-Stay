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
				"				<td class=languagebutton><a href='/displayhotelhomej'>日本語</a></td>" +
				"			</table>" +
				"		</div>" +
				"	<div id=searchmenu>" +
				"		<form action=/displayhotelj method='get'>" +
				"			<dt>施設を選んで下さい。" +
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
				"状件で検索:" +
				"				<dt>地域:" +
				"					<dd><select name ='locationselection'>" +
				"						<option value='All'>すべて</option>" +
				"						<option value='Miyanoura'>宮之浦</option>" +
				"						<option value='Anbo' >安房</option>" +
				"						<option value='North' >北の地域</option>" +
				"						<option value='South' >南の地域/option>" +
				"						<option value='East' >東の地域</option>" +
				"						<option value='West' >西の地域</option>" +
				"						</select></dd>" +
				"" +				"				<dt>宿泊のタイプ:" +
				"					<dd><input type='hidden' name='typeselection' value='placeholder'> " +
				"						<input id='campsite' type='checkbox' name='typeselection' value='Campsite' checked >キャンプ場<br>" +
				"						<input id='minshuku' type='checkbox' name='typeselection' value='Minshuku' checked >民宿<br>" +
				"						<input id='business' type='checkbox' name='typeselection' value='Business Hotel' checked >ビジネス・ホテル<br>" +
				"						<input id='hostel' type='checkbox' name='typeselection' value='Hostel' checked >ホステル<br>" +
				"						<input id='ryokan' type='checkbox' name='typeselection' value='Japanese Inn' checked >旅館<br>" +
				"						<input id='fullhotel' type='checkbox' name='typeselection' value='Full Hotel' checked 	>ホテル<br>" +
				"						<input id='o' type='checkbox' name='typeselection' value='Other' checked 	>他<br>" +
				"</dd>" +
				"" +
				"					<dt>値段:" +
				"					<dd><select name ='priceselection'>" +
				"						<option value='All'>すべて</option>" +
				"						<option value='price1'>< &yen;5,000</option>" +
				"						<option value='price2' >&yen;5,000 ~ &yen;10,000</option>" +
				"						<option value='price3' >&yen;10,000 < </option>" +
				"						</select></dd>" +
				"					<dt>Language:" +
				"					<dd><select name ='speakselection'>" +
				"						<option value='All'>すべて</option>" +
				"						<option value='Japanese'>日本語</option>" +
				"						<option value='English'>英語</option>" +
				"						<option value='French'>フランス語</option>" +
				"						<option value='German'>ドイツ語</option>" +	
				"						<option value='Chinese'>中国語</option>" +
				"						<option value='Korean'>韓国語</option>" +
				"						<option value='Russian'>ロシア語</option>" +
				"						</select></dd>" +
				"			<div style='text-align:right;'><input type='submit' value='検索' /></div>" +
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
				"地域：<i>" + locationselection + "</i><br>" +
				"宿泊太付：<i> ");
		int tcsize=typecollection.size();
		for (int j=1; j<tcsize-1; j++){
			pw.println(typecollection.get(j));
			if (j<tcsize-2)
				pw.println(", ");
		}
		pw.println("<br></i>料金: <i>");
		if (priceselection.equals("price1"))
			pw.println("&yen;5,000以下");
		else if (priceselection.equals("price2"))
			pw.println("&yen;5,000 ~ &yen;10,000");
		else
			pw.println("&yen;10,000以上");
		pw.println("<br></i>言語: <i>" + speakselection);


		int k=hotelKeysResults.size();
		pw.println("</i></div><p>この検索状件を満ちる施設 " + k + " 件があります.");
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
				"本サイトに掲載されるテキスト、写真の著作権は各施設に帰属しています。トップページの写真などの著作権は" +
				"ジェニー＠<a href=http://www.yakushimalife.com>Yakushima Life</a>に帰属しています。宿泊施設に" +
				"関する質問がある場合、それぞれの施設に直接ご連絡下さい。それ意外のコメントはジェンーまで連絡下さい。" +
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


