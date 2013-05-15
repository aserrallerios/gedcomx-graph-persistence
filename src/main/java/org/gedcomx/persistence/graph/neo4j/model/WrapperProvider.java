package org.gedcomx.persistence.graph.neo4j.model;

import org.gedcomx.persistence.graph.neo4j.exceptions.MissingFieldException;

public interface WrapperProvider {

    Agent createAgent(org.gedcomx.agent.Agent gedcomXAgent)
            throws MissingFieldException;

    Document createDocument(org.gedcomx.conclusion.Document gedcomXDocument)
            throws MissingFieldException;

    Event createEvent(org.gedcomx.conclusion.Event gedcomXEvent)
            throws MissingFieldException;

    Person createPerson(org.gedcomx.conclusion.Person gedcomXPerson)
            throws MissingFieldException;

    PlaceDescription createPlace(
            org.gedcomx.conclusion.PlaceDescription gedcomXPlace)
            throws MissingFieldException;

    Relationship createRelationship(
            org.gedcomx.conclusion.Relationship gedcomXRelationship)
            throws MissingFieldException;

    SourceDescription createSource(
            org.gedcomx.source.SourceDescription gedcomXSource)
            throws MissingFieldException;

}
