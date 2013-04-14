package org.gedcomx.persistence.graph.neo4j.model.constants;

public interface NodeProperties {

	public IndexNames getIndexName();

	public boolean isIndexed();

	public boolean isUnique();

	public String name();

}