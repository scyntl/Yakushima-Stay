package displayhotel;

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

public class Copy_2_of_DisplayHotelServlet extends HttpServlet {
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
	
	
	String hotelName = request.getParameter("hotelName");
	String hotelKeyString = request.getParameter("hotelKeyString");
	if (hotelName == null) {
		hotelName = "default";
	}
	Key hotelKey=null;
	if (hotelKeyString == null) {
	    hotelKey = KeyFactory.createKey("Hotel", hotelName);
	    hotelKeyString = KeyFactory.keyToString(hotelKey);
	}
	else{
	    hotelKey = KeyFactory.stringToKey(hotelKeyString);
	}
	
    Date date = new Date();       
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    
	// Run an ancestor query to ensure we see the most up-to-date
	// view of the Greetings belonging to the selected Guestbook.
	Query query = new Query("Hotel", hotelKey).addSort("date",
			Query.SortDirection.DESCENDING);
	List<Entity> hotels = datastore.prepare(query).asList(
			FetchOptions.Builder.withLimit(1));

	if (hotels.isEmpty()) {
		pw.println("I'm sorry, we have no data for " + hotelName + ". Please report this problem to the administrator.</p>");
	}
	else {
        pw.println("<p>Viewing info for " + hotelName + ".</p>");
	}


Query query2 = new Query("Hotel");
List<String> hotelNames = new ArrayList<String>();
PreparedQuery pq = datastore.prepare(query2);
for (Entity result : pq.asIterable()) 
{hotelNames.add((String) result.getProperty("hotelName"));}
	
	String hotelName_j;
	String includedamenitiesstring = null;
	String paidamenitiesstring = null;
	String inroomamenitiesstring = null;
	String sharedamenitiesstring = null;
	String mealsstring = null;
	String welcomingstring = null;
	String welnoanimals = null;
	String welnoanimalsstring=  null;
	String smokingstring = null;
	String acceptsstring = null;
	String speaksstring = null;
	String closestring = null;
	String location = null;
	String telephone = null;
	String mobile = null;
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
	
	try {
        Entity hotel = datastore.get(hotelKey);
        hotel.setProperty("date", date);
        String ipaddr = request.getRemoteAddr();
        hotelName_j = (String) hotel.getProperty("hotelName_j");
    	includedamenitiesstring=  Filternulls(hotel, "includedamenitiesstring");
    	paidamenitiesstring=  Filternulls(hotel, "paidamenitiesstring");
    	inroomamenitiesstring=  Filternulls(hotel, "inroomamenitiesstring");
    	sharedamenitiesstring=  Filternulls(hotel, "sharedamenitiesstring");
    	mealsstring=  Filternulls(hotel, "mealsstring");
    	welcomingstring=  Filternulls(hotel, "welcomingstring");
    	welnoanimalsstring=  Filternulls(hotel, "welnoanimalsstring");
    	smokingstring=  Filternulls(hotel, "smokingstring");
    	acceptsstring=  Filternulls(hotel, "acceptsstring");
    	speaksstring= Filternulls(hotel, "speaksstring");
    	closestring=  Filternulls(hotel, "closestring");
    	location = Filternulls(hotel, "location");
    	telephone = Filternulls(hotel, "telephone");
    	mobile = Filternulls(hotel, "mobile");
    	address = Filternulls(hotel, "address");
    	address_j = Filternulls(hotel, "address_j");
    	homepage = Filternulls(hotel, "homepage");
    	homepagelink = Filternulls(hotel, "homepagelink");
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
    	typestring = Filternulls(hotel, "typestring");
    	checkin = Filternulls(hotel, "checkin");
    	checkout = Filternulls(hotel, "checkout");
    	mapcode = Filternulls(hotel, "mapcode");
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
	

		

	pw.println("<BODY>");

	pw.println("<div id='bigcontainer'>" +
				"	<div id='bigbanner'>Yakushima Hotels" +
				"" +
				"" +
				"<a href =/displayhotel_j?=" + hotelName + "><input type=button value='ì˙ñ{åÍ' onclick='location.href(/displayhotel_j);'  ></a>" +
				"<a href =/displayhotel?=" + hotelName + "><input type=button value='English' onclick='location.href(/displayhotel);'  ></a>" +
				"</div>" +
				"	<div id=searchmenu>" +
				"		<form action=/gohotel method='post'>" +
				"			<dt>Choose an accomodation:" +
				"					<dt><select name ='hotelName' size=3 required style='width:190px;' >" +
				"						<option value='Choose' disabled selected>Choose . . .</option>");
	for (int j=0; j<hotelNames.size(); j++){
		pw.println("<option value=" + hotelNames.get(j) + ">" + hotelNames.get(j) + "</option>");
	}
	pw.println(	"						</select>" +
				"			<div style='text-align:right;'><input type='submit' value='Go!' /></div>" +
				"		</form>	" + 
				"" +
				"<form accept-charset='utf-16' id=searchform action='/searchhotel' method='post'>" +
				"" +
				"Or conduct a search:" +
				"				<dt>Location:" +
				"					<dd><select name ='location'>" +
				"						<option value='All'>All</option>" +
				"						<option value='Miyanoura'>Miyanoura</option>" +
				"						<option value='Kusukawa' >Kusukawa</option>" +
				"						<option value='Koseda' >Koseda</option>" +
				"						<option value='Funayuki' >Funayuki</option>" +
				"						<option value='Anbo' >Anbo</option>" +
				"						<option value='Mugio' >Mugio</option>" +
				"						<optiona value='Haru' >Haru</option>" +
				"						<option value='Onoaida' >Onoaida</option>" +
				"						<option value='Koshima' >Koshima</option>" +
				"						<option value='Hirauchi' >Hirauchi</option>" +
				"						<option value='Yudomari' >Yudomari</option>" +
				"						<option value='Nakama' >Nakama</option>" +
				"						<option value='Kurio' >Kurio</option>" +
				"						<option value='Nagata' >Nagata</option>" +
				"						<option value='Yoshida' >Yoshida</option>" +
				"						<option value='Issou' >Issou</option>" +
				"						<option value='Shitoko' >Shitoko</option>" +
				"						</select></dd>" +
				"" +				"				<dt>Accomodation Types:" +
				"					<dd><input type='hidden' name='type' value=''> " +
				"						<input id='campsite' type='checkbox' name='type' value='Campsite' checked >Campsite<br>" +
				"						<input id='minshuku' type='checkbox' name='type' value='Minshuku' checked >Minshuku<br>" +
				"						<input id='business' type='checkbox' name='type' value='Business Hotel' checked >Business Hotel<br>" +
				"						<input id='hostel' type='checkbox' name='type' value='Hostel' checked >Hostel<br>" +
				"						<input id='ryokan' type='checkbox' name='type' value='Japanese Inn' checked >Japanese Inn<br>" +
				"						<input id='fullhotel' type='checkbox' name='type' value='Full Hotel' checked 	>Full Hotel<br></dd>" +
				"" +
				"					<dt>Price:" +
				"					<dd><select name ='price'>" +
				"						<option value='All'>All</option>" +
				"						<option value='price1'>< &yen;5,000</option>" +
				"						<option value='price2' >&yen;5,000 ~ &yen;10,000</option>" +
				"						<option value='price3' >&yen;10,000 < </option>" +
				"						</select></dd>" +
				"			<div style='text-align:right;'><input type='submit' value='Search' /></div>" +
				"</form>" +
				"" +
				"" +
				"<form id=creditsform>" +
				"<div style='text-align:center;'><a href=>About this site</a></div>" +
				"<p>This site is maintained by Jenny at <a href=http://www.yakushimalife.com>Yakushima Life</a>. <br>Please " +
				"contact Jenny with any questions or comments." +
				"</form>" +
				"" +
				"</div>	" +
				"" +
				"" +
				"	<div id=centercontainer>" +
				"		<div id=results>" +
				"			<div id=dir>" +
				" 				<form accept-charset='utf-16' id=dirform action='/updatedir' method='post'>" +
				"					Yakushima >> " + location + " : <br><center><h3 style='font-size: 17px;'> " + hotelName + " (" + hotelName_j + ")</h3></center>" +
				"					" +
				"					<div id=tagline>Where ee i go sailing ishes in the sea.</div>" +
				"				</form>" +
				"			</div>" +
				"			<div id=photobox>" +
				"  			  <ul id='mycarousel' class='jcarousel-skin-tango'>" +
				"        <li class='jcarousel-item-1'><img src=" + image1Url + "  alt='' /></li>");
	if (!(image1Url.equals("null"))){
		pw.println("        <li class='jcarousel-item-2'><img src=" + image1Url + "  alt='' /></li>");
	}
	if (!(image2Url.contains("null"))){
		pw.println("        <li class='jcarousel-item-2'><img src=" + image2Url + "  alt='' /></li>");
	}
	if (!(image3Url.contains("null"))){
		pw.println("        <li class='jcarousel-item-2'><img src=" + image3Url + "  alt='' /></li>");
	}
	if (!(image4Url.contains("null"))){
		pw.println("        <li class='jcarousel-item-2'><img src=" + image4Url + "  alt='' /></li>");
	}
	if (!(image5Url.contains("null"))){
		pw.println("        <li class='jcarousel-item-2'><img src=" + image5Url + "  alt='' /></li>");
	}
	pw.println(	"      </ul>" +
				"" +
				"  <div id='mycarousel' class='jcarousel-skin-tango'>" +
				"    <div class='jcarousel-control'>" +
				"      <table id=photoboxcontrols><tr>");
	if (!(image1Url.contains("null"))){
		pw.println("   <td><a href='#'>1<br><img src=" + image1Url100 + " style='z-index:1;margin-left: auto; margin-right: auto;' /></a>");
	}
	if (!(image2Url.contains("null"))){
		pw.println("   <td><a href='#'>1<br><img src=" + image2Url100 + " style='z-index:1;margin-left: auto; margin-right: auto;' /></a>");
	}
	if (!(image3Url.contains("null"))){
		pw.println("   <td><a href='#'>1<br><img src=" + image3Url100 + " style='z-index:1;margin-left: auto; margin-right: auto;' /></a>");
	}
	if (!(image4Url.contains("null"))){
		pw.println("   <td><a href='#'>1<br><img src=" + image4Url100 + " style='z-index:1;margin-left: auto; margin-right: auto;' /></a>");
	}
	if (!(image5Url.contains("null"))){
		pw.println("   <td><a href='#'>1<br><img src=" + image5Url100 + " style='z-index:1;margin-left: auto; margin-right: auto;' /></a>");
	}
		pw.println("  </table>" +
				"    </div>" +
				"    </div>" +
				"" +
				"			</div>" +
				"			<div id=amenities>" +
				"				<form class=amenitiesform>");
	if (includedamenitiesstring!=""){
				pw.println("<h3>Amenities included in the price:</h3>" +	
				"					" + includedamenitiesstring + " <br>");
			}
			if (paidamenitiesstring!=""){
				pw.println("<h3>Amenities available at extra cost:</h3>" +
				"					" + paidamenitiesstring + " <br>");
			}
			if (inroomamenitiesstring!=""){
				pw.println("<h3>Inroom Facilities:</h3>" +
				"					" + inroomamenitiesstring + " <br>");
			}	
			if (sharedamenitiesstring!=""){
				pw.println("<h3>Communal/Shared Facilities:</h3>" +
				"					" + sharedamenitiesstring + " <br>");
			}
			if (mealsstring!=""){
				pw.println("<p><h3>Meal Plans:</h3>" +
				"			" + mealsstring + " <br>");
			}
			if (welcomingstring!=""){
			pw.println(welcomingstring );
				}
			if (smokingstring!=""){
					pw.println("<h3>Smoking:</h3>" +
				"					" + smokingstring + " <br>");
				}
			if (closestring!=""){
				pw.println("<p><h3> Area:</h3>" +
				"					" + closestring + " <br>");
			}
			if (acceptsstring!=""){
				pw.println("<p><h3>Payment can be made by:</h3>" +
				"					" + acceptsstring + " <br>");
			}
			if (speaksstring!=""){
				pw.println("<p><h3>Assistance is available in:</h3>" +
				"					" + speaksstring + " <br>");
			}
			pw.println(	"				</form>" +
				"			</div>" +
				"			<div id=outline></div>" +
				"		</div>" +
				"	</div>" +
				"	<div id=basics>" +
				"		<form id=basicsform action='/updatebasics' accept-charset='utf-8' method='post'>" +
				"			<input type=hidden name=hotelName value=" + hotelName + ">" +
				"			<div id=hotellogo><img src=" + imagelogoUrl196 + " /></div>" +
				"			<dl>");
			
			if (location!=""){
						pw.println("<dt>Town: " + location);
					}
			if (telephone!=""){
						pw.println("<dt>TEL: " + telephone);
					}
			if (mobile!=""){
						pw.println("<dt>MOBILE: " + mobile);
					}
			if (address!=""){
						pw.println("</dd><dt>Address:" +
				"					<dd>	" + address +"</dd>");
					}
			if (address_j!=""){
						pw.println("<dt>Address in Japanese:" +
				"					<dd>	" + address_j +"</dd>");
					}
			if (homepagelink!=""){
						pw.println("</dd><dt>Homepage:" +
				"					<dd><a href=" + homepagelink + ">" + homepagelink + "</a></dd>");
					}
			if (pricemin!=""){
						pw.println("<dt>Price: " +
				"					<dd>Åè" + pricemin + " ~ Åè" + pricemax + "</dd>");
					}
			if (guestsmax!=""){
						pw.println("<dt>Maximum Guests: " + guestsmax);
					}
			if (typestring!=""){
						pw.println("<dt>Accomodation Types:" +
				"					<dd>" + typestring);
					}
				pw.println("				<br><dt>Checkin/out:" +
				"					<dd>" + checkin + " / " +
				"						" + checkout + "</dd>" +
				"			</dl>" +
				"		</form>" +
				"		" + mapcode +
				"	</div>" +
				"</div>");
	pw.println("</BODY>");
	pw.println("</HTML>");
	pw.close();
	}
	
	
	
    private static String Filternulls(Entity hotel, String message) {
    	String y = (String) hotel.getProperty(message);
    	if (y == null | y == "null")
    	{y="";}
    	return y;
    	
    	
    }



}


