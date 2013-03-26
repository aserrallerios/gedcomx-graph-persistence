package org.gedcomx.persistence.graph.neo4j.model.constants;


public enum SourceProperties implements NodeProperties {

	ID, CITATION_TEMPLATE, NAME, EXTRACTED_CONCLUSIONS_REFERENCE, MEDIATOR_REFERENCE, SOURCE_DESCRIPTION_REFERENCE;

	private final boolean indexed;
	private final IndexNames indexName;

	private SourceProperties() {
		this.indexed = false;
		this.indexName = null;
	}

	private SourceProperties(final boolean indexed, final IndexNames indexName) {
		this.indexed = indexed;
		this.indexName = indexName;
	}

	@Override
	public IndexNames getIndexName() {
		return this.indexName;
	}

	@Override
	public boolean isIndexed() {
		return this.indexed;
	}
}