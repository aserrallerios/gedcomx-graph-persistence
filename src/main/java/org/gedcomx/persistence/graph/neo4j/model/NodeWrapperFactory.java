package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.model.constants.NodeTypes;
import org.neo4j.graphdb.Node;

public interface NodeWrapperFactory {

	Agent createAgent();

	Agent createAgent(org.gedcomx.agent.Agent gedcomXAgent);

	Document createDocument(org.gedcomx.conclusion.Document gedcomXDocument);

	Document createDocument(String text);

	Event createEvent();

	Event createEvent(org.gedcomx.conclusion.Event gedcomXEvent);

	Person createPerson();

	Person createPerson(org.gedcomx.conclusion.Person gedcomXPerson);

	PlaceDescription createPlace(
			org.gedcomx.conclusion.PlaceDescription gedcomXPlace);

	PlaceDescription createPlace(String name);

	Relationship createRelationship(
			org.gedcomx.conclusion.Relationship gedcomXRelationship);

	Relationship createRelationship(Person p1, Person p2);

	SourceDescription createSource(
			org.gedcomx.source.SourceDescription gedcomXSource);

	SourceDescription createSource(String citation);

	<T extends NodeWrapper> T wrapNode(Class<T> type, Node node);

	NodeWrapper wrapNode(NodeTypes type, Node node);
}
