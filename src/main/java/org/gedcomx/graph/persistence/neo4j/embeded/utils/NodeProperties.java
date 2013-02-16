package org.gedcomx.graph.persistence.neo4j.embeded.utils;

public interface NodeProperties {

	public static enum Agent implements NodeProperties {
		HOMEPAGE, OPENID, EMAILS, PHONES, STREET, STREET2, STREET3, VALUE, STATE_OR_PROVINCE, CITY, COUNTRY, POSTAL_CODE, ACCOUNT_NAME, SERVICE_HOMEPAGE, IDENTIFIER_TYPE;

		private final boolean indexed;
		private final IndexNodeNames indexName;

		private Agent() {
			this.indexed = false;
			this.indexName = null;
		}

		private Agent(final boolean indexed, final IndexNodeNames indexName) {
			this.indexed = indexed;
			this.indexName = indexName;
		}

		@Override
		public IndexNodeNames getIndexName() {
			return this.indexName;
		}

		@Override
		public boolean isIndexed() {
			return this.indexed;
		}

	}

	public static enum Conclusion implements NodeProperties {

		ID, CONFIDENCE(true, IndexNodeNames.TYPES), TEXT(true, IndexNodeNames.OTHER), ABOUT, LATITUDE, LONGITUDE, TEMPORAL_DESCRIPTION_ORIGINAL, SPATIAL_DESCRIPTION, TEMPORAL_DESCRIPTION_FORMAL, ORIGINAL, DATE_ORIGINAL, DATE_FORMAL, PREFERRED, FULL_TEXT, QUALIFIERS, DETAILS;

		private final boolean indexed;
		private final IndexNodeNames indexName;

		private Conclusion() {
			this.indexed = false;
			this.indexName = null;
		}

		private Conclusion(final boolean indexed, final IndexNodeNames indexName) {
			this.indexed = indexed;
			this.indexName = indexName;
		}

		@Override
		public IndexNodeNames getIndexName() {
			return this.indexName;
		}

		@Override
		public boolean isIndexed() {
			return this.indexed;
		}

	}

	public static enum Generic implements NodeProperties {
		ID(true, IndexNodeNames.IDS), NODE_TYPE(true, IndexNodeNames.NODE_TYPES), TYPE(true, IndexNodeNames.TYPES), VALUE, LANG, ATTRIBUTION_MODIFIED, ATTRIBUTION_CHANGE_MESSAGE;

		private final boolean indexed;
		private final IndexNodeNames indexName;

		private Generic() {
			this.indexed = false;
			this.indexName = null;
		}

		private Generic(final boolean indexed, final IndexNodeNames indexName) {
			this.indexed = indexed;
			this.indexName = indexName;
		}

		@Override
		public IndexNodeNames getIndexName() {
			return this.indexName;
		}

		@Override
		public boolean isIndexed() {
			return this.indexed;
		}
	}

	public static enum SourceDescription implements NodeProperties {

		ID;

		private final boolean indexed;
		private final IndexNodeNames indexName;

		private SourceDescription() {
			this.indexed = false;
			this.indexName = null;
		}

		private SourceDescription(final boolean indexed, final IndexNodeNames indexName) {
			this.indexed = indexed;
			this.indexName = indexName;
		}

		@Override
		public IndexNodeNames getIndexName() {
			return this.indexName;
		}

		@Override
		public boolean isIndexed() {
			return this.indexed;
		}
	}

	public IndexNodeNames getIndexName();

	public boolean isIndexed();

	public String name();

}
