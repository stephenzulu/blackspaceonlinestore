package com.example.blackspace.Repository;

import com.example.blackspace.Model.PageVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PageVisitRepository extends JpaRepository<PageVisit, Long> {

    @Query("SELECT p.visitDate, COUNT(p) FROM PageVisit p " +
           "WHERE p.visitDate >= :startDate " +
           "GROUP BY p.visitDate ORDER BY p.visitDate ASC")
    List<Object[]> countVisitsPerDay(@Param("startDate") LocalDate startDate);

    @Query("SELECT p.pagePath, COUNT(p) FROM PageVisit p " +
           "WHERE p.visitDate >= :startDate " +
           "GROUP BY p.pagePath ORDER BY COUNT(p) DESC")
    List<Object[]> countVisitsByPage(@Param("startDate") LocalDate startDate);

    List<PageVisit> findTop50ByOrderByCreatedAtDesc();
}
