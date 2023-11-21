package com.fruntier.fruntier_map_tool.domain;

import jakarta.persistence.*;

@Entity
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="start_vertex_id")
    private Vertex startVertex;

    @ManyToOne
    @JoinColumn(name="end_vertex_id")
    private Vertex endVertex;

    private double distance;
    private int slope;
    private int width;
    private int population;
    private double subjectiveScore;

    public Edge(){}

    public Edge(Vertex startVertex, Vertex endVertex, double distance, int slope, int width, int population, double score) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.distance = distance;
        this.slope = slope;
        this.width = width;
        this.population = population;
        this.subjectiveScore = score;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public void setEndVertex(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getSlope() {
        return slope;
    }

    public void setSlope(int slope) {
        this.slope = slope;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getSubjectiveScore() {
        return subjectiveScore;
    }

    public void setSubjectiveScore(double subjectiveScore) {
        this.subjectiveScore = subjectiveScore;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "edgeId=" + id +
                ", vstart=" + startVertex.getId() +
                ", vend=" + endVertex.getId() +
                ", distance=" + distance +
                ", angle=" + slope +
                ", width=" + width +
                ", crowded=" + population +
                ", score=" + subjectiveScore +
                '}';
    }
}
