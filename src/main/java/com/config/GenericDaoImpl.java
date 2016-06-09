package com.config;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.hibernate.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class GenericDaoImpl<Entity> implements GenericDao<Entity> {

	private final Class<Entity> clazz;

	@Autowired
	protected SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		clazz = (Class<Entity>) pt.getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Entity> list(String orderColumns) {
		List<Entity> savings;

		if (orderColumns.equals("")) {
			savings = sessionFactory.getCurrentSession().createQuery(" from " + clazz.getName()).list();
		} else {
			savings = sessionFactory.getCurrentSession()
					.createQuery(" from " + clazz.getName() + " order by " + orderColumns).list();

		}

		return savings;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Entity> list(String orderColumns, String criteria) {
		List<Entity> savings;

		if (orderColumns.equals("")) {
			savings = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + " where " + criteria)
					.list();
		} else {
			savings = sessionFactory.getCurrentSession()
					.createQuery("from " + clazz.getName() + " where " + criteria + " order by " + orderColumns).list();
		}

		return savings;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Entity> list(String orderColumns, String criteria, int firstResult, int maxResults) {
		List<Entity> list;

		list = sessionFactory.getCurrentSession()
				.createQuery("from " + clazz.getName() + (criteria.equals("") ? "" : " where " + criteria)
						+ (orderColumns.equals("") ? "" : " order by " + orderColumns))
				.setFirstResult(firstResult).setMaxResults(maxResults).list();

		return list;
	}

	@Transactional
	public void save(Entity object) {
		sessionFactory.getCurrentSession().saveOrUpdate(object);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public void delete(int id) {
		Entity object = (Entity) sessionFactory.getCurrentSession().get(clazz, id);
		sessionFactory.getCurrentSession().delete(object);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Entity find(int id) {
		Entity object = (Entity) sessionFactory.getCurrentSession().get(clazz, id);
		return object;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Entity findBy(String value, String column) {
		System.out.println("clas name" + clazz.getName());
		String hql = "select c from " + clazz.getName() + " c where c." + column + " = '" + value + "'";
		System.out.println("hql : " + hql);
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return (Entity) query.list().get(0);
	}

	@Transactional
	public boolean check(String value, String column) {
		String hql = "select c from " + clazz.getName() + " c where c." + column + " = '" + value + "'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return !query.list().isEmpty();
	}
}
