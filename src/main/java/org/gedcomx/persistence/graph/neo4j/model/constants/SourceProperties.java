package org.gedcomx.persistence.graph.neo4j.model.constants;

public enum SourceProperties implements NodeProperties {

	ID, CITATION_TEMPLATE, NAME, EXTRACTED_CONCLUSIONS_REFERENCE, MEDIATOR_REFERENCE, SOURCE_DESCRIPTION_REFERENCE;

	private final boolean indexed;
	private final IndexNames indexNames;
	private final boolean unique;

	private SourceProperties() {
		this.indexed = false;
		this.unique = false;
		this.indexNames = null;
	}

	private SourceProperties(final boolean indexed, final boolean unique, final IndexNames indexNames) {
		this.indexed = indexed;
		this.unique = unique;
		this.indexNames = indexNames;
	}

	@Override
	public IndexNames getIndexName() {
		return this.indexNames;
	}

	@Override
	public boolean isIndexed() {
		return this.indexed;
	}

	@Override
	public boolean isUnique() {
		return this.unique;
	}
}