import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.XSD;
import java.util.Scanner;

public class Ontology {
    String URI = "http://semantics.id/ns/example/film#";
    public void execute(){
        OntModel model = ModelFactory.createOntologyModel();
        RDFDataMgr.read(model, "film.ttl");

        System.out.println("Choose an operation\n" +
                "1 - Add Class\n" +
                "2 - Add Property\n" +
                "3 - Add Instance");

        Scanner input = new Scanner(System.in);

        int operation = input.nextInt();
        if(operation == 1){
            System.out.println("Choose the name for new class");
            input = new Scanner(System.in);
            String className = input.nextLine();
            model.createClass(URI + className);
        }else if (operation ==2){
            System.out.println("Choose the name for new property");
            input = new Scanner(System.in);
            String property = input.nextLine();
            System.out.println("Write domain (class) of the property");
            input = new Scanner(System.in);
            String domain = input.nextLine();
            ObjectProperty objectProperty = model.createObjectProperty(URI + property);
            objectProperty.addDomain(model.getOntClass(URI + domain));
            System.out.println("Write the type of the property by writing the index\n" +
                    "1 - String\n" +
                    "2 - Integer\n" +
                    "3 - DateTime");
            input = new Scanner(System.in);
            int setPropertyRange = input.nextInt();
            Resource type;
            type = XSD.integer;
            if (setPropertyRange == 1) {
                type = XSD.integer;
            } else if (setPropertyRange == 2) {
                type = XSD.normalizedString;
            } else if (setPropertyRange == 3) {
                type = XSD.dateTime;
            }
            objectProperty.addRange(type);
        }else if (operation == 3){

            Boolean addAnotherInstance = true;

            while (addAnotherInstance) {

                System.out.println("Class of the instance");
                input = new Scanner(System.in);
                String instanceClassName = input.nextLine();

                System.out.println("Name of the instance");
                input = new Scanner(System.in);
                String instanceName = input.nextLine();

                OntClass ontologyClass = model.getOntClass(URI + instanceClassName);
                ontologyClass.createIndividual(URI + instanceName);

                System.out.println("Add another instance\n" +
                        "[1] Yes\n" +
                        "[0] No");
                input = new Scanner(System.in);
                int anotherInstance = input.nextInt();

                if (anotherInstance == 0) {
                    addAnotherInstance = false;
                }

            }

        }
    }
}
