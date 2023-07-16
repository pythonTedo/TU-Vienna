import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.ReasonerFactory;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import java.util.Scanner;

public class Basic {
        String URI = "http://semantics.id/ns/example/film#";
        public void execute() throws FileNotFoundException {
                OntModelSpec ontModelSpec = new OntModelSpec(OntModelSpec.OWL_MEM_TRANS_INF);
                List<Resource> reasoners = ReasonerRegistry.theRegistry().getAllDescriptions().listSubjects().toList();
                for (int i = 0; i < reasoners.size(); i++) {
                        System.out.printf("%1$s - %2$s \n", i + 1, reasoners.get(i).getURI());
                }
                System.out.println("Write the number of the reasoner that you want to use: ");
                Scanner input = new Scanner(System.in);
                int selectedResoner = input.nextInt() - 1;
                ReasonerFactory reasonerFactory = ReasonerRegistry.theRegistry().getFactory(reasoners.get(selectedResoner).getURI());
                ontModelSpec.setReasonerFactory(reasonerFactory);

                OntModel model = ModelFactory.createOntologyModel(ontModelSpec);

                RDFDataMgr.read(model, "film.ttl");

                List<RDFFormat> rdfFormats = List.of(RDFFormat.RDFXML,
                        RDFFormat.JSONLD,
                        RDFFormat.TURTLE,
                        RDFFormat.NTRIPLES
                );

                for (int i = 0; i < rdfFormats.size(); i++) {
                        System.out.printf("[%1$s] %2$s \n", i + 1, rdfFormats.get(i).getLang());
                }

                System.out.println("----------------------------------------------");
                System.out.println("Please choose the index of one of the available rdf formats above: ");
                input = new Scanner(System.in);
                int chosenRdfFormat = input.nextInt() - 1;

                FileOutputStream fileOutput = new FileOutputStream(String.format("output.%1$s",
                        rdfFormats.get(chosenRdfFormat).getLang().getFileExtensions().stream().findFirst().get()));

                RDFDataMgr.write(fileOutput, model, rdfFormats.get(chosenRdfFormat));
        }
}
