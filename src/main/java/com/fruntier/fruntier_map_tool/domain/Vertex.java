package com.fruntier.fruntier_map_tool.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Vertex {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;
    private String location;

    @OneToMany(mappedBy = "startVertex", fetch=FetchType.LAZY)
    private List<Edge> outEdge = new ArrayList<>();

    @OneToMany(mappedBy = "endVertex", fetch=FetchType.LAZY)
    private List<Edge> inEdge = new ArrayList<>();


    public void addOutEdge(Edge edge){
        outEdge.add(edge);
    }

    public void addInEdge(Edge edge){
        inEdge.add(edge);
    }

    public Vertex(){}

    public Vertex(Double latitude, Double longitude, String location) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "vertexId=" + id +
                ", lat=" + latitude +
                ", lng=" + longitude +
                ", name='" + location + '\'' +
                '}';
    }
}
