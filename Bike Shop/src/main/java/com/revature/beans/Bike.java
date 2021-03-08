package com.revature.beans;

import java.util.Set;

public class Bike {
	private Integer bikeId;
	private String model;
	private String brand;
	private String color;
	private Status status;
	
	private Set<Offer> offers;
	
	public Set<Offer> getOffers() {
		return offers;
	}
	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}
	public Integer getBikeId() {
		return bikeId;
	}
	public void setBikeId(Integer bikeId) {
		this.bikeId = bikeId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((offers == null) ? 0 : offers.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Bike other = (Bike) obj;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (offers == null) {
			if (other.offers != null)
				return false;
		} else if (!offers.equals(other.offers))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Bike [bikeId=" + bikeId + ", model=" + model + ", brand=" + brand + ", color=" + color + ", status="
				+ status + ", offers=" + offers + "]";
	}
	
	public String toStringBike() {
		return "Bike [bikeId=" + bikeId + ", model=" + model + ", brand=" + brand + ", color=" + color + "]";
	}
}
