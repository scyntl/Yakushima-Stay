package updatehotel;

import javax.servlet.*;
import javax.servlet.jsp.PageContext;

import java.util.*;
import java.io.PrintStream;
import java.io.PrintWriter;

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

public class UpdateHotelAdminServlet extends HttpServlet {
	/**
	 * 
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
		{response.sendRedirect("/updatehoteladminhome?error=nokey&redirectpage="+rredirectpage+"firsterror="+error);}
		else{
			if (hotelKeyString==null|hotelKeyString.equals("null"))
			{response.sendRedirect("/updatehoteladminhome?error=nokey&redirectpage="+rredirectpage+"firsterror="+error);}
			else
			{hotelKey = KeyFactory.stringToKey(hotelKeyString);}
		}

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
			boolean isadmin=userService.isUserAdmin();
			if (!(userID.equals("104364285174483720328"))&&!isadmin)
			{response.sendRedirect("/updatehotel?hotelKeyString="+hotelKeyString+"&error=NotAdministrator.");}
			else{
				try {
					Entity hotel = datastore.get(hotelKey);
					BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

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
					}	

					/*Declare and get hotel properties.*/
					String hotelName = Filternulls(hotel, "hotelName");
					String hotelName_j = Filternulls(hotel, "hotelName_j");
					String tagline = Filternulls(hotel, "tagline");
					String tagline_j = Filternulls(hotel, "tagline_j");
					String incwasher = Filternulls(hotel, "incwasher");
					String incdryer =  Filternulls(hotel, "incdryer");
					String inclaundry =  Filternulls(hotel, "inclaundry");
					String incbodysoap =  Filternulls(hotel, "incbodysoap");
					String incshampoo =  Filternulls(hotel, "incshampoo");
					String incconditioner =  Filternulls(hotel, "incconditioner");
					String incrazor =  Filternulls(hotel, "incrazor");
					String inctoothbrush =  Filternulls(hotel, "inctoothbrush");
					String incpajamas =  Filternulls(hotel, "incpajamas");
					String inctowel =  Filternulls(hotel, "inctowel");
					String inchairdryer =  Filternulls(hotel, "inchairdryer");
					String inciron =  Filternulls(hotel, "inciron");
					String incdvd =  Filternulls(hotel, "incdvd");
					String incwifi =  Filternulls(hotel, "incwifi");
					String inccomputer =  Filternulls(hotel, "inccomputer");
					String inclocker =  Filternulls(hotel, "inclocker");
					String incbicycle =  Filternulls(hotel, "incbicycle");
					String inccar =  Filternulls(hotel, "inccar");
					String incluggage =  Filternulls(hotel, "incluggage");
					String incbus =  Filternulls(hotel, "incbus");
					String incparkinglot =  Filternulls(hotel, "incparkinglot");
					String inctents =  Filternulls(hotel, "inctents");
					String incsleepingbags =  Filternulls(hotel, "inctents");
					String inchikinggear =  Filternulls(hotel, "inchikinggear");
					String incbbqgrill =  Filternulls(hotel, "incbbqgrill");
					String includedotheramenities=  Filternulls(hotel, "includedotheramenities");
					String includedamenitiesstring=  Filternulls(hotel, "includedamenitiesstring");
					String includedotheramenities_j=  Filternulls(hotel, "includedotheramenities_j");
					String includedamenitiesstring_j=  Filternulls(hotel, "includedamenitiesstring_j");
					String paidwasher = Filternulls(hotel, "paidwasher");
					String paiddryer =  Filternulls(hotel, "paiddryer");
					String paidlaundry =  Filternulls(hotel, "paidlaundry");
					String paidtowel =  Filternulls(hotel, "paidtowel");
					String paidhairdryer =  Filternulls(hotel, "paidhairdryer");
					String paidiron =  Filternulls(hotel, "paidiron");
					String paiddvd =  Filternulls(hotel, "paiddvd");
					String paidwifi =  Filternulls(hotel, "paidwifi");
					String paidcomputer =  Filternulls(hotel, "paidcomputer");
					String paidlocker =  Filternulls(hotel, "paidlocker");
					String paidbicycle =  Filternulls(hotel, "paidbicycle");
					String paidcar =  Filternulls(hotel, "paidcar");
					String paidluggage =  Filternulls(hotel, "paidluggage");
					String paidbus =  Filternulls(hotel, "paidbus");
					String paidtents =  Filternulls(hotel, "paidtents");
					String paidsleepingbags =  Filternulls(hotel, "paidtents");
					String paidhikinggear =  Filternulls(hotel, "paidhikinggear");
					String paidbbqgrill =  Filternulls(hotel, "paidbbqgrill");
					String paidmassage =  Filternulls(hotel, "paidmassage");
					String paidotheramenities=  Filternulls(hotel, "paidotheramenities");
					String paidamenitiesstring=  Filternulls(hotel, "paidamenitiesstring");
					String paidotheramenities_j=  Filternulls(hotel, "paidotheramenities_j");
					String paidamenitiesstring_j=  Filternulls(hotel, "paidamenitiesstring_j");
					String roomtoilet = Filternulls(hotel, "roomtoilet");
					String roombath = Filternulls(hotel, "roombath");
					String roomkitchen = Filternulls(hotel, "roomkitchen");
					String roommicrowave = Filternulls(hotel, "roommicrowave");
					String roomfridge = Filternulls(hotel, "roomfridge");
					String roomac = Filternulls(hotel, "roomac");
					String roomheater = Filternulls(hotel, "roomheater");
					String roomtv = Filternulls(hotel, "roomtv");
					String roomcable = Filternulls(hotel, "roomcable");
					String roomphone = Filternulls(hotel, "roomphone");
					String roomwifi = Filternulls(hotel, "roomwifi");
					String roomotheramenities=  Filternulls(hotel, "roomotheramenities");
					String roomotheramenities_j=  Filternulls(hotel, "roomotheramenities_j");
					String roomamenitiesstring=  Filternulls(hotel, "roomamenitiesstring");
					String roomamenitiesstring_j=  Filternulls(hotel, "roomamenitiesstring_j");
					String sharedtoilet = Filternulls(hotel, "sharedtoilet");
					String sharedshower = Filternulls(hotel, "sharedshower");
					String sharedindoorbath = Filternulls(hotel, "sharedindoorbath");
					String sharedoutdoorbath = Filternulls(hotel, "sharedoutdoorbath");
					String sharedsauna = Filternulls(hotel, "sharedsauna");
					String sharedspa = Filternulls(hotel, "sharedspa");
					String sharedpool = Filternulls(hotel, "sharedpool");
					String sharedkitchen = Filternulls(hotel, "sharedkitchen");
					String sharedmicrowave = Filternulls(hotel, "sharedmicrowave");
					String sharedfridge = Filternulls(hotel, "sharedfridge");
					String sharedac = Filternulls(hotel, "sharedac");
					String sharedheater = (String) hotel.getProperty("sharedheater");
					String sharedtv = Filternulls(hotel, "sharedtv");
					String shareddvd = Filternulls(hotel, "shareddvd");
					String sharedcomputer = Filternulls(hotel, "sharedcomputer");
					String sharedsmokingarea = Filternulls(hotel, "sharedsmokingarea");
					String sharedgiftshop = Filternulls(hotel, "sharedgiftshop");
					String sharedotheramenities=  Filternulls(hotel, "sharedotheramenities");
					String sharedamenitiesstring=  Filternulls(hotel, "sharedamenitiesstring");
					String sharedotheramenities_j=  Filternulls(hotel, "sharedotheramenities_j");
					String sharedamenitiesstring_j=  Filternulls(hotel, "sharedamenitiesstring_j");
					String mealbreakfast=  Filternulls(hotel, "mealbreakfast");
					String mealdinner=  Filternulls(hotel, "mealdinner");
					String mealstayonly=  Filternulls(hotel, "mealstayonly");
					String mealvegetarian=  Filternulls(hotel, "mealvegetarian");
					String mealallergen=  Filternulls(hotel, "mealallergen");
					String mealsother =  Filternulls(hotel, "mealsother");
					String mealsstring =  Filternulls(hotel, "mealsstring");
					String mealsother_j =  Filternulls(hotel, "mealsother_j");
					String mealsstring_j =  Filternulls(hotel, "mealsstring_j");
					String welkid=  Filternulls(hotel, "welkid");
					String childrenspricing=  Filternulls(hotel, "childrenspricing");
					String welhandicapped=  (String) hotel.getProperty("welhandicapped");
					String welgroups=  (String) hotel.getProperty("welgroups");
					String welpet=  Filternulls(hotel, "welpet");
					String welforeigner=  Filternulls(hotel, "welforeigner");
					String welcouple=  Filternulls(hotel, "welcouple");
					String welnoanimals=  Filternulls(hotel, "welnoanimals");
					String smokingyes=  Filternulls(hotel, "smokingyes");
					String smokingno=  Filternulls(hotel, "smokingno");
					String smokingboth=  Filternulls(hotel, "smokingboth");
					String roomtypesjapanese=  Filternulls(hotel, "roomtypesjapanese");
					String roomtypeswestern=  Filternulls(hotel, "roomtypeswestern");
					String roomtypessingle=  Filternulls(hotel, "roomtypessingle");
					String roomtypestwin=  Filternulls(hotel, "roomtypestwin");
					String roomtypesdouble=  Filternulls(hotel, "roomtypesdouble");
					String roomtypesdormitories=  Filternulls(hotel, "roomtypesdormitories");
					String roomtypesstandalone=  Filternulls(hotel, "roomtypesstandalone");
					String roomtypestatamibanquetspace=  Filternulls(hotel, "roomtypestatamibanquetspace");
					String roomtypesmeetingbanquethall=  Filternulls(hotel, "roomtypesmeetingbanquethall");
					String roomtypeswesterntoilets=  Filternulls(hotel, "roomtypeswesterntoilets");
					String roomtypessquattoilets=  Filternulls(hotel, "roomtypessquattoilets");
					String otherroomtypes=  Filternulls(hotel, "otherroomtypes");					
					String otherroomtypes_j=  Filternulls(hotel, "otherroomtypes_j");					
					String acceptsvisa=  Filternulls(hotel, "acceptsvisa");
					String acceptsmastercard=  Filternulls(hotel, "acceptsmastercard");
					String acceptsdiscover=  Filternulls(hotel, "acceptsdiscover");
					String acceptsamericanexpress=  Filternulls(hotel, "acceptsamericanexpress");
					String acceptstravelerschecks=  Filternulls(hotel, "acceptstravelerschecks");
					String acceptsjcb=  Filternulls(hotel, "acceptsjcb");
					String acceptsdc=  Filternulls(hotel, "acceptsdc");
					String acceptsuc=  Filternulls(hotel, "acceptsjcb");
					String acceptssaison=  Filternulls(hotel, "acceptssaison");
					String acceptstransfer=  Filternulls(hotel, "acceptstransfer");
					String acceptscash=  Filternulls(hotel, "acceptscash");
					String acceptsothers=  Filternulls(hotel, "acceptsothers");
					String acceptsothers_j=  Filternulls(hotel, "acceptsothers_j");
					String acceptsstring=  Filternulls(hotel, "acceptsstring");
					String acceptsstring_j=  Filternulls(hotel, "acceptsstring_j");
					String speaksenglish=  Filternulls(hotel, "speaksenglish");
					String speaksjapanese=  Filternulls(hotel, "speaksjapanese");
					String speaksgerman=  Filternulls(hotel, "speaksgerman");
					String speaksfrench=  Filternulls(hotel, "speaksfrench");
					String speaksrussian=  Filternulls(hotel, "speaksrussian");
					String speakskorean=  Filternulls(hotel, "speakskorean");
					String speakschinese=  Filternulls(hotel, "speakschinese");
					String speaksothers=  Filternulls(hotel, "speaksothers");
					String speaksothers_j=  Filternulls(hotel, "speaksothers_j");
					String speaksstring= Filternulls(hotel, "speaksstring");
					String speaksstring_j= Filternulls(hotel, "speaksstring_j");
					String closeport=  Filternulls(hotel, "closeport");
					String closeportAnbo=  Filternulls(hotel, "closeportAnbo");
					String closeairport=  Filternulls(hotel, "closeairport");
					String closebus=  Filternulls(hotel, "closebus");
					String closesuper=  Filternulls(hotel, "closesuper");
					String closeconvenience=  Filternulls(hotel, "closeconvenience");
					String closerestaurant=  Filternulls(hotel, "closerestaurant");
					String closepost=  Filternulls(hotel, "closepost");
					String otherclose=  Filternulls(hotel, "otherclose");
					String closestring=  Filternulls(hotel, "closestring");
					String porttime5=  Filternulls(hotel, "porttime5");
					String porttime10=  Filternulls(hotel, "porttime10");
					String porttime15=  Filternulls(hotel, "porttime15");
					String porttimeAnbo5=  Filternulls(hotel, "porttimeAnbo5");
					String porttimeAnbo10=  Filternulls(hotel, "porttimeAnbo10");
					String porttimeAnbo15=  Filternulls(hotel, "porttimeAnbo15");
					String airporttime5=  Filternulls(hotel, "airporttime5");
					String airporttime10=  Filternulls(hotel, "airporttime10");
					String airporttime15=  Filternulls(hotel, "airporttime15");
					String busstoptime0=  Filternulls(hotel, "busstoptime0");
					String busstoptime5=  Filternulls(hotel, "busstoptime5");
					String busstoptime10=  Filternulls(hotel, "busstoptime10");
					String busstoptime15=  Filternulls(hotel, "busstoptime15");
					String busstopname=  Filternulls(hotel, "busstopname");
					String busstopname_j=  Filternulls(hotel, "busstopname_j");
					String supermarkettime0=  Filternulls(hotel, "supermarkettime0");
					String supermarkettime5=  Filternulls(hotel, "supermarkettime5");
					String supermarkettime10=  Filternulls(hotel, "supermarkettime10");
					String supermarkettime15=  Filternulls(hotel, "supermarkettime15");
					String restauranttime0=  Filternulls(hotel, "restauranttime0");
					String restauranttime5=  Filternulls(hotel, "restauranttime5");
					String restauranttime10=  Filternulls(hotel, "restauranttime10");
					String restauranttime15=  Filternulls(hotel, "restauranttime15");
					String conveniencestoretime0=  Filternulls(hotel, "conveniencestoretime0");
					String conveniencestoretime5=  Filternulls(hotel, "conveniencestoretime5");
					String conveniencestoretime10=  Filternulls(hotel, "conveniencestoretime10");
					String conveniencestoretime15=  Filternulls(hotel, "conveniencestoretime15");
					String posttime0=  Filternulls(hotel, "posttime0");
					String posttime5=  Filternulls(hotel, "posttime5");
					String posttime10=  Filternulls(hotel, "posttime10");
					String posttime15=  Filternulls(hotel, "posttime15");
					String travelporter = Filternulls(hotel, "travelporter");
					String yesyakushima = Filternulls(hotel, "yesyakushima");
					String touristassociation = Filternulls(hotel, "touristassociation");
					String location = Filternulls(hotel, "location");
					String Miyanoura = Filternulls(hotel, "Miyanoura");
					String Kusukawa = Filternulls(hotel, "Kusukawa");
					String Koseda = Filternulls(hotel, "Koseda");
					String Anbo = Filternulls(hotel, "Anbo");
					String Funayuki = Filternulls(hotel, "Funayuki");
					String Haru = Filternulls(hotel, "Haru");
					String Mugio = Filternulls(hotel, "Mugio");
					String Koshima = Filternulls(hotel, "Koshima");
					String Onoaida = Filternulls(hotel, "Onoaida");
					String Hirauchi = Filternulls(hotel, "Hirauchi");
					String Yudomari = Filternulls(hotel, "Yudomari");
					String Kurio = Filternulls(hotel, "Kurio");
					String Nakama = Filternulls(hotel, "Nakama");
					String Nagata = Filternulls(hotel, "Nagata");
					String Issou = Filternulls(hotel, "Issou");
					String Yoshida = Filternulls(hotel, "Yoshida");
					String Shitoko= Filternulls(hotel, "Shitoko");
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
					String campsite = Filternulls(hotel, "campsite");
					String cottages = Filternulls(hotel, "cottages");
					String minshuku = Filternulls(hotel, "minshuku");
					String business= Filternulls(hotel, "business");
					String hostel = Filternulls(hotel, "hostel");
					String ryokan = Filternulls(hotel, "ryokan");
					String fullhotel = Filternulls(hotel, "fullhotel");
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
					String image1Url = Filternulls(hotel, "image1Url");
					String image2Url = Filternulls(hotel, "image2Url");
					String image3Url = Filternulls(hotel, "image3Url");
					String image4Url = Filternulls(hotel, "image4Url");
					String image5Url = Filternulls(hotel, "image5Url");
					String imagelogoUrl = Filternulls(hotel, "imagelogoUrl");
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

					List<String> managerList = new ArrayList<String>();
					managerList = (List<String>) hotel.getProperty("managerList");
//					response.sendRedirect("/updatehoteladminhome?error="+hostel);

					pw.println("<p>Logged in as " + user.getNickname() + ", "+ user.getUserId()+ ". Viewing info for " + hotelName + ", key=" + hotelKeyString + ".</p>");

					//Begin HTML
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
							"	alert('Checking this box will make your page visible to others.');" +
							"	return true;" +
							"}" +
							"" +
							"function invisibilityAlert(){" +
							"	alert('Unchecking this box will make your page invisible to others.');" +
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

					pw.println("<BODY>");
					pw.println("<div id='bigcontainer'>" +
							"		<div id='bigbanner' title='Comprehensive Listing of Hotels in Yakushima'>" +
							"			<table id=languageselection>" +
							"				<tr><td class=languagebutton><a href='/updatehotelhome/'>English</a></td>" +
							"				<td class=languagebutton><a href='/updatehotelhomej/'>日本語</a></td>" +
							"			</table>" +
							"		</div>" +
							"	<div id=searchmenu>"  +
							"		<form action=/updatehoteladmin method='get'>" +
							"			<dt>Choose an accomodation:" +
							"			<dt><select name ='hotelKeyString' required style='width:190px;' >" +
							"					<option value='Choose' disabled selected>Choose . . .</option>");
					for (int j=0; j<hotelKeys.size(); j++){
						pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames.get(j) + "</option>");
					}
					pw.println(	"			</select>" +
							"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
							"		</form>	" + 
							"		<form action=/updatehoteladmin method='get'>" +
							"			<dt>施設を選んで下さい。" +
							"			<dt><select name ='hotelKeyString' required style='width:190px;' >" +
							"				<option value='Choose' disabled selected>Choose . . .</option>");
					for (int j=0; j<hotelKeys.size(); j++){
						pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");
					}
					pw.println(	"			</select>" +
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
							"					<input type=hidden name=permissions value='administrator'> " +
							"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"					<input id=published type='checkbox' name='publish' value=published "+published+" >Visible on Web" +
							"						<input type='submit' value='OK'>" +
							"				</form>" +
							" 				<form accept-charset='utf-16' id=dirform action='/updatetagline' method='post' style='height:180px;' >" +
							"					Yakushima >> " + location + " : <br><center><h3 style='font-size: 17px;'> " + hotelName + " (" + hotelName_j + ")</h3></center>" +
							"					" +
							"					<div id=tagline>" +
							"						Tagline:<br>" +
							"						<input type=hidden name=permissions value='administrator'> " +
							"						<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
							"						<textarea name='tagline' rows='1' cols='60' maxlength=60 style='resize: none;' >" + tagline + " </textarea>" +
							"						<br>Tagline (Japanese):<br>" +
							"						<textarea name='tagline_j' rows='1' cols='60' maxlength=60 style='resize: none;' >" + tagline_j + " </textarea>" +
							"						<br><input type='submit' value='Update Tag-Line'>" +
							"					</div>" +
							"				</form>" +
							" 					Current List of Users:<br>" +managerList+
							"					<h2>Administrative Actions</h2>" +
							"					<h3>Edit User Data</h3>" +

					"					<form accept-charset='utf-16' id=adduserform action='/adduser' method='post' style='height:80px;' " +
					"							onsubmit='return validateadduserform()'>" +
					"						<dt>Add User to " + hotelName + " management:<br>" +
					"						<input type=hidden name=permissions value='administrator'> " +
					"						<input type=hidden name=hotelKeyString value=" + hotelKeyString + " >" +
					"						<dd>User ID Number: <input type=text name=newuserID size=50 value='New User ID Number' /></dd>" +
					"						<dd>4-Digit Pin: <input type=text name=newuserPin size=4 maxlength=4 value='PIN' />" +
					"						<input type='submit' value='Add User'></dd>" +	
					"					</form>" +

					"					<form accept-charset='utf-16' id=deleteuserform action='/deleteuser' method='post' style='height:80px;' >" +					
					"					<dt>Delete User from " + hotelName + " management:<br>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " >" +
					"					<dd>User ID Number: <input name=oldmanagerID type=text size=50 value='Retiring User ID Number' /></dd>" +
					"					<dd>4-Digit Pin: <input name=oldmanagerPin type=text size=4 maxlength=4 value='PIN' />" +
					"					<input type='submit' value='Delete User'></dd>" +	
					"					</form>" +
					"					<form accept-charset='utf-16' id=changepinform action='/changepin' method='post' style='height:80px;' >" +
					"					<dt>Change User's 4-Digit Pin:<br>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " >" +
					"					<dd>User ID Number: <input name=managerID type=text size=50 value='User ID Number' /></dd>" +
					"					<dd>4-Digit Pin: <input name=newPin type=text size=4 maxlength=4 value='PIN' />" +
					"					<input type='submit' value='Change PIN'></dd>" +
					"					</form>" +	
					"					<h3>Edit Hotel Data</h3>" +
					"					<form accept-charset='utf-16' id=mapcodeform action='/updatemapcode' method='post' style='height:140px;' >" +
					"					<dt>Update MapCode:<br>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " >" +
					"			Map Code: <br><textarea name=mapcode cols=50 rows=4> " + mapcode + " </textarea>" +
					"					<input type=hidden name=oldmapcode value='"+mapcode+"' >" +
					"					<input type='submit' value='Update Map Code'>" +
					"					</form>" +						
					"					<form accept-charset='utf-16' id=changenameform action='/updatehotelname' method='post' style='height:100px;' >" +
					"					<dt>Change Hotel Name:<br>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " >" +
					"					<input type=hidden name=oldhotelName value=" + hotelName + " >" +
					"					<input type=hidden name=oldhotelName_j value=" + hotelName_j + " >" +
					"					<dd>New English Name: <input name=newhotelName type=text size=40 value="+ hotelName + " /></dd>" +
					"					<dd>New Japanese Name: <input name=newhotelName_j type=text size=40 value="+ hotelName_j + " />" +
					"					<input type='submit' value='Update Hotel Name'></dd>" +
					"					</form>" +						
					"			</div>" +
					"			<div id=photobox>" +
					"				<h2>Images</h2>" +
					"  				You can upload 4 photos (<500kb each) and a logo (200px x 80px). Which" +
					"				photo do you wish to upload?" +
					"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto1") + 
					" 					 method='post' enctype='multipart/form-data' > " +
					"					Photo 1 <br>Current Photo:<br>" +
					"					<img src=" + image1Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
					"					<input type='file' name='myFile'> " +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
					"					<input type='submit' value='Submit'>" +
					"					</form>	" +
					"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto2") + 
					" 					 method='post' enctype='multipart/form-data' > " +
					"					Photo 2 <br>Current Photo:<br>" +
					"					<img src=" + image2Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
					"					<input type='file' name='myFile'> " +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" +hotelKeyString + " > " +
					"					<input type='submit' value='Submit'>" +
					"					</form>	" +
					"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto3") + 
					" 					 method='post' enctype='multipart/form-data' > " +
					"					Photo 3 <br>Current Photo:<br>" +
					"					<img src=" + image3Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
					"					<input type='file' name='myFile'> " +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
					"					<input type='submit' value='Submit'>" +
					"					</form>	" +
					"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto4") + 
					" 					 method='post' enctype='multipart/form-data' > " +
					"					Photo 4 <br>Current Photo:<br>" +
					"					<img src=" + image4Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
					"					<input type='file' name='myFile'> " +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
					"					<input type='submit' value='Submit'>" +
					"					</form>	" +
					"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto5") + 
					" 					 method='post' enctype='multipart/form-data' > " +
					"					Photo 5 <br>Current Photo:<br>" +
					"					<img src=" + image5Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
					"					<input type='file' name='myFile'> " +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
					"					<input type='submit' value='Submit'>" +
					"					</form>	" +
					"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephotologo") + 
					" 					 method='post' enctype='multipart/form-data' > " +
					"					Logo <br>Current Photo:<br>" +
					"					<img src=" + imagelogoUrl196 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
					"					<input type='file' name='myFile'> " +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + " > " +
					"					<input type='submit' value='Submit'>" +
					"					</form>	" +				
					"" +
					"" +
					"			<br><br>" +
					"			<div id=amenities>" +
					"				<h2	>Amenities</h2>" +
					"				<form class=amenitiesform id=includedamenitiesform" +
					"				action='/updateincludedamenities' accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString	 + ">" +
					"					<span class=bold>Included Amenities</span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='includedamenities' value=''>" +
					"							<input id=incwasher type='checkbox' name='includedamenities'" +
					"								value='Washer'" + incwasher + ">Washer<br> " +
					"							<input id=incdryer type='checkbox' name='includedamenities'" +
					"							 	value='Dryer'" + incdryer + ">Dryer<br>" +
					"							<input id=inclaundry type='checkbox' name='includedamenities'" +
					"								value='Laundry Service'" + inclaundry + ">Laundry Service<br>" +
					"							<input id=incpajamas type='checkbox' name='includedamenities'" +
					"							 	value='Pajamas/Yukata'" + incpajamas + ">Pajamas/Yukata<br>" +
					"							<input id=incbodysoap type='checkbox' name='includedamenities'" +
					"								value='Hair Dryer'" + inchairdryer + ">Hair Dryer<br>" +
					"							<input id=inciron type='checkbox' name='includedamenities'" +
					"								value='Iron'" + inciron + ">Iron<br>" +
					"							<input id=incdvd type='checkbox' name='includedamenities'" +
					"							 	value='Body Soap'" + incbodysoap + ">Body Soap<br>" +
					"							<input id=incshampoo type='checkbox' name='includedamenities'" +
					"							 	value='Shampoo'" + incshampoo + ">Shampoo<br>" +
					"							<input id=incconditioner type='checkbox' name='includedamenities'" +
					"							 	value='Conditioner'" + incconditioner + ">Conditioner<br>" +
					"							<input id=incrazor type='checkbox' name='includedamenities'" +
					"							 	value='Razor'" + incrazor + ">Razor<br>" +
					"							<input id=inctoothbrush type='checkbox' name='includedamenities'" +
					"							 	value='Toothbrush'" + inctoothbrush + ">Toothbrush<br>" +
					"							<input id=inctowel type='checkbox' name='includedamenities'" +
					"								value='Towel'" + inctowel + ">Towel<br> " +
					"						<td><input id=inchairdryer type='checkbox' name='includedamenities'" +
					"							value='DVD-Player' " + incdvd + ">DVD-Player<br> " +
					"							<input id=incwifi type='checkbox' name='includedamenities' " +
					"							value='Wifi' " + incwifi + ">Wifi<br>" +
					"							<input id=inccomputer type='checkbox' name='includedamenities'" +
					"							value='Computer' " + inccomputer + ">Computer<br>" +
					"							<input id=inclocker type='checkbox' name='includedamenities'" +
					"							value='Locker' " + inclocker + ">Locker<br>" +
					"						<td><input id=incbicycle type='checkbox' name='includedamenities' " +
					"							value='Bicycles' " + incbicycle + ">Bicycles<br>" +
					"							<input id=inccar type='checkbox' name='includedamenities'" +
					"							value='Rental Car' " + inccar + ">Rental Car<br> " +
					"							<input id=incluggage type='checkbox' name='includedamenities'" +
					"							value='Luggage Holding' " + incluggage + ">Luggage Holding<br> " +
					"							<input id=incbus type='checkbox' name='includedamenities'" +
					"							value='Welcome Bus' " + incbus + ">One-time Pickup/Dropoff<br>" +
					"							<input id=incparkinglot type='checkbox' name='includedamenities'" +
					"							value='Parking Lot' " + incparkinglot + ">Parking Lot<br>" +
					"							<input id=inctents type='checkbox' name='incamenities'" +
					"								value='Tents' " + inctents + ">Tents<br>" +
					"							<input id=incsleepingbags type='checkbox' name='incamenities'" +
					"								value='Sleeping Bags' " + incsleepingbags + ">Sleeping Bags<br>" +
					"							<input id=inchikinggear type='checkbox' name='incamenities'" +
					"								value='Hiking Gear' " + inchikinggear + ">Hiking Gear<br>" +
					"							<input id=incbbqgrill type='checkbox' name='incamenities'" +
					"								value='BBQ Grill' " + incbbqgrill + ">BBQ Grill<br>" +
					"					</table>" +
					"					Others:<br>" +
					"					<textarea name=includedotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + includedotheramenities + "</textarea>" +
					"					<br>Others in Japanese:" +
					"					<textarea name=includedotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + includedotheramenities_j + "</textarea>" +
					"					<p><dt> Current List: <dd>" + includedamenitiesstring + "</dd>" +
					"					<dt> (Japanese): <dd>" + includedamenitiesstring_j + "</dd>" +
					"					<br><input type='submit' value='Update Included Amenities' /> <br>" +
					"				</form>" +
					"				<form class=amenitiesform id=paidamenitiesform" +
					"				action='/updatepaidamenities' accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
					"					<span class=bold>Paid Amenities</span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='paidamenities' value=''>" +
					"							<input id=paidwasher type='checkbox' name='paidamenities'" +
					"								value='Washer'" + paidwasher + ">Washer<br> " +
					"							<input id=paiddryer type='checkbox' name='paidamenities'" +
					"							 	value='Dryer'" + paiddryer + ">Dryer<br>" +
					"							<input id=paidlaundry type='checkbox' name='paidamenities'" +
					"								value='Laundry Service'" + paidlaundry + ">Laundry Service<br>" +
					"							<input id=paidtowel type='checkbox' name='paidamenities'" +
					"								value='Towel'" + paidtowel + ">Towel<br> " +
					"							<input id=paidhairdryer type='checkbox' name='paidamenities'" +
					"								value='Hair Dryer'" + paidhairdryer + ">Hair Dryer<br>" +
					"							<input id=paidiron type='checkbox' name='paidamenities'" +
					"								value='Iron'" + paidiron + ">Iron<br>" +
					"						<td><input id=paiddvd type='checkbox' name='paidamenities'" +
						"							value='DVD-Player' " + paiddvd + ">DVD-Player<br> " +
					"							<input id=paidwifi type='checkbox' name='paidamenities' " +
					"								value='Wifi' " + paidwifi + ">Wifi<br>" +
					"							<input id=paidcomputer type='checkbox' name='paidamenities'" +
					"								value='Computer' " + paidcomputer + ">Computer<br>" +
					"							<input id=paidlocker type='checkbox' name='paidamenities'" +
					"								value='Locker' " + paidlocker + ">Locker<br>" +
					"							<input id=paidbicycle type='checkbox' name='paidamenities' " +
					"								value='Bicycles' " + paidbicycle + ">Bicycles<br>" +
					"							<input id=paidcar type='checkbox' name='paidamenities'" +
					"								value='Rental Car' " + paidcar + ">Rental Car<br> " +
					"						<td><input id=paidluggage type='checkbox' name='paidamenities'" +
					"								value='Luggage Holding' " + paidluggage + ">Luggage Holding<br> " +
					"							<input id=paidbus type='checkbox' name='paidamenities'" +
					"								value='Welcome Bus' " + paidbus + ">Welcome Bus<br>" +
					"							<input id=paidtents type='checkbox' name='paidamenities'" +
					"								value='Tents' " + paidtents + ">Tents<br>" +
					"							<input id=paidsleepingbags type='checkbox' name='paidamenities'" +
					"								value='Sleeping Bags' " + paidsleepingbags + ">Sleeping Bags<br>" +
					"							<input id=paidhikinggear type='checkbox' name='paidamenities'" +
					"								value='Hiking Gear' " + paidhikinggear + ">Hiking Gear<br>" +
					"							<input id=paidbbqgrill type='checkbox' name='paidamenities'" +
					"								value='BBQ Grill' " + paidbbqgrill + ">BBQ Grill<br>" +
					"							<input id=paidmassage type='checkbox' name='paidamenities'" +
					"								value='Massage Service' " + paidmassage + ">Massage Service<br>" +
					"					</table>" +
					"					Others:<br>" +
					"					<textarea name=paidotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + paidotheramenities + "</textarea>" +
					"					<br>Others in Japanese:<br>" +
					"					<textarea name=paidotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + paidotheramenities_j + "</textarea>" +
					"					<p><dt> Current List: <dd>" + paidamenitiesstring + "</dd>" +
					"					<dt> (Japanese): <dd>" + paidamenitiesstring_j + "</dd>" +
					"					<br><input type='submit' value='Update Paid Amenities' /> <br>" +
					"				</form>" +
					"				<form class=amenitiesform id=inroomamenitiesform" +
					"				action='/updateinroomamenities' accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
					"					<span class=bold>In-Room Facilities:</span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='roomamenities' value=''>" +
					"							<input id=roomtoilet type='checkbox' name='roomamenities' value='Toilet'" + roomtoilet + ">Toilet<br> " +
					"							<input id=roombath type='checkbox' name='roomamenities' value='Bath/Shower'" + roombath + ">Bath/Shower<br>" +
					"							<input id=roomkitchen type='checkbox' name='roomamenities' value='Kitchen/Kitchenette'" + roomkitchen + ">Kitchen/Kitchenette<br>" +
					"						<td><input id=roommicrowave type='checkbox' name='roomamenities' value='Microwave'" + roommicrowave + ">Microwave<br>" +
					"							<input id=roomfridge type='checkbox' name='roomamenities' value='Fridge'" + roomfridge + ">Fridge<br> " +
					"							<input id=roomac type='checkbox' name='roomamenities' value='A/C'" + roomac + ">A/C<br>" +
					"							<input id=roomheater type='checkbox' name='roomamenities' value='Heater'" + roomheater + ">Heater<br>" +
					"						<td><input id=roomtv type='checkbox' name='roomamenities' value='Local TV'" + roomtv + ">Local TV<br> " +
					"							<input id=roomcable type='checkbox' name='roomamenities' value='Cable/Satelite TV'" + roomcable + ">Cable/Satelite TV<br> " +
					"							<input id=roomphone type='checkbox' name='roomamenities' value='Telephone'" + roomphone + ">Telephone<br>" +
					"							<input id=roomwifi type='checkbox' name='roomamenities' value='Wifi'" + roomwifi + ">Wifi<br>" +
					"					</table>" +
					"					Others:<br>" +
					"					<textarea name=roomotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + roomotheramenities + "</textarea>" +
					"					<br>Others in Japanese:<br>" +
					"					<textarea name=roomotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + roomotheramenities_j + "</textarea>" +
					"					<p><dt> Current List: <dd>" + roomamenitiesstring + "</dd>" +
					"					<dt> (Japanese): <dd>" + roomamenitiesstring_j + "</dd>" +
					"					<br><input type='submit' value='Update Inroom Amenities' />" +
					"				</form>" +
					"				<form class=amenitiesform id=sharedamenitiesform" +
					"				action='/updatesharedamenities' accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString+ ">" +
					"					<span class=bold>Shared Facilities:</span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='sharedamenities' value=''>" + 
					"							<input id=sharedtoilet type='checkbox' name='sharedamenities' value='Toilet'" + sharedtoilet + ">Toilet<br> " +
					"							<input id=sharedshower type='checkbox' name='sharedamenities' value='Shower'" + sharedshower + ">Shower<br>" +
					"							<input id=sharedindoorbath type='checkbox' name='sharedamenities' value='Indoor Bath'" + sharedindoorbath + ">Indoor Bath<br> " +
					"							<input id=sharedoutdoorbath type='checkbox' name='sharedamenities' value='Outdoor Bath'" + sharedoutdoorbath + ">Outdoor Bath<br> " +
					"							<input id=sharedsauna type='checkbox' name='sharedamenities' value='Sauna'" + sharedsauna + ">Sauna<br> " +
					"							<input id=sharedspa type='checkbox' name='sharedamenities' value='Spa'" + sharedspa + ">Spa<br> " +
					"							<input id=sharedpool type='checkbox' name='sharedamenities' value='Pool'" + sharedpool + ">Pool<br>" +
					"							<input id=sharedkitchen type='checkbox' name='sharedamenities' value='Kitchen/Kitchenette'" + sharedkitchen + ">Kitchen/Kitchenette<br>" +
					"						<td><input id=sharedmicrowave type='checkbox' name='sharedamenities' value='Microwave'" + sharedmicrowave + ">Microwave<br>" +
					"							<input id=sharedfridge type='checkbox' name='sharedamenities' value='Fridge'" + sharedfridge + ">Fridge<br> " +
					"							<input id=sharedac type='checkbox' name='sharedamenities' value='A/C'" + sharedac + ">A/C<br>" +
					"							<input id=sharedheater type='checkbox' name='sharedamenities' value='Heater'" + sharedheater + ">Heater<br> " +
					"							<input id=sharedtv type='checkbox' name='sharedamenities' value='Television'" + sharedtv + ">Television<br>" +
					"						<td><input id=shareddvd type='checkbox' name='sharedamenities' value='DVD Player'" + shareddvd + ">DVD Player<br>" +
					"							<input id=sharedcomputer type='checkbox' name='sharedamenities' value='Computer'" + sharedcomputer + ">Computer<br>" +
					"							<input id=sharedsmokingarea type='checkbox' name='sharedamenities' value='Smoking Area'" + sharedsmokingarea + ">Smoking Area<br>" +
					"							<input id=sharedgiftshop type='checkbox' name='sharedamenities' value='Gift Shop'" + sharedgiftshop + ">Gift Shop<br>" +
					"					</table>" +
					"					Others:<br>" +
					"					<textarea name=sharedotheramenities  rows='1' cols='60' maxlength=60 style='resize: none;' >" + sharedotheramenities + "</textarea>" +
					"					<br>Others in Japanese:<br>" +
					"					<textarea name=sharedotheramenities_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + sharedotheramenities_j + "</textarea>" +
					"					<p><dt> Current List: <dd>" + sharedamenitiesstring + "</dd>" +
					"					<dt> (Japanese): <dd>" + sharedamenitiesstring_j + "</dd>" +
					"					<br> <input type='submit' value='Update Shared Facilities' />" +
					"				</form>" +
					"				<form class=amenitiesform id=mealsform action='/updatemeals'" +
					"				accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
					"					<span class=bold>Dining Options:</span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='meals' value=''>" +
					"							<input id=mealbreakfast type='checkbox' name='meals' value='Breakfast'" + mealbreakfast + ">Breakfast" +
					"						<td><input id=mealdinner type='checkbox' name='meals' value='Dinner'" + mealdinner + ">Dinner" +
					"						<td><input id=mealstay type='checkbox' name='meals' value='Stay-Only'" + mealstayonly + ">Stay-Only" +
					"						<tr>" +
					"						<td><input id=mealvegetarian type='checkbox' name='meals' value='Vegetarian Options'" + mealvegetarian + ">Vegetarian Options" +
					"						<td><input id=mealallergen type='checkbox' name='meals' value='Non-allergen Options'" + mealallergen + ">Non-allergen Options" +
					"						<td>" +
					"						<td>" +
					"					</table>" +
					"					Others:<br>" +
					"					<textarea name=mealsother  rows='1' cols='60' maxlength=60 style='resize: none;' >" + mealsother + "</textarea>" +
					"					<br>Others in Japanese:<br>" +
					"					<textarea name=mealsother_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + mealsother_j + "</textarea>" +
					"					<p><dt> Current List: <dd>" + mealsstring + "</dd>" +
					"					<dt> (Japanese): <dd>" + mealsstring_j + "</dd>" +
					"					<br> <input type='submit' value='Update Meals' />" +
					"				</form>" +
					"				<form class=amenitiesform id=welcomingform action='/updatewelcoming'" +
					"				accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
					"					<span class=bold>This facility is proud to welcome:</span><br>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='welcoming' value=''> " +
					"							<input id=welpet type='checkbox' name='welcoming' value='pets'" + welpet + ">pets<br>" +
					"							<input id=welkid type='checkbox' name='welcoming' value='children'" + welkid + ">children<br>" +
					"							<input type='hidden' name='childrenspricing' value=''> " +
					"							<input id=welchildrenspricing type='checkbox' name='childrenspricing' value='* Special prices for children.'" + childrenspricing + ">* Special prices for children.<br>" +
					"							<input id=welhandicapped type='checkbox' name='welcoming' value='guests with physical disabilities'" + welhandicapped + ">guests with physical disabilities" +
					"						<td><input id=welforeigner type='checkbox' name='welcoming' value='foreigners'" + welforeigner + ">foreigners<br> " +
					"							<input id=welcouple type='checkbox' name='welcoming' value='couples'" + welcouple + ">couples<br>" +
					"							<input id=welgroups type='checkbox' name='welcoming' value='largegroups'" + welgroups + ">large groups" +
					"						<td><input type='hidden' name='welnoanimals' value=''> " +
					"							<input id=awelnoanimals type='checkbox' name='welnoanimals' value=' No animals are kept on the premises.'" + welnoanimals + "> No animals are kept on the premises." +
					"						<tr><td><br><input type='hidden' name='smoking' value=''>" +
					"							<input id=nonsmokingrooms type='radio' name='smoking' value='NonSmoking Rooms Only'" + smokingno + ">NonSmoking Rooms Only<br> " +
					"							<input id=smokingrooms type='radio' name='smoking' value='Smoking Rooms Only'" + smokingyes + ">Smoking Rooms Only<br>" +
					"							<input id=bothrooms type='radio' name='smoking' value='Smoking and Nonsmoking Rooms'" + smokingboth + ">Smoking and Nonsmoking Rooms" +
					"						<tr>" +
					"						<td><input type='hidden' name='roomtypes' value=''> " +
					"							<input id=roomtypesjapanese type='checkbox' name='roomtypes' value='Japanese Rooms'" + roomtypesjapanese + ">Japanese Rooms<br>" +
					"							<input id=roomtypeswestern type='checkbox' name='roomtypes' value='Western Rooms'" + roomtypeswestern + ">Western Rooms<br>" +
					"							<input id=roomtypessingle type='checkbox' name='roomtypes' value='Single Rooms'" + roomtypessingle + ">Single Rooms<br>" +
					"							<input id=roomtypestwin type='checkbox' name='roomtypes' value='Twin Rooms'" + roomtypestwin + ">Twin Rooms<br> " +
					"							<input id=roomtypesdouble type='checkbox' name='roomtypes' value='Double Rooms'" + roomtypesdouble + ">Double Rooms<br>" +
					"						<td><input id=roomtypesdormitories type='checkbox' name='roomtypes' value='Dormitories'" + roomtypesdormitories + ">Dormitories<br>" +
					"							<input id=roomtypesstandalone type='checkbox' name='roomtypes' value='Stand-Alone Rooms'" + roomtypesstandalone + ">Stand-Alone Rooms<br>" +
					"							<input id=roomtypestatamibanquetspace type='checkbox' name='roomtypes' value='Tatami Banquet Space'" + roomtypestatamibanquetspace + ">Tatami Banquet Space<br>" +
					"							<input id=roomtypesmeetingbanquethall type='checkbox' name='roomtypes' value='Meeting-Banquet Hall'" + roomtypesmeetingbanquethall + ">Meeting-Banquet Hall<br>" +
					"						<td><input id=roomtypeswesterntoilets type='checkbox' name='roomtypes' value='Western Toilets'" + roomtypeswesterntoilets + ">Western Toilets<br>" +
					"							<input id=roomtypessquattoilets type='checkbox' name='roomtypes' value='Squat Toilets'" + roomtypessquattoilets + ">Squat Toilets<br>" +
					"					</table>" +
					"					Others:<br>" +
					"					<textarea name=otherroomtypes  rows='1' cols='60' maxlength=60 style='resize: none;' >" + otherroomtypes + "</textarea>" +
					"					<br>Others in Japanese:<br>" +
					"					<textarea name=otherroomtypes_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + otherroomtypes_j + "</textarea>" +
					"					<br> <input type='submit' value='Update Welcoming' />" +
					"				</form>" +
					"				<form class=amenitiesform id=closetoform action='/updateclose'" +
					"				accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
					"					<span class=bold>Close to (walking time):<br></span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='close' value=''>" +
					"							<input id=closeport type='checkbox' name='close' value='Port'" + closeport + ">Miyanoura Port" +
					"							<br>( 	<select name='porttime'>" +
					"									<option value='under 5 min' "+ porttime5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ porttime10 +">5~10min</option>" +
					"									<option value='10~20 min' "+ porttime15 +">10~20min</option>" +
					"								</select> )<br> " +
					"							<input id=closeportAnbo type='checkbox' name='close' value='PortAnbo'" + closeportAnbo + ">Anbo Port" +
					"							<br>( 	<select name='porttimeAnbo'>" +
					"									<option value='under 5 min' "+ porttimeAnbo5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ porttimeAnbo10 +">5~10min</option>" +
					"									<option value='10~20 min' "+ porttimeAnbo15 +">10~20min</option>" +
					"								</select> )<br> " +
					"							<input id=closeairport type='checkbox' name='close' value='Airport'" + closeairport + ">Airport " +
					"							<br>( 	<select name='airporttime'>" +
					"									<option value='under 5 min' "+ airporttime5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ airporttime10 +">5~10min</option>" +
					"									<option value='10~15 min' "+ airporttime15 +">10~15min</option>" +
					"								</select> )<br> " +
					"							<input id=closebus type='checkbox' name='close' value='Bus Stop'" + closebus + ">Bus-Stop<br>" +
					"							( 	<select name='busstoptime'>" +
					"									<option value='right there!' "+ busstoptime0 +">right there!</option>" +
					"									<option value='under 5 min' "+ busstoptime5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ busstoptime10 +">5~10 min</option>" +
					"									<option value='10~15 min' "+ busstoptime15 +">10~15 min</option>" +
					"								</select> )" +
					"							<dd>English Stop Name:<br><input type=text name=busstopname size=12 value="+busstopname+">" +
					"							<br>Japanese Stop Name:<br><input type=text name=busstopname_j size=12 value= " + busstopname_j +"></dd> " +
					"						<td><input id=closesuper type='checkbox' name='close' value='Supermarket'" + closesuper + ">Supermarket " +
					"							<br>( 	<select name='supermarkettime'>" +
					"									<option value='right there!' "+ supermarkettime0 +"><right there!</option>" +
					"									<option value='under 5 min' "+ supermarkettime5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ supermarkettime10 +">5~10 min</option>" +
					"									<option value='10~15 min' "+ supermarkettime15 +">10~15 min</option>" +
					"								</select> )<br> " +
					"							<input id=closeconenience type='checkbox' name='close' value='Convenience Store' " + closeconvenience + ">Convenience Store " +
					"							<br>(  <select name='conveniencestoretime'>" +
					"									<option value='right there!' "+ conveniencestoretime0 +"><right there!</option>" +
					"									<option value='under 5 min' "+ conveniencestoretime5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ conveniencestoretime10 +">5~10min</option>" +
					"									<option value='10~15 min' "+ conveniencestoretime15 +">10~15min</option>" +
					"								</select> )<br> " +
					"							<input id=closerestaurant type='checkbox' name='close' value='Restaurant/Cafe'" + closerestaurant + ">Restaurant/Cafe " +
					"							<br>( 	<select name='restauranttime'>" +
					"									<option value='right there!' "+ restauranttime0 +"><right there!</option>" +
					"									<option value='under 5 min' "+ restauranttime5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ restauranttime10 +">5~10min</option>" +
					"									<option value='10~15 min' "+ restauranttime15 +">10~15min</option>" +
					"								</select> )<br> " +
					"							<input id=closepost type='checkbox' name='close' value='Post Office (with ATM)'" + closepost + ">Post Office (ATM) " +
					"							<br>( 	<select name='posttime'>" +
					"									<option value='right there!' "+ posttime0 +"><right there!</option>" +
					"									<option value='under 5 min' "+ posttime5 +"><5 min</option>" +
					"									<option value='5~10 min' "+ posttime10 +">5~10min</option>" +
					"									<option value='10~15 min' "+ posttime15 +">10~15min</option>" +
					"								</select> )<br>" +
					"					</table>" +
					"					<br> <input type='submit' value='Update Close to' />" +
					"				</form>" +
					"				<form class=amenitiesform id=acceptsform action='/updateaccepts'" +
					"				accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
					"					<span class=bold>Accepts Payment by</span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='accepts' value=''> " +
					"							<input id=acceptsvisa type='checkbox' name='accepts' value='Visa'" + acceptsvisa + ">Visa<br> " +
					"							<input id=acceptsmastercard type='checkbox' name='accepts' value='Mastercard'" + acceptsmastercard + ">Mastercard<br>" +
					"							<input id=acceptsdiscover type='checkbox' name='accepts' value='Discover'" + acceptsdiscover + ">Discover<br> " +
					"							<input id=acceptsjcb type='checkbox' name='accepts' value='JCB'" + acceptsjcb + ">JCB<br> " +
					"							<input id=acceptsamerican type='checkbox' name='accepts' value='American Express'" + acceptsamericanexpress + ">American Express<br> " +
					"							<input id=acceptssaison type='checkbox' name='accepts' value='Saison'" + acceptssaison + ">Saison<br> " +
					"							<input id=acceptsdc type='checkbox' name='accepts' value='DC'" + acceptsdc + ">DC<br> " +
					"							<input id=acceptsuc type='checkbox' name='accepts' value='UC'" + acceptsuc + ">UC<br> " +
					"							<input id=acceptstravelerschecks type='checkbox' name='accepts' value='Travelers Checks'" + acceptstravelerschecks + ">Travelers Checks<br>" +
					"							<input id=acceptstransfer type='checkbox' name='accepts' value='bank transfers'" + acceptstransfer + ">bank transfers<br>" +
					"							<input id=acceptstravelerschecks type='checkbox' name='accepts' value='travelers checks'" + acceptstravelerschecks + ">travelers checks<br>" +
					"							<input id=acceptscash type='checkbox' name='accepts' value='cash'" + acceptscash + ">cash<br>" +
					"					</table>" +	
					"					Others:<br>" +
					"					<textarea name=acceptsothers  rows='1' cols='60' maxlength=60 style='resize: none;' >" + acceptsothers + "</textarea>" +
					"					<br>Others (in Japanese):<br>" +
					"					<textarea name=acceptsothers_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + acceptsothers_j + "</textarea>" +
					"					<p><dt> Current List: <dd>" + acceptsstring + "</dd>" +
					"					<dt> (Japanese): <dd>" + acceptsstring_j + "</dd>" +
					"					<br> <input type='submit' value='Update Payment Options' />" +
					"				</form>" +
					"				<form class=amenitiesform id=speaksform action='/updatespeaks'" +
					"				accept-charset='utf-16' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
					"					<span class=bold>Can offer services in these languages:</span>" +
					"					<table>" +
					"						<tr>" +
					"						<td><input type='hidden' name='speaks' value=''> " +
					"							<input id=speaksenglish type='checkbox' name='speaks' value='English'" + speaksenglish + ">English<br> " +
					"							<input id=speaksjapanese type='checkbox' name='speaks' value='Japanese'" + speaksjapanese + ">Japanese<br> " +
					"							<input id=speaksgerman type='checkbox' name='speaks' value='German'" + speaksgerman + ">German<br>" +
					"							<input id=speaksrussian type='checkbox' name='speaks' value='Russian'" + speaksrussian + ">Russian<br> " +
					"							<input id=speakskorean type='checkbox' name='speaks' value='Korean'" + speakskorean + ">Korean<br>" +
					"							<input id=speakschinese type='checkbox' name='speaks' value='Chinese'" + speakschinese + ">Chinese<br> " +
					"							<input id=speaksfrench type='checkbox' name='speaks' value='French'" + speaksfrench + ">French<br>" +
					"					</table>" +
					"					Others:<br>" +
					"					<textarea name=speaksothers  rows='1' cols='60' maxlength=60 style='resize: none;' >" + speaksothers + "</textarea>" +
					"					<br>Others (in Japanese):<br>" +
					"					<textarea name=speaksothers_j  rows='1' cols='60' maxlength=60 style='resize: none;' >" + speaksothers_j + "</textarea>" +
					"					<p><dt> Current List: <dd>" + speaksstring + "</dd>" +
					"					<dt> (Japanese): <dd>" + speaksstring_j + "</dd>" +			
					"					<input type='submit' value='Update Languages' />" +
					"				</form>" +
					"			</div>" +
					"			<div id=outline>" +
					"				<form id=outlineform action='/updateoutline' accept-charset='utf-8' method='post'>" +
					"					<input type=hidden name=permissions value='administrator'> " +
					"					<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +	
					"<h2>Outline:</span><h2>" +
							"					<textarea name=outline cols=60 rows=20  wrap='hard'>");
					if (outline!=null)
					{pw.println(outline.getValue());}
					pw.println("</textarea>" +
							"" +
							"<h2>Japanese Outline:<h2>" +
							"					<textarea name=outline_j cols=60 rows=20 wrap='hard'>");
					if (outline_j!=null)
					{pw.println(outline_j.getValue());}
					pw.println("</textarea>" +
							"					<input type='submit' value='Update Outline' />" +
							"" +
							"				</form>" +
							"			</div>" +
							"		</div>" +
							"	</div>" +
							"	</div>" + //not sure where this matches up with. . . 
							"	<div id=basics>" +
							"		<form id=basicsform action='/updatebasics' accept-charset='utf-8' method='post' onsubmit='return validateForm()' >" +
							"			<input type=hidden name=permissions value='administrator'> " +
							"			<input type=hidden name=hotelKeyString value=" + hotelKeyString + ">" +
							"			<div id=hotellogo><img src=" + imagelogoUrl196 + " /></div>" +
							"			<dl>" +
							"				<dt>Services/Associations:" +
							"					<dd><input type='hidden' name='participation' value=''> " +
							"						<input id='touristassociation' type='checkbox' name='participation' value='Tourist Association' " + touristassociation + ">Tourist Association<br>" +
							"						<input id='travelporter' type='checkbox' name='participation' value='Travel Porter'" +  travelporter + ">Travel Porter<br>" +
							"						<input id='yesyakushima' type='checkbox' name='participation' value='YesYakushima'" +  yesyakushima + ">Yes!Yakushima<br>" +
							"				<dt>Location:" +
							"					<dd><select name ='location'>" +
							"						<option value='Miyanoura'" + Miyanoura + ">Miyanoura</option>" +
							"						<option value='Kusukawa'" + Kusukawa + ">Kusukawa</option>" +
							"						<option value='Koseda'" + Koseda + ">Koseda</option>" +
							"						<option value='Funayuki'" + Funayuki + ">Funayuki</option>" +
							"						<option value='Anbo'" + Anbo + ">Anbo</option>" +
							"						<option value='Mugio'" + Mugio + ">Mugio</option>" +
							"						<optiona value='Haru'" + Haru + ">Haru</option>" +
							"						<option value='Onoaida'" + Onoaida + ">Onoaida</option>" +
							"						<option value='Koshima'" + Koshima + ">Koshima</option>" +
							"						<option value='Hirauchi'" + Hirauchi + ">Hirauchi</option>" +
							"						<option value='Yudomari'" + Yudomari + ">Yudomari</option>" +
							"						<option value='Nakama'" + Nakama + ">Nakama</option>" +
							"						<option value='Kurio'" + Kurio + ">Kurio</option>" +
							"						<option value='Nagata'" + Nagata + ">Nagata</option>" +
							"						<option value='Yoshida'" + Yoshida + ">Yoshida</option>" +
							"						<option value='Issou'" + Issou + ">Issou</option>" +
							"						<option value='Shitoko'" + Shitoko + ">Shitoko</option>" +
							"						</select></dd>" +
							"				<dt>TEL:" +
							"					<dd><input type=hidden name='telephonea' size=4 value='0997'>" +
							"						0997-<input type=text name='telephoneb' size=2 maxlength='2' value=" + telephoneb + ">-" +
							"						<input type=text name='telephonec' size=4 maxlength='4' value=" + telephonec + ">" +
							"					</dd>" +
							"				<dt>MOBILE:" +
							"					<dd><input type=text name='mobilea' size=3 maxlength='3' value=" + mobilea + ">-" +
							"						<input type=text name='mobileb' size=4 maxlength='4' value=" + mobileb + ">-" +
							"						<input type=text name='mobilec' size=4 maxlength='4' value=" + mobilec + ">" +
							"				</dd><dt>Address:" +
							"						<dd><textarea name='address' rows='3' cols='16'>" + address + "</textarea>" +
							"				</dd><dt>住所:" +
							"						<dd><textarea name='address_j' rows='3' cols='16'>" + address_j + "</textarea>" +
							"				</dd><dt>Homepage:" +
							"					<dd>http://<input type=text name='homepage' size=16 value=" + homepage + ">" +
							"					<a href=" + homepagelink + ">" + homepagelink + "</a></dd>" +
							"				<dt>Price:" +
							"					<dd>￥<input type=text name='pricemin' size=6 value=" + pricemin + "> ~ " +
							"					￥<input type=text name='pricemax' size=6 value=" + pricemax + "></dd>" +
							"				<dt>Maximum Guests:" +
							"					<dd><input type=text name='guestsmax' size=6 value=" + guestsmax + "></dd>" +
							"				<dt>Accomodation Types:" +
							"					<dd><input type='hidden' name='type' value=''> " +
							"						<input id='campsite' type='checkbox' name='type' value='Campsite' " + campsite + ">Campsite<br>" +
							"						<input id='cottages' type='checkbox' name='type' value='Cottages' " + cottages + ">Cottages<br>" +
							"						<input id='minshuku' type='checkbox' name='type' value='Minshuku'" +  minshuku + ">Minshuku<br>" +
							"						<input id='business' type='checkbox' name='type' value='Business Hotel'" +  business + ">Business Hotel<br>" +
							"						<input id='hostel' type='checkbox' name='type' value='Hostel'" +  hostel + ">Hostel<br>" +
							"						<input id='ryokan' type='checkbox' name='type' value='Japanese Inn'" +  ryokan + ">Japanese Inn<br>" +
							"						<input id='fullhotel' type='checkbox' name='type' value='Full Hotel'" +  fullhotel + ">Full Hotel<br>" +
							"					Others:" +
							"					<textarea name=typesother cols=15 rows=2 style='resize: none;' >" + typesother + "</textarea>" +
							"					(Japanese):" +
							"					<textarea name=typesother_j cols=15 rows=2 style='resize: none;' >" + typesother_j + "</textarea></dd>" +
							"					Current Accomodation Types: " + typestring + 
							"					(Japanese): " + typestring_j + 
							"					<dt>Checkin: " +
							"					<textarea name='checkin' rows=1 cols=7 maxlength=12 style='resize: none;'>" + checkin + "</textarea>" +
							"					<dt>Checkout: " +
							"						<textarea name='checkout' rows=1 cols=7 maxlength=12 style='resize: none;'>" + checkout + "</textarea></dd>" +
							"			</dl>" +
							"			<input type='submit' value='Update Basic Information' />" +
							"		</form>");
					if (mapcode!=null)
						pw.println(mapcode.getValue());
					pw.println("	</div>" +
							"</div>");
					pw.println("</BODY>");
					pw.println("</HTML>");
					pw.close();
				}
				catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
				}
			}

		}
	}
	
	/*
					String homepagelink = Filternulls(hotel, "homepagelink");
					int priceminint =(int) (long) (Long) hotel.getProperty("pricemin"); */

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