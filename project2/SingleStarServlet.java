package project2;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class SingleStarServlet
 */
@WebServlet("/SingleStarServlet")
public class SingleStarServlet extends HttpServlet {
	private MoviedbUtil moviedbUtil;
	@Resource(name="jdbc/moviedb")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			moviedbUtil = new MoviedbUtil(dataSource);
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			listSingleStar(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void listSingleStar(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String starId = request.getParameter("starId");
		Star star = moviedbUtil.getSingleStar(starId);
		
		request.setAttribute("STAR", star);
		RequestDispatcher dispatcher =  request.getRequestDispatcher("/SingleStar.jsp");
		dispatcher.forward(request, response);
	}

	
	
	
}
