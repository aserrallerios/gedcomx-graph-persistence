package org.gedcomx.persistence.graph.neo4j.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gedcomx.persistence.graph.neo4j.annotations.injection.EmbededDB;
import org.gedcomx.persistence.graph.neo4j.annotations.interceptors.CheckForDuplicates;
import org.gedcomx.persistence.graph.neo4j.annotations.interceptors.Transactional;
import org.gedcomx.persistence.graph.neo4j.dao.GENgraphDAO;
import org.gedcomx.persistence.graph.neo4j.exceptions.GenericError;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredPropertyException;
import org.gedcomx.persistence.graph.neo4j.exceptions.MissingRequiredRelationshipException;
import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapperFactory;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.gedcomx.persistence.graph.neo4j.model.constants.GenericProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.IndexNames;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.gedcomx.persistence.graph.neo4j.properties.Messages;
import org.gedcomx.persistence.graph.neo4j.service.dto.QueryResult;
import org.gedcomx.persistence.graph.neo4j.utils.MessageResolver;
import org.gedcomx.persistence.graph.neo4j.utils.QueryResolver;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GENgraphPersistenceServiceImpl implements
        GENgraphPersistenceService {

    private static Logger logger = LogManager.getLogger("GENGraphService");
    private final GENgraphDAO dao;
    @Inject
    private NodeWrapperFactory nodeWrapperFactory;

    @Inject
    GENgraphPersistenceServiceImpl(final @EmbededDB GENgraphDAO dao) {
        this.dao = dao;
    }

    @Override
    @CheckForDuplicates
    public Agent addAgent(final org.gedcomx.agent.Agent agent)
            throws MissingFieldException {
        Agent a = null;
        try {
            a = this.nodeWrapperFactory.createAgent(agent);
            GENgraphPersistenceServiceImpl.logger.info(MessageResolver.resolve(
                    Messages.GEDCOMX_NODE_CREATED, agent.getId(),
                    NodeTypes.AGENT));
        } catch (final MissingFieldException e) {
            GENgraphPersistenceServiceImpl.logger.error(this
                    .getMissingFieldErrorMessage(e));
            throw e;
        }
        return a;
    }

    @Override
    @CheckForDuplicates
    public Conclusion addConclusion(
            final org.gedcomx.conclusion.Conclusion conclusion)
            throws MissingFieldException {
        Conclusion c = null;
        try {
            NodeTypes type = null;
            if (conclusion instanceof org.gedcomx.conclusion.Person) {
                // c = new Person((org.gedcomx.conclusion.Person) conclusion);
                c = this.nodeWrapperFactory
                        .createPerson((org.gedcomx.conclusion.Person) conclusion);
                type = NodeTypes.PERSON;
            } else if (conclusion instanceof org.gedcomx.conclusion.Document) {
                c = this.nodeWrapperFactory
                        .createDocument((org.gedcomx.conclusion.Document) conclusion);
                type = NodeTypes.DOCUMENT;
            } else if (conclusion instanceof org.gedcomx.conclusion.Event) {
                c = this.nodeWrapperFactory
                        .createEvent((org.gedcomx.conclusion.Event) conclusion);
                type = NodeTypes.EVENT;
            } else if (conclusion instanceof org.gedcomx.conclusion.Relationship) {
                c = this.nodeWrapperFactory
                        .createRelationship((org.gedcomx.conclusion.Relationship) conclusion);
                type = NodeTypes.RELATIONSHIP;
            } else if (conclusion instanceof org.gedcomx.conclusion.PlaceDescription) {
                c = this.nodeWrapperFactory
                        .createPlace((org.gedcomx.conclusion.PlaceDescription) conclusion);
                type = NodeTypes.PLACE_DESCRIPTION;
            } else {
                throw new GenericError(
                        MessageResolver
                                .resolve(Messages.GEDCOMX_CONCLUSION_TYPE));
            }
            GENgraphPersistenceServiceImpl.logger.info(MessageResolver.resolve(
                    Messages.GEDCOMX_NODE_CREATED, conclusion.getId(), type));
        } catch (final MissingFieldException e) {
            GENgraphPersistenceServiceImpl.logger.error(this
                    .getMissingFieldErrorMessage(e));
            throw e;
        }
        return c;
    }

    @Override
    @CheckForDuplicates
    public SourceDescription addSource(
            final org.gedcomx.source.SourceDescription sourceDescription)
            throws MissingFieldException {
        SourceDescription s = null;
        try {
            s = this.nodeWrapperFactory.createSource(sourceDescription);
            GENgraphPersistenceServiceImpl.logger.info(MessageResolver.resolve(
                    Messages.GEDCOMX_NODE_CREATED, sourceDescription.getId(),
                    NodeTypes.SOURCE_DESCRIPTION));
        } catch (final MissingFieldException e) {
            GENgraphPersistenceServiceImpl.logger.error(this
                    .getMissingFieldErrorMessage(e));
            throw e;
        }
        return s;
    }

    @Override
    @CheckForDuplicates
    public NodeWrapper addTopLevelElement(final Object gedcomxElement)
            throws MissingFieldException {
        NodeWrapper n = null;
        if (gedcomxElement instanceof org.gedcomx.agent.Agent) {
            n = this.addAgent((org.gedcomx.agent.Agent) gedcomxElement);
        } else if (gedcomxElement instanceof org.gedcomx.conclusion.Conclusion) {
            n = this.addConclusion((org.gedcomx.conclusion.Conclusion) gedcomxElement);
        } else if (gedcomxElement instanceof org.gedcomx.source.SourceDescription) {
            n = this.addSource((org.gedcomx.source.SourceDescription) gedcomxElement);
        } else {
            throw new GenericError(
                    MessageResolver.resolve(Messages.GEDCOMX_UNKNOWN_TYPE));
        }
        return n;
    }

    @Override
    @Transactional
    public void createGraphByGedcomX(final Map<String, String> metadata,
            final Collection<Object> gedcomxElements)
            throws MissingFieldException {
        final Node rootNode = this.getInitialGraphNode();

        this.dao.setNodeProperties(rootNode, metadata);

        final List<NodeWrapper> wrappers = new ArrayList<>();

        for (final Object gedcomxElement : gedcomxElements) {
            wrappers.add(this.addTopLevelElement(gedcomxElement));
        }
        for (final NodeWrapper wrapper : wrappers) {
            wrapper.resolveReferences();
        }
    }

    private List<Object> getGedcomXByType(final String type) {
        final List<Object> gedcomXObjects = new ArrayList<>();

        final List<NodeWrapper> wrappers = this.getNodesByType(type);

        for (final NodeWrapper wrapper : wrappers) {
            gedcomXObjects.add(wrapper.getGedcomX());
        }
        return gedcomXObjects;
    }

    @Override
    public List<Object> getGedcomXFromGraph() {
        final List<Object> gedcomXObjects = new ArrayList<>();

        gedcomXObjects
                .addAll(this.getGedcomXByType(NodeTypes.AGENT.toString()));
        gedcomXObjects.addAll(this
                .getGedcomXByType(NodeTypes.SOURCE_DESCRIPTION.toString()));
        gedcomXObjects
                .addAll(this.getGedcomXByType(NodeTypes.PERSON.toString()));
        gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.RELATIONSHIP
                .toString()));
        gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.DOCUMENT
                .toString()));
        gedcomXObjects.addAll(this.getGedcomXByType(NodeTypes.PLACE_DESCRIPTION
                .toString()));
        gedcomXObjects
                .addAll(this.getGedcomXByType(NodeTypes.EVENT.toString()));

        return gedcomXObjects;
    }

    @Override
    public QueryResult getGraph() {
        final List<org.neo4j.graphdb.Node> nodes = new ArrayList<>();
        for (final org.neo4j.graphdb.Node node : this.dao.getAllNodes()) {
            nodes.add(node);
        }
        final List<org.neo4j.graphdb.Relationship> relationships = new ArrayList<>();
        for (final org.neo4j.graphdb.Relationship relationship : this.dao
                .getAllRelationships()) {
            relationships.add(relationship);
        }
        return new QueryResult(nodes, relationships);
    }

    private Node getInitialGraphNode() {
        return this.dao.getReferenceNode();
    }

    private String getMissingFieldErrorMessage(final MissingFieldException e) {
        if (e instanceof MissingRequiredPropertyException) {
            final MissingRequiredPropertyException missingRelationship = (MissingRequiredPropertyException) e;
            return MessageResolver.resolve(Messages.GEDCOMX_MISSING_FIELD,
                    missingRelationship.getId(),
                    missingRelationship.getNodeType(),
                    missingRelationship.getProperty());
        }
        if (e instanceof MissingRequiredRelationshipException) {
            final MissingRequiredRelationshipException missingRelationship = (MissingRequiredRelationshipException) e;
            return MessageResolver.resolve(Messages.GEDCOMX_MISSING_FIELD,
                    missingRelationship.getId(),
                    missingRelationship.getNodeType(),
                    missingRelationship.getRelationship());
        } else {
            return null;
        }
    }

    @Override
    public NodeWrapper getNodeByGedcomXId(final String id) {
        final Node node = this.dao.getSingleNodeFromIndex(
                IndexNames.IDS.toString(), GenericProperties.ID.toString(), id);

        return this.getWrapperByNode(node);

    }

    @Override
    public NodeWrapper getNodeById(final Long id) {
        return this.getWrapperByNode(this.dao.getNode(id));
    }

    @Override
    public List<NodeWrapper> getNodesByFilters(
            final Map<NodeProperties, Object> filters) {
        return null;
    }

    @Override
    public List<NodeWrapper> getNodesByType(final String type) {
        final Iterator<Node> nodes = this.dao.getNodesFromIndex(
                IndexNames.NODE_TYPES.toString(),
                GenericProperties.NODE_TYPE.toString(), type);

        final List<NodeWrapper> wrappers = new ArrayList<>();
        while (nodes.hasNext()) {
            wrappers.add(this.nodeWrapperFactory.wrapNode(
                    NodeTypes.valueOf(type), nodes.next()));
        }
        return wrappers;
    }

    private NodeWrapper getWrapperByNode(final Node node) {
        return this.getWrapperByNode(node, null);
    }

    private NodeWrapper getWrapperByNode(final Node node, String type) {
        if (type == null) {
            type = (String) this.dao.getNodeProperty(node,
                    GenericProperties.TYPE.toString());
        }
        return this.nodeWrapperFactory.wrapNode(NodeTypes.valueOf(type), node);
    }

    @Override
    public QueryResult searchNodesByCypher(final String query) {
        final String finalQuery = QueryResolver.resolve(query);

        final ExecutionResult result = this.dao.executeCypherQuery(finalQuery);

        final List<org.neo4j.graphdb.Node> nodes = new ArrayList<>();
        final List<org.neo4j.graphdb.Relationship> relationships = new ArrayList<org.neo4j.graphdb.Relationship>();

        for (final Map<String, Object> row : result) {
            for (final Entry<String, Object> column : row.entrySet()) {
                if (column.getValue() instanceof org.neo4j.graphdb.Node) {
                    nodes.add((org.neo4j.graphdb.Node) column.getValue());
                }
                if (column.getValue() instanceof org.neo4j.graphdb.Relationship) {
                    relationships.add((org.neo4j.graphdb.Relationship) column
                            .getValue());
                }
            }
        }

        return new QueryResult(nodes, relationships);
    }
}
