package org.gedcomx.persistence.graph.neo4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NodeType {

    NodeTypes value();
}
