package updatehotel;

import java.util.*;

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
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.*;

public class UpdateHotelJServlet extends HttpServlet {
	/**
	 * User Update of Hotel Info. Requires users to login to a registered Google account.
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html; charset=shift_JIS");
		PrintWriter pw = response.getWriter();
		String hotelKeyString = request.getParameter("hotelKeyString");
		String error = request.getParameter("error");
		String rredirectpage = request.getParameter("redirectpage");

		/**Check for passed errors and hotel key.**/
		if (error!=null){
			pw.println("There has been an error: "+error);
		}
		Key hotelKey=null;
		if (hotelKeyString==null) 
		{response.sendRedirect("/updatehotelhome?error=nokey&redirectpage="+rredirectpage+"firsterror="+error);}
		else{
			if (hotelKeyString==null|hotelKeyString.equals("null"))
			{response.sendRedirect("/updatehotelhome?error=nokey&redirectpage="+rredirectpage+"firsterror="+error);}
			else
			{hotelKey = KeyFactory.stringToKey(hotelKeyString);}
		}



		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		UserService userService = UserServiceFactory.getUserService();

		/*Find the hotel with your hotel Key.*/
		Query query = new Query("Hotel", hotelKey).addSort("date",
				Query.SortDirection.DESCENDING);
		List<Entity> hotels = datastore.prepare(query).asList(
				FetchOptions.Builder.withLimit(1));

		/*Check that user is logged in and registered.*/
		User user = userService.getCurrentUser();
		if (user==null){
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}
		else{
			String userID = user.getUserId();
			Key managerKey = KeyFactory.createKey("Manager", userID);
			try {
				Entity hotel = datastore.get(hotelKey);
				List<String> managerList = new ArrayList<String>();
				managerList=(List<String>) hotel.getProperty("managerList");
				if (managerList.indexOf(userID)<0)
				{response.sendRedirect("/updatehotelhome?error=HotelManagerMismatch");}
				else{



					/*Build search menus.*/
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
					managerList= (List<String>) result.getProperty("managerList");
					managerLists.add(managerList);	
					}	

					/*Declare and get hotel properties.*/
					String hotelName = Filternulls(hotel, "hotelName");
					String hotelName_j = Filternulls(hotel, "hotelName_j");
					String tagline = Filternulls(hotel, "tagline");
					String tagline_j = Filternulls(hotel, "tagline_j");
					String incwasher = (String) hotel.getProperty("incwasher");
					String incdryer =  (String) hotel.getProperty("incdryer");
					String inclaundry =  (String) hotel.getProperty("inclaundry");
					String inctowel =  (String) hotel.getProperty("inctowel");
					String inchairdryer =  (String) hotel.getProperty("inchairdryer");
					String inciron =  (String) hotel.getProperty("inciron");
					String incdvd =  (String) hotel.getProperty("incdvd");
					String incwifi =  (String) hotel.getProperty("incwifi");
					String inccomputer =  (String) hotel.getProperty("inccomputer");
					String incbicycle =  (String) hotel.getProperty("incbicycle");
					String inccar =  (String) hotel.getProperty("inccar");
					String incluggage =  (String) hotel.getProperty("incluggage");
					String incbus =  (String) hotel.getProperty("incbus");
					String includedotheramenities=  Filternulls(hotel, "includedotheramenities");
					String includedamenitiesstring=  Filternulls(hotel, "includedamenitiesstring");
					String includedotheramenities_j=  Filternulls(hotel, "includedotheramenities_j");
					String includedamenitiesstring_j=  Filternulls(hotel, "includedamenitiesstring_j");
					String paidwasher = (String) hotel.getProperty("paidwasher");
					String paiddryer =  (String) hotel.getProperty("paiddryer");
					String paidlaundry =  (String) hotel.getProperty("paidlaundry");
					String paidtowel =  (String) hotel.getProperty("paidtowel");
					String paidhairdryer =  (String) hotel.getProperty("paidhairdryer");
					String paidiron =  (String) hotel.getProperty("paidiron");
					String paiddvd =  (String) hotel.getProperty("paiddvd");
					String paidwifi =  (String) hotel.getProperty("paidwifi");
					String paidcomputer =  (String) hotel.getProperty("paidcomputer");
					String paidbicycle =  (String) hotel.getProperty("paidbicycle");
					String paidcar =  (String) hotel.getProperty("paidcar");
					String paidluggage =  (String) hotel.getProperty("paidluggage");
					String paidbus =  (String) hotel.getProperty("paidbus");
					String paidotheramenities=  Filternulls(hotel, "paidotheramenities");
					String paidamenitiesstring=  Filternulls(hotel, "paidamenitiesstring");
					String paidotheramenities_j=  Filternulls(hotel, "paidotheramenities_j");
					String paidamenitiesstring_j=  Filternulls(hotel, "paidamenitiesstring_j");
					String roomtoilet = (String) hotel.getProperty("roomtoilet");
					String roombath = (String) hotel.getProperty("roombath");
					String roomkitchen = (String) hotel.getProperty("roomkitchen");
					String roommicrowave = (String) hotel.getProperty("roommicrowave");
					String roomfridge = (String) hotel.getProperty("roomfridge");
					String roomac = (String) hotel.getProperty("roomac");
					String roomheater = (String) hotel.getProperty("roomheater");
					String roomtv = (String) hotel.getProperty("roomtv");
					String roomcable = (String) hotel.getProperty("roomcable");
					String roomphone = (String) hotel.getProperty("roomphone");
					String roomwifi = (String) hotel.getProperty("roomwifi");
					String roomotheramenities=  Filternulls(hotel, "roomotheramenities");
					String roomotheramenities_j=  Filternulls(hotel, "roomotheramenities_j");
					String roomamenitiesstring=  Filternulls(hotel, "roomamenitiesstring");
					String roomamenitiesstring_j=  Filternulls(hotel, "roomamenitiesstring_j");
					String sharedtoilet = (String) hotel.getProperty("sharedtoilet");
					String sharedshower = (String) hotel.getProperty("sharedshower");
					String sharedpublicbath = (String) hotel.getProperty("sharedpublicbath");
					String sharedbath = (String) hotel.getProperty("sharedbath");
					String sharedoutdoorbath = (String) hotel.getProperty("sharedoutdoorbath");
					String sharedsauna = (String) hotel.getProperty("sharedsauna");
					String sharedspa = (String) hotel.getProperty("sharedspa");
					String sharedpool = (String) hotel.getProperty("sharedpool");
					String sharedkitchen = (String) hotel.getProperty("sharedkitchen");
					String sharedmicrowave = (String) hotel.getProperty("sharedmicrowave");
					String sharedfridge = (String) hotel.getProperty("sharedfridge");
					String sharedac = (String) hotel.getProperty("sharedac");
					String sharedheater = (String) hotel.getProperty("sharedheater");
					String sharedtv = (String) hotel.getProperty("sharedtv");
					String shareddvd = (String) hotel.getProperty("shareddvd");
					String sharedcomputer = (String) hotel.getProperty("sharedcomputer");
					String sharedotheramenities=  Filternulls(hotel, "sharedotheramenities");
					String sharedamenitiesstring=  Filternulls(hotel, "sharedamenitiesstring");
					String sharedotheramenities_j=  Filternulls(hotel, "sharedotheramenities_j");
					String sharedamenitiesstring_j=  Filternulls(hotel, "sharedamenitiesstring_j");
					String mealbreakfast=  (String) hotel.getProperty("mealbreakfast");
					String mealdinner=  (String) hotel.getProperty("mealdinner");
					String mealstayonly=  (String) hotel.getProperty("mealstayonly");
					String mealvegetarian=  (String) hotel.getProperty("mealvegetarian");
					String mealallergen=  (String) hotel.getProperty("mealallergen");
					String mealsother =  Filternulls(hotel, "mealsother");
					String mealsstring =  Filternulls(hotel, "mealsstring");
					String mealsother_j =  Filternulls(hotel, "mealsother_j");
					String mealsstring_j =  Filternulls(hotel, "mealsstring_j");
					String welkid=  (String) hotel.getProperty("welkid");
					String welhandicapped=  (String) hotel.getProperty("welhandicapped");
					String welgroups=  (String) hotel.getProperty("welgroups");
					String welpet=  (String) hotel.getProperty("welpet");
					String welforeigner=  (String) hotel.getProperty("welforeigner");
					String welcouple=  (String) hotel.getProperty("welcouple");
					String welnoanimals=  (String) hotel.getProperty("welnoanimals");
					String smokingyes=  (String) hotel.getProperty("smokingyes");
					String smokingno=  (String) hotel.getProperty("smokingno");
					String smokingboth=  (String) hotel.getProperty("smokingboth");
					String acceptsvisa=  (String) hotel.getProperty("acceptsvisa");
					String acceptsmastercard=  (String) hotel.getProperty("acceptsmastercard");
					String acceptsdiscover=  (String) hotel.getProperty("acceptsdiscover");
					String acceptsamericanexpress=  (String) hotel.getProperty("acceptsamericanexpress");
					String acceptstravelerschecks=  (String) hotel.getProperty("acceptstravelerschecks");
					String acceptsjtb=  (String) hotel.getProperty("acceptsjtb");
					String acceptstransfer=  (String) hotel.getProperty("acceptstransfer");
					String acceptscash=  (String) hotel.getProperty("acceptscash");
					String acceptsothers=  Filternulls(hotel, "acceptsothers");
					String acceptsothers_j=  Filternulls(hotel, "acceptsothers_j");
					String acceptsstring=  Filternulls(hotel, "acceptsstring");
					String acceptsstring_j=  Filternulls(hotel, "acceptsstring_j");
					String speaksenglish=  (String) hotel.getProperty("speaksenglish");
					String speaksjapanese=  (String) hotel.getProperty("speaksjapanese");
					String speaksgerman=  (String) hotel.getProperty("speaksgerman");
					String speaksfrench=  (String) hotel.getProperty("speaksfrench");
					String speaksrussian=  (String) hotel.getProperty("speaksrussian");
					String speakskorean=  (String) hotel.getProperty("speakskorean");
					String speakschinese=  (String) hotel.getProperty("speakschinese");
					String speaksothers=  Filternulls(hotel, "speaksothers");
					String speaksothers_j=  Filternulls(hotel, "speaksothers_j");
					String speaksstring= Filternulls(hotel, "speaksstring");
					String speaksstring_j= Filternulls(hotel, "speaksstring_j");
					String closeport=  (String) hotel.getProperty("closeport");
					String closeportAnbo=  (String) hotel.getProperty("closeportAnbo");
					String closeairport=  (String) hotel.getProperty("closeairport");
					String closebus=  (String) hotel.getProperty("closebus");
					String closesuper=  (String) hotel.getProperty("closesuper");
					String closeconvenience=  (String) hotel.getProperty("closeconvenience");
					String closerestaurant=  (String) hotel.getProperty("closerestaurant");
					String closepost=  (String) hotel.getProperty("closepost");
					String otherclose=  (String) hotel.getProperty("otherclose");
					String closestring=  Filternulls(hotel, "closestring");
					String porttime5=  (String) hotel.getProperty("porttime5");
					String porttime10=  (String) hotel.getProperty("porttime10");
					String porttime15=  (String) hotel.getProperty("porttime15");
					String airporttime5=  (String) hotel.getProperty("airporttime5");
					String airporttime10=  (String) hotel.getProperty("airporttime10");
					String airporttime15=  (String) hotel.getProperty("airporttime15");
					String busstoptime5=  (String) hotel.getProperty("busstoptime5");
					String busstoptime10=  (String) hotel.getProperty("busstoptime10");
					String busstoptime15=  (String) hotel.getProperty("busstoptime15");
					String busstopname=  Filternulls(hotel, "busstopname");
					String busstopname_j=  Filternulls(hotel, "busstopname_j");
					String supermarkettime5=  (String) hotel.getProperty("supermarkettime5");
					String supermarkettime10=  (String) hotel.getProperty("supermarkettime10");
					String supermarkettime15=  (String) hotel.getProperty("supermarkettime15");
					String restauranttime5=  (String) hotel.getProperty("restauranttime5");
					String restauranttime10=  (String) hotel.getProperty("restauranttime10");
					String restauranttime15=  (String) hotel.getProperty("restauranttime15");
					String conveniencestoretime5=  (String) hotel.getProperty("conveniencestoretime5");
					String conveniencestoretime10=  (String) hotel.getProperty("conveniencestoretime10");
					String conveniencestoretime15=  (String) hotel.getProperty("conveniencestoretime15");
					String posttime5=  (String) hotel.getProperty("posttime5");
					String posttime10=  (String) hotel.getProperty("posttime10");
					String posttime15=  (String) hotel.getProperty("posttime15");
					String travelporter = Filternulls(hotel, "travelporter");
					String yesyakushima = Filternulls(hotel, "yesyakushima");
					String touristassociation = Filternulls(hotel, "touristassociation");
					String location = Filternulls(hotel, "location");
					String Miyanoura = (String) hotel.getProperty("Miyanoura");
					String Kusukawa = (String) hotel.getProperty("Kusukawa");
					String Koseda = (String) hotel.getProperty("Koseda");
					String Anbo = (String) hotel.getProperty("Anbo");
					String Funayuki = (String) hotel.getProperty("Funayuki");
					String Hara = (String) hotel.getProperty("Hara");
					String Mugio = (String) hotel.getProperty("Mugio");
					String Koshima = (String) hotel.getProperty("Koshima");
					String Onoaida = (String) hotel.getProperty("Onoaida");
					String Hirauchi = (String) hotel.getProperty("Hirauchi");
					String Yudomari = (String) hotel.getProperty("Yudomari");
					String Kurio = (String) hotel.getProperty("Kurio");
					String Nakama = (String) hotel.getProperty("Nakama");
					String Nagata = (String) hotel.getProperty("Nagata");
					String Issou = (String) hotel.getProperty("Issou");
					String Yoshida = (String) hotel.getProperty("Yoshida");
					String Shitoko= (String) hotel.getProperty("Shitoko");
					String telephonea = Filternulls(hotel, "telephonea");
					String telephoneb = Filternulls(hotel, "telephoneb");
					String telephonec = Filternulls(hotel, "telephonec");
					String mobilea = Filternulls(hotel, "mobilea");
					String mobileb = Filternulls(hotel, "mobileb");
					String mobilec = Filternulls(hotel, "mobilec");
					String address = Filternulls(hotel, "address");
					String address_j = Filternulls(hotel, "address_j");
					String homepage = Filternulls(hotel, "homepage");
					String homepagelink = Filternulls(hotel, "homepagelink");
					int priceminint =(int) (long) (Long) hotel.getProperty("pricemin");
					int pricemaxint =(int) (long) (Long) hotel.getProperty("pricemax");
					String pricemin=Integer.toString(priceminint);
					String pricemax=Integer.toString(pricemaxint);
					String guestsmax = Filternulls(hotel, "guestsmax");
					String campsite = (String) hotel.getProperty("campsite");
					String minshuku = (String) hotel.getProperty("minshuku");
					String business= (String) hotel.getProperty("business");
					String hostel = (String) hotel.getProperty("hostel");
					String ryokan = (String) hotel.getProperty("ryokan");
					String fullhotel = (String) hotel.getProperty("fullhotel");
					String typesother = Filternulls(hotel, "typesother");
					String typesother_j = Filternulls(hotel, "typesother_j");
					String typestring = Filternulls(hotel, "typestring");
					String typestring_j = Filternulls(hotel, "typestring_j");
					String checkin = Filternulls(hotel, "checkin");
					String checkout = Filternulls(hotel, "checkout");
					String published = Filternulls(hotel, "published");
					Text mapcode = (Text) hotel.getProperty("mapcode");
					Text outline = (Text) hotel.getProperty("outline");
					Text outline_j = (Text) hotel.getProperty("outline_j");
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
					if (image1Url100.contains("null"))
						image1Url100="";
					if (image2Url100.contains("null"))
						image2Url100="";
					if (image3Url100.contains("null"))
						image3Url100="";
					if (image4Url100.contains("null"))
						image4Url100="";
					if (image5Url100.contains("null"))
						image5Url100="";
					if (imagelogoUrl196.contains("null"))
						imagelogoUrl196="";

					/*Begin html*/
					pw.println("<HTML>");
					pw.println("<HEAD>");
					pw.println("<head>" +
							"<meta http-equiv='content-type' content='text/html;charset=shift_JIS'>" +
							"<link type='text/css' rel='stylesheet' 	href='/css/hotelsmain.css'>" +
							"<link href='http://fonts.googleapis.com/css?family=Marcellus' 	rel='stylesheet' type='text/css'>" +
							"<script src='/jsor-jcarousel-7bb2e0a/lib/jquery-1.4.2.min.js'></script>" +
							"<script src=''/jsor-jcarousel-7bb2e0a/lib/jquery.jcarousel.min.js'></script>" +
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
							"function visibilityAlert(){" +
							"	alert('この選択肢を有効にすれば、宿泊ページがネットの皆さんに閲覧できるようになります。');" +
							"	return true;" +
							"}" +
							"" +
							"function invisibilityAlert(){" +
							"	alert('この選択肢を無効にすれば、宿泊ページがネットの皆さんに閲覧できないようになります。');" +
							"	return true;" +
							"}" +
							"" +
							"function validateForm(){" +
							"	var x=document.forms['basicsform']['pricemin'].value;" +
							"	if (x==null){" +
							"	  	return true;" +
							"	}" +
							"	else {" +
							"		var j=x.length;" +
							"		if (j>0){" +
							"			for (var i=0;i<j;i++){" +
							"				if (x.charCodeAt(i)<48 |x.charCodeAt(i)>57){" +
							"					alert('Please use only half-width numerals to fill in the Price fields.');" +
							"					return false;" +
							"				}" +
							"			}" +
							"		}" +
							"	}" +
							"	x=document.forms['basicsform']['pricemax'].value;" +
							"	if (x==null){" +
							"	  	return true;" +
							"	}" +
							"	else {" +
							"		j=x.length;" +
							"		if (j>0){" +
							"			for (var i=0;i<j;i++){" +
							"				if (x.charCodeAt(i)<48 |x.charCodeAt(i)>57){" +
							"					alert('Please use only half-width numerals to fill in the Price fields.');" +
							"					return false;" +
							"				}" +
							"			}" +
							"		}" +
							"	}" +
							"" +
							"	x=document.forms['basicsform']['guestsmax'].value;" +
							"	if (x==null){" +
							"	  	return true;" +
							"	}" +
							"	else {" +
							"		j=x.length;" +
							"		if (j>0){" +
							"			for (var i=0;i<j;i++){" +
							"				if (x.charCodeAt(i)<48 |x.charCodeAt(i)>57){" +
							"					alert('Please use only half-width numerals to fill in the Maximum Guests field.');" +
							"					return false;" +
							"				}" +
							"			}" +
							"		}" +
							"	}" +
							"return true;" +
							"}" +
							"</script></head>");

					/*Begin Body*/
					pw.println("<BODY>");
					pw.println("<div id='bigcontainer'>" +
							"		<div id='bigbanner' title='Comprehensive Listing of Hotels in Yakushima'>" +
							"			<table id=languageselection>" +
							"				<tr><td class=languagebutton><a href='/updatehotelhome'>English</a></td>" +
							"				<td class=languagebutton><a href='/updatehotelhomej'>日本語</a></td>" +
							"			</table>" +
							"		</div>" +
							"	<div id=searchmenu>"  +
							"		<form action=/updatehotelj method='get'>" +
							"			<dt>貴方の施設を選んで下さい。" +
							"					<dt><select name ='hotelKeyString' size=3 required style='width:190px;' >" +
							"						<option value='Choose' disabled selected>Choose . . .</option>");
					for (int j=0; j<hotelKeys.size(); j++){
						if (managerLists.get(j).contains(userID))
						{pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");}
					}
					pw.println(	"						</select>" +
							"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
							"		</form>	" + 
							"		<form action=/updatehotel method='get'>" +
							"			<dt>Choose your accomodation:" +
							"					<dt><select name ='hotelKeyString' size=3 required style='width:190px;' >" +
							"						<option value='Choose' disabled selected>Choose . . .</option>");
					for (int j=0; j<hotelKeys.size(); j++){
						if (managerLists.get(j).contains(userID))
						{pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames.get(j) + "</option>");}
					}
					pw.println(	"						</select>" +
							"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
							"		</form>	" + 
							"</div>" +
							"	<div id=centercontainer>" +
							"		<div id=results>" +
							"			<div id=dir>" +
							" 				<form accept-charset='utf-16' id=visform action='/publishhotel' method='post' style='height:30px; font-weight:bold; text-align:center;' ");
					if (published.equals("checked")) 
					{pw.println("				onsubmit='return invisibilityAlert()' ");}
					else
					{pw.println("				onsubmit='return visibilityAlert()' ");}
					pw.println("				>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"					<input id=published type='checkbox' name='publish' value=published "+published+" >この内容を公開しますか？" +
							"						<input type='submit' value='OK'>" +
							"					<a href='/displayhotelj?hotelKeyString" + hotelKeyString + " target='_blank' >公開ページの閲覧</a>" +
							"				</form>" +
							" 				<form accept-charset='utf-16' id=dirform action='/updatetagline' method='post' style='height:180px;' >" +
							"					屋久島 >> " + location + " : <br><center><h3 style='font-size: 17px;'> " + hotelName_j + " (" + hotelName + ")</h3></center>" +
							"					" +
							"					<div id=tagline>" +
							"						一言やキャッチフレーズ：<br>" +
							"						<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"						<textarea name='tagline_j' rows='1' cols='60' maxlength=60 style='resize: none;' >" + tagline_j + " </textarea>" +
							"						<br>（英訳）：<br>" +
							"						<textarea name='tagline' rows='1' cols='60' maxlength=60 style='resize: none;' >" + tagline + " </textarea>" +
							"						<br><input type='submit' value='更新'>" +
							"					</div>" +
							"				</form>" +
							"			</div>" +
							"			<div id=photobox>" +
							"				<h2>映像</h2>" +
							"  				5枚の写真とロゴをアップロードして下さい。写真の大きさは300kbまで、幅が高さより長いものがいいです。ロゴは200x80が理想です。" +
							"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto1") + 
							" 					 method='post' enctype='multipart/form-data' > " +
							"					写真 1 <br>現在の写真：<br>" +
							"					<img src=" + image1Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
							"					<input type='file' name='myFile'> " +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"					<input type='submit' value='更新'>" +
							"					</form>	" +
							"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto2") + 
							" 					 method='post' enctype='multipart/form-data' > " +
							"					写真 2 <br>現在の写真：<br>" +
							"					<img src=" + image2Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
							"					<input type='file' name='myFile'> " +
							"					<input type=hidden name=hotelKeyString value=" +hotelKeyString + " > " +
							"					<input type='submit' value='更新'>" +
							"					</form>	" +
							"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto3") + 
							" 					 method='post' enctype='multipart/form-data' > " +
							"					写真 3 <br>現在の写真：<br>" +
							"					<img src=" + image3Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
							"					<input type='file' name='myFile'> " +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"					<input type='submit' value='更新'>" +
							"					</form>	" +
							"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto4") + 
							" 					 method='post' enctype='multipart/form-data' > " +
							"					写真 4 <br>現在の写真：<br>" +
							"					<img src=" + image4Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
							"					<input type='file' name='myFile'> " +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"					<input type='submit' value='更新'>" +
							"					</form>	" +
							"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto5") + 
							" 					 method='post' enctype='multipart/form-data' > " +
							"					写真 5 <br>現在の写真：<br>" +
							"					<img src=" + image5Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
							"					<input type='file' name='myFile'> " +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"					<input type='submit' value='更新'>" +
							"					</form>	" +
							"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephotologo") + 
							" 					 method='post' enctype='multipart/form-data' > " +
							"					ロゴ <br>現在の写真：<br>" +
							"					<img src=" + imagelogoUrl196 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
							"					<input type='file' name='myFile'> " +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"					<input type='submit' value='更新'>" +
							"					</form>	" +				
							"" +
							"" +
							"			</div><br>" +
							"			<div id=amenities>" +
							"				<h2	>施設とサービス</h2>" +
							"				<form class=amenitiesform id=includedamenitiesform" +
							"				action='/updateincludedamenities' accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString	 + ">" +
							"					<span class=bold>無料で使える施設やサービス</span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='includedamenities' value=''>" +
							"							<input id=incwasher type='checkbox' name='includedamenities'" +
							"								value='Washer'" + incwasher + ">洗濯機<br> " +
							"							<input id=incdryer type='checkbox' name='includedamenities'" +
							"							 	value='Dryer'" + incdryer + ">乾燥機<br>" +
							"							<input id=inclaundry type='checkbox' name='includedamenities'" +
							"								value='Laundry Service'" + inclaundry + ">ランドリーサービス<br>" +
							"						<td><input id=inctowel type='checkbox' name='includedamenities'" +
							"							value='Towel'" + inctowel + ">タオル<br> " +
							"							<input id=inchairdryer type='checkbox' name='includedamenities'" +
							"							value='Hair Dryer'" + inchairdryer + ">ヘアドライヤー<br>" +
							"							<input id=inciron type='checkbox' name='includedamenities'" +
							"							value='Iron'" + inciron + ">アイアン<br>" +
							"						<td><input id=incdvd type='checkbox' name='includedamenities'" +
							"							value='DVD-Player' " + incdvd + ">DVDプレーヤー<br> " +
							"							<input id=incwifi type='checkbox' name='includedamenities' " +
							"							value='Wifi' " + incwifi + ">無線LAN<br>" +
							"							<input id=inccomputer type='checkbox' name='includedamenities'" +
							"							value='Computer' " + inccomputer + ">パソコン・ネット<br>" +
							"						<td><input id=incbicycle type='checkbox' name='includedamenities' " +
							"							value='Bicycles' " + incbicycle + ">自転車<br>" +
							"							<input id=inccar type='checkbox' name='includedamenities'" +
							"							value='Rental Car' " + inccar + ">レンタカー<br> " +
							"							<input id=incluggage type='checkbox' name='includedamenities'" +
							"							value='Luggage Holding' " + incluggage + ">手荷物預かり<br> " +
							"							<input id=incbus type='checkbox' name='includedamenities'" +
							"							value='Welcome Bus' " + incbus + ">送迎シャトルバス<br>" +
							"					</table>" +
							"					その他：<br>" +
							"					<textarea name=includedotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + includedotheramenities_j + "</textarea>" +
							"					<br>英訳：" +
							"					<textarea name=includedotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + includedotheramenities + "</textarea>" +
							"					<p><dt> 現在のリスト： <dd>" + includedamenitiesstring_j + "</dd>" +
							"					<dt> （英訳） <dd>" + includedamenitiesstring + "</dd>" +
							"					<br><input type='submit' value='更新' /> <br>" +
							"				</form>" +
							"				<form class=amenitiesform id=paidamenitiesform" +
							"				action='/updatepaidamenities' accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"					<span class=bold>有料の施設・サービス</span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='paidamenities' value=''>" +
							"							<input id=paidwasher type='checkbox' name='paidamenities'" +
							"								value='Washer'" + paidwasher + ">洗濯機<br> " +
							"							<input id=paiddryer type='checkbox' name='paidamenities'" +
							"							 	value='Dryer'" + paiddryer + ">乾燥機<br>" +
							"							<input id=paidlaundry type='checkbox' name='paidamenities'" +
							"								value='Laundry Service'" + paidlaundry + ">ランドリーサービ<br>" +
							"						<td><input id=paidtowel type='checkbox' name='paidamenities'" +
							"							value='Towel'" + paidtowel + ">タオル<br> " +
							"							<input id=paidhairdryer type='checkbox' name='paidamenities'" +
							"							value='Hair Dryer'" + paidhairdryer + ">ヘアドライヤー<br>" +
							"							<input id=paidiron type='checkbox' name='paidamenities'" +
							"							value='Iron'" + paidiron + ">アイアン<br>" +
							"						<td><input id=paiddvd type='checkbox' name='paidamenities'" +
							"							value='DVD-Player' " + paiddvd + ">DVDプレーヤー<br> " +
							"							<input id=paidwifi type='checkbox' name='paidamenities' " +
							"							value='Wifi' " + paidwifi + ">無線LAN<br>" +
							"							<input id=paidcomputer type='checkbox' name='paidamenities'" +
							"							value='Computer' " + paidcomputer + ">パソコン・ネット<br>" +
							"						<td><input id=paidbicycle type='checkbox' name='paidamenities' " +
							"							value='Bicycles' " + paidbicycle + ">自転車<br>" +
							"							<input id=paidcar type='checkbox' name='paidamenities'" +
							"							value='Rental Car' " + paidcar + ">レンタカー<br> " +
							"							<input id=paidluggage type='checkbox' name='paidamenities'" +
							"							value='Luggage Holding' " + paidluggage + ">Luggage Holding<br> " +
							"							<input id=paidbus type='checkbox' name='paidamenities'" +
							"							value='Welcome Bus' " + paidbus + ">送迎シャトルバス<br>" +
							"					</table>" +
							"					その他：<br>" +
							"					<textarea name=paidotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + paidotheramenities_j + "</textarea>" +
							"					<br>英訳：<br>" +
							"					<textarea name=paidotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + paidotheramenities + "</textarea>" +
							"					<p><dt> 現在のリスト： <dd>" + paidamenitiesstring_j + "</dd>" +
							"					<dt> （英訳） <dd>" + paidamenitiesstring + "</dd>" +
							"					<br><input type='submit' value='更新' /> <br>" +
							"				</form>" +
							"				<form class=amenitiesform id=inroomamenitiesform" +
							"				action='/updateinroomamenities' accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"					<span class=bold>各部屋にある施設</span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='roomamenities' value=''>" +
							"							<input id=roomtoilet type='checkbox' name='roomamenities' value='Toilet'" + roomtoilet + ">トイレ<br> " +
							"							<input id=roombath type='checkbox' name='roomamenities' value='Bath'" + roombath + ">お風呂<br>" +
							"							<input id=roomkitchen type='checkbox' name='roomamenities' value='Kitchen/Kitchenette'" + roomkitchen + ">キッチンかキチネット<br>" +
							"						<td><input id=roommicrowave type='checkbox' name='roomamenities' value='Microwave'" + roommicrowave + ">電子レンジ<br>" +
							"							<input id=roomfridge type='checkbox' name='roomamenities' value='Fridge'" + roomfridge + ">冷蔵庫<br> " +
							"							<input id=roomac type='checkbox' name='roomamenities' value='A/C'" + roomac + ">エアコン<br>" +
							"							<input id=roomheater type='checkbox' name='roomamenities' value='Heater'" + roomheater + ">ヒーター<br>" +
							"						<td><input id=roomtv type='checkbox' name='roomamenities' value='Local TV'" + roomtv + ">テレビ<br> " +
							"							<input id=roomcable type='checkbox' name='roomamenities' value='Cable or Satelite TV'" + roomcable + ">ケーブルまたはサテライトテレビ<br> " +
							"							<input id=roomphone type='checkbox' name='roomamenities' value='Telephone'" + roomphone + ">電<br>" +
							"							<input id=roomwifi type='checkbox' name='roomamenities' value='Wifi'" + roomwifi + ">無線LAN<br>" +
							"					</table>" +
							"					その他：<br>" +
							"					<textarea name=roomotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + roomotheramenities_j + "</textarea>" +
							"					<br>英訳：<br>" +
							"					<textarea name=roomotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + roomotheramenities + "</textarea>" +
							"					<p><dt >現在のリスト： <dd>" + roomamenitiesstring_j + "</dd>" +
							"					<dt> （英訳） <dd>" + roomamenitiesstring + "</dd>" +
							"					<br><input type='submit' value='更新' />" +
							"				</form>" +
							"				<form class=amenitiesform id=sharedamenitiesform" +
							"				action='/updatesharedamenities' accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString+ ">" +
							"					<span class=bold>共通の施設</span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='sharedamenities' value=''>" + 
							"							<input id=sharedtoilet type='checkbox' name='sharedamenities' value='Toilet'" + sharedtoilet + ">トイレ<br> " +
							"							<input id=sharedshower type='checkbox' name='sharedamenities' value='Shower'" + sharedshower + ">シャワー<br>" +
							"							<input id=sharedbath type='checkbox' name='sharedamenities' value='Bath'" + sharedbath + ">お風呂<br> " +
							"							<input id=sharedpublicbath type='checkbox' name='sharedamenities' value='Public Bath'" + sharedpublicbath + ">共同浴場<br> " +
							"							<input id=sharedoutdoorbath type='checkbox' name='sharedamenities' value='Outdoor Bath'" + sharedoutdoorbath + ">露天風呂<br> " +
							"							<input id=sharedsauna type='checkbox' name='sharedamenities' value='Sauna'" + sharedsauna + ">サウナ<br> " +
							"							<input id=sharedspa type='checkbox' name='sharedamenities' value='Spa'" + sharedspa + "スパー<br> " +
							"							<input id=sharedpool type='checkbox' name='sharedamenities' value='Pool'" + sharedpool + ">プール<br>" +
							"							<input id=sharedkitchen type='checkbox' name='sharedamenities' value='Kitchen/Kitchenette'" + sharedkitchen + ">キッチンかキチネット<br>" +
							"						<td><input id=sharedmicrowave type='checkbox' name='sharedamenities' value='Microwave'" + sharedmicrowave + ">電子レンジ<br>" +
							"							<input id=sharedfridge type='checkbox' name='sharedamenities' value='Fridge'" + sharedfridge + ">冷蔵庫<br> " +
							"							<input id=sharedac type='checkbox' name='sharedamenities' value='A/C'" + sharedac + ">エアコン<br>" +
							"							<input id=sharedheater type='checkbox' name='sharedamenities' value='Heater'" + sharedheater + ">ヒーター<br> " +
							"							<input id=sharedtv type='checkbox' name='sharedamenities' value='Television'" + sharedtv + ">テレビ<br>" +
							"						<td><input id=shareddvd type='checkbox' name='sharedamenities' value='DVD Player'" + shareddvd + ">DVDプレイヤー<br>" +
							"							<input id=sharedcomputer type='checkbox' name='sharedamenities' value='Computer'" + sharedcomputer + ">パソコン・ネット<br>" +
							"					</table>" +
							"					その他：<br>" +
							"					<textarea name=sharedotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + sharedotheramenities_j + "</textarea>" +
							"					<br>英訳：<br>" +
							"					<textarea name=sharedotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + sharedotheramenities + "</textarea>" +
							"					<p><dt> 現在のリスト： <dd>" + sharedamenitiesstring_j + "</dd>" +
							"					<dt> （英訳） <dd>" + sharedamenitiesstring + "</dd>" +
							"					<br> <input type='submit' value='更新' />" +
							"				</form>" +
							"				<form class=amenitiesform id=mealsform action='/updatemeals'" +
							"				accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"					<span class=bold>食事のプラン</span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='meals' value=''>" +
							"							<input id=mealbreakfast type='checkbox' name='meals' value='Breakfast'" + mealbreakfast + ">朝食" +
							"						<td><input id=mealdinner type='checkbox' name='meals' value='Dinner'" + mealdinner + ">夕食" +
							"						<td><input id=mealstay type='checkbox' name='meals' value='Stay-Only'" + mealstayonly + ">素泊まり" +
							"						<tr>" +
							"						<td><input id=mealvegetarian type='checkbox' name='meals' value='Vegetarian Options'" + mealvegetarian + ">ベジタリアン料理可" +
							"						<td><input id=mealallergen type='checkbox' name='meals' value='Non-allergen Options'" + mealallergen + ">アレルギー物資を使わない料理可" +
							"						<td>" +
							"						<td>" +
							"					</table>" +
							"					その他：<br>" +
							"					<textarea name=mealsother_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + mealsother_j + "</textarea>" +
							"					<br>Others in Japanese:<br>" +
							"					<textarea name=mealsother  rows='1' cols='60' maxlength=60 style='resize: none;' >" + mealsother + "</textarea>" +
							"					<p><dt> 現在のリスト <dd>" + mealsstring_j + "</dd>" +
							"					<dt> （英訳） <dd>" + mealsstring + "</dd>" +
							"					<br> <input type='submit' value='更新' />" +
							"				</form>" +
							"				<form class=amenitiesform id=welcomingform action='/updatewelcoming'" +
							"				accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelName value=" + hotelName + ">" +
							"					<span class=bold>以下のお客様も大歓迎です。</span><br>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='welcoming' value=''> " +
							"							<input id=welpet type='checkbox' name='welcoming' value='pets'" + welpet + ">ペットをお連れのお客様<br>" +
							"							<input id=welkid type='checkbox' name='welcoming' value='children'" + welkid + ">お子様<br>" +
							"							<input id=welhandicapped type='checkbox' name='welcoming' value='guests with physical disabilities'" + welhandicapped + ">体の不自由の方" +
							"						<td><input id=welforeigner type='checkbox' name='welcoming' value='foreigners'" + welforeigner + ">外国人のお客様<br> " +
							"							<input id=welcouple type='checkbox' name='welcoming' value='couples'" + welcouple + ">カップル<br>" +
							"							<input id=welgroups type='checkbox' name='welcoming' value='largegroups'" + welgroups + ">団体のお客様" +
							"						<tr><td><input type='hidden' name='welnoanimals' value=''> " +
							"							<input id=welnoanmals type='checkbox' name='welnoanimals' value=' No animals are kept on the premises.'" + welnoanimals + "> この施設は動物は一切飼っておりません。" +
							"						<tr><td><br><input type='hidden' name='smoking' value=''>" +
							"							<input id=nonsmokingrooms type='radio' name='smoking' value='NonSmoking Rooms Only'" + smokingno + ">全室は禁煙<br> " +
							"							<input id=smokingrooms type='radio' name='smoking' value='Smoking Rooms Only'" + smokingyes + ">全室は喫煙可<br>" +
							"							<input id=bothrooms type='radio' name='smoking' value='Smoking and Nonsmoking Rooms'" + smokingboth + ">喫煙と禁煙の部屋はあります。" +
							"					</table>" +
							"					<br> <input type='submit' value='Update Welcoming' />" +
							"				</form>" +
							"				<form class=amenitiesform id=closetoform action='/updateclose'" +
							"				accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"					<span class=bold>以下の施設と近いです。（歩き時間）<br></span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='close' value=''>" +
							"							<input id=closeport type='checkbox' name='close' value='Port'" + closeport + ">港（宮之浦）" +
							"							<br>( 	<select name='porttime'>" +
							"									<option value='under 5 min'"+ porttime5 +"><5分</option>" +
							"									<option value='5‾10 min' "+ porttime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ porttime15 +">10‾15分</option>" +
							"								</select> )<br> " +
							"							<input id=closeport type='checkbox' name='close' value='PortAnbo'" + closeportAnbo + ">港（安房）" +
							"							<br>( 	<select name='porttime'>" +
							"									<option value='under 5 min'"+ porttime5 +"><5分</option>" +
							"									<option value='5‾10 min' "+ porttime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ porttime15 +">10‾15分</option>" +
							"								</select> )<br> " +
							"							<input id=closeairport type='checkbox' name='close' value='Airport'" + closeairport + ">Airport " +
							"							<br>( 	<select name='airporttime'>" +
							"									<option value='under 5 min'"+ airporttime5 +"><5分</option>" +
							"									<option value='5‾10 min'"+ airporttime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ airporttime15 +">10‾15分</option>" +
							"								</select> )<br> " +
							"							<input id=closebus type='checkbox' name='close' value='Bus Stop'" + closebus + ">Bus-Stop<br>" +
							"							( 	<select name='busstoptime'>" +
							"									<option value='<5 min'"+ busstoptime5 +"><5分</option>" +
							"									<option value='5‾10 min'"+ busstoptime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ busstoptime15 +">10‾15分</option>" +
							"								</select> )" +
							"							<dd>English Stop Name:<br><input type=text name=busstopname size=12 value="+busstopname+">" +
							"							<br>Japanese Stop Name:<br><input type=text name=busstopname_j size=12 value= " + busstopname_j +"></dd> " +
							"						<td><input id=closesuper type='checkbox' name='close' value='Supermarket'" + closesuper + ">Supermarket " +
							"							<br>( 	<select name='supermarkettime'>" +
							"									<option value='under 5 min'"+ supermarkettime5 +"><5分</option>" +
							"									<option value='5‾10 min'"+ supermarkettime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ supermarkettime15 +">10‾15分</option>" +
							"								</select> )<br> " +
							"							<input id=closeconenience type='checkbox' name='close' value='Convenience Store' " + closeconvenience + ">Convenience Store " +
							"							<br>(  <select name='conveniencestoretime'>" +
							"									<option value='under 5 min'"+ conveniencestoretime5 +"><5分</option>" +
							"									<option value='5‾10 min'"+ conveniencestoretime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ conveniencestoretime15 +">10‾15分</option>" +
							"								</select> )<br> " +
							"							<input id=closerestaurant type='checkbox' name='close' value='Restaurant/Cafe'" + closerestaurant + ">Restaurant/Cafe " +
							"							<br>( 	<select name='restauranttime'>" +
							"									<option value='under 5 min'"+ restauranttime5 +"><5分</option>" +
							"									<option value='5‾10 min'"+ restauranttime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ restauranttime15 +">10‾15分</option>" +
							"								</select> )<br> " +
							"							<input id=closepost type='checkbox' name='close' value='Post Office (with ATM)'" + closepost + ">Post Office (ATM) " +
							"							<br>( 	<select name='posttime'>" +
							"									<option value='under 5 min'"+ posttime5 +"><5分</option>" +
							"									<option value='5‾10 min'"+ posttime10 +">5‾10分</option>" +
							"									<option value='10‾15 min'"+ posttime15 +">10‾15分</option>" +
							"								</select> )<br>" +
							"					</table>" +
							"					<br> <input type='submit' value='更新' />" +
							"				</form>" +
							"				<form class=amenitiesform id=acceptsform action='/updateaccepts'" +
							"				accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"					<span class=bold>支払方法</span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='accepts' value=''> " +
							"							<input id=acceptsvisa type='checkbox' name='accepts' value='Visa'" + acceptsvisa + ">ビザ<br> " +
							"							<input id=acceptsmastercard type='checkbox' name='accepts' value='Mastercard'" + acceptsmastercard + ">マスターカード<br>" +
							"							<input id=acceptsjtb type='checkbox' name='accepts' value='JTB'" + acceptsjtb + ">JTB<br> " +
							"							<input id=acceptsdiscover type='checkbox' name='accepts' value='Discover'" + acceptsdiscover + ">ディスカバーカード<br> " +
							"							<input id=acceptsamerican type='checkbox' name='accepts' value='American Express'" + acceptsamericanexpress + ">アメリカンエキスプレス<br> " +
							"							<input id=acceptstravelerschecks type='checkbox' name='accepts' value='Travelers Checks'" + acceptstravelerschecks + ">トラベラーズチェック<br>" +
							"							<input id=acceptstransfer type='checkbox' name='accepts' value='bank transfers'" + acceptstransfer + ">振込<br>" +
							"							<input id=acceptscash type='checkbox' name='accepts' value='cash'" + acceptscash + ">現<br>" +
							"					</table>" +	
							"					その他：<br>" +
							"					<textarea name=acceptsothers  rows='1' cols='60' maxlength=60 style='resize: none;' >" + acceptsothers + "</textarea>" +
							"					<br>現在のリスト：<br>" +
							"					<textarea name=acceptsothers_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + acceptsothers_j + "</textarea>" +
							"					<p><dt> 現在のリスト <dd>" + acceptsstring_j + "</dd>" +
							"					<dt> （英訳） <dd>" + acceptsstring + "</dd>" +
							"					<br> <input type='submit' value='更新' />" +
							"				</form>" +
							"				<form class=amenitiesform id=speaksform action='/updatespeaks'" +
							"				accept-charset='utf-16' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"					<span class=bold>以下の言語で対応できます。</span>" +
							"					<table>" +
							"						<tr>" +
							"						<td><input type='hidden' name='speaks' value=''> " +
							"							<input id=speaksenglish type='checkbox' name='speaks' value='English'" + speaksenglish + ">英語<br> " +
							"							<input id=speaksjapanese type='checkbox' name='speaks' value='Japanese'" + speaksjapanese + ">日本語<br> " +
							"							<input id=speaksgerman type='checkbox' name='speaks' value='German'" + speaksgerman + ">ドイツ語<br>" +
							"							<input id=speaksrussian type='checkbox' name='speaks' value='Russian'" + speaksrussian + ">ロシア語<br> " +
							"							<input id=speakskorean type='checkbox' name='speaks' value='Korean'" + speakskorean + ">韓国語<br>" +
							"							<input id=speakschinese type='checkbox' name='speaks' value='Chinese'" + speakschinese + ">中国語<br> " +
							"							<input id=speaksfrench type='checkbox' name='speaks' value='French'" + speaksfrench + ">フランス語<br>" +
							"					</table>" +
							"					その他：<br>" +
							"					<textarea name=speaksothers_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + speaksothers_j + "</textarea>" +
							"					<br>英訳：<br>" +
							"					<textarea name=speaksother  rows='1' cols='60' maxlength=60 style='resize: none;' >" + speaksothers + "</textarea>" +
							"					<p><dt> 現在のリスト <dd>" + speaksstring + "</dd>" +
							"					<dt> （英訳） <dd>" + speaksstring_j + "</dd>" +			
							"					<input type='submit' value='設備登録' />" +
							"				</form>" +
							"			</div>" +
							"			<div id=outline>" +
							"				<form id=outlineform action='/updateoutline' accept-charset='utf-8' method='post'>" +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +	
							"<h2>施設の概要</span><h2>" +
							"					<textarea name=outline_j cols=60 rows=20  wrap='hard'>");
					if (outline_j!=null)
					{pw.println(outline_j.getValue());}
					pw.println(	"</textarea>" +
							"" +
							"<h2>概要の英訳<h2>" +
							"					<textarea name=outline cols=60 rows=20 wrap='hard'>");
					if (outline!=null)
					{pw.println(outline.getValue());}
					pw.println(	"</textarea>" +
							"					<input type='submit' value='更新' />" +
							"" +
							"				</form>" +
							"			</div>" +
							"		</div>" +
							"	</div>" +
							"	<div id=basics>" +
							"		<form id=basicsform action='/updatebasics' accept-charset='utf-8' method='post' onsubmit='return validateForm()' >" +
							"			<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"			<div id=hotellogo><img src=" + imagelogoUrl196 + " /></div>" +
							"			<dl>" +
							"				" +
							"				<dt>サービスや協会参加：" +
							"					<dd><input type='hidden' name='participation' value=''> " +
							"						<input id='touristassociation' type='checkbox' name='type' value='Tourist Association' " + touristassociation + ">観光協会<br>" +
							"						<input id='travelporter' type='checkbox' name='type' value='Travel Porter'" +  travelporter + ">Travel Porter<br>" +
							"						<input id='yesyakushima' type='checkbox' name='type' value='YesYakushima'" +  yesyakushima + ">Yes!Yakushima<br>" +
							"				<dt>Location:" +
							"					<dd><select name ='location'>" +
							"						<option value='Miyanoura'" + Miyanoura + ">宮之浦</option>" +
							"						<option value='Kusukawa'" + Kusukawa + ">楠川</option>" +
							"						<option value='Koseda'" + Koseda + ">小瀬田</option>" +
							"						<option value='Funayuki'" + Funayuki + ">船行</option>" +
							"						<option value='Anbo'" + Anbo + ">安房</option>" +
							"						<option value='Mugio'" + Mugio + ">麦生</option>" +
							"						<optiona value='Hara'" + Hara + ">原</option>" +
							"						<option value='Onoaida'" + Onoaida + ">尾之間</option>" +
							"						<option value='Koshima'" + Koshima + ">小島</option>" +
							"						<option value='Hirauchi'" + Hirauchi + ">平打</option>" +
							"						<option value='Yudomari'" + Yudomari + ">湯泊</option>" +
							"						<option value='Nakama'" + Nakama + ">中間</option>" +
							"						<option value='Kurio'" + Kurio + ">栗生</option>" +
							"						<option value='Nagata'" + Nagata + ">永田</option>" +
							"						<option value='Yoshida'" + Yoshida + ">吉田</option>" +
							"						<option value='Issou'" + Issou + ">一奏</option>" +
							"						<option value='Shitoko'" + Shitoko + ">志戸子</option>" +
							"						</select></dd>" +
							"				<dt>TEL：" +
							"					<dd><input type=hidden name='telephonea' size=4 value='0997'>" +
							"						0997-<input type=text name='telephoneb' size=2 maxlength='2' value=" + telephoneb + ">-" +
							"						<input type=text name='telephonec' size=4 maxlength='4' value=" + telephonec + ">" +
							"					</dd>" +
							"				<dt>携帯：" +
							"					<dd><input type=text name='mobilea' size=3 maxlength='3' value=" + mobilea + ">-" +
							"						<input type=text name='mobileb' size=4 maxlength='4' value=" + mobileb + ">-" +
							"						<input type=text name='mobilec' size=4 maxlength='4' value=" + mobilec + ">" +
							"				</dd><dt>住所：" +
							"						<dd><textarea name='address_j' rows='3' cols='16'>" + address + "</textarea>" +
							"				</dd><dt>住所（英訳）:" +
							"						<dd><textarea name='address' rows='3' cols='16'>" + address_j + "</textarea>" +
							"				</dd><dt>ウエブサイト：" +
							"					<dd>http://<input type=text name='homepage' size=16 value=" + homepage + ">" +
							"					<a href=" + homepagelink + ">" + homepagelink + "</a></dd>" +
							"				<dt>一人当たりの値段:" +
							"					<dd>￥<input type=text name='pricemin' size=6 value=" + pricemin + "> ‾ " +
							"					￥<input type=text name='pricemax' size=6 value=" + pricemax + "></dd>" +
							"				<dt>定員：" +
							"					<dd><input type=text name='guestsmax' size=6 value=" + guestsmax + "></dd>" +
							"				<dt>種類別：" +
							"					<dd><input type='hidden' name='type' value=''> " +
							"						<input id='campsite' type='checkbox' name='type' value='Campsite' " + campsite + ">キャンプ場<br>" +
							"						<input id='minshuku' type='checkbox' name='type' value='Minshuku'" +  minshuku + ">民宿<br>" +
							"						<input id='business' type='checkbox' name='type' value='Business Hotel'" +  business + ">ビズネスホテル<br>" +
							"						<input id='hostel' type='checkbox' name='type' value='Hostel'" +  hostel + ">ホステル<br>" +
							"						<input id='ryokan' type='checkbox' name='type' value='Japanese Inn'" +  ryokan + ">旅館<br>" +
							"						<input id='fullhotel' type='checkbox' name='type' value='Full Hotel'" +  fullhotel + ">ホテル<br>" +
							"					その他：" +
							"					<textarea name=typesother_j cols=15 rows=2 style='resize: none;' >" + typesother_j + "</textarea>" +
							"					英訳：" +
							"					<textarea name=typesother cols=15 rows=2 style='resize: none;' >" + typesother + "</textarea></dd>" +
							"					現在のリスト： " + typestring + 
							"					（英訳） " + typestring_j + 
							"					<dt>チェックイン・チェックアウト:" +
							"					<dd><input type=text name='checkin' size=6 value=" + checkin + ">/" +
							"						<input type=text name='checkout' size=6 value=" + checkout + "></dd>" +
							"			</dl>" +			
							"			地図のコード： <br><textarea name=mapcode cols=20 rows=8>" + mapcode + "</textarea>" +
							"			<input type='submit' value='更新' />" +
							"		</form>");
					if (mapcode!=null)
					pw.println(mapcode.getValue());
					else
						pw.println("地図について、詳細は管理員にご相談下さい。");
					pw.println(	" </div>" +
							"</div>");
					pw.println("</BODY>");
					pw.println("</HTML>");
					pw.close();
				}
			} catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				response.sendRedirect("/updatehotelhome?error=NoData.");
			}
		}
	}
	private static String Filternulls(Entity hotel, String message) {
		String y = (String) hotel.getProperty(message);
		if (y == null | y == "null")
		{y="";}
		return y;
	}

	private static String Filternullphotos(String photolink) {
		if (photolink.contains("null"))
		{photolink="http://yakutenki.appspot.com/images/map.jpg";}
		return photolink;
	}


	private static String htmlFilter(String message) {
		if (message == null) return null;
		int len = message.length();
		StringBuffer result = new StringBuffer(len + 20);
		char aChar;

		for (int i = 0; i < len; ++i) {
			aChar = message.charAt(i);
			switch (aChar) {
			case '<': result.append("&lt;"); break;
			case '>': result.append("&gt;"); break;
			case '&': result.append("&amp;"); break;
			case '"': result.append("&quot;"); break;
			default: result.append(aChar);
			}
		}
		return (result.toString());
	}
}