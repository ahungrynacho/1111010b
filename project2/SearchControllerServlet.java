package project2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private SearchModelJdbc database;

	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			database = new SearchModelJdbc();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String name = request.getParameter("name");
		try {
			ArrayList<Movie> movies = database.getMovies(title, year, director, name);
			request.setAttribute("MOVIES", movies);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/search-view.jsp");
			dispatcher.forward(request, response);
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
