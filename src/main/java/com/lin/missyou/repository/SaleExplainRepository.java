package com.lin.missyou.repository;

import com.lin.missyou.model.SaleExplain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SaleExplainRepository extends JpaRepository<SaleExplain,Long> {

    @Query(value = "",nativeQuery = true)
    List<SaleExplain> findByFixedOrderByIndex(Boolean fixed);
}
