// Brian Huynh
package project2;

public class Sale {
	private int id;
	private int customerId;
	private int movieId;
	private String saleDate;
	
	public Sale(int id, int customerId, int movieId, String saleDate) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.movieId = movieId;
		this.saleDate = saleDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	
	
}
