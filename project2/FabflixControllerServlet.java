package project2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/FabflixControllerServlet")
public class FabflixControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private FabflixModelJdbc database;
	@Resource(name="jdbc/moviedb") 		// Critical for connecting the SQL database and tomcat in eclipse.
	private DataSource dataSource;
	private ArrayList<Movie> movies;
	private ArrayList<Movie> shoppingCart;
	private Customer customer;
	private HttpSession session;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			database = new FabflixModelJdbc(dataSource);
			shoppingCart = new ArrayList<Movie>();

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	private void login(HttpServletRequest request, 
					HttpServletResponse response) throws Exception {
		/* Temporarily implemented login function. */
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		this.customer = database.login(email, password);
		session.setAttribute("CUSTOMER", this.customer);
		
		// TODO: where to redirect page, if needed at all?
		RequestDispatcher dispatcher = request.getRequestDispatcher("main-page-view.jsp");
		dispatcher.forward(request, response);
		
	}
	
	private void searchMovies(HttpServletRequest request, 
							HttpServletResponse response) throws Exception{
		/* 
		 * Get user input from the html form with multiple input 
		 * fields and execute a query. Populate a list of movie 
		 * objects with each matching record and send the list 
		 * back to the html page.
		 */
		
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");

		this.movies = database.searchMovies(title, year, 
													director, firstName, 
													lastName);
		request.setAttribute("MOVIES", movies);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/search-view.jsp");
		dispatcher.forward(request, response);

	}
	
	private void searchByKeywords(HttpServletRequest request, 
								HttpServletResponse response) throws Exception {
		/*
		 * Get user input from the html form with a single input field
		 * and execute a query. Populate a list of movie objects with
		 * each matching record and send the list back to the html page.
		 */
		String keywords = request.getParameter("keywords");
		this.movies = database.moviesByKeywords(keywords);
		request.setAttribute("MOVIES", movies);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/search-view.jsp");
		dispatcher.forward(request, response);
	}
	
	private String currentDate() {
		/* Converts java's current date to sql's date format. */
		
		int day = LocalDateTime.now().getDayOfMonth();
		int month = LocalDateTime.now().getMonthValue();
		int year = LocalDateTime.now().getYear();
		return String.format("'%d/%d/%d'", year, month, day);
	}
	
	private void processPayment(HttpServletRequest request, 
								HttpServletResponse response) throws Exception {
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String ccid = request.getParameter("ccid");
		String expDate = request.getParameter("expDate");
		
		if (database.processPayment(firstName, lastName, ccid, expDate)) {
			request.setAttribute("SUCCESS", true);

			for (Movie m : this.shoppingCart) {
				database.addSale(customer.getId(), m.getId(), currentDate());
			}
			
		}
		else {
			request.setAttribute("SUCCESS", false);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/confirmation-view.jsp");
		dispatcher.forward(request, response);
	}

	private void linkToMovie(HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		String movieId = request.getParameter("movieId");
		Movie movie = database.getMovie(movieId);
		
		if (movie != null) {
			request.setAttribute("MOVIE", movie);
			RequestDispatcher dispatcher = request.getRequestDispatcher("movie-view.jsp");
			dispatcher.forward(request, response);
		}
		
	}
	
	private void addToCart(HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		/* Adds a movie to the shopping cart with the user-specified quantity. */
		// TODO: implement JSP sessions to save each shopping cart 
		// between pages for each user
		
		String quantity = request.getParameter("quantity");
		String movieId = request.getParameter("movieId");
		Movie movie = database.getMovie(movieId);
		
		if (quantity != null && movie != null) {
			movie.setQuantity(Integer.parseInt(quantity));
			this.shoppingCart.add(movie);
			session.setAttribute("SHOPPING_CART", this.shoppingCart);
			RequestDispatcher dispatcher = request.getRequestDispatcher("shopping-cart-view.jsp");
			dispatcher.forward(request, response);
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		session = request.getSession();
		String command = request.getParameter("command");
		
		if (command == null)
			command = "searchByFields";
		
		try {
			switch(command) {
			
			case "searchByFields":
				searchMovies(request, response);
				break;
				
			case "searchByKeywords":
				searchByKeywords(request, response);
				break;
				
			case "linkToMovie":
				linkToMovie(request, response);
				break;
				
			case "addToCart":
				addToCart(request, response);
				break;

			default:
				break;		// do nothing
			}
			
		} catch (Exception e) {
			throw new ServletException();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		session = request.getSession();
		String command = request.getParameter("command");
		
		try {
			switch(command) {
			
			case "login":
				login(request, response);
				break;
			
			case "creditCardInfo":
				processPayment(request, response);
				break;
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	
}

