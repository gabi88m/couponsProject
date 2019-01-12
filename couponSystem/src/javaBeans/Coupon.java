package javaBeans;

import java.time.LocalDate;

public class Coupon {
	private long id;
	private String title;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long amount;
	private CouponType couponType;
	private String message;
	private double price;
	private String image;

	public enum CouponType {
		RESTURANS, ELECTRICITY, FOOD, HEALTH, SPORTS, CAMPING, TRAVELLING
	}// ENUM - CouponType

	public Coupon() {

	}// ctor

	public Coupon(long id, String title, LocalDate startDate, LocalDate endDate, long amount, String couponType,
			String message, double price, String image) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.couponType = CouponType.valueOf(couponType);
		this.message = message;
		this.price = price;
		this.image = image;
	}// ctor

	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public CouponType getCouponType() {
		return couponType;
	}

	public void setCouponType(CouponType couponType) {
		this.couponType = couponType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	// end of getters and setters

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", amount=" + amount + ", couponType=" + couponType + ", message=" + message + ", price=" + price
				+ ", image=" + image + "]\n";
	}// toString

}
