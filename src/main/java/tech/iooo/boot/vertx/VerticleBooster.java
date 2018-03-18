package tech.iooo.boot.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created on 2018/3/8 下午10:55
 *
 * @author Ivan97
 */
@Component
public class VerticleBooster {

  private static final Logger logger = LoggerFactory.getLogger(VerticleBooster.class);

  private final Vertx vertx;

  public VerticleBooster(Vertx vertx) {
    this.vertx = vertx;
  }

  public Future<String> deploy(Class<? extends Verticle> clazz, DeploymentOptions options,
      Object... params) {
    if (options.isMultiThreaded()) {
      Assert.isTrue(options.isWorker(), "async的同时，必须为worker");
    }
    if (params.length == 0) {
      return deploy(clazz, options);
    } else {
      Future<String> future = Future.future();
      Class<?>[] classes = new Class[params.length];
      for (int i = 0; i < params.length; i++) {
        classes[i] = params[i].getClass();
      }
      vertx.deployVerticle(() -> {
        Verticle verticle = null;
        try {
          verticle = clazz.getDeclaredConstructor(classes).newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          future.fail(e);
        }
        if (future.succeeded()) {
          return verticle;
        } else {
          return null;
        }
      }, options, handler -> {
        if (handler.failed()) {
          future.fail(handler.cause());
        } else {
          logger.info("{} deployed", clazz.getSimpleName());
          future.complete(handler.result());
        }
      });
      return future;
    }
  }

  public Future<String> deploy(Class<? extends Verticle> clazz, DeploymentOptions options) {
    if (options.isMultiThreaded()) {
      Assert.isTrue(options.isWorker(), "async的同时，必须为worker");
    }
    Future<String> future = Future.future();
    vertx.deployVerticle(() -> {
      Verticle verticle = null;
      try {
        verticle = clazz.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        future.fail(e);
      }
      return verticle;
    }, options, handler -> {
      if (handler.failed()) {
        future.fail(handler.cause());
      } else {
        logger.info("{} deployed", clazz.getSimpleName());
        future.complete(handler.result());
      }
    });
    return future;
  }
}
