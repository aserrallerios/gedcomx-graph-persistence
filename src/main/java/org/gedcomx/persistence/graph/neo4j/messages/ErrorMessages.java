package org.gedcomx.persistence.graph.neo4j.messages;

public abstract class ErrorMessages {

	public static final String GEDCOMX_MISSING_FIELD = "GedcomX object missing field or relation, the node won't be created";
	public static final String GEDCOMX_UNKNOWN_TYPE = "Unknown GedcomX Top level type";
	public static final String GEDCOMX_CONCLUSION_TYPE = "Unknown GedcomX Conclusion type";
	public static final String SERVICE_CANT_INSTANTIATE = "Can't instantiate service";
	public static final String DAO_CANT_LOAD_PROPERTIES = "Can't read DAO config";
}
