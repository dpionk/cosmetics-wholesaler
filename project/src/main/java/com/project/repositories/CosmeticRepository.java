package com.project.repositories;

import com.project.domains.Cart;
import com.project.domains.Category;
import com.project.domains.Cosmetic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CosmeticRepository extends CrudRepository<Cosmetic, Long> {
    @Query("SELECT c FROM Cosmetic c WHERE c.name LIKE %:name%")
    List<Cosmetic> findCosmeticsByName(@Param("name") String name);

    @Query("SELECT c FROM Cosmetic c WHERE c.category =:category")
    List<Cosmetic> findCosmeticsByCategory(@Param("category") Category category);

    @Query("SELECT c FROM Cosmetic c JOIN c.carts carts WHERE carts =:cart ORDER BY c.name")
    List<Cosmetic> findCosmeticsByCart(@Param("cart") Cart cart );

    @Query("SELECT c FROM Cosmetic c  ORDER BY c.name DESC")
    List<Cosmetic> findCosmeticsByNameDESC();

    @Query("SELECT c FROM Cosmetic c  ORDER BY c.name ASC")
    List<Cosmetic> findCosmeticsByNameASC();

    @Query("SELECT c FROM Cosmetic c  ORDER BY c.price DESC")
    List<Cosmetic> findCosmeticsByPriceDESC();

    @Query("SELECT c FROM Cosmetic c  ORDER BY c.price ASC")
    List<Cosmetic> findCosmeticsByPriceASC();

    @Query("SELECT carts FROM Cosmetic c  LEFT JOIN c.carts carts where c.id = :cosmeticId")
    List<Cart> findCosmeticsInCart(Long cosmeticId);

}
