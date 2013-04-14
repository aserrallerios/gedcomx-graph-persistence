package org.gedcomx.persistence.graph.neo4j.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.exception.GenericError;
import org.neo4j.graphdb.Node;
import org.reflections.Reflections;

public class NodeTypeMapper {

	private static Map<String, Class<? extends NodeWrapper>> nodesByType = new HashMap<>();

	static {
		final Reflections reflections = new Reflections(NodeWrapper.class.getPackage().getName());

		for (final Class<? extends NodeWrapper> subclass : reflections.getSubTypesOf(NodeWrapper.class)) {
			final NodeType nodeType = subclass.getAnnotation(NodeType.class);
			if (nodeType != null) {
				NodeTypeMapper.nodesByType.put(nodeType.value(), subclass);
			}
		}
	}

	public static <T extends NodeWrapper> T createNode(final Class<T> type, final Node node) {
		Constructor<T> constructor;
		T wrapper = null;
		try {
			constructor = type.getDeclaredConstructor(Node.class);
			wrapper = constructor.newInstance(node);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new GenericError("Error creating wrapper from node with id " + node.getId() + " and type " + type);
		}
		return wrapper;
	}

	public static NodeWrapper createNode(final String type, final Node node) {
		return NodeTypeMapper.createNode(NodeTypeMapper.getWrapperByType(type), node);
	}

	public static String getTypeByWrapper(final Class<? extends NodeWrapper> wrapper) {
		return wrapper.getAnnotation(NodeType.class).value();
	}

	public static Class<? extends NodeWrapper> getWrapperByType(final String type) {
		return NodeTypeMapper.nodesByType.get(type);
	}
}
