package org.gedcomx.persistence.graph.neo4j.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.annotations.NodeType;
import org.gedcomx.persistence.graph.neo4j.annotations.injection.NodeWrapperReflections;
import org.gedcomx.persistence.graph.neo4j.exceptions.GenericError;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.neo4j.graphdb.Node;
import org.reflections.Reflections;

import com.google.inject.Inject;

public class NodeWrapperFactory {

    @Inject
    private WrapperProvider wrapperProvider;
    private final Map<NodeTypes, Class<? extends NodeWrapper>> nodesByType = new HashMap<>();

    @Inject
    NodeWrapperFactory(final @NodeWrapperReflections Reflections reflections) {
        for (final Class<? extends NodeWrapper> subclass : reflections
                .getSubTypesOf(NodeWrapper.class)) {
            final NodeType nodeType = subclass.getAnnotation(NodeType.class);
            if (nodeType != null) {
                this.nodesByType.put(nodeType.value(), subclass);
            }
        }
    }

    public Agent createAgent(final org.gedcomx.agent.Agent gedcomXAgent)
            throws MissingFieldException {
        return this.wrapperProvider.createAgent(gedcomXAgent);
    }

    public Document createDocument(
            final org.gedcomx.conclusion.Document gedcomXDocument)
            throws MissingFieldException {
        return this.wrapperProvider.createDocument(gedcomXDocument);
    }

    public Event createEvent(final org.gedcomx.conclusion.Event gedcomXEvent)
            throws MissingFieldException {
        return this.wrapperProvider.createEvent(gedcomXEvent);
    }

    public NodeWrapper createNode(final NodeTypes type,
            final Object gedcomxObject) throws MissingFieldException {
        return this.wrapperProvider
                .createPerson((org.gedcomx.conclusion.Person) gedcomxObject);
    }

    public Person createPerson(final org.gedcomx.conclusion.Person gedcomXPerson)
            throws MissingFieldException {
        return this.wrapperProvider.createPerson(gedcomXPerson);
    }

    public PlaceDescription createPlace(
            final org.gedcomx.conclusion.PlaceDescription gedcomXPlace)
            throws MissingFieldException {
        return this.wrapperProvider.createPlace(gedcomXPlace);
    }

    public Relationship createRelationship(
            final org.gedcomx.conclusion.Relationship gedcomXRelationship)
            throws MissingFieldException {
        return this.wrapperProvider.createRelationship(gedcomXRelationship);
    }

    public SourceDescription createSource(
            final org.gedcomx.source.SourceDescription gedcomXSource)
            throws MissingFieldException {
        return this.wrapperProvider.createSource(gedcomXSource);
    }

    Class<? extends NodeWrapper> getWrapperByType(final NodeTypes type) {
        return this.nodesByType.get(type);
    }

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

    public NodeWrapper wrapNode(final NodeTypes type, final Node node) {
        return this.wrapNode(this.getWrapperByType(type), node);
    }
}
