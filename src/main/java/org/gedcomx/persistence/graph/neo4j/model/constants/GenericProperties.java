package org.gedcomx.persistence.graph.neo4j.model.constants;


public enum GenericProperties implements NodeProperties {
	ID(true, IndexNames.IDS), ABOUT, NODE_TYPE(true, IndexNames.NODE_TYPES), TYPE(true, IndexNames.TYPES), VALUE, LANG, MODIFIED, CHANGE_MESSAGE, SUBJECT, TEXT, CONTRIBUTOR_REFERENCE;

	private final boolean indexed;
	private final IndexNames indexName;

	private GenericProperties() {
		this.indexed = false;
		this.indexName = null;
	}

	private GenericProperties(final boolean indexed, final IndexNames indexName) {
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