package com.fruntier.fruntier_map_tool.service;

import com.fruntier.fruntier_map_tool.controller.EdgeForm;
import com.fruntier.fruntier_map_tool.controller.VertexForm;
import com.fruntier.fruntier_map_tool.domain.Edge;
import com.fruntier.fruntier_map_tool.domain.Vertex;
import com.fruntier.fruntier_map_tool.repository.EdgeRepository;
import com.fruntier.fruntier_map_tool.repository.VertexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Transactional
@Service
public class MapperService {
    private final VertexRepository vertexRepository;
    private final EdgeRepository edgeRepository;

    @Autowired
    public MapperService(VertexRepository vertexRepository, EdgeRepository edgeRepository) {
        this.vertexRepository = vertexRepository;
        this.edgeRepository = edgeRepository;
    }

    public ConcurrentMap<Long, VertexForm> getAllVertexAsMap(){
        ConcurrentMap<Long, VertexForm> vertices = new ConcurrentHashMap<>();
        Iterable<Vertex> vertexIterator = vertexRepository.findAll();

        for(Vertex v: vertexIterator){
            VertexForm vertexForm = new VertexForm();
            vertexForm.setVid(v.getVertexId());
            vertexForm.setVlat(v.getLat());
            vertexForm.setVlng(v.getLng());
            vertexForm.setVname((v.getName()));
            vertices.put(v.getVertexId(), vertexForm);
        }

        return vertices;
    }

    public ConcurrentMap<Long, EdgeForm> getAllEdgeAsMap(){
        ConcurrentMap<Long, EdgeForm> edges = new ConcurrentHashMap<>();
        Iterable<Edge> edgeIterator = edgeRepository.findAll();

        for(Edge e: edgeIterator){
            EdgeForm edgeForm = new EdgeForm();
            edgeForm.setVstartId(e.getStartVertex().getVertexId());
            edgeForm.setVendId(e.getEndVertex().getVertexId());
            edgeForm.setEid(e.getEdgeId());
            edgeForm.setDistance(e.getDistance());
            edgeForm.setSlope(e.getSlope());
            edgeForm.setWidth(e.getWidth());
            edgeForm.setPopulation(e.getPopulation());
            edgeForm.setScore(e.getSubjectiveScore());

            edges.put(e.getEdgeId(), edgeForm);
        }

        return edges;
    }

    public Vertex setVertex(VertexForm vertexForm){
        Optional<Vertex> vertexOptional = vertexRepository.findById(vertexForm.getVid());
        Vertex vertex;
        if(vertexOptional.isPresent()){
            vertex = vertexOptional.get();
            vertex.setLat(vertexForm.getVlat());
            vertex.setLng(vertexForm.getVlng());
            vertex = vertexRepository.save(vertexOptional.get());
        }
        else{
            vertex = new Vertex(vertexForm.getVlat(), vertexForm.getVlng(), vertexForm.getVname());
            vertex = vertexRepository.save(vertex);
        }
        System.out.println("vertex = " + vertex);
        return vertex;
    }

    public Boolean deleteVertex(VertexForm vertexForm) {
        Optional<Vertex> vertexOptional = vertexRepository.findById(vertexForm.getVid());
        if(vertexOptional.isEmpty()){
            return false;
        }

        Vertex vertex = vertexOptional.get();
        vertexRepository.delete(vertex);

        return true;
    }

    public Edge setEdge(EdgeForm edgeForm) throws IllegalAccessException {
        Optional<Edge> edgeOptional = edgeRepository.findById(edgeForm.getEid());

        Edge edge1, edge2;

        Optional<Vertex> vstartOptional = vertexRepository.findById(edgeForm.getVstartId());
        Optional<Vertex> vendOptional = vertexRepository.findById(edgeForm.getVendId());

        if(vstartOptional.isEmpty() || vendOptional.isEmpty()){
            throw new IllegalAccessException();
        }

        Vertex vstart = vstartOptional.get();
        Vertex vend = vendOptional.get();
        if(edgeOptional.isPresent()){
            edge1 = edgeOptional.get();
            edge1.setStartVertex(vstart);
            edge1.setEndVertex(vend);
            edge1.setDistance(edgeForm.getDistance());
            edge1.setSlope(edgeForm.getSlope());
            edge1.setWidth(edgeForm.getWidth());
            edge1.setPopulation(edgeForm.getPopulation());
            edge1.setSubjectiveScore(edgeForm.getScore());
            edge1 = edgeRepository.save(edge1);

            edge2 = edgeOptional.get();
            edge2.setStartVertex(vend);
            edge2.setEndVertex(vstart);
            edge2.setDistance(edgeForm.getDistance());
            edge2.setSlope(-edgeForm.getSlope());
            edge2.setWidth(edgeForm.getWidth());
            edge2.setPopulation(edgeForm.getPopulation());
            edge2.setSubjectiveScore(edgeForm.getScore());
            edge2 = edgeRepository.save(edge2);
        }
        else{
            edge1 = new Edge(vstart, vend, edgeForm.getDistance(), edgeForm.getSlope(),
                    edgeForm.getWidth(), edgeForm.getPopulation(), edgeForm.getScore());
            edge1 = edgeRepository.save(edge1);

            edge2 = new Edge(vend, vstart, edgeForm.getDistance(), -edgeForm.getSlope(),
                    edgeForm.getWidth(), edgeForm.getPopulation(), edgeForm.getScore());
            edge2 = edgeRepository.save(edge2);
        }

        vstart.addOutEdge(edge1);
        vend.addInEdge(edge1);

        vstart.addInEdge(edge2);
        vend.addOutEdge(edge1);

        vstart = vertexRepository.save(vstart);
        vend = vertexRepository.save(vend);

        return edge1;
    }

    public boolean deleteEdge(EdgeForm edgeForm){
        Optional<Edge> edgeOptional = edgeRepository.findById(edgeForm.getEid());
        if(edgeOptional.isEmpty()){
            return false;
        }
        Edge edge = edgeOptional.get();

        Optional<Edge> otherEdge = edgeRepository.findByVstartAndVend(
                edge.getEndVertex(),
                edge.getStartVertex()
        );
        System.out.println("edge = " + edge);
        System.out.println("otherEdge.get() = " + otherEdge.get());
        edgeRepository.delete(edge);
        edgeRepository.delete(otherEdge.get());


        return true;
    }
}
