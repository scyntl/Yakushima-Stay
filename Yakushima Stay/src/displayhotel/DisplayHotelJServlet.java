package displayhotel;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.*;

public class DisplayHotelJServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html; charset=shift_JIS");
		PrintWriter pw = response.getWriter();

		String hotelKeyString = request.getParameter("hotelKeyString");
		Key hotelKey=null;
		if (hotelKeyString==null) 
			response.sendRedirect("/displayhotelhome?error=NoKey");
		else{
			hotelKey = KeyFactory.stringToKey(hotelKeyString);
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			try {
				Entity hotel = datastore.get(hotelKey);

				/*Build search menus.*/
				Query query2 = new Query("Hotel");
				List<String> hotelNames = new ArrayList<String>();
				List<String> hotelNames_j = new ArrayList<String>();
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

				/*Declare and get hotel properties.*/
				String hotelName = Filternulls(hotel, "hotelName");
				String hotelName_j = Filternulls(hotel, "hotelName_j");
				String tagline = Filternulls(hotel, "tagline_j");
				String includedamenitiesstring=  Filternulls(hotel, "includedamenitiesstring_j");
				String paidamenitiesstring=  Filternulls(hotel, "paidamenitiesstring_j");
				String roomamenitiesstring=  Filternulls(hotel, "roomamenitiesstring_j");
				String welcomingstring = Filternulls(hotel, "welcomingstring_j");
				String roomtypesstring = Filternulls(hotel, "roomtypesstring_j");
				String smokingstring =  Filternulls(hotel, "smokingstring_j");
				String sharedamenitiesstring=  Filternulls(hotel, "sharedamenitiesstring_j");
				String acceptsstring=  Filternulls(hotel, "acceptsstring_j");
				String mealsstring=  Filternulls(hotel, "mealsstring_j");
				String speaksstring= Filternulls(hotel, "speaksstring_j");
				String closestring=  Filternulls(hotel, "closestring_j");
				String travelporter = Filternulls(hotel, "travelporter");
				String yesyakushima = Filternulls(hotel, "yesyakushima");
				String touristassociation = Filternulls(hotel, "touristassociation");
				String location = Filternulls(hotel, "location_j");
				String telephone = Filternulls(hotel, "telephone");
				String mobile = Filternulls(hotel, "mobile");
				String address = Filternulls(hotel, "address");
				String address_j = Filternulls(hotel, "address_j");
				String homepage = Filternulls(hotel, "homepage");
				String homepagelink = Filternulls(hotel, "homepagelink");
				int priceminint =(int) (long) (Long) hotel.getProperty("pricemin");
				int pricemaxint =(int) (long) (Long) hotel.getProperty("pricemax");
				String pricemin=Integer.toString(priceminint);
				String pricemax=Integer.toString(pricemaxint);
				String guestsmax = Filternulls(hotel, "guestsmax");
				String typestring = Filternulls(hotel, "typestring_j");
				String checkin = Filternulls(hotel, "checkin");
				String checkout = Filternulls(hotel, "checkout");
				String published = Filternulls(hotel, "published");
				Text mapcode = (Text) hotel.getProperty("mapcode");
				Text outline = (Text) hotel.getProperty("outline_j");
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
				Date updateddate= (Date) hotel.getProperty("date");
				String updated = sdf.format(updateddate);
				
				String image1Url = (String) hotel.getProperty("image1Url");
				String image2Url = (String) hotel.getProperty("image2Url");
				String image3Url = (String) hotel.getProperty("image3Url");
				String image4Url = (String) hotel.getProperty("image4Url");
				String image5Url = (String) hotel.getProperty("image5Url");
				String imagelogoUrl = (String) hotel.getProperty("imagelogoUrl");
				String image1Url100 = image1Url + "=s100";
				String image2Url100 = image2Url + "=s100";
				String image3Url100 = image3Url + "=s100";
				String image4Url100 = image4Url + "=s100";
				String image5Url100 = image5Url + "=s100";
				String imagelogoUrl196 = imagelogoUrl + "=s196";
				if (image1Url100.contains("null")){
					image1Url100="/images/defaultt.jpg";
					image1Url="/images/default.jpg";
				}

				pw.println("<HTML>");
				pw.println("<hHEAD>" +
						"<meta http-equiv='content-type' content='text/html;charset=shift_JIS'>" +
						"<link type='text/css' rel='stylesheet' 	href='/css/hotelsmain.css'>" +
						"<link href='http://fonts.googleapis.com/css?family=Marcellus' 	rel='stylesheet' type='text/css'>" +
						"<script src='/jsor-jcarousel-7bb2e0a/lib/jquery-1.4.2.min.js'></script>" +
						"<script src='/jsor-jcarousel-7bb2e0a/lib/jquery.jcarousel.min.js'></script>" +
						"<link rel='stylesheet' type='text/css'" +
						"	href='/jsor-jcarousel-7bb2e0a/skins/tango/skin.css' />");
				pw.println("<script>" +
						"function mycarousel_initCallback(carousel) {" +
						"jQuery('.jcarousel-control a').bind('click', function() {" +
						"carousel.scroll(jQuery.jcarousel.intval(jQuery(this).text()));" +
						"return false;" +
						"});" +
						"}; " +
						"jQuery(document).ready(function() {" +
						"  jQuery('#mycarousel').jcarousel({" +
						"        scroll: 1," +
						"        initCallback: mycarousel_initCallback," +
						"    });" +
						"});" +
						"" +
						"" +
						"function mycarousel_initCallback(carousel) {" +
						"jQuery('.jcarousel-control a').bind('click', function() {" +
						"        carousel.scroll(jQuery.jcarousel.intval(jQuery(this).text()));" +
						"        return false;" +
						"    });};" +
						"// Ride the carousel..." +
						"jQuery(document).ready(function() {" +
						"    jQuery('#mycarousel').jcarousel({" +
						"        scroll: 1," +
						"        initCallback: mycarousel_initCallback," +
						"        // This tells jCarousel NOT to autobuild prev/next buttons" +
						"    });" +
						"});" +
						"" +
						"</script>" +
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
						"					<dt><select name ='hotelKeyString' required style='width:190px;' >" +
						"						<option value='Choose' disabled selected>Choose . . .</option>");
				for (int j=0; j<hotelKeys.size(); j++){
					if (hotelPublishedStates.get(j).equals( "checked")){
						pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");
					}
				}
				pw.println(	"						</select>" +
						"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
						"		</form>	" + 
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
						"						<option value='price2' >&yen;5,000 ‾ &yen;10,000</option>" +
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
						"</div>	" +
						"" +
						"" +
						"	<div id=centercontainer>" +
						"		<div id=results>" +
						"			<div id=dir>" +
						" 				<form accept-charset='utf-16' id=dirform action='/updatedir' method='post'>" +
						"					屋久島 >> " + location + " : <br><center><h3 style='font-size: 17px;'> " + hotelName_j + "</h3></center>" +
						"					<div id=tagline>");
				if (!tagline.equals("")){
					pw.println(tagline);
				}	
				pw.println("				</div>" +
						"				</form><center><table><tr>");
				if(!touristassociation.equals(""))
					pw.println("<td><img src=/images/check.jpg height=30><img src=/images/yta_banner.jpg height=40>");

				if(!travelporter.equals(""))
					pw.println("<td><img src=/images/check.jpg height=30><img src=/images/TravelPorter.png height=40><span style='font-size:1.2em;'>Travel Porter</span><tr>");

				if(!yesyakushima.equals(""))
					pw.println("<td><img src=/images/check.jpg height=30><img src=/images/YesYakushima.jpg height=40>");

				pw.println("					" +
						"</table></center></div>" +
						"			<div id=photobox>"+
						"  			  <ul id='mycarousel' class='jcarousel-skin-tango'>");
				if (!image1Url100.contains("null")){
					pw.println(	
							"        <li class='jcarousel-item-1'><img src=" + image1Url + "  alt='' /></li>");}
				if (!image2Url100.contains("null")){
					pw.println(	
							"        <li class='jcarousel-item-2'><img src=" + image2Url + "  alt='' /></li>");}
				if (!image3Url100.contains("null")){
					pw.println(	
							"        <li class='jcarousel-item-3'><img src=" + image3Url + "  alt='' /></li>");}
				if (!image4Url100.contains("null")){
					pw.println(	
							"        <li class='jcarousel-item-4'><img src=" + image4Url + "  alt='' /></li>");}
				if (!image5Url100.contains("null")){
					pw.println(	
							"        <li class='jcarousel-item-4'><img src=" + image5Url + "  alt='' /></li>");}
				pw.println(	
						"      </ul>" +
								"" +
								"  <div id='mycarousel' class='jcarousel-skin-tango'>" +
								"    <div class='jcarousel-control' >" +
						"      <center><table id=photoboxcontrols><tr>");
				if (!image1Url100.contains("null")){
					pw.println(	
							"		<td><a href='#'>1<br><img src=" + image1Url100 + " /></a>");}
				if (!image2Url100.contains("null")){
					pw.println(	
							"      <td><a href='#'>2<br><img src=" + image2Url100 + " /></a>");}
				if (!image3Url100.contains("null")){
					pw.println(	
							"      <td><a href='#'>3<br><img src=" + image3Url100 + " /></a>");}
				if (!image4Url100.contains("null")){
					pw.println(	
							"      <td><a href='#'>4<br><img src=" + image4Url100 + " /></a>");}
				if (!image5Url100.contains("null")){
					pw.println(	
							"      <td><a href='#'>5<br><img src=" + image5Url100 + " /></a>" );}
				pw.println(
						"      </table></center>" +
								"    </div>" +
								"    </div>" +
								"" +


				"			</div><br>" +
				"			<div id=amenities>" +
						"				<form class=amenitiesform>");
				if (!includedamenitiesstring.equals("")){
					pw.println("<h3>お泊まりのお客様は以下の施設を無料で使えます。</h3>" +	
							"					" + includedamenitiesstring + " <br>");
				}
				if (!paidamenitiesstring.equals("")){
					pw.println("<p>別の料金で" + paidamenitiesstring + "の利用ができます。</p>");
				}
				if (!roomamenitiesstring.equals("")){
					pw.println("<p>各部屋には" + roomamenitiesstring + " が用意されています。</p>");
				}	
				if (!sharedamenitiesstring.equals("")){
					pw.println("<p>共通施設として" + sharedamenitiesstring + "が用意されています。<br>");
				}
				if (!mealsstring.equals("")){
					pw.println("<p><h3>食事のプラン：</h3>" +
							"			" + mealsstring + " <br>");
				}
				if (!welcomingstring.equals("")){
					pw.println("<p>" + welcomingstring + " </p>" );
				}
				if (!roomtypesstring.equals("")){
					pw.println("<h3>部屋：</h3>" +roomtypesstring + "" );
				}
				if (!smokingstring.equals("")){
					pw.println("<br>" + smokingstring + " <br>");
				}
				if (!closestring.equals("")){
					pw.println("<p><h3>歩いて行ける近所の施設：</h3>" +
							"					" + closestring + "");
				}
				if (!acceptsstring.equals("")){
					pw.println("<p><h3>お支払い方法：</h3>" +
							"					" + acceptsstring + " <br>");
				}
				if (!speaksstring.equals("")){
					pw.println("<p><h3>対応できる言語：</h3>" +
							"					" + speaksstring + " <br>");
				}
				pw.println(	"				</form>" +
						"			</div>" +
						"		</div>" +
						"<div id=footer>"); try {
							request.getRequestDispatcher("/main/propertyfooter.html").include (request,response);
						} catch (ServletException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} pw.println("</div>"  +
						"	</div>" +
						"	<div id=basics>" +
						"		<form id=basicsform >");
				if (!(imagelogoUrl196.contains("null")))
					pw.println(	"			<div id=hotellogo><img src=" + imagelogoUrl196 + " style='max-width: 300px;' /></div>");
				pw.println(	"			<dl>");


				if (!homepage.equals("")){
					pw.println("</dd><dt>Homepage:" +
							"					<dd><a href=" + homepagelink + ">" + homepagelink + "</a></dd>");
				}
				if (!location.equals("")){
					pw.println("<dt>集落: " + location);
				}
				if (!telephone.equals("")){
					pw.println("<dt>TEL: " + telephone);
				}
				if (!mobile.equals("")){
					pw.println("<dt>MOBILE: " + mobile);
				}
				if (!address.equals("")){
					pw.println("</dd><dt>住所:" +
							"					<dd>	" + address_j +"</dd>");
				}
				if (!pricemin.equals("")){
					pw.println("<dt>料金: " +
							"					<dd>￥" + pricemin + " ‾ ￥" + pricemax + "</dd>");
				}
				if (!guestsmax.equals("")){
					pw.println("<dt>定員：" + guestsmax);
				}
				if (typestring.length()>14){
					pw.println("<dt>施設のタイプ：" +
							"					<dd>" + typestring);
				}
				if (!checkin.equals("")|!checkout.equals("")){
					pw.println("				<dt>チェックイン・チェックアウト：" +
							"					<dd>" + checkin + " / " +
							"						" + checkout + "</dd>");
				}
				pw.println("			</dl>" +
						"		</form>");
				
				if (mapcode!=null)
					pw.println(mapcode.getValue());
				if (outline!=null){
					pw.println("<div id=#reviewbox>" +
							"<h3>私の印象:</h3>" +
							"<textarea name=review cols=30 rows=10>"+outline.getValue()+"</textarea>" +
									"</div>");
				}
				if (updated!=null){
					pw.println("<div id=#updatedbox>" +
							"この施設の情報は"+updated+"に更新されました。" +
									"</div>");
				}
					pw.println(	"	</div>" +

				"</div>" +
						"");
				pw.println("</BODY>");
				pw.println("</HTML>");
				pw.close();

			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/displayhotelhome?error=NoKey");
			}
		}
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

