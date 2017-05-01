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
 * Servlet implementation class SingleMovieServlet
 */
@WebServlet("/SingleMovieServlet")
public class SingleMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
			listSingleMovie(request,response);
		} catch (Exception e) {
			// if all else fails, go to the home page
			RequestDispatcher dispatcher =  request.getRequestDispatcher("main-page.jsp");
			dispatcher.forward(request, response);
		}
	}

	private void listSingleMovie(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String movieId = request.getParameter("movieId");
		Movie movie = moviedbUtil.getSingleMovie(movieId);
		
		request.setAttribute("MOVIE", movie);
		RequestDispatcher dispatcher =  request.getRequestDispatcher("/SingleMovie.jsp");
		dispatcher.forward(request, response);

		
		
	}

	
	
}
