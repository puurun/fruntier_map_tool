package com.fruntier.fruntier_map_tool.domain;

import jakarta.persistence.*;

@Entity
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long edgeId;

    @ManyToOne
    @JoinColumn(name="vstart_id")
    private Vertex vstart;

    @ManyToOne
    @JoinColumn(name="vend_id")
    private Vertex vend;

    private double distance;
    private int slope;
    private int width;
    private int population;
    private double score;

    public Edge(){}

    public Edge(Vertex vstart, Vertex vend, double distance, int slope, int width, int population, double score) {
        this.vstart = vstart;
        this.vend = vend;
        this.distance = distance;
        this.slope = slope;
        this.width = width;
        this.population = population;
        this.score = score;
    }

    public Long getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(Long edgeId) {
        this.edgeId = edgeId;
    }

    public Vertex getVstart() {
        return vstart;
    }

    public void setVstart(Vertex vstart) {
        this.vstart = vstart;
    }

    public Vertex getVend() {
        return vend;
    }

    public void setVend(Vertex vend) {
        this.vend = vend;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "edgeId=" + edgeId +
                ", vstart=" + vstart.getVertexId() +
                ", vend=" + vend.getVertexId() +
                ", distance=" + distance +
                ", angle=" + slope +
                ", width=" + width +
                ", crowded=" + population +
                ", score=" + score +
                '}';
    }
}
