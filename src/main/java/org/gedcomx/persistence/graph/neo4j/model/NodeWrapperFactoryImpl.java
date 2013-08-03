package org.gedcomx.persistence.graph.neo4j.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.annotations.injection.NodeWrapperReflections;
import org.gedcomx.persistence.graph.neo4j.exceptions.GenericError;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.neo4j.graphdb.Node;
import org.reflections.Reflections;
import org.testng.collections.Maps;

import com.google.inject.Inject;

public class NodeWrapperFactoryImpl implements NodeWrapperFactory {

	private final Map<NodeTypes, Class<? extends NodeWrapper>> nodesByType = Maps
			.newHashMap();

	@Inject
	NodeWrapperFactoryImpl(final @NodeWrapperReflections Reflections reflections) {
		for (final Class<? extends NodeWrapper> subclass : reflections
				.getSubTypesOf(NodeWrapper.class)) {
			final NodeType nodeType = subclass.getAnnotation(NodeType.class);
			if (nodeType != null) {
				this.nodesByType.put(nodeType.value(), subclass);
			}
		}
	}

	@Override
	public Agent createAgent() {
		return new Agent();
	}

	@Override
	public Agent createAgent(final org.gedcomx.agent.Agent gedcomXAgent) {
		return new Agent(gedcomXAgent);
	}

	@Override
	public Document createDocument(
			final org.gedcomx.conclusion.Document gedcomXDocument) {
		return new Document(gedcomXDocument);
	}

	@Override
	public Document createDocument(final String text) {
		return new Document(text);
	}

	@Override
	public Event createEvent() {
		return new Event();
	}

	@Override
	public Event createEvent(final org.gedcomx.conclusion.Event gedcomXEvent) {
		return new Event(gedcomXEvent);
	}

	@Override
	public Person createPerson() {
		return new Person();
	}

	@Override
	public Person createPerson(final org.gedcomx.conclusion.Person gedcomXPerson) {
		return new Person(gedcomXPerson);
	}

	@Override
	public PlaceDescription createPlace(
			final org.gedcomx.conclusion.PlaceDescription gedcomXPlace) {
		return new PlaceDescription(gedcomXPlace);
	}

	@Override
	public PlaceDescription createPlace(final String name) {
		return new PlaceDescription(name);
	}

	@Override
	public Relationship createRelationship(
			final org.gedcomx.conclusion.Relationship gedcomXRelationship) {
		return new Relationship(gedcomXRelationship);
	}

	@Override
	public Relationship createRelationship(final Person p1, final Person p2) {
		return new Relationship(p1, p2);
	}

	@Override
	public SourceDescription createSource(
			final org.gedcomx.source.SourceDescription gedcomXSource) {
		return new SourceDescription(gedcomXSource);
	}

	@Override
	public SourceDescription createSource(final String citation) {
		return new SourceDescription(citation);
	}

	Class<? extends NodeWrapper> getWrapperByType(final NodeTypes type) {
		return this.nodesByType.get(type);
	}

	@Override
	public <T extends NodeWrapper> T wrapNode(final Class<T> type,
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

	@Override
	public NodeWrapper wrapNode(final NodeTypes type, final Node node) {
		return this.wrapNode(this.getWrapperByType(type), node);
	}
}
