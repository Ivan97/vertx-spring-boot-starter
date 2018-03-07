package tech.iooo.boot.vertx;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Created on 2018/3/7 上午9:34
 *
 * @author Ivan97
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableVertx
@Import({VertxAutoConfiguration.class})
public @interface EnableVertx {

}
