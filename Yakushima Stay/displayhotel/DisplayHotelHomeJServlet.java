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


public class DisplayHotelHomeJServlet extends HttpServlet {
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
				"" +
				"<form accept-charset='utf-16' id=searchform action='/searchhotelj' method='get'>" +
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
				"					<dt>言語:" +
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
				"				<form><center style='font-size:1.2em;'>現在、屋久島の宿泊施設" + hotelcount + "件の情報を提供しております。" +
						"</center></form>" +
				"			" +
				"			<img src=/images/cover.jpg style='border-radius:25px;box-shadow: 8px 8px 4px #888888;' class=picture width=540><br><br>" +
				"			<div id=photobox>" +			
				"<br>" +
				"<p>ご希望の週箔は見つかりませんか。その情報は以下のページにあるかもしれません！" +
				"<ul>" +
				"<li>日本語対応: <br><img src=/images/yta_banner.jpg><br>" +
				"<li>英語対応: <br><img src=/images/YesYakushima.jpg height=80>" +
				"</ul>" +
				"			</div>" +
				"			<div id=amenities>" +

				"				<form class=amenitiesform></form>" +
				"			</div>" +
				"			<div id=outline>" +
				"<div id=footer>" +
				"本サイトに掲載されるテキスト、写真の著作権は各施設に帰属しています。トップページの写真などの著作権は" +
				"ジェニー＠<a href=http://www.yakushimalife.com>Yakushima Life</a>に帰属しています。宿泊施設に" +
				"関する質問がある場合、それぞれの施設に直接ご連絡下さい。それ意外のコメントはジェンーまで連絡下さい。</div>" +
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

