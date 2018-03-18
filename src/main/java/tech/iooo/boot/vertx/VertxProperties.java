package tech.iooo.boot.vertx;

import io.vertx.core.VertxOptions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Created on 2018/3/17 下午11:28
 *
 * @author Ivan97
 */
@Data
@ConfigurationProperties(prefix = "vertx")
public class VertxProperties {

  @NestedConfigurationProperty
  private VertxOptions vertxOptions = new VertxOptions();
}
