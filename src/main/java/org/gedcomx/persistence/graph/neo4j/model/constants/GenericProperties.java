package org.gedcomx.persistence.graph.neo4j.model.constants;

public enum GenericProperties implements NodeProperties {
    ID(true, true, IndexNames.IDS), ABOUT, NODE_TYPE(true, false,
            IndexNames.NODE_TYPES), TYPE(true, false, IndexNames.TYPES), VALUE, LANG, MODIFIED, CHANGE_MESSAGE, SUBJECT, TEXT, CONTRIBUTOR_REFERENCE, NAME;

    private final boolean indexed;
    private final IndexNames indexName;
    private final boolean unique;

    private GenericProperties() {
        this.indexed = false;
        this.unique = false;
        this.indexName = null;
    }

    private GenericProperties(final boolean indexed, final boolean unique,
            final IndexNames indexName) {
        this.indexed = indexed;
        this.unique = unique;
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

    @Override
    public boolean isUnique() {
        return this.unique;
    }
}
