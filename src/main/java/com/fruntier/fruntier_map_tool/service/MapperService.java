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

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

    public ConcurrentMap<Long, Edge> getAllEdgeAsMap(){
        ConcurrentMap<Long, Edge> edges = new ConcurrentHashMap<>();
        Iterable<Edge> edgeIterator = edgeRepository.findAll();

        for(Edge e: edgeIterator){
            edges.put(e.getEdgeId(), e);
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
        if(vertexOptional.isPresent()){
            Vertex vertex = vertexOptional.get();
            vertexRepository.delete(vertex);
        }
        else {
            return false;
        }
        return true;
    }

    public Edge setEdge(EdgeForm edgeForm) throws IllegalAccessException {
        Optional<Edge> edgeOptional = edgeRepository.findById(edgeForm.getEid());

        Edge edge;

        Optional<Vertex> vstartOptional = vertexRepository.findById(edgeForm.getVstartId());
        Optional<Vertex> vendOptional = vertexRepository.findById(edgeForm.getVendId());

        if(vstartOptional.isEmpty() || vendOptional.isEmpty()){
            throw new IllegalAccessException();
        }

        Vertex vstart = vstartOptional.get();
        Vertex vend = vendOptional.get();
        if(edgeOptional.isPresent()){
            edge = edgeOptional.get();
            edge.setVstart(vstart);
            edge.setVend(vend);
            edge.setDistance(edgeForm.getDistance());
            edge.setSlope(edgeForm.getSlope());
            edge.setWidth(edgeForm.getWidth());
            edge.setPopulation(edgeForm.getPopulation());
            edge.setScore(edgeForm.getScore());
            edge = edgeRepository.save(edge);
        }
        else{
            edge = new Edge(vstart, vend, edgeForm.getDistance(), edgeForm.getSlope(),
                    edgeForm.getWidth(), edgeForm.getPopulation(), edgeForm.getScore());
            edge = edgeRepository.save(edge);
        }

        vstart.addOutEdge(edge);
        vend.addInEdge(edge);
        vstart = vertexRepository.save(vstart);
        vend = vertexRepository.save(vend);

        return edge;
    }

    public void createExample(){
        saveExample();
        printVertex();
    }

    @Transactional
    private void saveExample(){
        Vertex v1 = new Vertex(135D, 136D, "v1");
        Vertex v2 = new Vertex(137D, 135D, "v2");
        v1 = vertexRepository.save(v1);
        v2 = vertexRepository.save(v2);

        Vertex v3 = vertexRepository.findById(1L).get();
        Vertex v4 = vertexRepository.findById(2L).get();

        Edge e1 = new Edge();
        e1.setVstart(v3);
        e1.setVend(v4);
        e1.setSlope(0);
        e1.setPopulation(0);
        e1.setDistance(10);
        e1.setWidth(13);
        e1.setScore(30);
        e1 = edgeRepository.save(e1);
        List<Edge> outEdge = v3.getOutEdge();
        outEdge.add(e1);
        v3.setOutEdge(outEdge);

        List<Edge> inEdge = v4.getInEdge();
        inEdge.add(e1);
        v4.setInEdge(inEdge);
        v3 = vertexRepository.save(v3);
        v4 = vertexRepository.save(v4);

    }

    @Transactional
    private void printVertex(){
        for (Vertex vertex : vertexRepository.findAll()) {
            System.out.println("vertex = " + vertex);
            for (Edge edge : vertex.getInEdge()) {
                System.out.println("edge = " + edge);
            }
            for (Edge edge : vertex.getOutEdge()) {
                System.out.println("edge = " + edge);
            }
        }
    }
}
