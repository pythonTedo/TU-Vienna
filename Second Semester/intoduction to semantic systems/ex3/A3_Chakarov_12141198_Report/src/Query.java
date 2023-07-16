import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Query {
    public void execute() throws FileNotFoundException {
        OntModel model = ModelFactory.createOntologyModel();
        RDFDataMgr.read(model, "film.ttl");

        System.out.println("1 Get the title of the movie and the name of the actor, order by name.\n" +
                "2 Get the actors and their birthdates.\n" +
                "3 Get the Film studio and the writers for each movie.\n" +
                "4 Get the names of the movies released between 2014 and 2020.\n" +
                "5 Get the name of the movie and the writer, orderd by the writers name.");
        Scanner input = new Scanner(System.in);
        int selectedQuery = input.nextInt();

        String queryString = "src/query/Q1.sparql";
        if (selectedQuery == 1) {
            queryString = "src/query/Q1.sparql";
        } else if (selectedQuery == 2) {
            queryString = "src/query/Q2.sparql";
        } else if (selectedQuery == 3) {
            queryString = "src/query/Q3.sparql";
        } else if (selectedQuery == 4) {
            queryString = "src/query/Q4.sparql";
        } else if (selectedQuery == 5) {
            queryString = "src/query/Q5.sparql";
        }

        String readQueryString = null;
        try {
            readQueryString = readFile(queryString, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        QueryExecution execution = QueryExecutionFactory.create(readQueryString, model);

        if (readQueryString.toUpperCase().contains("CONSTRUCT")) {
            Model constructModel = execution.execConstruct();
            FileOutputStream fileOutput2 = new FileOutputStream("QueryWithConstruct.ttl");
            RDFDataMgr.write(fileOutput2, constructModel, Lang.TURTLE);
        } else {
            ResultSet resultSet = execution.execSelect();
            ResultSetFormatter.out(resultSet);
            System.out.println(resultSet);
        }
    }
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}