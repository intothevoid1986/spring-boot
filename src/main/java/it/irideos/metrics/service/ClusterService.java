package it.irideos.metrics.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.irideos.metrics.models.ClusterModel;
import it.irideos.metrics.models.ImageModel;
import it.irideos.metrics.repository.ClusterRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClusterService {

    private List<ClusterModel> clusterModels;

    @Autowired
    private ClusterRepository clusterRepository;

    public List<ClusterModel> getClusterName(ImageModel imageModels)
            throws JsonMappingException, JsonProcessingException {
        if (imageModels.getService() != null) {
            List<ClusterModel> clusterName = clusterRepository.findClusterNameByService(imageModels.getService());
            // Mappo la risposta
            String cls = clusterName.toString();
            clusterModels = parseCluster(cls);
            for (ClusterModel clusterModel : clusterModels) {
                System.out.println("CLUSTER NAME: " + clusterModel.getCluster_name());

            }
        }
        return clusterModels;
    }

    private List<ClusterModel> parseCluster(String value) throws NotFoundException {
        List<ClusterModel> res = new ArrayList<>();

        if (value.equals("[]")) {
            // throw new NotFoundException();
            log.info(value);
        } else {
            ClusterModel r = new ClusterModel();
            String[] values = StringUtils.split(value, ", ");
            value = values[0];

            // Strip away square brackets
            String firstCleanValue = StringUtils.delete(values[0], "[");
            String firstClean = StringUtils.delete(firstCleanValue, "ClusterModel");

    // // Strip away square brackets
    String firstResultString = StringUtils.delete(firstClean, "(");
    value = values[1];
    String secondCleanValue = StringUtils.delete(values[1], "]");
    String secondClean = StringUtils.delete(secondCleanValue, ")");
    String resultString = firstResultString + ", " + secondClean;

    // Convert comma separated String to String[]
    String[] s = StringUtils.commaDelimitedListToStringArray(resultString);
    String srv = String.valueOf(s[1].substring(9, s[1].length()));
    String cls_name = String.valueOf(s[2].substring(14, s[2].length()));

    // Map to Object
    r.setService(srv);
    r.setCluster_name(cls_name);
    res.add(r);
}

return res;
}

}
