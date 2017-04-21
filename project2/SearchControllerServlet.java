package project2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchControllerServlet")
public class SearchControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private SearchModelJdbc database;
	@Resource(name="jdbc/moviedb") 		// Critical for connecting the SQL database and tomcat in eclipse.
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			database = new SearchModelJdbc(dataSource);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	private void searchMovies(HttpServletRequest request, HttpServletResponse response) throws Exception{
		/* 
		 * Get user input from the html form and execute a query. 
		 * Populate a list of movie objects with each matching record 
		 * and send the list back to the html page.
		 */
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");

		ArrayList<Movie> movies = database.searchMovies(title, year, 
													director, firstName, 
													lastName);
		request.setAttribute("movies", movies);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/search-view.jsp");
		dispatcher.forward(request, response);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		PrintWriter out = response.getWriter();
//		response.setContentType("text/plain");	
		
		try {
			searchMovies(request, response);	
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
