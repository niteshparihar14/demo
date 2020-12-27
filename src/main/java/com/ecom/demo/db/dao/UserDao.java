package com.ecom.demo.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.demo.db.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

	public List<User> findUserByPhone(final String phone);
}
