package it.irideos.metrics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.irideos.metrics.models.ClusterModel;
import it.irideos.metrics.models.ImageModel;
import it.irideos.metrics.models.VMModel;
import it.irideos.metrics.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private ClusterService clusterService;

  public List<ImageModel> parseImage(String value) throws NotFoundException {
    List<ImageModel> res = new ArrayList<>();

    if (value.equals("[]")) {
      log.info(value);
    } else {
      ImageModel r = new ImageModel();
      String[] values = StringUtils.split(value, ", ");
      value = values[0];

      String firstCleanValue = StringUtils.delete(values[0], "[");
      String firstClean = StringUtils.delete(firstCleanValue, "ImageModel");

      String firstResultString = StringUtils.delete(firstClean, "(");
      value = values[1];
      String secondCleanValue = StringUtils.delete(values[1], "]");
      String secondClean = StringUtils.delete(secondCleanValue, ")");
      String resultString = firstResultString + ", " + secondClean;

      String[] s = StringUtils.commaDelimitedListToStringArray(resultString);
      String srv = String.valueOf(s[1].substring(9, s[1].length()));
      String iref = String.valueOf(s[2].substring(11, s[2].length()));

      r.setService(srv);
      r.setImage_ref(iref);
      res.add(r);
    }
    return res;
  }

  public List<ImageModel> getImageModel(VMModel vmResource)
      throws JsonMappingException, JsonProcessingException, NotFoundException {
    List<ImageModel> imageModels = new ArrayList<ImageModel>();
    if (vmResource.getImageRef() != null) {
      List<ImageModel> imageRef = imageRepository.findImageRefByImageModels(vmResource.getImageRef());
      String img = imageRef.toString();
      imageModels = parseImage(img);

    }
    return imageModels;
  }

  public Map<ClusterModel, String> getClusterMap(List<ImageModel> images, VMModel vmResource)
      throws NotFoundException, RuntimeException {
    Map<ClusterModel, String> clusterModelMap = new HashMap<>();
    for (ImageModel image : images) {
      clusterModelMap = clusterService.findClusterName(image.getService(),
          vmResource.getDisplayName());
    }
    return clusterModelMap;
  }
}
