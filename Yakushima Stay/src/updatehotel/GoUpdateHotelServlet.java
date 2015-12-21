package updatehotel;


import java.io.IOException;
import javax.servlet.http.*;


public class GoUpdateHotelServlet extends HttpServlet {
	/**
	 * Send users to Manager and Administrator update pages
	 * for individual hotels. Does not check user login status!
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String hotelKeyString = req.getParameter("hotelKeyString");
		String permissions = req.getParameter("permissions");
		String language = req.getParameter("language");
		if (permissions==null)
			if (language==null)
				resp.sendRedirect("/updatehotel?hotelKeyString=" + hotelKeyString);
			else
				resp.sendRedirect("/updatehotelj?hotelKeyString=" + hotelKeyString);
		else{
			if (permissions.equals("administrator")){
				resp.sendRedirect("/updatehoteladmin?hotelKeyString=" + hotelKeyString);}
		}		
	}
}


