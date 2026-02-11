import java.util.stream.Stream;

public class Userinterface {


    public Userinterface() {
    }

    public String startProgram(String[] args) {
        Programcontrol pc = new Programcontrol();
        if ((args.length > 1)) {
            //Throw invalid argument
            return "invalid";
        } else {
            if (args.length == 0) {
                return pc.listFiles();
            }
            String argument = args[0];
            if (argument.chars().allMatch(Character::isDigit)) {
                return pc.handleRequest(argument);
            } else {
                //Throw invalid argument
                return "invalid";
            }
        }
    }
}

/**Questions
 * help with creating valid argument exceptions
 * gradle issue from before, how do I make sure this wont cause issues with grading
 * anything important to note
 */
