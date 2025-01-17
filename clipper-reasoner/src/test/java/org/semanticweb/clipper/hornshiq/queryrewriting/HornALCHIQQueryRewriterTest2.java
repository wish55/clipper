package org.semanticweb.clipper.hornshiq.queryrewriting;

import com.google.common.collect.Lists;
import gnu.trove.set.hash.TIntHashSet;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.clipper.hornshiq.ontology.ClipperInversePropertyOfAxiom;
import org.semanticweb.clipper.hornshiq.queryanswering.ClipperManager;
import org.semanticweb.clipper.hornshiq.queryanswering.EnforcedRelation;
import org.semanticweb.clipper.hornshiq.queryanswering.HornImplication;
import org.semanticweb.clipper.hornshiq.queryrewriting.HornALCHIQQueryRewriter;
import org.semanticweb.clipper.hornshiq.rule.Atom;
import org.semanticweb.clipper.hornshiq.rule.CQ;
import org.semanticweb.clipper.hornshiq.rule.Constant;
import org.semanticweb.clipper.hornshiq.rule.DLPredicate;
import org.semanticweb.clipper.hornshiq.rule.InternalCQParser;
import org.semanticweb.clipper.hornshiq.rule.NonDLPredicate;
import org.semanticweb.clipper.hornshiq.rule.Term;
import org.semanticweb.clipper.hornshiq.rule.Variable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * 
 * Test all cases when rewriting step can be applicable. See more in the thesis.
 */
public class HornALCHIQQueryRewriterTest2 {

	TIntHashSet s1; // {11}
	TIntHashSet s2; // {2}
	TIntHashSet s3; // {3}
	TIntHashSet s4; // {4}
	TIntHashSet s34; // {3,4}

	TIntHashSet r1; // {22}
	TIntHashSet r2; // {4}
	TIntHashSet r3; // {6}
	TIntHashSet r4; // {8}
	TIntHashSet r34; // {6,8}

	HornImplication imp1_2; // imp({11},{2})
	HornImplication imp1_3; // imp({11},{3})
	HornImplication imp2_3; // imp({2},{3})
	HornImplication imp2_4; // imp({2},{4})
	HornImplication imp3_4; // imp({3},{4});
	HornImplication imp1_234; // imp({11},{2,3,4})
	HornImplication imp2_34; // imp({2},{3,4};

	EnforcedRelation s34_r2_s3;// enf({3,4} {4},{3})
	EnforcedRelation s1_r2_s3; // enf({11},{4},{3})
	EnforcedRelation s2_r2_s3;// enf({2},{4},{3})
	EnforcedRelation s1_r2_s2; // enf({11},{4},{2})
	EnforcedRelation s1_r2_s234; // enf({11}{4}{2,3,4})

	// EnforcedRelation e2 = new EnforcedRelation(s1, r2, s2, s34);
	// EnforcedRelation e3 = new EnforcedRelation(s2, r2, s3);
	// EnforcedRelation e1= new EnforcedRelation(s1, r1, s2);

	private Atom createRoleAssertion(DLPredicate r, Term x1, Term x2) {
		List<Term> terms = new ArrayList<Term>();
		terms.add(x1);
		terms.add(x2);
		return new Atom(r, terms);
	}

	private Atom createConceptAssertion(DLPredicate c, Term x1) {
		List<Term> terms = new ArrayList<Term>();
		terms.add(x1);
		return new Atom(c, terms);
	}

	private Atom createHead(NonDLPredicate q, Term x1) {
		List<Term> terms = new ArrayList<Term>();
		terms.add(x1);
		return new Atom(q, terms);
	}

	@Before
	public void setup() {
		s1 = new TIntHashSet();
		s1.add(11);
		s2 = new TIntHashSet();
		s2.add(2);
		s3 = new TIntHashSet();
		s3.add(3);
		s4 = new TIntHashSet();
		s4.add(4);
		s34 = new TIntHashSet();
		s34.add(3);
		s34.add(4);

		r1 = new TIntHashSet();
		r1.add(22);
		r2 = new TIntHashSet();
		r2.add(4);
		r3 = new TIntHashSet();
		r3.add(6);
		r4 = new TIntHashSet();
		r4.add(8);
		r34 = new TIntHashSet();
		r34.add(6);
		r34.add(8);

		imp1_2 = new HornImplication(s1, 2);
		imp1_3 = new HornImplication(s1, 3);
		imp2_3 = new HornImplication(s2, 3);
		imp2_4 = new HornImplication(s2, 4);
		imp3_4 = new HornImplication(s3, 4);
		// imp1_234 = new HornImplication(s1, s2, s34);
		// imp2_34 = new HornImplication(s2, s3, s4);

		s34_r2_s3 = new EnforcedRelation(s34, r2, s3);
		s1_r2_s3 = new EnforcedRelation(s1, r2, s3);
		s2_r2_s3 = new EnforcedRelation(s2, r2, s3);
		s1_r2_s2 = new EnforcedRelation(s1, r2, s2);
		TIntHashSet s234 = new TIntHashSet();
		s234.add(2);
		s234.add(3);
		s234.add(4);
		s1_r2_s234 = new EnforcedRelation(s1, r2, s234);
	}

	@Test
	public void test01() {
		CQ cq = new CQ();
		DLPredicate r = new DLPredicate(4, 2);
		DLPredicate r1 = new DLPredicate(6, 2);
		DLPredicate a = new DLPredicate(2, 1);
		DLPredicate b = new DLPredicate(3, 1);
		Variable x0 = new Variable(0);
		Variable x1 = new Variable(1);
		Variable x2 = new Variable(2);
		// List<Term> term4= new ArrayList<>();
		NonDLPredicate q = new NonDLPredicate(0, 1);

		List<Term> t = new ArrayList<Term>();
		t.add(x0);
		Atom head = new Atom(q, t);
		cq.setHead(head);

		List<Term> terms0 = new ArrayList<Term>();
		terms0.add(x0);
		terms0.add(x1);

		Atom body0 = new Atom(r, terms0);

		List<Term> terms1 = new ArrayList<Term>();
		terms1.add(x0);

		Atom body1 = new Atom(a, terms1);

		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(x1);

		Atom body2 = new Atom(b, terms2);

		List<Term> terms3 = new ArrayList<Term>();
		terms3.add(new Constant(1));

		Atom body3 = new Atom(b, terms3);

		List<Atom> body = Lists.newArrayList();
        body.add(body0);
		body.add(body1);
		body.add(body2);
		// body.add(body3);

		cq.setBody(body);

		System.out.println("Input query:" + cq);
		// Set<Variable> vars = cq.getNonDistinguishedVars();
		// System.out.println("Non Distinguished Vars :" + vars);
		// System.out.println(" Rho w.r.t x1: " + cq.computeRho(x1));
		// System.out.println(" Rho w.r.t x0: " + cq.computeRho(x0));
		// System.out.println(" Renamed vars w.r.t x0: "
		// + cq.computeRenameVars(x0));
		// System.out.println(" Renamed vars w.r.t x0: "
		// + cq.computeRenameVars(x1));
		// TIntHashSet bs = new TIntHashSet();
		// bs.set(4);
		// bs.set(6);
		// System.out.println(" qPrime w.r.t x1 " + cq.computeQprime(bs, x1));
		// System.out.println(" Compute type of x1 " + cq.computeTypeOfX(x1));

		Set<EnforcedRelation> enfs = new HashSet<EnforcedRelation>();
		// enfs.add(s1_r2_s234);
		// s34_r2_s3 = new EnforcedRelation(s34, r2, s3);
		enfs.add(s34_r2_s3);
		System.out.println("Enfs :" + enfs);
		List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms = new ArrayList<ClipperInversePropertyOfAxiom>();
		HornALCHIQQueryRewriter qr = new HornALCHIQQueryRewriter(enfs, inversePropertyOfAxioms);
		System.out.println("Rewriting queries:");
		qr.rewrite(cq);
		Set<CQ> ucq = qr.getUcq();
		for (CQ query : ucq)
			System.out.println(query);
		InternalCQParser parser = new InternalCQParser();
		parser.setQueryString("q0(X0) :- r4(X0,X1), c2(X0), c3(X1).");
		CQ cq1 = parser.getCq();
		parser.setQueryString("q0(X1) :- c2(X1), c4(X1), c3(X1).");
		CQ cq2 = parser.getCq();

		assertTrue(ucq.contains(cq1));
		assertTrue(ucq.contains(cq2));
		// System.out.println(body);
	}

	@Test
	public void test02() {
		CQ cq = new CQ();
		DLPredicate r = new DLPredicate(4, 2);
		DLPredicate r1 = new DLPredicate(6, 2);
		DLPredicate a = new DLPredicate(2, 1);
		DLPredicate b = new DLPredicate(3, 1);
		Variable x0 = new Variable(0);
		Variable x1 = new Variable(1);
		Variable x2 = new Variable(2);
		// List<Term> term4= new ArrayList<>();
		NonDLPredicate q = new NonDLPredicate(0, 1);

		List<Term> t = new ArrayList<>();
		t.add(x0);
		Atom head = new Atom(q, t);
		cq.setHead(head);

		List<Term> terms0 = new ArrayList<>();
		terms0.add(x0);
		terms0.add(x1);

		Atom body0 = new Atom(r, terms0);

		List<Term> terms1 = new ArrayList<Term>();
		terms1.add(x0);

		Atom body1 = new Atom(a, terms1);

		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(x1);

		Atom body2 = new Atom(b, terms2);

		List<Term> terms3 = new ArrayList<Term>();
		terms3.add(new Constant(1));

		Atom body5 = new Atom(b, terms3);

		Term d = new Constant(1);
		List<Term> term4 = new ArrayList<Term>();
		term4.add(d);
		term4.add(x1);
		Atom body4 = new Atom(r, term4);

		List<Atom> body = Lists.newArrayList();
		body.add(body0);
		body.add(body1);
		body.add(body2);
		body.add(body4);
		body.add(body5);

		cq.setBody(body);

		System.out.println("Input query:" + cq);
		// Set<Variable> vars = cq.getNonDistinguishedVars();
		// System.out.println("Non Distinguished Vars :" + vars);
		// System.out.println(" Rho w.r.t x1: " + cq.computeRho(x1));
		// System.out.println(" Rho w.r.t x0: " + cq.computeRho(x0));
		// System.out.println(" Renamed vars w.r.t x0: "
		// + cq.computeRenameVars(x0));
		// System.out.println(" Renamed vars w.r.t x0: "
		// + cq.computeRenameVars(x1));
		// TIntHashSet bs = new TIntHashSet();
		// bs.set(4);
		// bs.set(6);
		// System.out.println(" qPrime w.r.t x1 " + cq.computeQprime(bs, x1));
		// System.out.println(" Compute type of x1 " + cq.computeTypeOfX(x1));

		Set<EnforcedRelation> enfs = new HashSet<EnforcedRelation>();
		// enfs.add(s1_r2_s234);
		// s34_r2_s3 = new EnforcedRelation(s34, r2, s3);
		enfs.add(s34_r2_s3);
		System.out.println("Enfs :" + enfs);
		List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms = new ArrayList<ClipperInversePropertyOfAxiom>();
		HornALCHIQQueryRewriter qr = new HornALCHIQQueryRewriter(enfs, inversePropertyOfAxioms);
		System.out.println("Rewriting queries:");
		qr.rewrite(cq);
		for (CQ query : qr.getUcq())
			System.out.println(query);

		// System.out.println(body);
	}

	@Test
	public void test03() {
		CQ cq = new CQ();
		List<Atom> body = Lists.newArrayList();
		DLPredicate r = new DLPredicate(4, 2);
		DLPredicate rBar = new DLPredicate(6, 2);
		DLPredicate c2 = new DLPredicate(2, 1);
		DLPredicate c3 = new DLPredicate(3, 1);
		Variable x0 = new Variable(0);
		Variable x1 = new Variable(1);
		Variable x2 = new Variable(2);

		// set head
		NonDLPredicate q = new NonDLPredicate(0, 1);
		List<Term> t = new ArrayList<Term>();
		t.add(x0);
		Atom head = new Atom(q, t);
		cq.setHead(head);
		// set body
		List<Term> terms01 = new ArrayList<Term>();
		terms01.add(x0);
		terms01.add(x1);
		Atom body0 = new Atom(r, terms01);
		body.add(body0);

		List<Term> term0 = new ArrayList<Term>();
		term0.add(x0);
		Atom body1 = new Atom(c2, term0);
		body.add(body1);

		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(x1);
		Atom body2 = new Atom(c3, terms2);
		body.add(body2);

		Term d1 = new Constant(1);
		List<Term> terms3 = new ArrayList<Term>();
		terms3.add(d1);
		Atom body3 = new Atom(c3, terms3);
		body.add(body3);

		List<Term> term4 = new ArrayList<Term>();
		term4.add(x1);
		term4.add(d1);
		Atom body4 = new Atom(rBar, term4);
		body.add(body4);

		cq.setBody(body);

		System.out.println("Input query:" + cq);

		// create enfs
		Set<EnforcedRelation> enfs = new HashSet<EnforcedRelation>();
		TIntHashSet s34 = new TIntHashSet();
		s34.add(3);
		s34.add(4);
		TIntHashSet s3 = new TIntHashSet();
		s3.add(3);
		TIntHashSet r4 = new TIntHashSet();
		r4.add(4);
		EnforcedRelation s34_r4_s3 = new EnforcedRelation(s34, r4, s3);
		enfs.add(s34_r4_s3);

		// creat InversePropertyAxioms
		List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms = new ArrayList<ClipperInversePropertyOfAxiom>();
		ClipperInversePropertyOfAxiom invAxiom1 = new ClipperInversePropertyOfAxiom(4, 6);
		inversePropertyOfAxioms.add(invAxiom1);

		System.out.println("Enfs :" + enfs);
		System.out.println("Enfs :" + inversePropertyOfAxioms);

		HornALCHIQQueryRewriter qr = new HornALCHIQQueryRewriter(enfs, inversePropertyOfAxioms);
		System.out.println("Rewriting queries:");
		qr.rewrite(cq);
		for (CQ query : qr.getUcq())
			System.out.println(query);

		Atom c2_d1 = createConceptAssertion(c2, d1);
		DLPredicate c4 = new DLPredicate(4, 1);
		Atom c4_d1 = createConceptAssertion(c4, d1);
		List<Atom> cq1body = Lists.newArrayList();
		cq1body.add(c2_d1);
        cq1body.add(body3);
		cq1body.add(c4_d1);
		Atom cq1head = createHead(q, d1);
		CQ cq1 = new CQ();
		cq1.setHead(cq1head);
		cq1.setBody(cq1body);
		assertTrue(qr.getUcq().contains(cq1));
		assertTrue(qr.getUcq().contains(cq));

	}

	@Test
	public void test04() {
		CQ cq = new CQ();
		List<Atom> body = Lists.newArrayList();
		DLPredicate r4 = new DLPredicate(4, 2);
		DLPredicate r6 = new DLPredicate(6, 2);
		DLPredicate r8 = new DLPredicate(8, 2);
		DLPredicate c2 = new DLPredicate(2, 1);
		DLPredicate c3 = new DLPredicate(3, 1);
		Variable x0 = new Variable(0);
		Variable x1 = new Variable(1);
		Variable x2 = new Variable(2);

		// set head
		NonDLPredicate q = new NonDLPredicate(0, 0);
		List<Term> t = new ArrayList<Term>();
		// t.add(x0);
		Atom head = new Atom(q);
		System.out.println(head.getTerms());
		cq.setHead(head);
		// set body
		Atom body5 = createRoleAssertion(r8, x0, x2);
		body.add(body5);

		Atom body0 = createRoleAssertion(r4, x0, x1);
		body.add(body0);

		Atom body1 = createConceptAssertion(c2, x0);
		body.add(body1);

		Atom body2 = createConceptAssertion(c3, x1);
		body.add(body2);

		Term d1 = new Constant(1);
		Atom body3 = createConceptAssertion(c3, d1);
		body.add(body3);

		Atom body4 = createRoleAssertion(r6, x1, d1);
		body.add(body4);

		cq.setBody(body);
		System.out.println("Input query:" + cq);
		// create enfs
		Set<EnforcedRelation> enfs = new HashSet<EnforcedRelation>();
		TIntHashSet cbs34 = new TIntHashSet();
		cbs34.add(3);
		cbs34.add(4);
		TIntHashSet cbs3 = new TIntHashSet();
		cbs3.add(3);
		TIntHashSet rs4 = new TIntHashSet();
		rs4.add(4);
		EnforcedRelation enf_34_4_3 = new EnforcedRelation(cbs34, rs4, cbs3);
		enfs.add(enf_34_4_3);

		TIntHashSet cbs2 = new TIntHashSet();
		cbs2.add(5);
		TIntHashSet rs8 = new TIntHashSet();
		rs8.add(8);
		EnforcedRelation enf_2_8_3 = new EnforcedRelation(cbs2, rs8, cbs3);
		enfs.add(enf_2_8_3);

		// creat InversePropertyAxioms
		List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms = new ArrayList<ClipperInversePropertyOfAxiom>();
		ClipperInversePropertyOfAxiom invAxiom1 = new ClipperInversePropertyOfAxiom(4, 6);
		inversePropertyOfAxioms.add(invAxiom1);

		System.out.println("Enfs :" + enfs);
		System.out.println("Enfs :" + inversePropertyOfAxioms);

		HornALCHIQQueryRewriter qr = new HornALCHIQQueryRewriter(enfs, inversePropertyOfAxioms);
		System.out.println("Rewriting queries:");
		qr.rewrite(cq);
		Set<CQ> ucq = qr.getUcq();
		for (CQ query : ucq)
			System.out.println(query);
		InternalCQParser parser = new InternalCQParser();
		parser.setQueryString("q0() :- r8(d1,X2), c2(d1), c3(d1), c4(d1).");
		CQ cq1 = parser.getCq();
		parser.setQueryString("q0() :- r8(X0,X2), r4(X0,X1), c2(X0), c3(X1), c3(d1), r6(X1,d1).");
		CQ cq2 = parser.getCq();
		parser.setQueryString("q0() :- c2(d1), c3(d1), c4(d1), c5(d1).");
		CQ cq3 = parser.getCq();
		parser.setQueryString("q0() :- r4(X2,X1), c2(X2), c3(X1), c3(d1), r6(X1,d1), c5(X2).");
		CQ cq4 = parser.getCq();
		assertTrue(ucq.contains(cq1));
		assertTrue(ucq.contains(cq2));
		assertTrue(ucq.contains(cq3));
		assertTrue(ucq.contains(cq4));
		// System.out.println(body);
	}

    @Ignore // FIXME
	@Test
	public void test05() { // consider the case of inverseOf(r) exist in enfs
		CQ cq;
		InternalCQParser cqParser = new InternalCQParser();
		cqParser.setQueryString("q0(X0) :- r4(X1,d1), c3(d1), c3(X1), r8(X0,X2), c2(X0), r3(X0,X1).");
		cq = cqParser.getCq();
		System.out.println("Input query:" + cq);

		// create enfs
		Set<EnforcedRelation> enfs = new HashSet<>();
		TIntHashSet cbs34 = new TIntHashSet(new int[]{3,4});

		TIntHashSet cbs3 = new TIntHashSet(new int[]{3});

		TIntHashSet rs4 = new TIntHashSet(new int[]{4});

		TIntHashSet rs3 = new TIntHashSet(new int[]{3});

		EnforcedRelation enf_34_4_3 = new EnforcedRelation(cbs34, rs3, cbs3);
		enfs.add(enf_34_4_3);

		TIntHashSet cbs2 = new TIntHashSet(new int[]{5});

		TIntHashSet rs8 = new TIntHashSet(new int[]{8});

		EnforcedRelation enf_2_8_3 = new EnforcedRelation(cbs2, rs8, cbs3);
		enfs.add(enf_2_8_3);

		// create InversePropertyAxioms
		List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms = new ArrayList<>();
		ClipperInversePropertyOfAxiom invAxiom1 = new ClipperInversePropertyOfAxiom(4, 6);
		inversePropertyOfAxioms.add(invAxiom1);

		System.out.println("Enfs :" + enfs);
		System.out.println("Invs :" + inversePropertyOfAxioms);

		HornALCHIQQueryRewriter qr = new HornALCHIQQueryRewriter(enfs, inversePropertyOfAxioms);
		System.out.println("Rewriting queries:");
		qr.rewrite(cq);
		Set<CQ> ucq = qr.getUcq();
		for (CQ query : ucq)
			System.out.println(query);

		InternalCQParser parser = new InternalCQParser();
		parser.setQueryString("q0(X2) :- r6(X1,d1), c2(X2), c3(d1), c3(X1), r4(X2,X1), c5(X2).");
		CQ cq1 = parser.getCq();
		// System.out.println(ucq.contains(cq1));
		// assertTrue(ucq.contains(cq1));
		// System.out.println(cq1);
		// assertTrue(ucq.contains(cq1));
		parser.setQueryString("q0(d1) :- c2(d1), c3(d1), c4(d1), c5(d1).");
		CQ cq2 = parser.getCq();
		assertTrue(ucq.contains(cq2));
		parser.setQueryString("q0(X0) :- r6(X1,d1), c3(X1), c3(d1), r8(X0,X2), c2(X0), r4(X0,X1).");
		CQ cq3 = parser.getCq();

		assertTrue(ucq.contains(cq3));
		parser.setQueryString("q0(d1) :- r8(d1,X2), c2(d1), c3(d1), c4(d1).");
		CQ cq4 = parser.getCq();

		assertTrue(ucq.contains(cq4));
		// System.out.println(body);
	}

    @Ignore // FIXME
	@Test
	public void test06() {
		CQ cq = new CQ();
		InternalCQParser cqParser = new InternalCQParser();
		cqParser.setQueryString("q0(X0) :- r6(X1,d1), c3(d1), c3(X1), r8(X0,X2), c2(X0), r4(X0,X1).");
		cq = cqParser.getCq();
		System.out.println("Input query:" + cq);
		ClipperManager.getInstance().setVerboseLevel(1);
		// create enfs
		Set<EnforcedRelation> enfs = new HashSet<EnforcedRelation>();
		TIntHashSet cbs34 = new TIntHashSet();
		cbs34.add(3);
		cbs34.add(4);
		TIntHashSet cbs3 = new TIntHashSet();
		cbs3.add(3);
		TIntHashSet rs4 = new TIntHashSet();
		rs4.add(4);
		TIntHashSet rs3 = new TIntHashSet();
		rs3.add(3);
		EnforcedRelation enf_34_4_3 = new EnforcedRelation(cbs34, rs3, cbs3);
		enfs.add(enf_34_4_3);

		TIntHashSet cbs2 = new TIntHashSet();
		cbs2.add(5);
		TIntHashSet rs8 = new TIntHashSet();
		rs8.add(8);
		EnforcedRelation enf_2_8_3 = new EnforcedRelation(cbs2, rs8, cbs3);
		enfs.add(enf_2_8_3);

		// creat InversePropertyAxioms
		List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms = new ArrayList<ClipperInversePropertyOfAxiom>();
		ClipperInversePropertyOfAxiom invAxiom1 = new ClipperInversePropertyOfAxiom(4, 6);
		inversePropertyOfAxioms.add(invAxiom1);

		System.out.println("Enfs :" + enfs);
		System.out.println("Enfs :" + inversePropertyOfAxioms);

		HornALCHIQQueryRewriter qr = new HornALCHIQQueryRewriter(enfs, inversePropertyOfAxioms);
		System.out.println("Rewriting queries:");
		qr.rewrite(cq);
		Set<CQ> ucq = qr.getUcq();
		for (CQ query : ucq){
            System.out.println(query);
        }
		InternalCQParser parser = new InternalCQParser();
		parser.setQueryString("q0(X2) :- r6(X1,d1), c2(X2), c3(d1), c3(X1), r4(X2,X1), c5(X2).");
		CQ cq1 = parser.getCq();
		// System.out.println(ucq.contains(cq1));
		// assertTrue(ucq.contains(cq1));
		// System.out.println(cq1);
		// assertTrue(ucq.contains(cq1));
		parser.setQueryString("q0(d1) :- c2(d1), c3(d1), c4(d1), c5(d1).");
		CQ cq2 = parser.getCq();
		assertTrue(ucq.contains(cq2));
		parser.setQueryString("q0(X0) :- r6(X1,d1), c3(X1), c3(d1), r8(X0,X2), c2(X0), r4(X0,X1).");
		CQ cq3 = parser.getCq();

		assertTrue(ucq.contains(cq3));
		parser.setQueryString("q0(d1) :- r8(d1,X2), c2(d1), c3(d1), c4(d1).");
		CQ cq4 = parser.getCq();

		assertTrue(ucq.contains(cq4));
		// System.out.println(body);
	}

	@Test
	public void test07() {
		CQ cq = new CQ();
		InternalCQParser cqParser = new InternalCQParser();
		cqParser.setQueryString("q(X1,X2) :- r4(X2,X3), r4(X1,X2).");
		cq = cqParser.getCq();
		System.out.println("Input query:" + cq);

		// create enfs
		Set<EnforcedRelation> enfs = new HashSet<EnforcedRelation>();
		TIntHashSet cbs2 = new TIntHashSet();
		cbs2.add(2);

		TIntHashSet rs4 = new TIntHashSet();
		rs4.add(4);

		EnforcedRelation enf_2_4_2 = new EnforcedRelation(cbs2, rs4, cbs2);
		enfs.add(enf_2_4_2);

		// creat InversePropertyAxioms
		List<ClipperInversePropertyOfAxiom> inversePropertyOfAxioms = new ArrayList<ClipperInversePropertyOfAxiom>();
		ClipperInversePropertyOfAxiom invAxiom1 = new ClipperInversePropertyOfAxiom(4, 6);
		inversePropertyOfAxioms.add(invAxiom1);

		System.out.println("Enfs :" + enfs);
		System.out.println("Enfs :" + inversePropertyOfAxioms);

		HornALCHIQQueryRewriter qr = new HornALCHIQQueryRewriter(enfs, inversePropertyOfAxioms);
		System.out.println("Rewriting queries:");
		qr.rewrite(cq);
		Set<CQ> ucq = qr.getUcq();
		for (CQ query : ucq)
			System.out.println(query);

	}

}
