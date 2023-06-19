package com.yeeshop.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yeeshop.dao.ICategoryDAO;
import com.yeeshop.entity.Category;

@Transactional @Repository
public class CategoryDAOImpl implements ICategoryDAO{

    @Autowired
    SessionFactory factory;

    @Override
    public List<Category> findAll() {
        String hql="FROM Category";
        Session session= factory.getCurrentSession();
        TypedQuery <Category> query= session.createQuery(hql, Category.class);
        List <Category> categories= query.getResultList();
        return categories;
    }

    @Override
    public Category findById(Integer id) {
        Session session=factory.getCurrentSession();
		Category category=session.find(Category.class, id);
		return category;
    }

    @Override
    public Category create(Category category) {
        Session session=factory.getCurrentSession();
		session.save(category);
		return null;
    }

    @Override
    public void update(Category category) {
        Session session=factory.getCurrentSession();
		session.update(category);
    }

    @Override
    public Category delete(Integer id) {
        Session session=factory.getCurrentSession();
		Category category=session.find(Category.class, id);
		session.delete(category);
		return category;
    }

    @Override
    public boolean findExistCate(Category c) {
        String hql= "From Category c where c.name=:name and c.nameVN=:nameVN";
        Session session=factory.getCurrentSession();
        TypedQuery<Category> query=session.createQuery(hql,Category.class);
        query.setParameter("name", c.getName());
        query.setParameter("nameVN", c.getNameVN());
        List<Category> categories= query.getResultList();
        if(categories.size()>0)return true;
        return false;
    }  
}


