//
// This file was automatically generated by jrepresenter
// Any changes may be lost!
//
package gen.com.thoughtworks.go.config.representers;

import cd.go.jrepresenter.RequestContext;
import com.thoughtworks.go.domain.config.ConfigurationProperty;
import gen.cd.go.jrepresenter.Constants;
import java.lang.Object;
import java.lang.String;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Representer for {@link ConfigurationProperty}.
 * Generated using representer {@link com.thoughtworks.go.config.representers.ConfigurationPropertyRepresenter}.
 */
public class ConfigurationPropertyMapper {
  public static Map<String, Object> toJSON(ConfigurationProperty value,
      RequestContext requestContext) {
    Map<String, Object> jsonObject = new LinkedHashMap<String, Object>();
    jsonObject.put("key", Constants.Serializers.CONFIGURATION_KEY.apply(value.getKey()));
    if (!Constants.SkipRenderers.IF_SECURE_CONFIGURATION_VALUE.apply(value)) {
      jsonObject.put("value", Constants.Serializers.CONFIGURATION_VALUE.apply(value.getConfigurationValue()));
    }
    if (!Constants.SkipRenderers.IF_PLAIN_TEXT_CONFIGURATION_VALUE.apply(value)) {
      jsonObject.put("encrypted_value", value.getEncryptedValue());
    }
    return jsonObject;
  }

  public static List toJSON(List<ConfigurationProperty> values, RequestContext requestContext) {
    return values.stream().map(eachItem -> ConfigurationPropertyMapper.toJSON(eachItem, requestContext)).collect(Collectors.toList());
  }

  public static ConfigurationProperty fromJSON(Map jsonObject) {
    return Constants.ToJSONMappers.CONFIGURATION_PROPERTY.apply(jsonObject);
  }

  public static List<ConfigurationProperty> fromJSON(List<Map> jsonArray) {
    if (jsonArray == null) {
      return Collections.emptyList();
    }
    return jsonArray.stream().map(eachItem -> ConfigurationPropertyMapper.fromJSON(eachItem)).collect(Collectors.toList());
  }
}
