package com.fruntier.fruntier_map_tool.controller;

import com.fruntier.fruntier_map_tool.domain.Edge;
import com.fruntier.fruntier_map_tool.domain.Vertex;
import com.fruntier.fruntier_map_tool.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class MapperController {

    private final MapperService mapperService;

    @Autowired
    public MapperController(MapperService mapperService){
        this.mapperService = mapperService;
    }

    @GetMapping("/tool/mapper")
    public String mapper(){
        return "mapper";
    }

    @GetMapping("/tool/example")
    public String example(){
        mapperService.createExample();
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/tool/mapper/get-all-vertex")
    public ConcurrentMap<Long, VertexForm> getAllVertex(){
        return mapperService.getAllVertexAsMap();
    }

    @ResponseBody
    @GetMapping("/tool/mapper/get-all-edge")
    public ConcurrentMap<Long, Edge> getAllEdge(){
        return mapperService.getAllEdgeAsMap();
    }


    @ResponseBody
    @PostMapping("/tool/mapper/set-vertex")
    public Long setVertex(@RequestBody VertexForm vertexForm){
        Vertex vertex = mapperService.setVertex(vertexForm);
        return vertex.getVertexId();
    }

    @ResponseBody
    @PostMapping("/tool/mapper/delete-vertex")
    public Boolean deleteVertex(@RequestBody VertexForm vertexForm){
        return mapperService.deleteVertex(vertexForm);
    }

    @ResponseBody
    @PostMapping("/tool/mapper/set-edge")
    public Long setEdge(@RequestBody EdgeForm edgeForm){
        System.out.println(edgeForm);
        Edge edge;
        try {
            edge = mapperService.setEdge(edgeForm);
        }
        catch(Exception e){
            System.out.println(e);
            return -1L;
        }
        return edge.getEdgeId();
    }

}
