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
	private Movie currentMovie;
	private ArrayList<Movie> shoppingCart;
	private Customer customer;
	private HttpSession session;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			this.database = new FabflixModelJdbc(dataSource);
			this.movies = new ArrayList<Movie>();
			this.currentMovie = null;
			this.shoppingCart = new ArrayList<Movie>();
			this.customer = null;
			this.session = null;

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
	
	private void listMovies(HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		/* Sends the currently cached list of movies to the jsp. */
		
		session.setAttribute("MOVIES", this.movies);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/search-view.jsp");
		dispatcher.forward(request, response);
	}
	
	private void searchMovies(HttpServletRequest request, 
							HttpServletResponse response) throws Exception{
		/* 
		 * Get user input from the html form with multiple input 
		 * fields and execute a query. Populate a list of movie 
		 * objects with each matching record and send the list 
		 * back to the jsp page.
		 */
		
		String title = request.getParameter("title");
		String year = request.getParameter("year");
		String director = request.getParameter("director");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");

		this.movies = database.searchMovies(title, year, 
											director, firstName, 
											lastName);
		
		session.setAttribute("MOVIES", this.movies);
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
		session.setAttribute("MOVIES", this.movies);
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
	
	@SuppressWarnings("unchecked")
	private void processPayment(HttpServletRequest request, 
								HttpServletResponse response) throws Exception {
		
		/*
		 * Updates the sales table in the database for every movie purchased.
		 * Redirects the user to the confirmation page if successful; otherwise
		 * updates the current payment page with a invalid credit card error message.
		 */
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String ccid = request.getParameter("ccid");
		String expDate = request.getParameter("expDate");
		
		this.shoppingCart = (ArrayList<Movie>) session.getAttribute("SHOPPING_CART");
		RequestDispatcher dispatcher = null;
		
		if (database.processPayment(firstName, lastName, ccid, expDate) && 
				this.shoppingCart != null) {
			
			
			/* NullPointerException thrown because 
			 * the customer login information has 
			 * not been recorded yet in the session. */
			
			// Customer customer = (Customer) session.getAttribute("CUSTOMER");
			for (Movie m : this.shoppingCart) {
				database.addSale(customer.getId(), m.getId(), currentDate());
			}
			dispatcher = request.getRequestDispatcher("/confirmation-view.jsp");
			dispatcher.forward(request, response);
			
		}
		else {
			request.setAttribute("FAIL", true);
			dispatcher = request.getRequestDispatcher("/checkout-view.jsp");
			dispatcher.forward(request, response);
		}
		

	}

	private void linkToMovie(HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		
		/* 
		 * Sets the movie session attribute to a Movie object currently being
		 * viewed in the single movie page.
		 */
		String movieId = request.getParameter("movieId");
		this.currentMovie = database.getMovie(movieId);
		
		if (this.currentMovie != null) {
			session.setAttribute("MOVIE", this.currentMovie);
			RequestDispatcher dispatcher = request.getRequestDispatcher("movie-view.jsp");
			dispatcher.forward(request, response);
		}
		
	}
	
	private void addToCart(HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		/* Adds a movie to the shopping cart with the user-specified quantity. */
		
		String quantity = request.getParameter("quantity");
		this.currentMovie = (Movie) session.getAttribute("MOVIE");
		
		if (quantity != null && Integer.parseInt(quantity) > 0 
				&& this.currentMovie != null) {
			
			this.currentMovie.setQuantity(Integer.parseInt(quantity));
			this.shoppingCart.add(this.currentMovie);
			session.setAttribute("SHOPPING_CART", this.shoppingCart);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("shopping-cart-view.jsp");
			dispatcher.forward(request, response);
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void updateCart(HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		/*
		 * Updates the quantity of each movie in the shopping cart get getting
		 * the new quantity from the user's input. A quantity of 0 removes movies
		 * from the shopping cart.
		 */
		this.shoppingCart = (ArrayList<Movie>) session.getAttribute("SHOPPING_CART");
		ArrayList<Movie> toRemove = new ArrayList<Movie>();
		
		for (Movie m : this.shoppingCart) {
			String movieId = Integer.toString(m.getId());
			int quantity = Integer.parseInt(request.getParameter(movieId));
	
			if (quantity == 0)
				toRemove.add(m);
			
			else if (quantity > 0)
				m.setQuantity(quantity);
		}
		
		for (Movie m : toRemove) {		// removing movies afterwards prevents ConcurrentModificationException
			this.shoppingCart.remove(m);
		}
		
		session.setAttribute("SHOPPING_CART", this.shoppingCart);
		RequestDispatcher dispatcher = request.getRequestDispatcher("shopping-cart-view.jsp");
		dispatcher.forward(request, response);
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		session = request.getSession();
		String command = request.getParameter("command");
		
		if (command == null)
			command = "listMovies";
		
		try {
			switch(command) {
			
			case "listMovies":
				listMovies(request, response);
				break;
			
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
				
			case "update":
				updateCart(request, response);
				break;

			case "linkToJsp":
				break;		// dummy value for buttons that act like href links
				
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
				
			default:
				break;
				
			}
				
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	
}

