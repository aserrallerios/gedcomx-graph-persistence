/**
 * Copyright 2012 Intellectual Reserve, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gedcomx.persistence.graph.neo4j.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gedcomx.common.ResourceReference;
import org.gedcomx.common.URI;
import org.gedcomx.conclusion.Fact;
import org.gedcomx.conclusion.Gender;
import org.gedcomx.conclusion.Name;
import org.gedcomx.conclusion.NameForm;
import org.gedcomx.conclusion.Person;
import org.gedcomx.conclusion.PlaceReference;
import org.gedcomx.conclusion.Relationship;
import org.gedcomx.types.FactType;
import org.gedcomx.types.GenderType;
import org.gedcomx.types.NameType;
import org.gedcomx.types.RelationshipType;

public class ExampleGedcomxFileData {
    public static List<Object> create() {
        // /////////////////////////////////////////////////////////////////////////////////////
        // primary

        Name name = new Name();
        name.setKnownType(NameType.BirthName);
        name.setNameForms(new ArrayList<NameForm>());
        name.getNameForms().add(new NameForm());
        name.getNameForms().get(0).setFullText("Israel Heaton");

        Gender gender = new Gender();
        gender.setKnownType(GenderType.Male);

        final org.gedcomx.conclusion.Date birthDate = new org.gedcomx.conclusion.Date();
        birthDate.setOriginal("30 January 1880");
        final PlaceReference birthPlace = new PlaceReference();
        birthPlace.setOriginal("Orderville, UT");
        final Fact factBirth = new Fact();
        factBirth.setKnownType(FactType.Birth);
        factBirth.setDate(birthDate);
        factBirth.setPlace(birthPlace);

        final org.gedcomx.conclusion.Date deathDate = new org.gedcomx.conclusion.Date();
        deathDate.setOriginal("29 August 1936");
        final PlaceReference deathPlace = new PlaceReference();
        deathPlace.setOriginal("Kanab, Kane, UT");
        final Fact factDeath = new Fact();
        factDeath.setKnownType(FactType.Death);
        factDeath.setDate(deathDate);
        factDeath.setPlace(deathPlace);

        final Person searchedPerson1 = new Person();
        searchedPerson1.setId("98765");
        searchedPerson1.setPersistentId(URI
                .create("http://familysearch.org/persons/98765"));
        searchedPerson1.setNames(Arrays.asList(name));
        searchedPerson1.setGender(gender);
        searchedPerson1.setFacts(new ArrayList<Fact>());
        searchedPerson1.getFacts().add(factBirth);
        searchedPerson1.getFacts().add(factDeath);

        // /////////////////////////////////////////////////////////////////////////////////////
        // primary's father

        gender = new Gender();
        gender.setKnownType(GenderType.Male);

        name = new Name();
        name.setNameForms(new ArrayList<NameForm>());
        name.getNameForms().add(new NameForm());
        name.getNameForms().get(0).setFullText("Jonathan Heaton");

        final Person searchedPerson1Father = new Person();
        searchedPerson1Father.setId("87654");
        searchedPerson1Father.setNames(new ArrayList<Name>());
        searchedPerson1Father.getNames().add(name);
        searchedPerson1Father.setGender(gender);

        // /////////////////////////////////////////////////////////////////////////////////////
        // primary's mother

        gender = new Gender();
        gender.setKnownType(GenderType.Female);

        name = new Name();
        name.setNameForms(new ArrayList<NameForm>());
        name.getNameForms().add(new NameForm());
        name.getNameForms().get(0).setFullText("Clarissa Hoyt");

        final Person searchedPerson1Mother = new Person();
        searchedPerson1Mother.setId("76543");
        searchedPerson1Mother.setNames(new ArrayList<Name>());
        searchedPerson1Mother.getNames().add(name);
        searchedPerson1Mother.setGender(gender);

        // /////////////////////////////////////////////////////////////////////////////////////
        // primary's spouse

        gender = new Gender();
        gender.setKnownType(GenderType.Female);

        name = new Name();
        name.setNameForms(new ArrayList<NameForm>());
        name.getNameForms().add(new NameForm());
        name.getNameForms().get(0).setFullText("Charlotte Cox");

        final Person searchedPerson1Spouse = new Person();
        searchedPerson1Spouse.setId("65432");
        searchedPerson1Spouse.setNames(new ArrayList<Name>());
        searchedPerson1Spouse.getNames().add(name);
        searchedPerson1Spouse.setGender(gender);

        // /////////////////////////////////////////////////////////////////////////////////////
        // primary's child

        gender = new Gender();
        gender.setKnownType(GenderType.Male);

        name = new Name();
        name.setNameForms(new ArrayList<NameForm>());
        name.getNameForms().add(new NameForm());
        name.getNameForms().get(0).setFullText("Alma Heaton");

        final Person searchedPerson1Child = new Person();
        searchedPerson1Child.setId("54321");
        searchedPerson1Child.setNames(new ArrayList<Name>());
        searchedPerson1Child.getNames().add(name);
        searchedPerson1Child.setGender(gender);

        // /////////////////////////////////////////////////////////////////////////////////////
        // build relationships

        final ResourceReference primary1Ref = new ResourceReference();
        primary1Ref.setResource(new URI("#" + searchedPerson1.getId()));

        final ResourceReference father1Ref = new ResourceReference();
        father1Ref.setResource(new URI("#" + searchedPerson1Father.getId()));

        final ResourceReference mother1Ref = new ResourceReference();
        mother1Ref.setResource(new URI("#" + searchedPerson1Mother.getId()));

        final ResourceReference spouse1Ref = new ResourceReference();
        spouse1Ref.setResource(new URI("#" + searchedPerson1Spouse.getId()));

        final ResourceReference child1Ref = new ResourceReference();
        child1Ref.setResource(new URI("#" + searchedPerson1Child.getId()));

        final Relationship relToFather = new Relationship();
        relToFather.setId("RRRR-F01");
        relToFather.setPerson1(father1Ref);
        relToFather.setPerson2(primary1Ref);
        relToFather.setKnownType(RelationshipType.ParentChild);

        final Relationship relToMother = new Relationship();
        relToMother.setId("RRRR-M01");
        relToMother.setPerson1(mother1Ref);
        relToMother.setPerson2(primary1Ref);
        relToMother.setKnownType(RelationshipType.ParentChild);

        final Relationship relToSpouse = new Relationship();
        relToSpouse.setId("RRRR-S01");
        relToSpouse.setPerson1(primary1Ref);
        relToSpouse.setPerson2(spouse1Ref);
        relToSpouse.setKnownType(RelationshipType.Couple);

        final Relationship relToChild = new Relationship();
        relToChild.setId("RRRR-C01");
        relToChild.setPerson1(primary1Ref);
        relToChild.setPerson2(child1Ref);
        relToChild.setKnownType(RelationshipType.ParentChild);

        // /////////////////////////////////////////////////////////////////////////////////////
        // build list of objects

        final List<Object> resources = new ArrayList<Object>();
        resources.add(searchedPerson1);
        resources.add(searchedPerson1Father);
        resources.add(searchedPerson1Mother);
        resources.add(searchedPerson1Spouse);
        resources.add(searchedPerson1Child);
        resources.add(relToFather);
        resources.add(relToMother);
        resources.add(relToSpouse);
        resources.add(relToChild);

        return resources;
    }
}
