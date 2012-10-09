package org.semanticweb.clipper.hornshiq.cli;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;

import lombok.Getter;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Getter
@Parameters(commandNames = { "gen" }, separators = "=", commandDescription = "Load ABox facts to Database")
public class CommandGenerateMapFile extends ReasoningCommandBase {

	public CommandGenerateMapFile(JCommander jc) {
		super(jc);
	}

	@Parameter(names = "-jdbcUrl", description = "JDBC URL")
	private String jdbcUrl;

	@Parameter(names = "-user", description = "User")
	private String user;

	@Parameter(names = "-password", description = "Password")
	private String password = "";

	@Override
	boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	void exec() {
		long t1 = System.currentTimeMillis();
		// String url = "jdbc:postgresql://localhost/dlvdb_university";
		Properties props = new Properties();
		props.setProperty("user", this.getUser());
		props.setProperty("password", this.getPassword());
		// props.setProperty("ssl", "true");
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection(jdbcUrl, props);
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		ShortFormProvider sfp = new SimpleShortFormProvider();

		OWLOntology ontology = null;
		for (String ontologyFile : this.getOntologyFiles()) {
			try {
				ontology = manager.loadOntologyFromOntologyDocument(new File(
						ontologyFile));

				header();

				mapConcepts(stmt, sfp, ontology);

				mapObjectRoles(stmt, sfp, ontology);

				mapResults();

				stmt.close();
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long t2 = System.currentTimeMillis();
		System.err.println("TIME: " + (t2 - t1));

	}

	private void mapResults() {

		String s = "CREATE result1 (att1) MAPTO result1 (integer) KEEP_AFTER_EXECUTION. \n";
		s += "CREATE result2 (att1,att2) MAPTO result2 (integer,integer) KEEP_AFTER_EXECUTION. \n";
		s += "CREATE result3 (att1,att2,att3) MAPTO result3 (integer,integer,integer) KEEP_AFTER_EXECUTION. \n";
		s += "CREATE result4 (att1,att2,att3,att4) MAPTO result4 (integer,integer,integer,integer) KEEP_AFTER_EXECUTION. \n";
		s += "CREATE result5 (att1,att2,att3,att4,att5) MAPTO result5 (integer,integer,integer,integer,integer) KEEP_AFTER_EXECUTION. \n";
		s += "CREATE result6 (att1,att2,att3,att4,att5,att6) MAPTO result6 (integer,integer,integer,integer,integer,integer) KEEP_AFTER_EXECUTION. \n";

		System.out.println(s);
	}

	private void header() {
		String[] split = jdbcUrl.split("/");
		String database = split[split.length - 1];
		String head = String.format("USEDB %s:%s:%s LIKE POSTGRES.", database,
				user, password);
		System.out.println(head);
		System.out.println();

	}

	private void mapConcepts(Statement stmt, ShortFormProvider sfp,
			OWLOntology ontology) throws SQLException {
		Set<OWLClass> classes = ontology.getClassesInSignature();

		for (OWLClass cls : classes) {
			String clsName = sfp.getShortForm(cls).toLowerCase();

			String s = String
					.format("USE %s(individual) \n"
							+ "AS (\"SELECT individual FROM concept_assertion, predicate_name \n"
							+ "WHERE concept_assertion.concept=predicate_name.id and  predicate_name.name='%s'\") \n"
							+ "MAPTO %s(integer).", clsName, clsName, clsName);

			System.out.println(s);
			System.out.println();
			// String sql = String.format(
			// "INSERT INTO predicate_name (name) VALUES ('%s')", clsName);
			// stmt.execute(sql);
		}
	}

	private void mapObjectRoles(Statement stmt, ShortFormProvider sfp,
			OWLOntology ontology) throws SQLException {

		Set<OWLObjectProperty> objectProperties = ontology
				.getObjectPropertiesInSignature(false);

		for (OWLObjectProperty property : objectProperties) {
			String propertyName = sfp.getShortForm(property).toLowerCase();

			String s = String
					.format("USE %s(a, b) \n"
							+ "AS (\"SELECT a,b FROM object_role_assertion, predicate_name \n"
							+ "WHERE object_role_assertion.object_role=predicate_name.id and  predicate_name.name='%s'\") \n"
							+ "MAPTO %s(integer, integer).", propertyName,
							propertyName, propertyName);

			System.out.println(s);
			System.out.println();

		}
	}

}
