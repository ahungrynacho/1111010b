// Brian Huynh
package project2;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

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
	
	private List<Movie> movies;
	private List<Movie> shoppingCart;
	private Customer customer;
	private HttpSession session;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			this.database = new FabflixModelJdbc(dataSource);
			this.movies = new ArrayList<Movie>();
			this.shoppingCart = new ArrayList<Movie>();
			this.customer = null;
			this.session = null;

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	private void initLoginPage(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		/* Upon server startup, redirects user to the login page. */
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}
	
	private void login(HttpServletRequest request, 
					HttpServletResponse response) throws Exception {
		/* Temporarily implemented login function. */
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		this.customer = database.login(email, password);
		
		if(this.customer != null)
		{
			//maintain session id and store it somewhere
			//replace validation.jsp to the next real state (main page)
			session.setAttribute("CUSTOMER", this.customer);
			session.setAttribute("MOVIES_FROM_QUERY", this.movies);
			session.setAttribute("SHOPPING_CART", this.shoppingCart);
			
			RequestDispatcher dispatcher =  request.getRequestDispatcher("main-page.jsp");
			dispatcher.forward(request, response);
		}
		else
		{
			request.setAttribute("FAIL", true);
			RequestDispatcher dispatcher =  request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);	
		}
		
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
		
		session.setAttribute("MOVIES_FROM_SEARCH", this.movies);
		RequestDispatcher dispatcher = request.getRequestDispatcher("MovieListServlet");
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
		session.setAttribute("MOVIES_FROM_SEARCH", this.movies);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/search-view.jsp");
		dispatcher.forward(request, response);
	}
	
	public static String currentDate() {
		/* Converts java's current date to sql's date format. */
		String javaDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		return javaDate.replace("-", "/");
	}
	
	@SuppressWarnings("unchecked")
	private void clearShoppingCart(HttpServletRequest request, HttpServletResponse response) {
		
		this.shoppingCart = (List<Movie>) session.getAttribute("SHOPPING_CART");
		this.shoppingCart.clear();
		session.setAttribute("SHOPPING_CART", this.shoppingCart);
		
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
		
		this.shoppingCart = (List<Movie>) session.getAttribute("SHOPPING_CART");
		RequestDispatcher dispatcher = null;
		
		if (this.shoppingCart != null && this.shoppingCart.size() == 0) {
			request.setAttribute("EMPTY_CART", true);
			dispatcher = request.getRequestDispatcher("/checkout-view.jsp");
			dispatcher.forward(request, response);
		}
		else if (database.processPayment(firstName, lastName, ccid, expDate) && 
				this.shoppingCart != null && this.shoppingCart.size() > 0) {
			
			Customer customer = (Customer) session.getAttribute("CUSTOMER");
			for (Movie m : this.shoppingCart) {
				database.addSale(customer.getId(), m.getId(), currentDate());
			}
			
			List<Sale> sales = database.getSales(customer.getId());
			session.setAttribute("BOUGHT_BY_CUSTOMER", sales);		// transactions made by the logged-in customer
			
			dispatcher = request.getRequestDispatcher("/confirmation-view.jsp");
			dispatcher.forward(request, response);
			
		}
		else {
			request.setAttribute("DECLINED_CARD", true);
			dispatcher = request.getRequestDispatcher("/checkout-view.jsp");
			dispatcher.forward(request, response);
		}
		

	}
	
	private boolean inShoppingCart(List<Movie> cart, int movieId) {
		for (Movie m : cart) {
			if (m.getId() == movieId)
				return true;
		}
		return false;
	}
	
	private void addQuantity(List<Movie> cart, int movieId, int amount) {
		for (Movie m : cart) {
			if (m.getId() == movieId) {
				int newQuant = m.getQuantity() + amount;
				m.setQuantity(newQuant);
			}
		}
	}
	
	private void setQuantity(List<Movie> cart, int movieId, int amount) {
		for (Movie m : cart) {
			if (m.getId() == movieId) {
				m.setQuantity(amount);
				return;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addToCart(HttpServletRequest request, 
							HttpServletResponse response) throws Exception {
		/* Adds a movie to the shopping cart with the user-specified quantity. */

		this.shoppingCart = (List<Movie>) session.getAttribute("SHOPPING_CART");
		int quantity = 0;
		if (request.getParameter("quantity") != null)
			quantity = Integer.parseInt(request.getParameter("quantity"));
		
		
		try {
			if (request.getParameter("quantity") == null) {
				// button clicked from movie list
				
				String movieId = request.getParameter("movieId");
				Movie movie = null;
				
				if (inShoppingCart(this.shoppingCart, Integer.parseInt(movieId))) {
					addQuantity(this.shoppingCart, Integer.parseInt(movieId), 1);
				} else {
					movie = database.getMovie(movieId);
					movie.setQuantity(1);
					this.shoppingCart.add(movie);
				}
				
				session.setAttribute("SHOPPING_CART", this.shoppingCart);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("shopping-cart-view.jsp");
				dispatcher.forward(request, response);			
			}
			
			else if (quantity > 0 ) {
				// button clicked from single-movie page

				String movieId = request.getParameter("movieId");
				
				if (inShoppingCart(this.shoppingCart, Integer.parseInt(movieId))) {
					addQuantity(this.shoppingCart, Integer.parseInt(movieId), quantity);
				}
				else {
					Movie movie = database.getMovie(movieId);
					movie.setQuantity(quantity);
					this.shoppingCart.add(movie);
				}
					
				session.setAttribute("SHOPPING_CART", this.shoppingCart);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("shopping-cart-view.jsp");
				dispatcher.forward(request, response);
			}
			else {
				// button clicked from single-movie page, but with invalid input
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			// button clicked from single-movie page, but with invalid input
			request.setAttribute("ERROR_MSG", true);
			RequestDispatcher dispatcher = request.getRequestDispatcher("SingleMovieServlet");
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
		this.shoppingCart = (List<Movie>) session.getAttribute("SHOPPING_CART");
		List<Movie> toRemove = new ArrayList<Movie>();
		
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
			command = "initLoginPage";
		
		try {
			switch(command) {
			
			case "initLoginPage":
				initLoginPage(request, response);
				break;
			
			case "searchByFields":
				searchMovies(request, response);
				break;
				
			case "searchByKeywords":
				searchByKeywords(request, response);
				break;
				
			case "addToCart":
				addToCart(request, response);
				break;
				
			case "update":
				updateCart(request, response);
				break;
				
			case "clearShoppingCart":
				clearShoppingCart(request, response);
				break;
				
			default:
				break;		// do nothing
			}
			
		} catch (Exception e) {
			// if all else fails, go to the home page
			e.printStackTrace();
//			RequestDispatcher dispatcher =  request.getRequestDispatcher("main-page.jsp");
//			dispatcher.forward(request, response);
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
			e.printStackTrace();
		}
	}

}

