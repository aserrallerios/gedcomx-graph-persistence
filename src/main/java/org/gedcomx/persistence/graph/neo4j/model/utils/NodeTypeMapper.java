package org.gedcomx.persistence.graph.neo4j.model.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.annotations.NodeWrapperReflections;
import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.neo4j.graphdb.Node;
import org.reflections.Reflections;

import com.google.inject.Inject;

public class NodeTypeMapper {

    private final Map<NodeTypes, Class<? extends NodeWrapper>> nodesByType = new HashMap<>();

    @Inject
    NodeTypeMapper(final @NodeWrapperReflections Reflections reflections) {
        for (final Class<? extends NodeWrapper> subclass : reflections
                .getSubTypesOf(NodeWrapper.class)) {
            final NodeType nodeType = subclass.getAnnotation(NodeType.class);
            if (nodeType != null) {
                this.nodesByType.put(nodeType.value(), subclass);
            }
        }
    }

    public <T extends NodeWrapper> T createNode(final Class<T> type,
            final Node node) {
        Constructor<T> constructor;
        T wrapper = null;
        try {
            constructor = type.getDeclaredConstructor(Node.class);
            wrapper = constructor.newInstance(node);
        } catch (NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new GenericError("Error creating wrapper from node with id "
                    + node.getId() + " and type " + type);
        }
        return wrapper;
    }

    public NodeWrapper createNode(final NodeTypes type, final Node node) {
        return this.createNode(this.getWrapperByType(type), node);
    }

    public NodeTypes getTypeByWrapper(final Class<? extends NodeWrapper> wrapper) {
        return wrapper.getAnnotation(NodeType.class).value();
    }

    public Class<? extends NodeWrapper> getWrapperByType(final NodeTypes type) {
        return this.nodesByType.get(type);
    }
}
