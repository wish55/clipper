package org.semanticweb.clipper.hornshiq.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.semanticweb.clipper.cqparser.CQParser;
import org.semanticweb.clipper.hornshiq.rule.CQ;
import org.semanticweb.clipper.hornshiq.sparql.SparqlParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import lombok.Getter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Common base class for ComamndQuery and CommandRewrite
 * 
 * @author xiao
 * 
 */
@Getter
public abstract class ReasoningCommandBase extends CommandBase {

	@Parameter(description = "<ontology1.owl> ... <ontologyk.owl>")
	protected List<String> ontologyFiles;

	@Parameter(names = "-cq", description = "<query.cq> query file in CQ format")
	protected String cqFile;

	@Parameter(names = "-sparql", description = "<query.sparql> query file in SPARQL format")
	protected String sparqlFile;

	@Parameter(names = { "-r", "-rewriter" }, description = "rewriter", hidden = true)
	protected String rewriter = "new";
	
	public ReasoningCommandBase(JCommander jc) {
		super(jc);
		jc.addCommand(this);
	}

	protected CQ parseCQ(Set<OWLOntology> ontologies) {
		CQ cq = null;
	
		if (sparqlFile != null) {
	
			try {
				SparqlParser sparqlParser = new SparqlParser(sparqlFile);
				cq = sparqlParser.query();
			} catch (RecognitionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (cqFile != null) {
			CQParser parser;
			try {
				parser = new CQParser(new File(cqFile), ontologies);
				cq = parser.parse();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		return cq;
	}

	protected Set<OWLOntology> loadOntologies() {
		Set<OWLOntology> ontologies = new HashSet<OWLOntology>();
	
		for (String ontologyFile : this.getOntologyFiles()) {
			try {
				OWLOntology ontology = OWLManager.createOWLOntologyManager()
						.loadOntologyFromOntologyDocument(
								new File(ontologyFile));
	
				ontologies.add(ontology);
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}
		return ontologies;
	}

	protected String errorMessage;

}