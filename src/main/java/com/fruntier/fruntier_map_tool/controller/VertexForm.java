package com.fruntier.fruntier_map_tool.controller;

public class VertexForm {
    Long vid;
    Double vlat;
    Double vlng;
    String vname;

    public VertexForm(){}
    public Double getVlng() {
        return vlng;
    }

    public Long getVid() {
        return vid;
    }

    public void setVid(Long vid) {
        this.vid = vid;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public void setVlng(Double vlng) {
        this.vlng = vlng;
    }

    public Double getVlat() {
        return vlat;
    }

    public void setVlat(Double vlat) {
        this.vlat = vlat;
    }

    @Override
    public String toString() {
        return "VertexForm{" +
                "vid=" + vid +
                ", vlat=" + vlat +
                ", vlng=" + vlng +
                ", vname='" + vname + '\'' +
                '}';
    }
}
