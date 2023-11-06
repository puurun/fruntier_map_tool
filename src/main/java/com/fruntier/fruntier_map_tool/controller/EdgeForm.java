package com.fruntier.fruntier_map_tool.controller;

public class EdgeForm {
    Long eid;
    Long vstartId;
    Long vendId;
    Double distance;
    Integer slope;
    Integer width;
    Integer population;
    Double score;

    public Long getEid() {
        return eid;
    }

    public void setEid(Long eid) {
        this.eid = eid;
    }

    public Long getVstartId() {
        return vstartId;
    }

    public void setVstartId(Long vstartId) {
        this.vstartId = vstartId;
    }

    public Long getVendId() {
        return vendId;
    }

    public void setVendId(Long vendId) {
        this.vendId = vendId;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Integer getSlope() {
        return slope;
    }

    public void setSlope(Integer slope) {
        this.slope = slope;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "EdgeForm{" +
                "eid=" + eid +
                ", vstartId=" + vstartId +
                ", vendId=" + vendId +
                ", distance=" + distance +
                ", slope=" + slope +
                ", width=" + width +
                ", population=" + population +
                ", score=" + score +
                '}';
    }
}
