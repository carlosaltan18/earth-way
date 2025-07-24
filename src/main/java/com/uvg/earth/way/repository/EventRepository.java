package com.uvg.earth.way.repository;

import com.uvg.earth.way.model.Event;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // Buscar eventos cercanos
    @Query(value = """
    SELECT * FROM event e
    WHERE ST_DWithin(e.location, :punto, :distancia)
""", nativeQuery = true)
    List<Event> findEventosCercanos(@Param("punto") Point punto, @Param("distancia") double distancia);

    List<Event> findByOrganizerId(Long organizerId);

}

