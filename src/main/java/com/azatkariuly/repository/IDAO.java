package com.azatkariuly.repository;

import java.util.List;

public interface IDAO<T> {
	public List<T> getList() throws Exception;
	public void add(T o) throws Exception;
	public void update(T o) throws Exception;
	public void delete(String id) throws Exception;
	public T get(int id) throws Exception;
	public String getMaxID() throws Exception;
	public int getIdByAbbr(String abbreviation) throws Exception;
}
