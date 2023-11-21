package com.fruntier.fruntier_map_tool.repository;

import com.fruntier.fruntier_map_tool.domain.Edge;
import com.fruntier.fruntier_map_tool.domain.Vertex;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EdgeRepository extends CrudRepository<Edge, Long> {
    Optional<Edge> findByStartVertexAndEndVertex(Vertex startVertex, Vertex endVertex);

}
