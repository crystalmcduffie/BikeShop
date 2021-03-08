package com.revature.data;

import java.util.Set;

import com.revature.beans.Offer;

public interface GenericDAO<T> {
	// CRUD operations (create, read, update, delete)
	//public boolean add(T t)l
	public T getById(Integer id);
	public Set<T> getAll();
	public boolean update(T t);
	public boolean delete(T t);
}
