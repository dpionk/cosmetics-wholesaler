package com.project.repositories;

import com.project.domains.Cart;
import com.project.domains.Cosmetic;
import com.project.domains.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user =: user")
    List<Cart> findCartByUser(@Param("user") User user);
}
