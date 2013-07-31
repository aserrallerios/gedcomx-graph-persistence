package org.gedcomx.persistence.graph.neo4j.model;


public interface WrapperProvider {

	Agent createAgent(org.gedcomx.agent.Agent gedcomXAgent);

	Document createDocument(org.gedcomx.conclusion.Document gedcomXDocument);

	Event createEvent(org.gedcomx.conclusion.Event gedcomXEvent);

	Person createPerson(org.gedcomx.conclusion.Person gedcomXPerson);

	PlaceDescription createPlace(
			org.gedcomx.conclusion.PlaceDescription gedcomXPlace);

	Relationship createRelationship(
			org.gedcomx.conclusion.Relationship gedcomXRelationship);

	SourceDescription createSource(
			org.gedcomx.source.SourceDescription gedcomXSource);

}
