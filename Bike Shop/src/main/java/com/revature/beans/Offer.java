package com.revature.beans;

public class Offer {
	
	private Integer offerId;
	private Double bid;
	
	public Integer getOfferId() {
		return offerId;
	}
	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}
	public Double getBid() {
		return bid;
	}
	public void setBid(Double bid) {
		this.bid = bid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bid == null) ? 0 : bid.hashCode());
		result = prime * result + ((offerId == null) ? 0 : offerId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Offer other = (Offer) obj;
		if (bid == null) {
			if (other.bid != null)
				return false;
		} else if (!bid.equals(other.bid))
			return false;
		if (offerId == null) {
			if (other.offerId != null)
				return false;
		} else if (!offerId.equals(other.offerId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Offer [offerId=" + offerId + ", bid=" + bid + "]";
	}
	
}