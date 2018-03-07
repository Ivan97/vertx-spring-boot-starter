package tech.iooo.boot.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created on 2018/3/7 上午9:34
 *
 * @author Ivan97
 */
@Configuration
public class VertxAutoConfiguration implements BeanFactoryAware {

  private BeanFactory beanFactory;

  @Bean
  @ConditionalOnMissingBean
  public Vertx vertx() {
    ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
    Vertx vertx = Vertx.vertx();
    configurableBeanFactory.registerSingleton("vertx", vertx);
    return vertx;
  }

  @Bean
  @ConditionalOnMissingBean
  public EventBus eventBus(Vertx vertx) {
    return vertx.eventBus();
  }

  @Bean
  @ConditionalOnMissingBean
  public SharedData sharedData(Vertx vertx) {
    return vertx.sharedData();
  }

  @Bean
  @ConditionalOnMissingBean
  public FileSystem fileSystem(Vertx vertx) {
    return vertx.fileSystem();
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

}
