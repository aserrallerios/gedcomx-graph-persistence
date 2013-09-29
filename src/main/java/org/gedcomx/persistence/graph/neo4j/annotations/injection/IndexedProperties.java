package org.gedcomx.persistence.graph.neo4j.annotations.injection;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

@BindingAnnotation
@Target({ PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface IndexedProperties {

}
