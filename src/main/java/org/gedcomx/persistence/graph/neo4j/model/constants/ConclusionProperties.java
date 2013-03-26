package org.gedcomx.persistence.graph.neo4j.model.constants;


public enum ConclusionProperties implements NodeProperties {

	ID, CONFIDENCE(true, IndexNames.TYPES), TEXT(true, IndexNames.OTHER), LATITUDE, LONGITUDE, TEMPORAL_DESCRIPTION_ORIGINAL, SPATIAL_DESCRIPTION, TEMPORAL_DESCRIPTION_FORMAL, ORIGINAL, DATE_ORIGINAL, DATE_FORMAL, PREFERRED, FULL_TEXT, QUALIFIERS, DETAILS, LIVING, PERSON_REFERENCE, PLACE_DESC_REFERENCE, PERSON1_REFERENCE, PERSON2_REFERENCE;

	private final boolean indexed;
	private final IndexNames indexName;

	private ConclusionProperties() {
		this.indexed = false;
		this.indexName = null;
	}

	private ConclusionProperties(final boolean indexed, final IndexNames indexName) {
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