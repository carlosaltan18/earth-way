package com.uvg.earth.way.repository;

import com.uvg.earth.way.model.Event;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Buscar eventos cercanos
    @Query(value = """
    SELECT * FROM event e
    WHERE ST_DWithin(e.location, :punto, :distancia)
""", nativeQuery = true)
    List<Event> findEventosCercanos(@Param("punto") Point punto, @Param("distancia") double distancia);

    List<Event> findByOrganizerId(Long organizerId);

    Optional<Event> findEventByName(String name);
    Optional<Event> findByName(String name);
    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

