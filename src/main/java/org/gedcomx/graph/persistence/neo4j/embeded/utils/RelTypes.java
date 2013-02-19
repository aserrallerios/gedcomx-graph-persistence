package org.gedcomx.graph.persistence.neo4j.embeded.utils;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {

	HAS_CONCLUSION, HAS_AGENT, HAS_SOURCE_DESCRIPTION, IS_A, HAS_ADDRESS, HAS_ACCOUNT, HAS_IDENTIFIER, HAS_NAME, HAS_NOTE, HAS_SOURCE_REFERENCE, PLACE, PLACE_DESCRIPTION, HAS_ROLE, HAS_NAME_FORM, HAS_NAME_PART, PERSON, GENDER, HAS_FACT, PERSON2, PERSON1, SOURCE_DESCRIPTION, HAS_CITATION, HAS_SOURCE, HAS_TITLE, COMPONENT_OF, MEDIATOR, HAS_CITATION_FIELD

}
