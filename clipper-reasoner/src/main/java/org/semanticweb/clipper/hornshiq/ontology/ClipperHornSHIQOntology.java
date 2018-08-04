package org.semanticweb.clipper.hornshiq.ontology;

import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClipperHornSHIQOntology {

	private List<ClipperAndSubAtomAxiom> andSubAtomAxioms;

	private List<ClipperDisjointObjectPropertiesAxiom> disjointObjectPropertiesAxioms;

	private List<ClipperAtomSubSomeAxiom> atomSubSomeAxioms;

	private List<ClipperAtomSubAllAxiom> atomSubAllAxioms;

	private List<ClipperAtomSubMaxOneAxiom> atomSubMaxOneAxioms;

	private List<ClipperAtomSubMinAxiom> atomSubMinAxioms;

	private List<ClipperSomeSubAtomAxiom> someSubAtomAxioms;

	private List<ClipperConceptAssertionAxiom> conceptAssertionAxioms;

	private List<ClipperPropertyAssertionAxiom> propertyAssertionAxioms;

	private List<ClipperSubPropertyAxiom> subPropertyAxioms;

	private List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms;

	private Set<ClipperTransitivityAxiom> transitivityAxioms;

	TIntHashSet aboxConcepts;

	public ClipperHornSHIQOntology() {
		this.andSubAtomAxioms = new ArrayList<>();
		this.atomSubMaxOneAxioms = new ArrayList<>();
		this.atomSubMinAxioms = new ArrayList<>();
		this.atomSubSomeAxioms = new ArrayList<>();
		this.atomSubAllAxioms = new ArrayList<>();
		this.conceptAssertionAxioms = new ArrayList<>();
		this.disjointObjectPropertiesAxioms = new ArrayList<>();
		this.propertyAssertionAxioms = new ArrayList<>();
		this.someSubAtomAxioms = new ArrayList<>();
		this.subPropertyAxioms = new ArrayList<>();
		this.inversePropertyOfAxioms = new ArrayList<>();
		this.transitivityAxioms = new HashSet<>();
	}

	public List<ClipperAxiom> getAllAxioms() {

		List<ClipperAxiom> result = new ArrayList<>();
		result.addAll(andSubAtomAxioms);
		result.addAll(atomSubAllAxioms);
		result.addAll(atomSubMaxOneAxioms);
		result.addAll(atomSubMinAxioms);
		result.addAll(atomSubSomeAxioms);
		result.addAll(conceptAssertionAxioms);
		result.addAll(disjointObjectPropertiesAxioms);
		result.addAll(propertyAssertionAxioms);
		result.addAll(someSubAtomAxioms);
		result.addAll(subPropertyAxioms);
		result.addAll(inversePropertyOfAxioms);
		result.addAll(transitivityAxioms);

		return result;

	}


    public List<ClipperAndSubAtomAxiom> getAndSubAtomAxioms() {
        return this.andSubAtomAxioms;
    }

    public List<ClipperDisjointObjectPropertiesAxiom> getDisjointObjectPropertiesAxioms() {
        return this.disjointObjectPropertiesAxioms;
    }

    public List<ClipperAtomSubSomeAxiom> getAtomSubSomeAxioms() {
        return this.atomSubSomeAxioms;
    }

    public List<ClipperAtomSubAllAxiom> getAtomSubAllAxioms() {
        return this.atomSubAllAxioms;
    }

    public List<ClipperAtomSubMaxOneAxiom> getAtomSubMaxOneAxioms() {
        return this.atomSubMaxOneAxioms;
    }

    public List<ClipperAtomSubMinAxiom> getAtomSubMinAxioms() {
        return this.atomSubMinAxioms;
    }

    public List<ClipperSomeSubAtomAxiom> getSomeSubAtomAxioms() {
        return this.someSubAtomAxioms;
    }

    public List<ClipperConceptAssertionAxiom> getConceptAssertionAxioms() {
        return this.conceptAssertionAxioms;
    }

    public List<ClipperPropertyAssertionAxiom> getPropertyAssertionAxioms() {
        return this.propertyAssertionAxioms;
    }

    public List<ClipperSubPropertyAxiom> getSubPropertyAxioms() {
        return this.subPropertyAxioms;
    }

    public List<ClipperInversePropertyOfAxiom> getInversePropertyOfAxioms() {
        return this.inversePropertyOfAxioms;
    }

    public Set<ClipperTransitivityAxiom> getTransitivityAxioms() {
        return this.transitivityAxioms;
    }

    public TIntHashSet getAboxConcepts() {
        return this.aboxConcepts;
    }


}
