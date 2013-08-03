package org.gedcomx.persistence.graph.neo4j.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gedcomx.persistence.graph.neo4j.model.Agent;
import org.gedcomx.persistence.graph.neo4j.model.Conclusion;
import org.gedcomx.persistence.graph.neo4j.model.Document;
import org.gedcomx.persistence.graph.neo4j.model.Event;
import org.gedcomx.persistence.graph.neo4j.model.NodeWrapper;
import org.gedcomx.persistence.graph.neo4j.model.Person;
import org.gedcomx.persistence.graph.neo4j.model.PlaceDescription;
import org.gedcomx.persistence.graph.neo4j.model.Relationship;
import org.gedcomx.persistence.graph.neo4j.model.SourceDescription;
import org.gedcomx.persistence.graph.neo4j.model.constants.NodeProperties;
import org.gedcomx.persistence.graph.neo4j.service.dto.QueryResult;

public interface GENgraphPersistenceService {

	Agent addAgent();

	Agent addAgent(org.gedcomx.agent.Agent agent);

	Conclusion addConclusion(org.gedcomx.conclusion.Conclusion conclusion);

	Document addDocument(String text);

	Event addEvent();

	Person addPerson();

	PlaceDescription addPlace(String name);

	Relationship addRelationship(Person p1, Person p2);

	SourceDescription addSource(
			org.gedcomx.source.SourceDescription sourceDescription);

	SourceDescription addSource(String citation);

	NodeWrapper addTopLevelElement(final Object gedcomxElement);

	void createGraphByGedcomX(Map<String, String> metadata,
			Collection<Object> gedcomxElements);

	List<Object> getGedcomXFromGraph();

	QueryResult getGraph();

	NodeWrapper getNodeByGedcomXId(String id);

	NodeWrapper getNodeById(Long id);

	List<NodeWrapper> getNodesByFilters(Map<NodeProperties, Object> filters);

	List<NodeWrapper> getNodesByType(String type);

	QueryResult searchNodesByCypher(String query);

}
