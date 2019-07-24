package daos;

import java.util.List;

public interface DAOInterface {
	List<?> getList() throws Exception;
	void add(Object obj) throws Exception;
	void update(Object obj) throws Exception;
	void delete(String id) throws Exception;
	Object get(int id) throws Exception;
	String getMaxID() throws Exception;
	int getIdByAbbr(String abbreviation) throws Exception;
}
