package org.gedcomx.persistence.graph.neo4j.model.constants;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {

    HAS_CONCLUSION, HAS_AGENT, HAS_SOURCE_DESCRIPTION, IS_A, HAS_ADDRESS(true), HAS_ACCOUNT(
            true), HAS_IDENTIFIER(true), HAS_NAME, HAS_NOTE, HAS_SOURCE_REFERENCE, PLACE, PLACE_DESCRIPTION, HAS_ROLE(
            true), HAS_NAME_FORM(true), HAS_NAME_PART, PERSON, GENDER, HAS_FACT, PERSON2, PERSON1, DESCRIPTION, HAS_CITATION, HAS_SOURCE, HAS_TITLE, COMPONENT_OF, MEDIATOR, HAS_CITATION_FIELD, CONTRIBUTOR, ATTRIBUTION, HAS_MEDIA, HAS_QUALIFIER;

    private boolean ordered = false;

    private RelationshipTypes() {
    }

    private RelationshipTypes(final boolean ordered) {
        this.ordered = ordered;
    }

    public boolean isOrdered() {
        return this.ordered;
    }

}
