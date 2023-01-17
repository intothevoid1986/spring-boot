package it.irideos.metrics.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.irideos.metrics.models.ImageModel;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {

  public List<ImageModel> parseImage(String value) throws NotFoundException {
    List<ImageModel> res = new ArrayList<>();

    if (value.equals("[]")) {
      // throw new NotFoundException();
      log.info(value);
    } else {
      ImageModel r = new ImageModel();
      String[] values = StringUtils.split(value, ", ");
      value = values[0];

      // Strip away square brackets
      String firstCleanValue = StringUtils.delete(values[0], "[");
      String firstClean = StringUtils.delete(firstCleanValue, "ImageModel");

      // // Strip away square brackets
      String firstResultString = StringUtils.delete(firstClean, "(");
      value = values[1];
      String secondCleanValue = StringUtils.delete(values[1], "]");
      String secondClean = StringUtils.delete(secondCleanValue, ")");
      String resultString = firstResultString + ", " + secondClean;

      // Convert comma separated String to String[]
      String[] s = StringUtils.commaDelimitedListToStringArray(resultString);
      String srv = String.valueOf(s[1].substring(9, s[1].length()));
      String iref = String.valueOf(s[2].substring(11, s[2].length()));

      // Map to Object
      r.setService(srv);
      r.setImage_ref(iref);
      res.add(r);
    }

    return res;
  }

  // private void toBeCompleted() {
  // if (vmResource.getImageRef() != null) {
  // img_ref = vmResource.getImageRef();
  // List<ImageModel> i = imageRepository.findByImageModels(img_ref);
  // img = i.toString();
  // imageModels = parseImage(img);
  // for (ImageModel imageModel : imageModels) {
  // srv = imageModel.getService();
  // System.out.println("SERVICE: " + srv);
  // ArrayList<ClusterModel> clm = new ArrayList<>();
  // clm = clusterRepository.findNameByClusterService(srv);
  // clusterName = clm.toString();
  // // clustModels = objectMapper.readValue(clusterName, new
  // // TypeReference<List<ClusterModel>>() {
  // // });
  // }
  // }
  // }
}
