package org.gedcomx.graph.persistence.neo4j.embeded.model.utils;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {

	HAS_CONCLUSION, HAS_AGENT, HAS_SOURCE_DESCRIPTION, IS_A, HAS_ADDRESS, HAS_ACCOUNT, HAS_IDENTIFIER, HAS_NAME

}
