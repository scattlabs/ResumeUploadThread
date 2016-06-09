package com.config;

import java.util.List;

public interface GenericDao<Entity> {

	public Entity find(int id);

	public List<Entity> list(String orderColumns);

	public List<Entity> list(String orderColumns, String criteria);

	public List<Entity> list(String orderColumns, String criteria, int firstResult, int maxResults);

	public void save(Entity object);

	public void delete(int id);

	public Entity findBy(String value, String column);

	public boolean check(String value, String column);

}
