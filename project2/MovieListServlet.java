package project2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import javax.servlet.RequestDispatcher;
//import org.apache.catalina.servlet4preview.RequestDispatcher;

/**
 * Servlet implementation class MovieListServlet
 */
@WebServlet("/MovieListServlet")
@MultipartConfig
public class MovieListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MoviedbUtil moviedbUtil;
	@Resource(name="jdbc/moviedb")
	private DataSource dataSource;
	private HttpSession session;
	
	
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
		try {
			try {
				listMovies(request, response);
			} catch (SQLException e) {
				// clicking on the "continue shopping" link when the list of movies is empty
				RequestDispatcher dispatcher =  request.getRequestDispatcher("/movie-list.jsp");
				dispatcher.forward(request, response);
			}
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
	}



	@SuppressWarnings("unchecked")
	private void listMovies(HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		session = request.getSession();

		Integer limit = null;
		Integer page = null;
		Integer maxPage = null;
		String sort = null;
		String order = null;
		String browseTitle = null;
		String browseGenre = null;
		String titleSort = null;
		String genreName = null;
		List<Movie> newMovieList = new ArrayList<Movie>();
		List<Movie> movies = new ArrayList<Movie>();

		// determines if the user is currently browsing or searching
		
		String prevMethod = null;
		if (session.getAttribute("METHOD") != null)
			prevMethod = (String) session.getAttribute("METHOD");
		
		String currMethod = request.getParameter("method");
		if (currMethod != null)
			session.setAttribute("METHOD", currMethod);
		
		boolean switchedMethod = false;
		if (currMethod != null && prevMethod != null && !currMethod.equalsIgnoreCase(prevMethod))
			switchedMethod = true;
		
		boolean search = false;
		boolean browse = false;
		if (currMethod != null && currMethod.equalsIgnoreCase("search") || 
				prevMethod != null && prevMethod.equalsIgnoreCase("search")) {
			search = true;
		}
		else if (currMethod != null && currMethod.equalsIgnoreCase("browse") || 
				prevMethod != null && prevMethod.equalsIgnoreCase("browse")) {
			browse = true;
		}
		
		
		if(request.getParameter("genreName") != null) {
			genreName = request.getParameter("genreName");
			session.setAttribute("browseGenre", genreName );
			//Sets the browseTitle to null to keep genre browsing
			session.removeAttribute("browseTitle"); 
			
		}
		else if(request.getParameter("titleSort")!= null) {
			titleSort = request.getParameter("titleSort");
			session.setAttribute("browseTitle",titleSort);
			//Sets the browseGenre to null to keep title browsing
			session.removeAttribute("browseGenre"); 
		}
		
		if (switchedMethod)
			limit = 10;
		else if ( request.getParameter("limit") != null) {
			limit = Integer.parseInt(request.getParameter("limit"));
			session.setAttribute("limit",limit);
		}
		else if(session.getAttribute("limit") != null)
			limit = (Integer) session.getAttribute("limit");
		else
			limit = 10;
		
		
		if ( request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
			session.setAttribute("page",page);
		}
		else {
			page = 1;
			session.setAttribute("page",page);		
		}
		
		browseTitle = (String) session.getAttribute("browseTitle");
		browseGenre = (String) session.getAttribute("browseGenre");
		
		
		/* movie list page generation below */
		
		// If browsed by title or genre create a new sql query otherwise
		// get movie list from session
		
		
		//TODO: debug transitions between search/browse pages

		
		
		if(titleSort !=  null || genreName != null) {
			// Browse.jsp
			movies = moviedbUtil.getMovies(sort, order, limit, page,  browseTitle, browseGenre);
			session.setAttribute("MOVIES_FROM_BROWSE", movies);
		}
		
		else if (search && session.getAttribute("MOVIES_FROM_SEARCH") != null) {
			// search-view.jsp with data from FabflixControllerServlet
			movies = (List<Movie>) session.getAttribute("MOVIES_FROM_SEARCH");
		}
		
		else if(browse && session.getAttribute("MOVIES_FROM_BROWSE") != null) {
			// executes in subsequent pages during pagination
			movies = (List<Movie>) session.getAttribute("MOVIES_FROM_BROWSE");
		}
		
		else {
			// Browse.jsp
			movies = moviedbUtil.getMovies(sort, order, limit, page,  browseTitle, browseGenre);
			session.setAttribute("MOVIES_FROM_BROWSE", movies);
		}
		
		if(request.getAttribute("maxPage") != null){
			
		}
		else{
			double movieListSize = movies.size();
			maxPage = (int) Math.ceil(movieListSize / limit);
		}
		
		
		
		if(request.getParameter("sort")!= null && request.getParameter("order") !=null){
		
			sort = request.getParameter("sort");
			order = request.getParameter("order");
			session.setAttribute("sort", sort);
			session.setAttribute("order", order);
		}
		else if (session.getAttribute("sort") != null && session.getAttribute("order") !=null){
			sort = (String) session.getAttribute("sort");
			order = (String) session.getAttribute("order");
		}
		
		
	    if (sort!=null && order!=null ){
            if (sort.equals("year") && order.equals("ASC")) {
            	Collections.sort(movies, new Comparator<Movie>() {
            		@Override public int compare(Movie o1, Movie o2) {
	            return o1.getYear() - o2.getYear(); // Ascending
	        }
	        
            	});	
            }
            
            else if (sort.equals("year") && order.equals("DESC")) {
            	Collections.sort(movies, new Comparator<Movie>() {
            		@Override public int compare(Movie o1, Movie o2) {
	            return o2.getYear() - o1.getYear(); // Ascending
	        }
	        
            	});	
            }
            
            else if (sort.equals("title") && order.equals("ASC")) {
            	Collections.sort(movies, new Comparator<Movie>() {
            		@Override public int compare(Movie o1, Movie o2) {
	            return o1.getTitle().compareTo(o2.getTitle());// Ascending
	        }
	        
            	});	
            }
            
            else if (sort.equals("title") && order.equals("DESC")) {
            	Collections.sort(movies, new Comparator<Movie>() {
            		@Override public int compare(Movie o1, Movie o2) {
	            return o2.getTitle().compareTo(o1.getTitle());// Ascending
	        }
	        
            	});	
            }
	    }
            	
	    newMovieList = new ArrayList<Movie>();
		int offset = (page - 1) * limit;
		
		//pagination to select n movies from movies
		for(int i = 0; i < limit && offset < movies.size() ; i++) {
			if (movies.get(offset) != null){
				newMovieList.add(movies.get(offset));
				offset++;
			}
		}
		request.setAttribute("MOVIES_PER_PAGE", newMovieList);
		request.setAttribute("maxPage", maxPage);
		RequestDispatcher dispatcher =  request.getRequestDispatcher("/movie-list.jsp");
		dispatcher.forward(request, response);
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	

}
