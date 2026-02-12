
/**
 * User interface class for terminal use of TopSecret program
 */
public class Userinterface {

    private Programcontrol pc =  new Programcontrol(); //program control instantiation

    /**
     * default constructor
     */
    public Userinterface() {
    }

    /**
     * Starts the program
     * @param args
     * @return String for output, from program control
     * @throws InvalidInputException
     */
    public String startProgram(String[] args) throws InvalidInputException {
        if ((args.length > 1)) { //if too many arguments throw error
            throw new InvalidInputException("Too many arguments. Accepted format: java TopSecret [argument]");
        } else {
            if (args.length == 0) { //if no arguments just list files
                return this.pc.listFiles();
            }
            String argument = args[0]; //there should only be one argument
            if (argument.chars().allMatch(Character::isDigit)) { //ensure argument is valid (no characters)
                return this.pc.handleRequest(argument); //pass on request to program controller
            } else {
                throw new InvalidInputException("Invalid argument.");
            }
        }
    }

    /**
     * Program control getter
     * @return pc
     */
    public Programcontrol getPc() {
        return pc;
    }

    /**
     * Program control setter
     * @param pc
     */
    public void setPc(Programcontrol pc) {
        this.pc = pc;
    }
}
