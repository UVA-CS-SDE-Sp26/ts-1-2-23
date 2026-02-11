import java.util.stream.Stream;

public class Userinterface {


    public Userinterface() {
    }

    public String startProgram(String[] args) throws InvalidInputException {
        Programcontrol pc = new Programcontrol();
        if ((args.length > 1)) {
            throw new InvalidInputException("Too many arguments. Accepted format: java TopSecret [argument]");
        } else {
            if (args.length == 0) {
                return pc.listFiles();
            }
            String argument = args[0];
            if (argument.chars().allMatch(Character::isDigit)) {
                return pc.handleRequest(argument);
            } else {
                throw new InvalidInputException("Invalid argument.");
            }
        }
    }
}
