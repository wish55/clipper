package org.semanticweb.clipper.hornshiq.queryanswering;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.junit.Test;
import org.semanticweb.clipper.hornshiq.rule.CQ;
import org.semanticweb.clipper.hornshiq.sparql.SparqlLexer;
import org.semanticweb.clipper.hornshiq.sparql.SparqlParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * @author kien
 * 
 */
public class TBoxReasonerTest {

	// /////////////////////////////////////////////////////////
	// Really test the whole system from here.
	// See the master thesis for more detail of the test cases.
	// /////////////////////////////////////////////////////////

	// ///////////////////////////////////////
	// TEST CASE FOR SATURATION
	// ///////////////////////////////////////

	/**
	 * Test case 1
	 */
	@Test
	public void testRoleInclusion() throws RecognitionException, OWLOntologyCreationException {


		String ontologyFile = "src/test/resources/AllTestCases/testRoleInclusion.owl";
		String sparql = "PREFIX uri: <http://www.kr.tuwien.ac.at.testRoleInclusion.owl#> \n"
				+ "SELECT ?x1 \n"
				+ "WHERE { \n"
				+ "  ?x1    uri:R2 ?x2  . \n"
				+ "} \n";
		String tempDatalogFile = "src/test/resources/AllTestCases/testRoleInclusion.dl";
		testQuery(ontologyFile, sparql, 1, tempDatalogFile);
	}

	/**
	 * Test case 2: Test Inverse Role Inclusion
	 * 
	 * @throws RecognitionException
	 */
	@Test
	public void testRoleInclusionWithInverse() throws RecognitionException, OWLOntologyCreationException {
		String tempDatalogFile = "src/test/resources/AllTestCases/testInverseRoleInclusion.dl";

		String ontologyFile = "src/test/resources/AllTestCases/testInverseRoleInclusion.owl";


		String sparql = "PREFIX uri: <http://www.kr.tuwien.ac.at.testRoleInclusion.owl#> \n"
				+ "SELECT ?x1 \n"
				+ "WHERE { \n"
				+ "  ?x2    uri:R2 ?x1  . \n"
				+ "} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);

	}

	/**
	 * Test case 3. Test Concept Inclusion
	 * **/
	@Test
	public void testConceptInclusion() throws RecognitionException, OWLOntologyCreationException {

		String tempDatalogFile = "src/test/resources/AllTestCases/testConceptInclusion.dl";

		String ontologyFile = "src/test/resources/AllTestCases/testConceptInclusion.owl";

		String sparql = "PREFIX uri: <http://www.kr.tuwien.ac.at.testConceptInclusion.owl#> \n"
				+ "SELECT ?x1 \n"
				+ "WHERE { \n"
				+ "  ?x1    uri:R ?x2  . \n"
				+ "  ?x2 a  uri:B2 . \n " + "} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);
	}

	/**
	 * Test case 4. Note that it's a little different from one in the thesis.
	 * Test Bottom rule
	 * **/
	@Test
	public void testBottomRule() throws RecognitionException, OWLOntologyCreationException {

		String tempDatalogFile = "src/test/resources/AllTestCases/testBottomRule.dl";

		String ontologyFile = "src/test/resources/AllTestCases/testBottomRule.owl";

		String sparql = "PREFIX uri: <http://www.kr.tuwien.ac.at#> \n"
				+ "SELECT ?x1 \n" + "WHERE { \n"
				+ "  ?x1  a  uri:Nothing  . \n"
                + "} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);
	}


	/**
	 * Test case 6 in the Thesis
	 * 
	 */
	@Test
	public void testForallRule1() throws RecognitionException, OWLOntologyCreationException {

		String tempDatalogFile = "src/test/resources/AllTestCases/testForallRule1.dl";

		String ontologyFile = "src/test/resources/AllTestCases/testForallRule1.owl";

		String sparql = "PREFIX uri: <http://www.kr.tuwien.ac.at#> \n"
				+ "SELECT ?x \n" + "WHERE { \n" + "  ?x  a  uri:A  . \n"
				+ "  ?x  a  uri:M  . \n" + "  ?x    uri:R ?y . \n"
				+ "  ?y  a  uri:N  . \n" + "  ?y  a  uri:B  . \n" +
				"} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);
	}

	/**
	 * Test case 7 in the thesis
	 * **/
	@Test
	public void testForallRule2() throws RecognitionException, OWLOntologyCreationException {

		String tempDatalogFile = "src/test/resources/AllTestCases/testForallRule2.dl";

		String ontologyFile = "src/test/resources/AllTestCases/testForallRule2.owl";

		String sparql = "PREFIX uri: <http://www.kr.tuwien.ac.at#> \n"
				+ "SELECT ?x \n" + "WHERE { \n" + "  ?x  a  uri:B ; \n"
				+ "      a  uri:M . \n " + "} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);

	}

	/**
	 * Test case 8 in the thesis
	 * Test rule: \forall_3
	 * **/
	@Test
	public void testForallRule3() throws RecognitionException, OWLOntologyCreationException {
		String tempDatalogFile = "src/test/resources/AllTestCases/testForallRule3.dl";

		String ontologyFile = "src/test/resources/AllTestCases/testForallRule3.owl";

		String sparql = "PREFIX uri: <http://www.kr.tuwien.ac.at#> \n"
				+ "SELECT ?x \n"
				+ "WHERE { \n"
				+ "  ?x  a  uri:A  ; \n"
				+ "      a  uri:C  ; \n"
				+ "         uri:R ?y . \n"
				+ "  ?y  a  uri:B1  ; \n"
				+ "      a  uri:B2  . \n"
				+ "} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);
	}

	/**
	 * Test case 9 in the thesis
	 * @throws RecognitionException
	 */
	@Test
	public void testAtMostOne_MergeChildren() throws RecognitionException, OWLOntologyCreationException {
		String tempDatalogFile = "src/test/resources/AllTestCases/testAtMostOne_MergeChildren.dl";

		String ontologyFile = "src/test/resources/AllTestCases/testAtMostOne_MergeChildren.owl";

		String sparql = "PREFIX ub: <http://www.kr.tuwien.ac.at#> \n"
				+ "SELECT ?x \n"
				+ "WHERE { \n"
				+ "  ?x    ub:R ?y . \n"
				+ "  ?y  a ub:B  ;	"
				+ "    a ub:C1  ;	"
				+ "    a ub:C2  .	"
				+ "} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);
	}

	/**
	 * Test case 10 in the thesis
	 * @throws RecognitionException
	 */
	@Test
	public void testAtMostOne_ParentChildCollapse() throws RecognitionException, OWLOntologyCreationException {

		String tempDatalogFile = "src/test/resources/AllTestCases/atMostParentChildCollapse.dl";

		String ontologyFile = "src/test/resources/AllTestCases/atMostParentChildCollapse.owl";

		String sparql = "PREFIX ub: <http://www.kr.tuwien.ac.at/testcase02.owl#> \n"
				+ "SELECT ?x \n"
				+ "WHERE { \n"
				+ "  ?x  a  ub:A  . \n"
				+ "  ?x  a  ub:B  .	" + "} \n";

		testQuery(ontologyFile, sparql, 1, tempDatalogFile);
	}
	

	private static void testQuery(String ontologyFile, String sparqlString, int expected, String tmpDatalogFile) throws OWLOntologyCreationException, RecognitionException {
		System.setProperty("entityExpansionLimit", "512000");
		QAHornSHIQ qaHornSHIQ = new QAHornSHIQ();
		//ClipperManager.getInstance().setNamingStrategy(NamingStrategy.INT_ENCODING);
		//ClipperManager.getInstance().setNamingStrategy(NamingStrategy.LOWER_CASE_FRAGMENT);
		qaHornSHIQ.setNamingStrategy(NamingStrategy.LOWER_CASE_FRAGMENT);
		//qaHornSHIQ.setQueryRewriter("new");
		ClipperManager.getInstance().setVerboseLevel(1);

		qaHornSHIQ.setDatalogFileName(tmpDatalogFile);

		qaHornSHIQ.setOntologyName(ontologyFile);
		OWLOntology ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(
				new File(ontologyFile));
		qaHornSHIQ.addOntology(ontology);


		System.out.println(sparqlString);

		CharStream stream = new ANTLRStringStream(sparqlString);
		SparqlLexer lexer = new SparqlLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		SparqlParser parser = new SparqlParser(tokenStream);
		CQ cq = parser.query();

		String queryString = cq.toString();
		System.out.println(queryString);
		qaHornSHIQ.setQuery(cq);
		qaHornSHIQ.execQuery();


		System.out.println("TBox reasoning time: " + qaHornSHIQ.getClipperReport().getReasoningTime()
				+ "  millisecond");
		System.out.println("Query rewriting time: "
				+ qaHornSHIQ.getClipperReport().getQueryRewritingTime() + "  millisecond");

		List<String> actualAnswers = qaHornSHIQ.getAnswers();

		assertEquals(expected, actualAnswers.size());
	}



}
