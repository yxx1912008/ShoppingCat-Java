package cn.luckydeer.baseaction.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自己实现的注解，用于在方法执行之前
 * 忽略Token验证
 * @author yuanxx
 * @version $Id: IgnoreAuth.java, v 0.1 2018年6月15日 下午2:03:15 yuanxx Exp $
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

}
