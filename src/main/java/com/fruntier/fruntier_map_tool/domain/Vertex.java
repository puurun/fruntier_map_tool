package com.fruntier.fruntier_map_tool.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Vertex {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long vertexId;
    private Double lat;
    private Double lng;
    private String name;

    @OneToMany(mappedBy = "vstart", fetch=FetchType.LAZY)
    private List<Edge> outEdge = new ArrayList<>();

    @OneToMany(mappedBy = "vend", fetch=FetchType.LAZY)
    private List<Edge> inEdge = new ArrayList<>();


    public void addOutEdge(Edge edge){
        outEdge.add(edge);
    }

    public void addInEdge(Edge edge){
        inEdge.add(edge);
    }

    public Vertex(){}

    public Vertex(Double lat, Double lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public Long getVertexId() {
        return vertexId;
    }

    public void setVertexId(Long vertexId) {
        this.vertexId = vertexId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public List<Edge> getOutEdge() {
        return outEdge;
    }

    public void setOutEdge(List<Edge> outEdge) {
        this.outEdge = outEdge;
    }

    public List<Edge> getInEdge() {
        return inEdge;
    }

    public void setInEdge(List<Edge> inEdge) {
        this.inEdge = inEdge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "vertexId=" + vertexId +
                ", lat=" + lat +
                ", lng=" + lng +
                ", name='" + name + '\'' +
                '}';
    }
}
