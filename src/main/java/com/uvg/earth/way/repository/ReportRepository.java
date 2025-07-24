package com.uvg.earth.way.repository;
import com.uvg.earth.way.model.Report;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByAuthorId(Long authorId);

    @Query(value = "SELECT * FROM report r WHERE ST_DWithin(r.location, :point, :distance)", nativeQuery = true)
    List<Report> findNearbyReports(@Param("point") Point point, @Param("distance") double distance);



}
