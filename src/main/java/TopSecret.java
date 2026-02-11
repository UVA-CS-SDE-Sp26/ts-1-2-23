/**
 * Commmand Line Utility
 */
public class TopSecret {
    public static void main(String args[]){
        Userinterface ui = new Userinterface();
        try {
            System.out.println(ui.startProgram(args));
        } catch (InvalidInputException e){
            System.out.println(e.getMessage());
        }
    }
}
