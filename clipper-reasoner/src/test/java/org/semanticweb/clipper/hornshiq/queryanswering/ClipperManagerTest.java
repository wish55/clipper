package org.semanticweb.clipper.hornshiq.queryanswering;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.clipper.hornshiq.ontology.ClipperAtomSubSomeAxiom;
import org.semanticweb.clipper.hornshiq.ontology.ClipperAxiom;
import org.semanticweb.clipper.hornshiq.ontology.ClipperConceptAssertionAxiom;
import org.semanticweb.clipper.hornshiq.ontology.ClipperPropertyAssertionAxiom;
import org.semanticweb.clipper.util.SymbolEncoder;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLPropertyAssertionObject;
import org.semanticweb.owlapi.model.OWLPropertyExpression;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ClipperManagerTest {

    ClipperManager km = ClipperManager.getInstance();

	private OWLOntologyManager manager;
	private OWLDataFactory factory;
	private OWLIndividual a;
	private OWLIndividual b;
	private OWLIndividual c;
	private OWLClass A;
	private OWLClass B;
	private OWLClass C;
	private OWLObjectProperty r;
	OWLObjectInverseOf inv_r;
	private OWLDataProperty r_d;
	private OWLObjectProperty s;
	private OWLDataProperty s_d;
	private OWLClass A1;
	private OWLClass B1;
	private OWLClass C1;
	private OWLClass A2;
	private OWLClass B2;
	private OWLClass C2;
	private OWLClass A3;
	private OWLClass B3;
	private OWLClass C3;
	private OWLClass A4;
	private OWLClass B4;
	private OWLClass C4;

	@Before
	public void setUp() {
        km.reset();

		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		A = factory.getOWLClass(IRI.create("http://www.example.org/#A"));
		A1 = factory.getOWLClass(IRI.create("http://www.example.org/#A1"));
		A2 = factory.getOWLClass(IRI.create("http://www.example.org/#A2"));
		A3 = factory.getOWLClass(IRI.create("http://www.example.org/#A3"));
		A4 = factory.getOWLClass(IRI.create("http://www.example.org/#A4"));
		B = factory.getOWLClass(IRI.create("http://www.example.org/#B"));
		B1 = factory.getOWLClass(IRI.create("http://www.example.org/#B1"));
		B2 = factory.getOWLClass(IRI.create("http://www.example.org/#B2"));
		B3 = factory.getOWLClass(IRI.create("http://www.example.org/#B3"));
		B4 = factory.getOWLClass(IRI.create("http://www.example.org/#B4"));
		C = factory.getOWLClass(IRI.create("http://www.example.org/#C"));
		C1 = factory.getOWLClass(IRI.create("http://www.example.org/#C1"));
		C2 = factory.getOWLClass(IRI.create("http://www.example.org/#C2"));
		a = factory.getOWLNamedIndividual(IRI.create("http://www.example.org/#a"));
		b = factory.getOWLNamedIndividual(IRI.create("http://www.example.org/#b"));
		c = factory.getOWLNamedIndividual(IRI.create("http://www.example.org/#c"));
		r = factory.getOWLObjectProperty(IRI.create("http://www.example.org/#r"));

		inv_r = factory.getOWLObjectInverseOf(r);

		s = factory.getOWLObjectProperty(IRI.create("http://www.example.org/#s"));
		r_d = factory.getOWLDataProperty(IRI.create("http://www.example.org/#r_d"));
		s_d = factory.getOWLDataProperty(IRI.create("http://www.example.org/#s_d"));
	}

	@Test
	public void test001() {

		SymbolEncoder<OWLClass> owlClassEncoder = km.getOwlClassEncoder();
		SymbolEncoder<OWLPropertyExpression> owlDataPropertyEncoder = km.getOwlPropertyExpressionEncoder();
		SymbolEncoder<OWLPropertyExpression> owlObjectPropertyEncoder = km
				.getOwlPropertyExpressionEncoder();

		assertEquals(2, owlClassEncoder.getValueBySymbol(A));
		assertEquals(2, owlClassEncoder.getValueBySymbol(A));
		assertEquals(3, owlClassEncoder.getValueBySymbol(B));

		assertEquals(A, owlClassEncoder.getSymbolByValue(2));
		assertEquals(B, owlClassEncoder.getSymbolByValue(3));
	}

	@Test
	public void test002() {
		SymbolEncoder<OWLClass> owlClassEncoder = km.getOwlClassEncoder();

		SymbolEncoder<OWLPropertyExpression> owlObjectPropertyEncoder = km
				.getOwlPropertyExpressionEncoder();
		SymbolEncoder<OWLPropertyAssertionObject> owlIndividualEncoder = km.getOwlIndividualAndLiteralEncoder();

		int v_A = owlClassEncoder.getValueBySymbol(A);
		int v_B = owlClassEncoder.getValueBySymbol(B);
		int v_r = owlObjectPropertyEncoder.getValueBySymbol(r);
		int v_inv_r = owlObjectPropertyEncoder.getValueBySymbol(inv_r);

		int v_a = owlIndividualEncoder.getValueBySymbol(a);
		int v_b = owlIndividualEncoder.getValueBySymbol(b);

		List<ClipperAxiom> axioms = new ArrayList<ClipperAxiom>();
		axioms.add(new ClipperAtomSubSomeAxiom(v_A, v_r, v_B));
		axioms.add(new ClipperAtomSubSomeAxiom(v_A, v_inv_r, v_B));
		axioms.add(new ClipperConceptAssertionAxiom(v_A, v_b));
		axioms.add(new ClipperAtomSubSomeAxiom(v_A, v_r, v_B));
		axioms.add(new ClipperPropertyAssertionAxiom(v_r, v_a, v_b));

		for (ClipperAxiom ax : axioms) {
			System.out.println(ax);
		}
	}

	@Test
	public void testThing() {
		SymbolEncoder<OWLClass> owlClassEncoder = km.getOwlClassEncoder();
		SymbolEncoder<OWLPropertyExpression> owlObjectPropertyEncoder = km
		.getOwlPropertyExpressionEncoder();
		assertEquals(km.getThing(), owlClassEncoder.getValueBySymbol(OWLManager.getOWLDataFactory().getOWLThing()));
		assertEquals(km.getNothing(), owlClassEncoder.getValueBySymbol(OWLManager.getOWLDataFactory().getOWLNothing()));
		assertEquals(km.getTopProperty(), owlObjectPropertyEncoder.getValueBySymbol(OWLManager.getOWLDataFactory()
				.getOWLTopObjectProperty()));
		assertEquals(km.getBottomProperty(), owlObjectPropertyEncoder.getValueBySymbol(OWLManager.getOWLDataFactory()
				.getOWLBottomObjectProperty()));
	
		System.out.println("thing = " + km.getThing());
		System.out.println("nothing = " + km.getNothing());
		System.out.println("topProperty = " + km.getTopProperty());
		System.out.println("bottomProperty = " + km.getBottomProperty());
	}
}
