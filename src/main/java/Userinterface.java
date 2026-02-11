import java.util.stream.Stream;

public class Userinterface {


    public Userinterface() {
    }

    public String startProgram(String[] args) {
        //Programcontrol pc = new Programcontrol();
        if ((args.length > 1)) {
            //Throw invalid argument
        } else {
            if (args.length == 0) {
                //return pc.handleRequest();
                return "test";
            }
            String argument = args[0];
            if (argument.chars().allMatch(Character::isDigit)) {
                //return pc.handleRequest(argument);
                return "test";
            } else {
                //Throw invalid argument
            }
            //return pc.handleRequest();
            return "test";
        }
        return "test";
    }

    private String errorMessage() {
        return "Structure should be: java TopSecret [argument]";
    }
}

/**Questions
 * help with creating valid argument exceptions
 * anything important to note
 */
