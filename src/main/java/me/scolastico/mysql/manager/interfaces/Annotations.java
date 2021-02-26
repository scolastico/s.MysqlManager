package me.scolastico.mysql.manager.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Annotations {

  @Retention(RetentionPolicy.RUNTIME)
  public static @interface Table {
    public String tableName() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  public static @interface TableEntry {
    public String name() default "";
  }

  @Retention(RetentionPolicy.RUNTIME)
  public static @interface TableId {}

}
