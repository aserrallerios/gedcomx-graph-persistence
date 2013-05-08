package org.gedcomx.persistence.graph.neo4j;

import org.gedcomx.persistence.graph.neo4j.model.Person;

public interface WrapperProvider {

    Person createPerson(org.gedcomx.conclusion.Person gedcomXPerson);

}
