package project2;

import java.util.ArrayList;

public class Movie {
	private int id;
	private String title;
	private int year;
	private String director;
	private String bannerUrl;
	private String trailerUrl;
	private ArrayList<Genre> genres;
	private ArrayList<Star> stars;
	private int quantity = 0;		// number of movies purchased
	
	public Movie(int id, String title, int year, 
			String director, String bannerUrl, String trailerUrl, ArrayList<Genre> genres, ArrayList<Star> stars) {
		
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.bannerUrl = bannerUrl;
		this.trailerUrl = trailerUrl;
		this.genres = genres;
		this.stars = stars;

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

	public ArrayList<Genre> getGenres() {
		return genres;
	}

	public void setGenres(ArrayList<Genre> genres) {
		this.genres = genres;
	}

	public ArrayList<Star> getstars() {
		return stars;
	}

	public void setstars(ArrayList<Star> stars) {
		this.stars = stars;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


}
