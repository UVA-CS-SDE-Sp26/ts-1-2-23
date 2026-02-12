import java.util.stream.Stream;

public class Userinterface {

    private Programcontrol pc =  new Programcontrol();

    public Userinterface() {
    }

    public String startProgram(String[] args) throws InvalidInputException {
        if ((args.length > 1)) {
            throw new InvalidInputException("Too many arguments. Accepted format: java TopSecret [argument]");
        } else {
            if (args.length == 0) {
                return this.pc.listFiles();
            }
            String argument = args[0];
            if (argument.chars().allMatch(Character::isDigit)) {
                return this.pc.handleRequest(argument);
            } else {
                throw new InvalidInputException("Invalid argument.");
            }
        }
    }

    public Programcontrol getPc() {
        return pc;
    }

    public void setPc(Programcontrol pc) {
        this.pc = pc;
    }
}
