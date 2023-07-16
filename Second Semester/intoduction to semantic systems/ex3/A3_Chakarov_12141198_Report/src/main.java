import java.io.IOException;
import java.util.Scanner;

public class main {
    public static void main(String[] args) throws IOException {

        Basic basic = new Basic();
        Ontology ontology = new Ontology();
        Query query = new Query();

        while (true){
            try {
                System.out.println("Select One of the Operations\n" +
                        "1 - Basic operations\n" +
                        "2 - Ontology operations\n" +
                        "3 - Basic query operations\n" +
                        "4 - Exit");

                Scanner input = new Scanner(System.in);
                int operation = input.nextInt();

                if(operation == 1){
                    basic.execute();
                }else if (operation ==2){
                    ontology.execute();
                }else if (operation == 3){
                    query.execute();
                }else if(operation == 4){
                    System.exit(0);
                }else{
                    System.exit(0);
                }
            }
            catch(Exception e) {
                System.out.println("-------------------------------------------------------");
                System.out.println("There was a problem in the program.");
                System.out.println(e);
                System.out.println("-------------------------------------------------------");
                main(args);
            }
        }
    }




}