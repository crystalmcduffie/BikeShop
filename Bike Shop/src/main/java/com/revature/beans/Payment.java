package com.revature.beans;

public class Payment {
	
	private Integer paymentId;
	private Double totalCost;
	private Double amountPaid;
	private Bike bike;
	
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}
	public Bike getBike() {
		return bike;
	}
	public void setBike(Bike bike) {
		this.bike = bike;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountPaid == null) ? 0 : amountPaid.hashCode());
		result = prime * result + ((bike == null) ? 0 : bike.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((totalCost == null) ? 0 : totalCost.hashCode());
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
		Payment other = (Payment) obj;
		if (amountPaid == null) {
			if (other.amountPaid != null)
				return false;
		} else if (!amountPaid.equals(other.amountPaid))
			return false;
		if (bike == null) {
			if (other.bike != null)
				return false;
		} else if (!bike.equals(other.bike))
			return false;
		if (paymentId == null) {
			if (other.paymentId != null)
				return false;
		} else if (!paymentId.equals(other.paymentId))
			return false;
		if (totalCost == null) {
			if (other.totalCost != null)
				return false;
		} else if (!totalCost.equals(other.totalCost))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Payment [paymentId=" + paymentId + ", totalCost=" + totalCost + ", amountPaid=" + amountPaid + ", bike="
				+ bike + "]";
	}
	
}
