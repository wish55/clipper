package org.semanticweb.clipper.hornshiq.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Parameters(commandNames = { "load" }, separators = "=", commandDescription = "Load ABox facts to Database")
public class CommandLoad extends DBCommandBase {

	private Connection conn;

	public CommandLoad(JCommander jc) {
		super(jc);
	}

	@Override
	boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	Map<String, Integer> concept2IdMap = new HashMap<String, Integer>();
	Map<String, Integer> objectRole2IdMap = new HashMap<String, Integer>();
	Map<String, Integer> individual2IdMap = new HashMap<String, Integer>();;

	@Override
	void exec() {
		long t1 = System.currentTimeMillis();
		conn = createConnection();

		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		ShortFormProvider sfp = new SimpleShortFormProvider();

		OWLOntology ontology = null;
		try {
			for (String ontologyFile : this.getOntologyFiles()) {
				ontology = manager.loadOntologyFromOntologyDocument(new File(
						ontologyFile));

				insertConcepts(stmt, sfp, ontology);
				//
				insertObjectRoles(stmt, sfp, ontology);

				insertIndividuals(stmt, ontology);

				insertConceptAssertionsOld(stmt, sfp, ontology);
				//
				insertObjectRoleAssertions(stmt, sfp, ontology);
				//
				manager.removeOntology(ontology);
				//
				// stmt.executeBatch();

				System.err.println(ontologyFile + " loaded!");
			}
			stmt.close();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
		System.out.println("TIME: " + (t2 - t1));

	}

	// @SuppressWarnings("unused")
	private void insertIndividuals(Statement stmt, OWLOntology ontology) {
		Set<OWLNamedIndividual> individuals = ontology
				.getIndividualsInSignature();

		if (true) {
			StringBuilder exisitingValuesBuilder = new StringBuilder();

			Set<String> newInds = new HashSet<String>();

			boolean first = true;
			for (OWLNamedIndividual ind : individuals) {
				newInds.add(ind.toString());
				if (!first)
					exisitingValuesBuilder.append(",");
				first = false;
				exisitingValuesBuilder.append("('").append(ind).append("')");
			}

			StringBuilder sqlExistingNamesBuilder = new StringBuilder();

			sqlExistingNamesBuilder
					.append("SELECT individual_name.name FROM (VALUES ");
			sqlExistingNamesBuilder.append(exisitingValuesBuilder.toString());
			sqlExistingNamesBuilder
					.append(") AS foo (name), individual_name WHERE foo.name = individual_name.name");

			StringBuilder sqlCountNewValuesBuilder = new StringBuilder();
			sqlCountNewValuesBuilder.append("SELECT COUNT(*) FROM (VALUES ");
			sqlCountNewValuesBuilder.append(exisitingValuesBuilder.toString());
			sqlCountNewValuesBuilder.append(") AS foo (name)");

			StringBuilder sqlBuilder3 = new StringBuilder();

			sqlBuilder3
					.append("SELECT individual_name.id, individual_name.name FROM (VALUES ");
			sqlBuilder3.append(exisitingValuesBuilder.toString());
			sqlBuilder3
					.append(") AS foo (name), individual_name WHERE foo.name = individual_name.name");

			try {
				Set<String> existingInds = new HashSet<String>();
				Statement statement = conn.createStatement();
				// System.out.println(sqlBuilder1);
				ResultSet rs = statement.executeQuery(sqlExistingNamesBuilder
						.toString());
				while (rs.next()) {
					String ind = rs.getString(1);
					existingInds.add(ind);
				}

				newInds.removeAll(existingInds);

				if (newInds.size() > 0) {

					StringBuilder newValuesBuilder = new StringBuilder();
					first = true;
					for (String ind : newInds) {
						if (!first)
							newValuesBuilder.append(",");
						first = false;
						newValuesBuilder.append("('").append(ind).append("')");
					}

					StringBuilder sqlBuilder4 = new StringBuilder();

					sqlBuilder4
							.append("INSERT INTO individual_name(name) VALUES ");
					sqlBuilder4.append(newValuesBuilder.toString());
					System.err.println(sqlBuilder4);
					int i = statement.executeUpdate(sqlBuilder4.toString());
					System.err.println("i=" + i);
				}
				//
				// System.out.println(sqlBuilder2);
				// ResultSet rs2 =
				// statement.executeQuery(sqlBuilder2.toString());
				// while (rs2.next()) {
				// System.out.println(rs2.getInt(1));
				// }

				// System.out.println(sqlBuilder3);
				ResultSet rs3 = statement.executeQuery(sqlBuilder3.toString());
				while (rs3.next()) {
					// System.out.println(rs3.getInt(1) + " " +
					// rs3.getString(2));
					individual2IdMap.put(rs3.getString(2), rs3.getInt(1));
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}
		}

		if (false) {
			String sql = "insert INTO individual_name (name)"
					+ "SELECT (?)"
					+ "WHERE NOT EXISTS (SELECT * FROM individual_name WHERE name=?)  ";

			try {
				conn.setAutoCommit(false);

				PreparedStatement preparedStatement = conn
						.prepareStatement(sql);

				for (OWLNamedIndividual ind : individuals) {
					preparedStatement.setString(1, ind.toString());
					preparedStatement.setString(2, ind.toString());
					preparedStatement.executeUpdate();

					conn.commit();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void insertConcepts(Statement stmt, ShortFormProvider sfp,
			OWLOntology ontology) throws SQLException {
		Set<OWLClass> classes = ontology.getClassesInSignature(true);

		for (OWLClass cls : classes) {
			String clsName = sfp.getShortForm(cls).toLowerCase();
			String sql = String
					.format("INSERT INTO predicate_name (name)"
							+ "SELECT ('%s')"
							+ "WHERE NOT EXISTS (SELECT * FROM predicate_name WHERE name='%s')  ",
							clsName, clsName);
			//stmt.addBatch(sql);
			 stmt.execute(sql);

//			sql = String.format("DROP TABLE IF EXISTS %s CASCADE", clsName);
//
//			stmt.addBatch(sql);
//			 stmt.execute(sql);
//
//			sql = String.format("CREATE TABLE %s ("
//					+ "individual integer NOT NULL )", clsName);
//
//			stmt.addBatch(sql);
//			 stmt.execute(sql);
		}
	}

	private void insertObjectRoles(Statement stmt, ShortFormProvider sfp,
			OWLOntology ontology) throws SQLException {

		Set<OWLObjectProperty> objectProperties = ontology
				.getObjectPropertiesInSignature(true);

		for (OWLObjectProperty property : objectProperties) {
			String propertyName = sfp.getShortForm(property).toLowerCase();
			String sql = String
					.format("INSERT INTO predicate_name (name)"
							+ "SELECT ('%s')"
							+ "WHERE NOT EXISTS (SELECT * FROM predicate_name WHERE name='%s')  ",
							propertyName, propertyName);
			//stmt.addBatch(sql);
			 stmt.execute(sql);

			sql = String
					.format("DROP TABLE IF EXISTS %s CASCADE", propertyName);

			//stmt.addBatch(sql);
			 stmt.execute(sql);

			sql = String.format("CREATE TABLE %s (" //
					+ "a integer NOT NULL, " //
					+ "b integer NOT NULL" + " )", propertyName);

			//stmt.addBatch(sql);
			 stmt.execute(sql);
		}
	}

	 private void insertConceptAssertionsOld(Statement stmt, ShortFormProvider
	 sfp,
	 OWLOntology ontology) throws SQLException {
	 Set<OWLClassAssertionAxiom> classAssertionAxioms = ontology.getAxioms(
	 AxiomType.CLASS_ASSERTION, false);
	
	 String sql =
	 "INSERT INTO concept_assertion (concept , individual)            "
	 + "SELECT predicate_name.id, individual_name.id                  "
	 + "FROM predicate_name, individual_name                          "
	 + "WHERE predicate_name.name = ? and individual_name.name = ?"
	 + "AND NOT EXISTS (SELECT * FROM concept_assertion "
	 + "WHERE predicate_name.id = concept_assertion.concept "
	 + "   AND individual_name.id = concept_assertion.individual)";
	
	 PreparedStatement preparedStatement = conn.prepareStatement(sql);
	
	 for (OWLClassAssertionAxiom axiom : classAssertionAxioms) {
	 OWLClassExpression classExpression = axiom.getClassExpression();
	 String tableName = sfp.getShortForm(classExpression.asOWLClass())
	 .toLowerCase();
	
	 preparedStatement.setString(1, tableName);
	 preparedStatement.setString(2, axiom.getIndividual().toString());
	 preparedStatement.executeUpdate();
	 //conn.commit();
	 }
	 }

	private void insertConceptAssertions(Statement stmt, ShortFormProvider sfp,
			OWLOntology ontology) throws SQLException {
		Set<OWLClassAssertionAxiom> classAssertionAxioms = ontology.getAxioms(
				AxiomType.CLASS_ASSERTION, false);

		String createRuleFormat = "CREATE RULE \"%1$s_on_duplicate_ignore\" AS ON INSERT TO \"%1$s\" \n"
				+ " WHERE EXISTS(SELECT 1 FROM %1$s \n"
				+ "           WHERE (%2$s, %3$s)=(NEW.%2$s, NEW.%3$s)) \n"
				+ " DO INSTEAD NOTHING;";
		
		String createRuleSql = String.format(createRuleFormat, "concept_assertion", "concept", "individual");
		
		//Statement stmt = conn.createStatement();
		
		System.err.println(createRuleSql);
		
		stmt.execute(createRuleSql);

		String insertFormat = "WITH  cname_iname (cname, iname) AS (VALUES %s)  "
				+ "INSERT INTO concept_assertion (concept, individual) "
				+ "SELECT predicate_name.id, individual_name.id                  "
				+ "FROM predicate_name, individual_name, cname_iname             "
				+ "WHERE predicate_name.name = cname_iname.cname "
				+ "    and individual_name.name = cname_iname.iname";
		

		StringBuilder valuesBuilder = new StringBuilder(); 
		boolean first = true;
		//int i = 0;
		for (OWLClassAssertionAxiom axiom : classAssertionAxioms) {
			//i++;
//			if (i > 3) break;
			OWLClassExpression classExpression = axiom.getClassExpression();
			String tableName = sfp.getShortForm(classExpression.asOWLClass())
					.toLowerCase();

			if (!first)
				valuesBuilder.append(",");
			first = false;
			valuesBuilder.append("('").append(tableName).append("', '").append(axiom.getIndividual()) .append("')");

		}
		
		String insertSql = String.format(insertFormat, valuesBuilder.toString());
		//System.err.println(insertSql);
		stmt.execute(insertSql);
		
		String dropRuleSql = String.format("DROP RULE \"%1$s_on_duplicate_ignore\" ON \"%1$s\"", "concept_assertion");
		System.err.println(dropRuleSql);
		stmt.execute(dropRuleSql);

	}

	private void insertObjectRoleAssertions(Statement stmt,
			ShortFormProvider sfp, OWLOntology ontology) throws SQLException {
		Set<OWLObjectPropertyAssertionAxiom> objectRoleAssertionAxioms = ontology
				.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION, false);
		
//		String createRuleFormat = "CREATE RULE \"%1$s_on_duplicate_ignore\" AS ON INSERT TO \"%1$s\" \n"
//				+ " WHERE EXISTS(SELECT 1 FROM %1$s \n"
//				+ "           WHERE (%2$s, %3$s, %4$s)=(NEW.%2$s, NEW.%3$s, NEW.%4$s)) \n"
//				+ " DO INSTEAD NOTHING;";
//		
//		String createRuleSql = String.format(createRuleFormat, "object_role_assertion", "object_role", "a", "b");
//		
//		//Statement stmt = conn.createStatement();
//		
//		System.err.println(createRuleSql);
//		
//		stmt.execute(createRuleSql);

		for (OWLObjectPropertyAssertionAxiom axiom : objectRoleAssertionAxioms) {
			OWLObjectProperty property = axiom.getProperty()
					.asOWLObjectProperty();

			String tableName = sfp.getShortForm(property).toLowerCase();
			String sql = String
					.format("INSERT INTO object_role_assertion (object_role, a, b)            "
							+ "SELECT predicate_name.id, ind1.id, ind2.id                  "
							+ "FROM predicate_name, individual_name as ind1, individual_name as ind2 "
							+ "WHERE predicate_name.name = '%s' and ind1.name = '%s' and ind2.name = '%s'"
							+ "AND NOT EXISTS (SELECT * FROM object_role_assertion "
							+ "WHERE predicate_name.id = object_role_assertion.object_role "
							+ "   AND ind1.id = object_role_assertion.a"
							+ "   AND ind2.id = object_role_assertion.b)",
							tableName, axiom.getSubject(), axiom.getObject());

			//stmt.addBatch(sql);
			 stmt.execute(sql);
		}
	}
}
