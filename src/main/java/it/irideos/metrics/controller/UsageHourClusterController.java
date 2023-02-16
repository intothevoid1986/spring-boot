package it.irideos.metrics.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.irideos.metrics.models.UsageHourClusterModel;
import it.irideos.metrics.service.UsageHourClusterService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/ocloud_cost/")
public class UsageHourClusterController {

    @Autowired
    private UsageHourClusterService usageHourClusterService;

    @GetMapping("/usage_hour_cluster/{cluster_name}")
    public ResponseEntity<List<UsageHourClusterModel>> getClusterCostDay(
            @PathVariable("cluster_name") String clustName) {
        try {
            List<UsageHourClusterModel> clusterDayCost = new ArrayList<UsageHourClusterModel>();
            if (clustName != null) {
                clusterDayCost = usageHourClusterService.getClusterDayCost(clustName);
            }
            return new ResponseEntity<>(clusterDayCost, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
