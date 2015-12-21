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

public class Copy_2_of_UpdateHotelServlet extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
	response.setContentType("text/html");
	
	BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	

	response.setContentType("text/html; charset=shift_JIS");
	PrintWriter pw = response.getWriter();

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
					"</script></head>");
	
	
	String hotelName = request.getParameter("hotelName");
	if (hotelName == null) {
		hotelName = "default";
	}
    Key hotelKey = KeyFactory.createKey("Hotel", hotelName);
    Date date = new Date();       
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
	// Run an ancestor query to ensure we see the most up-to-date
	// view of the Greetings belonging to the selected Guestbook.
	Query query = new Query("Hotel", hotelKey).addSort("date",
			Query.SortDirection.DESCENDING);
	List<Entity> hotels = datastore.prepare(query).asList(
			FetchOptions.Builder.withLimit(1));
	String errorcode = request.getParameter("error");
	if (!(errorcode == null)){
		pw.println("There has been an error.");
	}
		
	if (hotels.isEmpty()) {
		pw.println("Hotel " + hotelName + " has no data.</p>");
	}
	else {
        pw.println("<p>Viewing info for " + hotelName + ".</p>");
	}
	
	
	Query query2 = new Query("Hotel");
	List<String> hotelNames = new ArrayList<String>();
	PreparedQuery pq2 = datastore.prepare(query2);
	for (Entity result : pq2.asIterable()) 
	{hotelNames.add((String) result.getProperty("hotelName"));}	
	
	Query query3 = new Query("Hotel");
	List<String> hotelNames_j = new ArrayList<String>();
	PreparedQuery pq3 = datastore.prepare(query3);
	for (Entity result : pq3.asIterable()) 
	{hotelNames_j.add((String) result.getProperty("hotelName_j"));}
	
	Query query4 = new Query("Hotel");
	List<String> hotelKeys = new ArrayList<String>();
	PreparedQuery pq4 = datastore.prepare(query4);
	for (Entity result : pq4.asIterable()) 
	{hotelKeys.add((String) result.getProperty("Key"));}	
	
	String hotelName_j;
	String incwasher = null;
	String incdryer = null;
	String inclaundry = null;
	String inctowel = null;
	String inchairdryer = null;
	String inciron = null;
	String incdvd = null;
	String incwifi = null;
	String inccomputer = null;
	String incbicycle = null;
	String inccar = null;
	String incluggage = null;
	String incbus = null;
	String otherincludedamenities = null;
	String includedamenitiesstring = null;
	String paidwasher = null;
	String paiddryer = null;
	String paidlaundry = null;
	String paidtowel = null;
	String paidhairdryer = null;
	String paidiron = null;
	String paiddvd = null;
	String paidwifi = null;
	String paidcomputer = null;
	String paidbicycle = null;
	String paidcar = null;
	String paidluggage = null;
	String paidbus = null;
	String otherpaidamenities = null;
	String paidamenitiesstring = null;
	String roomtoilet = null;
	String roombath = null;
	String roomkitchen = null;
	String roommicrowave = null;
	String roomfridge = null;
	String roomac = null;
	String roomheater = null;
	String roomtv = null;
	String roomcable = null;
	String roomphone = null;
	String roomwifi = null;
	String otherinroomamenities = null;
	String inroomamenitiesstring = null;
	String sharedtoilet = null;
	String sharedshower = null;
	String sharedindoorbath = null;
	String sharedoutdoorbath = null;
	String sharedsauna = null;
	String sharedpool = null;
	String sharedkitchen = null;
	String sharedmicrowave = null;
	String sharedfridge = null;
	String sharedac = null;
	String sharedheater = null;
	String sharedtv = null;
	String shareddvd = null;
	String sharedcomputer = null;
	String othersharedamenities = null;
	String sharedamenitiesstring = null;
	String mealbreakfast = null;
	String mealdinner = null;
	String mealstayonly = null;
	String mealvegetarian = null;
	String mealallergen = null;
	String othermeals = null;
	String mealsstring = null;
	String welpet = null;
	String welkid = null;
	String welforeigner = null;
	String welcouple = null;
	String welnoanimals = null;
	String noanimals = null;
	String smokingyes = null;
	String smokingno = null;
	String smokingboth = null;
	String smokingdefault = null;
	String acceptsvisa = null;
	String acceptsmastercard = null;
	String acceptsdiscover = null;
	String acceptsamericanexpress = null;
	String acceptstravelerschecks = null;
	String acceptsjtb = null;
	String acceptstransfer = null;
	String acceptscash = null;
	String otheraccepts = null;
	String acceptsstring = null;
	String speaksjapanese = null;
	String speaksenglish = null;
	String speaksgerman = null;
	String speaksfrench = null;
	String speakschinese = null;
	String speakskorean = null;
	String speaksrussian = null;
	String otherspeaks = null;
	String speaksstring = null;
	String closeport = null;
	String closeairport = null;
	String closebus = null;
	String closesuper = null;
	String closeconvenience = null;
	String closerestaurant = null;
	String closepost = null;
	String otherclose = null;
	String closestring = null;
	String porttime5 = null;
	String porttime10 = null;
	String porttime15 = null;
	String airporttime5 = null;
	String airporttime10 = null;
	String airporttime15 = null;
	String busstoptime5 = null;
	String busstoptime10 = null;
	String busstoptime15 = null;
	String supermarkettime5 = null;
	String supermarkettime10 = null;
	String supermarkettime15 = null;
	String restauranttime5 = null;
	String restauranttime10 = null;
	String restauranttime15 = null;
	String conveniencestoretime5 = null;
	String conveniencestoretime10 = null;
	String conveniencestoretime15 = null;
	String posttime5 = null;
	String posttime10 = null;
	String posttime15 = null;
	String location = null;
	String Miyanoura = null;
	String Kusukawa = null;
	String Koseda = null;
	String Anbo = null;
	String Haru = null;
	String Koshima = null;
	String Mugio = null;
	String Yudomari = null;
	String Hirauchi = null;
	String Nakama = null;
	String Kurio = null;
	String Nagata = null;
	String Issou = null;
	String Funayuki = null;
	String Onoaida = null;
	String Yoshida = null;
	String Shitoko = null;
	String telephonea = null;
	String telephoneb = null;
	String telephonec = null;
	String mobilea = null;
	String mobileb = null;
	String mobilec = null;
	String address = null;
	String address_j = null;
	String homepage = null;
	String homepagelink = null;
	String pricemin = null;
	String pricemax = null;
	String guestsmax = null;
	String campsite = null;
	String minshuku = null;
	String business = null;
	String hostel = null;
	String ryokan = null;
	String fullhotel = null;
	String othertypes = null;
	String othertypes_j = null;
	String typestring = null;
	String checkin = null;
	String checkout = null;
	String mapcode = null;
	String image1Url = "";
	String image2Url = "";
	String image3Url = "";
	String image4Url = "";
	String image5Url = "";
	String image1Url100="";
	String image2Url100="";
	String image3Url100="";
	String image4Url100="";
	String image5Url100="";
	String imagelogoUrl="";
	String imagelogoUrl196="";
	String outline="";
	String outline_j="";
	String tagline="";
	
	try {
        Entity hotel = datastore.get(hotelKey);
        hotel.setProperty("date", date);
        String ipaddr = request.getRemoteAddr();
        hotelName_j = Filternulls(hotel, "hotelName_j");
    	incwasher = (String) hotel.getProperty("incwasher");
    	incdryer =  (String) hotel.getProperty("incdryer");
    	inclaundry =  (String) hotel.getProperty("inclaundry");
    	inctowel =  (String) hotel.getProperty("inctowel");
    	inchairdryer =  (String) hotel.getProperty("inchairdryer");
    	inciron =  (String) hotel.getProperty("inciron");
    	incdvd =  (String) hotel.getProperty("incdvd");
    	incwifi =  (String) hotel.getProperty("incwifi");
    	inccomputer =  (String) hotel.getProperty("inccomputer");
    	incbicycle =  (String) hotel.getProperty("incbicycle");
    	inccar =  (String) hotel.getProperty("inccar");
    	incluggage =  (String) hotel.getProperty("incluggage");
    	incbus =  (String) hotel.getProperty("incbus");
    	otherincludedamenities=  Filternulls(hotel, "otherincludedamenities");
    	includedamenitiesstring=  Filternulls(hotel, "includedamenitiesstring");
    	paidwasher = (String) hotel.getProperty("paidwasher");
    	paiddryer =  (String) hotel.getProperty("paiddryer");
    	paidlaundry =  (String) hotel.getProperty("paidlaundry");
    	paidtowel =  (String) hotel.getProperty("paidtowel");
    	paidhairdryer =  (String) hotel.getProperty("paidhairdryer");
    	paidiron =  (String) hotel.getProperty("paidiron");
    	paiddvd =  (String) hotel.getProperty("paiddvd");
    	paidwifi =  (String) hotel.getProperty("paidwifi");
    	paidcomputer =  (String) hotel.getProperty("paidcomputer");
    	paidbicycle =  (String) hotel.getProperty("paidbicycle");
    	paidcar =  (String) hotel.getProperty("paidcar");
    	paidluggage =  (String) hotel.getProperty("paidluggage");
    	paidbus =  (String) hotel.getProperty("paidbus");
    	otherpaidamenities=  Filternulls(hotel, "otherpaidamenities");
    	paidamenitiesstring=  Filternulls(hotel, "paidamenitiesstring");
    	roomtoilet = (String) hotel.getProperty("roomtoilet");
    	roombath = (String) hotel.getProperty("roombath");
    	roomkitchen = (String) hotel.getProperty("roomkitchen");
    	roommicrowave = (String) hotel.getProperty("roommicrowave");
    	roomfridge = (String) hotel.getProperty("roomfridge");
    	roomheater = (String) hotel.getProperty("roomheater");
    	roomtv = (String) hotel.getProperty("roomtv");
    	roomcable = (String) hotel.getProperty("roomcable");
    	roomphone = (String) hotel.getProperty("roomphone");
    	roomwifi = (String) hotel.getProperty("roomwifi");
    	otherinroomamenities=  Filternulls(hotel, "otherinroomamenities");
    	inroomamenitiesstring=  Filternulls(hotel, "inroomamenitiesstring");
    	sharedtoilet = (String) hotel.getProperty("sharedtoilet");
    	sharedshower = (String) hotel.getProperty("sharedshower");
    	sharedindoorbath = (String) hotel.getProperty("sharedindoorbath");
    	sharedoutdoorbath = (String) hotel.getProperty("sharedoutdoorbath");
    	sharedsauna = (String) hotel.getProperty("sharedsauna");
    	sharedpool = (String) hotel.getProperty("sharedpool");
    	sharedkitchen = (String) hotel.getProperty("sharedkitchen");
    	sharedmicrowave = (String) hotel.getProperty("sharedmicrowave");
    	sharedfridge = (String) hotel.getProperty("sharedfridge");
    	sharedac = (String) hotel.getProperty("sharedac");
    	sharedheater = (String) hotel.getProperty("sharedheater");
    	sharedtv = (String) hotel.getProperty("sharedtv");
    	shareddvd = (String) hotel.getProperty("shareddvd");
    	sharedcomputer = (String) hotel.getProperty("sharedcomputer");
    	othersharedamenities=  Filternulls(hotel, "othersharedamenities");
    	sharedamenitiesstring=  Filternulls(hotel, "sharedamenitiesstring");
    	mealbreakfast=  (String) hotel.getProperty("mealbreakfast");
    	mealdinner=  (String) hotel.getProperty("mealdinner");
    	mealstayonly=  (String) hotel.getProperty("mealstayonly");
    	mealvegetarian=  (String) hotel.getProperty("mealvegetarian");
    	mealallergen=  (String) hotel.getProperty("mealallergen");
    	othermeals =  Filternulls(hotel, "othermeals");
    	mealsstring =  Filternulls(hotel, "mealsstring");
    	welkid=  (String) hotel.getProperty("welkid");
    	welpet=  (String) hotel.getProperty("welpet");
    	welforeigner=  (String) hotel.getProperty("welforeigner");
    	welcouple=  (String) hotel.getProperty("welcouple");
    	welnoanimals=  (String) hotel.getProperty("welnoanimals");
    	noanimals=  (String) hotel.getProperty("noanimals");
    	smokingyes=  (String) hotel.getProperty("smokingyes");
    	smokingno=  (String) hotel.getProperty("smokingno");
    	smokingboth=  (String) hotel.getProperty("smokingboth");
    	acceptsvisa=  (String) hotel.getProperty("acceptsvisa");
    	acceptsmastercard=  (String) hotel.getProperty("acceptsmastercard");
    	acceptsdiscover=  (String) hotel.getProperty("acceptsdiscover");
    	acceptsamericanexpress=  (String) hotel.getProperty("acceptsamericanexpress");
    	acceptstravelerschecks=  (String) hotel.getProperty("acceptstravelerschecks");
    	acceptsjtb=  (String) hotel.getProperty("acceptsjtb");
    	acceptstransfer=  (String) hotel.getProperty("acceptstransfer");
    	acceptscash=  (String) hotel.getProperty("acceptscash");
    	otheraccepts=  Filternulls(hotel, "otheraccepts");
    	acceptsstring=  Filternulls(hotel, "acceptsstring");
    	speaksenglish=  (String) hotel.getProperty("speaksenglish");
    	speaksjapanese=  (String) hotel.getProperty("speaksjapanese");
    	speaksgerman=  (String) hotel.getProperty("speaksgerman");
    	speaksfrench=  (String) hotel.getProperty("speaksfrench");
    	speaksrussian=  (String) hotel.getProperty("speaksrussian");
    	speakskorean=  (String) hotel.getProperty("speakskorean");
    	speakschinese=  (String) hotel.getProperty("speakschinese");
    	otherspeaks=  Filternulls(hotel, "otherspeaks");
    	speaksstring= Filternulls(hotel, "speaksstring");
    	closeport=  (String) hotel.getProperty("closeport");
    	closeairport=  (String) hotel.getProperty("closeairport");
    	closebus=  (String) hotel.getProperty("closebus");
    	closesuper=  (String) hotel.getProperty("closesuper");
    	closeconvenience=  (String) hotel.getProperty("closeconvenience");
    	closerestaurant=  (String) hotel.getProperty("closerestaurant");
    	closepost=  (String) hotel.getProperty("closepost");
    	otherclose=  (String) hotel.getProperty("otherclose");
    	closestring=  Filternulls(hotel, "closestring");
    	porttime5=  (String) hotel.getProperty("porttime5");
    	porttime10=  (String) hotel.getProperty("porttime10");
    	porttime15=  (String) hotel.getProperty("porttime15");
    	airporttime5=  (String) hotel.getProperty("airporttime5");
    	airporttime10=  (String) hotel.getProperty("airporttime10");
    	airporttime15=  (String) hotel.getProperty("airporttime15");
    	busstoptime5=  (String) hotel.getProperty("busstoptime5");
    	busstoptime10=  (String) hotel.getProperty("busstoptime10");
    	busstoptime15=  (String) hotel.getProperty("busstoptime15");
    	supermarkettime5=  (String) hotel.getProperty("supermarkettime5");
    	supermarkettime10=  (String) hotel.getProperty("supermarkettime10");
    	supermarkettime15=  (String) hotel.getProperty("supermarkettime15");
    	restauranttime5=  (String) hotel.getProperty("restauranttime5");
    	restauranttime10=  (String) hotel.getProperty("restauranttime10");
    	restauranttime15=  (String) hotel.getProperty("restauranttime15");
    	conveniencestoretime5=  (String) hotel.getProperty("conveniencestoretime5");
    	conveniencestoretime10=  (String) hotel.getProperty("conveniencestoretime10");
    	conveniencestoretime15=  (String) hotel.getProperty("conveniencestoretime15");
    	posttime5=  (String) hotel.getProperty("posttime5");
    	posttime10=  (String) hotel.getProperty("posttime10");
    	posttime15=  (String) hotel.getProperty("posttime15");
    	location = (String) hotel.getProperty("location");
    	Miyanoura = (String) hotel.getProperty("Miyanoura");
    	Kusukawa = (String) hotel.getProperty("Kusukawa");
    	Koseda = (String) hotel.getProperty("Koseda");
    	Anbo = (String) hotel.getProperty("Anbo");
    	Funayuki = (String) hotel.getProperty("Funayuki");
    	Haru = (String) hotel.getProperty("Haru");
    	Mugio = (String) hotel.getProperty("Mugio");
    	Koshima = (String) hotel.getProperty("Koshima");
    	Onoaida = (String) hotel.getProperty("Onoaida");
    	Hirauchi = (String) hotel.getProperty("Hirauchi");
    	Yudomari = (String) hotel.getProperty("Yudomari");
    	Kurio = (String) hotel.getProperty("Kurio");
    	Nakama = (String) hotel.getProperty("Nakama");
    	Nagata = (String) hotel.getProperty("Nagata");
    	Issou = (String) hotel.getProperty("Issou");
    	Yoshida = (String) hotel.getProperty("Yoshida");
    	Shitoko= (String) hotel.getProperty("Shitoko");
    	telephonea = Filternulls(hotel, "telephonea");
    	telephoneb = Filternulls(hotel, "telephoneb");
    	telephonec = Filternulls(hotel, "telephonec");
    	mobilea = Filternulls(hotel, "mobilea");
    	mobileb = Filternulls(hotel, "mobileb");
    	mobilec = Filternulls(hotel, "mobilec");
    	address = Filternulls(hotel, "address");
    	address_j = Filternulls(hotel, "address_j");
    	homepage = Filternulls(hotel, "homepage");
    	homepagelink = (String) hotel.getProperty("homepagelink");
    	pricemin = Filternulls(hotel, "pricemin");
    	pricemax = Filternulls(hotel, "pricemax");
    	guestsmax = Filternulls(hotel, "guestsmax");
    	campsite = (String) hotel.getProperty("campsite");
    	minshuku = (String) hotel.getProperty("minshuku");
    	business= (String) hotel.getProperty("business");
    	hostel = (String) hotel.getProperty("hostel");
    	ryokan = (String) hotel.getProperty("ryokan");
    	fullhotel = (String) hotel.getProperty("fullhotel");
    	othertypes = Filternulls(hotel, "othertypes");
    	othertypes_j = Filternulls(hotel, "othertypes_j");
    	typestring = Filternulls(hotel, "typestring");
    	checkin = Filternulls(hotel, "checkin");
    	checkout = Filternulls(hotel, "checkout");
    	mapcode = Filternulls(hotel, "mapcode");
    	outline = Filternulls(hotel, "outline");
    	outline_j = Filternulls(hotel, "outline_j");
    	image1Url = (String) hotel.getProperty("image1Url");
    	image2Url = (String) hotel.getProperty("image2Url");
    	image3Url = (String) hotel.getProperty("image3Url");
    	image4Url = (String) hotel.getProperty("image4Url");
    	image5Url = (String) hotel.getProperty("image5Url");
    	imagelogoUrl = (String) hotel.getProperty("imagelogoUrl");
    	image1Url100 = image1Url + "=s100";
    	image2Url100 = image2Url + "=s100";
    	image3Url100 = image3Url + "=s100";
    	image4Url100 = image4Url + "=s100";
    	image5Url100 = image5Url + "=s100";
    	imagelogoUrl196 = imagelogoUrl + "=s196";
        hotel.setProperty("ipaddr", ipaddr);
        hotelKey = datastore.put(hotel);
        
    } catch (com.google.appengine.api.datastore.EntityNotFoundException e) {
    	Entity hotel = new Entity("Hotel", hotelName);
        hotel.setProperty("date", date);
        hotelKey = datastore.put(hotel);
        hotelName_j="";
    }
	

    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    boolean isadmin=userService.isUserAdmin();
    if (user != null) {
    	pw.println("<p>Hello,Ç±ÇÒÇ…ÇøÇÕ " + user.getNickname() + ". You can" +
    		"<a href=" + userService.createLogoutURL(request.getRequestURI())+ " >sign out</a>.)</p>");
   
      } 
    else {
    	pw.println("Hello!Ç±ÇÒÇ…ÇøÇÕÅB " + 
    			"<a href=" + userService.createLoginURL(request.getRequestURI()) + " >Sign in</a>" +
    			" to include your name with greetings you post.</p>");
    	}

 if (isadmin){
		

	pw.println("<BODY>");
	pw.println("<div id='bigcontainer'>" +
				"	<div id='bigbanner'>Yakushima Hotels</div>" +
				"	<div id=searchmenu>" +
				"		<form action=/goupdatehotel method='post'>" +
				"			<dt>Choose an accomodation:" +
				"					<dt><select name ='hotelName' size=3 required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
	for (int j=0; j<hotelNames_j.size(); j++){
		pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames_j.get(j) + "</option>");
	}
	pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"		<form action=/goupdatehotel method='post'>" +
				"			<dt>Choose an accomodation:" +
				"					<dt><select name ='hotelName' size=3 required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
	for (int j=0; j<hotelNames.size(); j++){
		pw.println("<option value=" + hotelKeys.get(j) + ">" + hotelNames.get(j) + "</option>");
	}
	pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"</div>" +
				"	<div id=centercontainer>" +
				"		<div id=results>" +
				"			<div id=dir>" +
				" 				<form accept-charset='utf-16' id=dirform action='/updatedir' method='post'>" +
				"					English Name: " +
				"					<textarea name='hotelName' rows='1' cols='12' required>" + hotelName + "</textarea>" +
				"					<br>Japanese Name: <textarea name='hotelName_j' rows='1' cols='12' required>" + hotelName_j + "</textarea>" +
				"					<br><input type='submit' value='Edit or Create New Hotel' />" +
				"				</form>" +
				"			</div>" +
				"			<div id=tagline>jf" + tagline + "</div>" +
				"			<div id=photobox>" +
				"  				You can upload 4 photos (<500kb each) and a logo (200px x 80px). Which" +
				"				photo do you wish to upload?" +
				"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto1") + 
				" 					 method='post' enctype='multipart/form-data' > " +
				"					Photo 1 <br>Current Photo:<br>" +
				"					<img src=" + image1Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
				"					<input type='file' name='myFile'> " +
				"					<input type=hidden name=hotelName value=" + hotelName + " > " +
				"					<input type='submit' value='Submit'>" +
				"					</form>	" +
				"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto2") + 
				" 					 method='post' enctype='multipart/form-data' > " +
				"					Photo 2 <br>Current Photo:<br>" +
				"					<img src=" + image2Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
				"					<input type='file' name='myFile'> " +
				"					<input type=hidden name=hotelName value=" + hotelName + " > " +
				"					<input type='submit' value='Submit'>" +
				"					</form>	" +
				"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto3") + 
				" 					 method='post' enctype='multipart/form-data' > " +
				"					Photo 3 <br>Current Photo:<br>" +
				"					<img src=" + image3Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
				"					<input type='file' name='myFile'> " +
				"					<input type=hidden name=hotelName value=" + hotelName + " > " +
				"					<input type='submit' value='Submit'>" +
				"					</form>	" +
				"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto4") + 
				" 					 method='post' enctype='multipart/form-data' > " +
				"					Photo 4 <br>Current Photo:<br>" +
				"					<img src=" + image4Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
				"					<input type='file' name='myFile'> " +
				"					<input type=hidden name=hotelName value=" + hotelName + " > " +
				"					<input type='submit' value='Submit'>" +
				"					</form>	" +
				"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephoto5") + 
				" 					 method='post' enctype='multipart/form-data' > " +
				"					Photo 5 <br>Current Photo:<br>" +
				"					<img src=" + image5Url100 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
				"					<input type='file' name='myFile'> " +
				"					<input type=hidden name=hotelName value=" + hotelName + " > " +
				"					<input type='submit' value='Submit'>" +
				"					</form>	" +
				"				<form id=photoboxform action=" + blobstoreService.createUploadUrl("/updatephotologo") + 
				" 					 method='post' enctype='multipart/form-data' > " +
				"					Logo <br>Current Photo:<br>" +
				"					<img src=" + imagelogoUrl196 + " style='z-index:1;margin-left: auto; margin-right: auto; vertical-align:middle' />" +
				"					<input type='file' name='myFile'> " +
				"					<input type=hidden name=hotelName value=" + hotelName + " > " +
				"					<input type='submit' value='Submit'>" +
				"					</form>	" +				
				"" +
				"" +
				"			</div>" +
				"			<div id=amenities>" +
				"				<h3>Included Amenities</h3>" +
				"				<form class=amenitiesform id=includedamenitiesform" +
				"				action='/updateincludedamenities' accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					Included Amenities" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='includedamenities' value=''>" +
				"							<input id=incwasher type='checkbox' name='includedamenities'" +
				"								value='Washer'" + incwasher + ">Washer<br> " +
				"							<input id=incdryer type='checkbox' name='includedamenities'" +
				"							 	value='Dryer'" + incdryer + ">Dryer<br>" +
				"							<input id=inclaundry type='checkbox' name='includedamenities'" +
				"								value='Laundry Service'" + inclaundry + ">Laundry Service<br>" +
				"						<td><input id=inctowel type='checkbox' name='includedamenities'" +
				"							value='Towel'" + inctowel + ">Towel<br> " +
				"							<input id=inchairdryer type='checkbox' name='includedamenities'" +
				"							value='Hair Dryer'" + inchairdryer + ">Hair Dryer<br>" +
				"							<input id=inciron type='checkbox' name='includedamenities'" +
				"							value='Iron'" + inciron + ">Iron<br>" +
				"						<td><input id=incdvd type='checkbox' name='includedamenities'" +
				"							value='DVD-Player' " + incdvd + ">DVD-Player<br> " +
				"							<input id=incwifi type='checkbox' name='includedamenities' " +
				"							value='Wifi' " + incwifi + ">Wifi<br>" +
				"							<input id=inccomputer type='checkbox' name='includedamenities'" +
				"							value='Computer' " + inccomputer + ">Computer<br>" +
				"						<td><input id=incbicycle type='checkbox' name='includedamenities' " +
				"							value='Bicycles' " + incbicycle + ">Bicycles<br>" +
				"							<input id=inccar type='checkbox' name='includedamenities'" +
				"							value='Rental Car' " + inccar + ">Rental Car<br> " +
				"							<input id=incluggage type='checkbox' name='includedamenities'" +
				"							value='Luggage Holding' " + incluggage + ">Luggage Holding<br> " +
				"							<input id=incbus type='checkbox' name='includedamenities'" +
				"							value='Welcome Bus' " + incbus + ">Welcome Bus<br>" +
				"					</table>" +
				"					<br>Others:" +
				"					<textarea name=otherincludedamenities cols=50 rows=1>" + otherincludedamenities + "</textarea>" +
				"					<br> Total: " + includedamenitiesstring + " <br>" +
				"					<input type='submit' value='Update Included Amenities' /> <br>" +
				"				</form>" +
				"				<form class=amenitiesform id=paidamenitiesform" +
				"				action='/updatepaidamenities' accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					Paid Amenities" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='paidamenities' value=''>" +
				"							<input id=paidwasher type='checkbox' name='paidamenities'" +
				"								value='Washer'" + paidwasher + ">Washer<br> " +
				"							<input id=paiddryer type='checkbox' name='paidamenities'" +
				"							 	value='Dryer'" + paiddryer + ">Dryer<br>" +
				"							<input id=paidlaundry type='checkbox' name='paidamenities'" +
				"								value='Laundry Service'" + paidlaundry + ">Laundry Service<br>" +
				"						<td><input id=paidtowel type='checkbox' name='paidamenities'" +
				"							value='Towel'" + paidtowel + ">Towel<br> " +
				"							<input id=paidhairdryer type='checkbox' name='paidamenities'" +
				"							value='Hair Dryer'" + paidhairdryer + ">Hair Dryer<br>" +
				"							<input id=paidiron type='checkbox' name='paidamenities'" +
				"							value='Iron'" + paidiron + ">Iron<br>" +
				"						<td><input id=paiddvd type='checkbox' name='paidamenities'" +
				"							value='DVD-Player' " + paiddvd + ">DVD-Player<br> " +
				"							<input id=paidwifi type='checkbox' name='paidamenities' " +
				"							value='Wifi' " + paidwifi + ">Wifi<br>" +
				"							<input id=paidcomputer type='checkbox' name='paidamenities'" +
				"							value='Computer' " + paidcomputer + ">Computer<br>" +
				"						<td><input id=paidbicycle type='checkbox' name='paidamenities' " +
				"							value='Bicycles' " + paidbicycle + ">Bicycles<br>" +
				"							<input id=paidcar type='checkbox' name='paidamenities'" +
				"							value='Rental Car' " + paidcar + ">Rental Car<br> " +
				"							<input id=paidluggage type='checkbox' name='paidamenities'" +
				"							value='Luggage Holding' " + paidluggage + ">Luggage Holding<br> " +
				"							<input id=paidbus type='checkbox' name='paidamenities'" +
				"							value='Welcome Bus' " + paidbus + ">Welcome Bus<br>" +
				"					</table>" +
				"					<br>Others:" +
				"					<textarea name=otherpaidamenities cols=50 rows=1>" + otherpaidamenities + "</textarea>" +
				"					<br> Total: " + paidamenitiesstring + " <br>" +
				"					<input type='submit' value='Update Paid Amenities' /> <br>" +
				"				</form>" +
				"				<form class=amenitiesform id=inroomamenitiesform" +
				"				action='/updateinroomamenities' accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					In-Room Facilities:" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='inroomamenities' value=''>" +
				"							<input id=roomtoilet type='checkbox' name='inroomamenities' value='Toilet'" + roomtoilet + ">Toilet<br> " +
				"							<input id=roombath type='checkbox' name='inroomamenities' value='Bath/Shower'" + roombath + ">Bath/Shower<br>" +
				"							<input id=roomkitchen type='checkbox' name='inroomamenities' value='Kitchen/Kitchenette'" + roomkitchen + ">Kitchen/Kitchenette<br>" +
				"						<td><input id=roommicrowave type='checkbox' name='inroomamenities' value='Microwave'" + roommicrowave + ">Microwave<br>" +
				"							<input id=roomfridge type='checkbox' name='inroomamenities' value='Fridge'" + roomfridge + ">Fridge<br> " +
				"							<input id=roomac type='checkbox' name='inroomamenities' value='A/C'" + roomac + ">A/C<br>" +
				"							<input id=roomheater type='checkbox' name='inroomamenities' value='Heater'" + roomheater + ">Heater<br>" +
				"						<td><input id=roomtv type='checkbox' name='inroomamenities' value='Local TV'" + roomtv + ">Local TV<br> " +
				"							<input id=roomcable type='checkbox' name='inroomamenities' value='Cable/Satelite TV'" + roomcable + ">Cable/Satelite TV<br> " +
				"							<input id=roomphone type='checkbox' name='inroomamenities' value='Telephone'" + roomphone + ">Telephone<br>" +
				"							<input id=roomwifi type='checkbox' name='inroomamenities' value='Wifi'" + roomwifi + ">Wifi<br>" +
				"					</table>" +
				"					<br>Others:" +
				"					<textarea name=otherinroomamenities cols=50 rows=1>" + otherinroomamenities + "</textarea>" +
				"					<br> Total: " + inroomamenitiesstring + " <br>" +
				"					<input type='submit' value='Update Inroom Amenities' />" +
				"				</form>" +
				"				<form class=amenitiesform id=sharedamenitiesform" +
				"				action='/updatesharedamenities' accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName+ ">" +
				"					Shared Facilities:" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='sharedamenities' value=''>" + 
				"							<input id=sharedtoilet type='checkbox' name='sharedamenities' value='Toilet'" + sharedtoilet + ">Toilet<br> " +
				"							<input id=sharedshower type='checkbox' name='sharedamenities' value='Shower'" + sharedshower + ">Shower<br>" +
				"							<input id=sharedindoorbath type='checkbox' name='sharedamenities' value='Indoor Bath'" + sharedindoorbath + ">Indoor Bath<br> " +
				"							<input id=sharedoutdoorbath type='checkbox' name='sharedamenities' value='Outdoor Bath'" + sharedoutdoorbath + ">Outdoor Bath<br> " +
				"							<input id=sharedsauna type='checkbox' name='sharedamenities' value='Sauna/Spa'" + sharedsauna + ">Sauna/Spa<br> " +
				"							<input id=sharedpool type='checkbox' name='sharedamenities' value='Pool'" + sharedpool + ">Pool<br>" +
				"							<input id=sharedkitchen type='checkbox' name='sharedamenities' value='Kitchen/Kitchenette'" + sharedkitchen + ">Kitchen/Kitchenette<br>" +
				"						<td><input id=sharedmicrowave type='checkbox' name='sharedamenities' value='Microwave'" + sharedmicrowave + ">Microwave<br>" +
				"							<input id=sharedfridge type='checkbox' name='sharedamenities' value='Fridge'" + sharedfridge + ">Fridge<br> " +
				"							<input id=sharedac type='checkbox' name='sharedamenities' value='A/C'" + sharedac + ">A/C<br>" +
				"							<input id=sharedheater type='checkbox' name='sharedamenities' value='Heater'" + sharedheater + ">Heater<br> " +
				"							<input id=sharedtv type='checkbox' name='sharedamenities' value='Television'" + sharedtv + ">Television<br>" +
				"						<td><input id=shareddvd type='checkbox' name='sharedamenities' value='DVD Player'" + shareddvd + ">DVD Player<br>" +
				"							<input id=sharedcomputer type='checkbox' name='sharedamenities' value='Computer'" + sharedcomputer + ">Computer<br>" +
				"					</table>" +
				"					<br>Others:" +
				"					<textarea name=othersharedamenities cols=50 rows=1>" + othersharedamenities + "</textarea>" +
				"					<br> Total: " + sharedamenitiesstring + " <br>" +
				"					<br> <input type='submit' value='Update Shared Facilities' />" +
				"				</form>" +
				"				<form class=amenitiesform id=mealsform action='/updatemeals'" +
				"				accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					Meals:" +
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
				"					<br>Others:" +
				"					<textarea name=othermeals cols=50 rows=1>" + othermeals + "</textarea>" +
				"					<br> Total: " + mealsstring + " <br>" +
				"					<br> <input type='submit' value='Update Meals' />" +
				"				</form>" +
				"				<form class=amenitiesform id=welcomingform action='/updatewelcoming'" +
				"				accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					This facility is proud to welcome:" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='welcoming' value=''> " +
				"							<input id=welpet type='checkbox' name='welcoming' value='pets'" + welpet + ">pets<br>" +
				"							<input id=welkid type='checkbox' name='welcoming' value='children'" + welkid + ">children" +
				"						<td><input id=welforeigner type='checkbox' name='welcoming' value='foreigners'" + welforeigner + ">foreigners<br> " +
				"							<input id=welcouple type='checkbox' name='welcoming' value='couples'" + welcouple + ">couples<br>" +
				"						<tr><td><input type='hidden' name='welnoanimals' value=''> " +
				"							<input id=welnoanmals type='checkbox' name='welnoanimals' value=' No animals are kept on the premises.'" + welnoanimals + "> No animals are kept on the premises." +
				"						<tr><td><br><input type='hidden' name='smoking' value=''>" +
				"							<input id=nonsmokingrooms type='radio' name='smoking' value='NonSmoking Rooms Only'" + smokingno + ">NonSmoking Rooms Only<br> " +
				"							<input id=smokingrooms type='radio' name='smoking' value='Smoking Rooms Only'" + smokingyes + ">Smoking Rooms Only<br>" +
				"							<input id=bothrooms type='radio' name='smoking' value='Smoking and Nonsmoking Rooms'" + smokingboth + ">Smoking and Nonsmoking Rooms" +
				"					</table>" +
				"					<br> <input type='submit' value='Update Welcoming' />" +
				"				</form>" +
				"				<form class=amenitiesform id=closetoform action='/updateclose'" +
				"				accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					Close to (walking time):<br>" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='close' value=''>" +
				"							<input id=closeport type='checkbox' name='close' value='Port'" + closeport + ">Port" +
				"							<br>( 	<select name='porttime'>" +
				"									<option value='under 5 min'"+ porttime5 +"><5 min</option>" +
				"									<option value='5~10 min' "+ porttime10 +">5~10min</option>" +
				"									<option value='10~15 min'"+ porttime15 +">10~15min</option>" +
				"								</select> )<br> " +
				"							<input id=closeairport type='checkbox' name='close' value='Airport'" + closeairport + ">Airport " +
				"							<br>( 	<select name='airporttime'>" +
				"									<option value='under 5 min'"+ airporttime5 +"><5 min</option>" +
				"									<option value='5~10 min'"+ airporttime10 +">5~10min</option>" +
				"									<option value='10~15 min'"+ airporttime15 +">10~15min</option>" +
				"								</select> )<br> " +
				"							<input id=closebus type='checkbox' name='close' value='Bus Stop'" + closebus + ">Bus-Stop " +
				"							<br><input type=text name=busstopname size=12> " +
				"							( 	<select name='busstoptime'>" +
				"									<option value='<5 min'"+ busstoptime5 +"><5 min</option>" +
				"									<option value='5~10 min'"+ busstoptime10 +">5~10min</option>" +
				"									<option value='10~15 min'"+ busstoptime15 +">10~15min</option>" +
				"								</select> )" +
				"						<td><input id=closesuper type='checkbox' name='close' value='Supermarket'" + closesuper + ">Supermarket " +
				"							<br>( 	<select name='supermarkettime'>" +
				"									<option value='under 5 min'"+ supermarkettime5 +"><5 min</option>" +
				"									<option value='5~10 min'"+ supermarkettime10 +">5~10min</option>" +
				"									<option value='10~15 min'"+ supermarkettime15 +">10~15min</option>" +
				"								</select> )<br> " +
				"							<input id=closeconenience type='checkbox' name='close' value='Convenience Store' " + closeconvenience + ">Convenience Store " +
				"							<br>(  <select name='conveniencestoretime'>" +
				"									<option value='under 5 min'"+ conveniencestoretime5 +"><5 min</option>" +
				"									<option value='5~10 min'"+ conveniencestoretime10 +">5~10min</option>" +
				"									<option value='10~15 min'"+ conveniencestoretime15 +">10~15min</option>" +
				"								</select> )<br> " +
				"							<input id=closerestaurant type='checkbox' name='close' value='Restaurant/Cafe'" + closerestaurant + ">Restaurant/Cafe " +
				"							<br>( 	<select name='restauranttime'>" +
				"									<option value='under 5 min'"+ restauranttime5 +"><5 min</option>" +
				"									<option value='5~10 min'"+ restauranttime10 +">5~10min</option>" +
				"									<option value='10~15 min'"+ restauranttime15 +">10~15min</option>" +
				"								</select> )<br> " +
				"							<input id=closepost type='checkbox' name='close' value='Post Office (with ATM)'" + closepost + ">Post Office (ATM) " +
				"							<br>( 	<select name='posttime'>" +
				"									<option value='under 5 min'"+ posttime5 +"><5 min</option>" +
				"									<option value='5~10 min'"+ posttime10 +">5~10min</option>" +
				"									<option value='10~15 min'"+ posttime15 +">10~15min</option>" +
				"								</select> )<br>" +
				"					</table>" +
				"					<br> <input type='submit' value='Update Close to' />" +
				"				</form>" +
				"				<form class=amenitiesform id=acceptsform action='/updateaccepts'" +
				"				accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					Accepts" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='accepts' value=''> " +
				"							<input id=acceptsvisa type='checkbox' name='accepts' value='Visa'" + acceptsvisa + ">Visa<br> " +
				"							<input id=acceptsmastercard type='checkbox' name='accepts' value='Mastercard'" + acceptsmastercard + ">Mastercard<br>" +
				"							<input id=acceptsjtb type='checkbox' name='accepts' value='JTB'" + acceptsjtb + ">JTB<br> " +
				"							<input id=acceptsdiscover type='checkbox' name='accepts' value='Discover'" + acceptsdiscover + ">Discover<br> " +
				"							<input id=acceptsamerican type='checkbox' name='accepts' value='American Express'" + acceptsamericanexpress + ">American Express<br> " +
				"							<input id=acceptstravelerschecks type='checkbox' name='accepts' value='Travelers Checks'" + acceptstravelerschecks + ">Travelers Checks<br>" +
				"							<input id=acceptstransfer type='checkbox' name='accepts' value='bank transfers'" + acceptstransfer + ">bank transfers<br>" +
				"							<input id=acceptstravelerschecks type='checkbox' name='accepts' value='travelers checks'" + acceptstravelerschecks + ">travelers checks<br>" +
				"							<input id=acceptscash type='checkbox' name='accepts' value='cash'" + acceptscash + ">cash<br>" +
				"					</table>" +
				"					<br>Others:" +
				"					<textarea name=otheraccepts cols=50 rows=1>" + otheraccepts + "</textarea>" +
				"					<br> Total: " + acceptsstring + " <br>" +					
				"					<br> <input type='submit' value='Update Accepts' />" +
				"				</form>" +
				"				<form class=amenitiesform id=speaksform action='/updatespeaks'" +
				"				accept-charset='utf-16' method='post'>" +
				"					<input type=hidden name=hotelName value=" + hotelName + ">" +
				"					Speaks" +
				"					<table>" +
				"						<tr>" +
				"						<td><input type='hidden' name='speaks' value=''> " +
				"							<input id=speaksjapanese type='checkbox' name='speaks' value='Japanese'" + speaksjapanese + ">Japanese<br> " +
				"							<input id=speaksenglish type='checkbox' name='speaks' value='English'" + speaksenglish + ">English<br> " +
				"							<input id=speaksgerman type='checkbox' name='speaks' value='German'" + speaksgerman + ">German<br>" +
				"							<input id=speaksrussian type='checkbox' name='speaks' value='Russian'" + speaksrussian + ">Russian<br> " +
				"							<input id=speakskorean type='checkbox' name='speaks' value='Korean'" + speakskorean + ">Korean<br>" +
				"							<input id=speakschinese type='checkbox' name='speaks' value='Chinese'" + speakschinese + ">Chinese<br> " +
				"							<input id=speaksfrench type='checkbox' name='speaks' value='French'" + speaksfrench + ">French<br>" +
				"					</table>" +
				"					<br>Others:" +
				"					<textarea name=otherspeaks cols=50 rows=1>" + otherspeaks + "</textarea>" +
				"					<br> Total: " + speaksstring + " <br>" +				
				"					<input type='submit' value='ê›îıìoò^' />" +
				"				</form>" +
				"			</div>" +
				"			<div id=outline>" +
				"				<form id=basicsform action='/updateoutline' accept-charset='utf-8' method='post'>" +
				"Outline:" +
				"					<textarea name=outline cols=60 rows=20>" + outline + "</textarea>" +
				"" +
				"Japanese Outline:" +
				"					<textarea name=outline_j cols=60 rows=20>" + outline_j + "</textarea>" +
				"					<input type='submit' value='Update' />" +
				"" +
				"				</form>" +
				"			</div>" +
				"		</div>" +
				"	</div>" +
				"	<div id=basics>" +
				"		<form id=basicsform action='/updatebasics' accept-charset='utf-8' method='post'>" +
				"			<input type=hidden name=hotelName value=" + hotelName + ">" +
				"			<div id=hotellogo><img src=" + imagelogoUrl196 + " /></div>" +
				"			<dl>" +
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
				"				</dd><dt>èZèä:" +
				"						<dd><textarea name='address_j' rows='3' cols='16'>" + address_j + "</textarea>" +
				"				</dd><dt>Homepage:" +
				"					<dd>http://<input type=text name='homepage' size=16 value=" + homepage + ">" +
				"					<a href=" + homepagelink + ">" + homepagelink + "</a></dd>" +
				"				<dt>Price:" +
				"					<dd>Åè<input type=text name='pricemin' size=6 value=" + pricemin + "> ~ " +
				"					Åè<input type=text name='pricemax' size=6 value=" + pricemax + "></dd>" +
				"				<dt>Maximum Guests:" +
				"					<dd><input type=text name='guestsmax' size=6 value=" + guestsmax + "></dd>" +
				"				<dt>Accomodation Types:" +
				"					<dd><input type='hidden' name='type' value=''> " +
				"						<input id='campsite' type='checkbox' name='type' value='Campsite' " + campsite + ">Campsite<br>" +
				"						<input id='minshuku' type='checkbox' name='type' value='Minshuku'" +  minshuku + ">Minshuku<br>" +
				"						<input id='business' type='checkbox' name='type' value='Business Hotel'" +  business + ">Business Hotel<br>" +
				"						<input id='hostel' type='checkbox' name='type' value='Hostel'" +  hostel + ">Hostel<br>" +
				"						<input id='ryokan' type='checkbox' name='type' value='Japanese Inn'" +  ryokan + ">Japanese Inn<br>" +
				"						<input id='fullhotel' type='checkbox' name='type' value='Full Hotel'" +  fullhotel + ">Full Hotel<br></dd>" +
				"					Others:" +
				"					<textarea name=othertypes cols=20 rows=2>" + othertypes + "</textarea>" +
				"					Others_j:" +
				"					<textarea name=othertypes_j cols=20 rows=2>" + othertypes_j + "</textarea>" +
				"					<br> Total: " + typestring + " <br>" +
				"					<br><dt>Checkin/out:" +
				"					<dd><input type=text name='checkin' size=6 value=" + checkin + ">/" +
				"						<input type=text name='checkout' size=6 value=" + checkout + "></dd>" +
				"			</dl>" +			
				"			Map Code: <br><textarea name=mapcode cols=20 rows=8>" + mapcode + "</textarea>" +
				"			<input type='submit' value='ê›îıìoò^' />" +
				"		</form>" +
				"		<iframe width='196' height='196' frameborder='0' scrolling='no' marginheight='0' marginwidth='0' src='https://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=%E5%B1%8B%E4%B9%85%E5%B3%B6%E7%94%BA%E5%AE%89%E6%88%BF126-1&amp;aq=&amp;sll=30.314561,130.655959&amp;sspn=0.010762,0.021136&amp;ie=UTF8&amp;hq=&amp;hnear=Japan,+Kagoshima-ken,+Kumage-gun,+Yakushima-ch%C5%8D,+Anb%C5%8D,+%EF%BC%91%EF%BC%92%EF%BC%96&amp;t=m&amp;ll=30.31458,130.655937&amp;spn=0.014819,0.01708&amp;z=14&amp;iwloc=A&amp;output=embed'></iframe>" +
				"	</div>" +
				"</div>");
	pw.println("</BODY>");
	pw.println("</HTML>");
	pw.close();
    }
 else
 	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
}
    private static String Filternulls(Entity hotel, String message) {
    	String y = (String) hotel.getProperty(message);
    	if (y == null | y == "null")
    	{y="";}
    	return y;
    	
    	
    	
    	
    }
}
