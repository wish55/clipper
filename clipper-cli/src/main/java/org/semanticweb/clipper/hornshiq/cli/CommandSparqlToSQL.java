package org.semanticweb.clipper.hornshiq.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import org.semanticweb.clipper.hornshiq.rule.CQ;
import org.semanticweb.clipper.util.Ucq2SqlConverter;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.Set;

@Parameters(commandNames = { "sparql2sql" },
separators = "=", commandDescription = "SPARQL to SQL ")
public class CommandSparqlToSQL  extends ReasoningCommandBase{
	
	public CommandSparqlToSQL(JCommander jc) {
		super(jc);
	}

	@Override
	boolean validate() {
		return true;
	}

	@Override
	void exec() {
		Set<OWLOntology> ontologies = loadOntologies();
		CQ cq = parseCQ(ontologies);
		String sql = new Ucq2SqlConverter().convert(cq);
		System.out.println(sql);
	}
}
