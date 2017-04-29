// Brian Huynh, Daniel Nguyen
package project2;

import java.util.List;

public class Movie {
	private int id;
	private String title;
	private int year;
	private String director;
	private String bannerUrl;
	private String trailerUrl;
	private List<Genre> genres;
	private List<Star> stars;
	private int quantity = 0;		// number of movies purchased
	
	public Movie(int id, String title, int year, 
				String director, String bannerUrl, String trailerUrl, 
				List<Genre> genres, List<Star> stars) {
		
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.bannerUrl = bannerUrl;
		this.trailerUrl = trailerUrl;
		this.genres = genres;
		this.stars = stars;

	}

	public Movie(int id, String title, String director, int year, 
				List<Genre> genres, List<Star> stars) {
		
		this.id = id;
		this.title = title;
		this.director = director;
		this.year = year;
		this.genres = genres;
		this.stars = stars;
		
	}
	public Movie(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public Movie() {
		
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getbannerUrl() {
		return bannerUrl;
	}

	public void setbannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String gettrailerUrl() {
		return trailerUrl;
	}

	public void settrailerUrl(String trailerUrl) {
		this.trailerUrl = trailerUrl;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Star> getstars() {
		return stars;
	}

	public void setstars(List<Star> stars) {
		this.stars = stars;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


}
