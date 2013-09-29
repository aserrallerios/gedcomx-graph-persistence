package org.gedcomx.persistence.graph.neo4j.model.constants;

public enum GenericProperties implements NodeProperties {
	ID(true, true, IndexNames.IDS), ABOUT, NODE_TYPE(true, false,
			IndexNames.NODE_TYPES), TYPE(true, false, IndexNames.TYPES), VALUE, LANG, MODIFIED, CHANGE_MESSAGE, SUBJECT, TEXT, CONTRIBUTOR_REFERENCE, NAME, EVIDENCE_REFERENCE;

	private final boolean indexed;
	private final IndexNames indexNames;
	private final boolean unique;

	private GenericProperties() {
		this.indexed = false;
		this.unique = false;
		this.indexNames = null;
	}

	private GenericProperties(final boolean indexed, final boolean unique,
			final IndexNames indexNames) {
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
