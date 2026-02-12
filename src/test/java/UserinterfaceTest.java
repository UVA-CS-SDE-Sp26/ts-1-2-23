import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserinterfaceTest {

    @Test
    public void testEmptyInput() throws InvalidInputException {
        Userinterface ui = new Userinterface();
        assertEquals(ui.getPc().listFiles(),ui.startProgram(new String[]{}), "Empty input should list files");
    }

    @Test
    public void testOneInput() throws InvalidInputException {
        Userinterface ui = new Userinterface();
        assertEquals(ui.getPc().handleRequest(new String("11")),ui.startProgram(new String[] {"11"}),"One valid argument should be handled");
    }

    @Test
    public void testOneInputDiffNums() throws InvalidInputException {
        Userinterface ui = new Userinterface();
        assertEquals(ui.getPc().handleRequest(new String("11")),ui.startProgram(new String[] {"11"}),"One valid argument should be handled");
    }

    @Test
    public void testInputWithChar() throws InvalidInputException {
        Userinterface ui = new Userinterface();
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> ui.startProgram(new String[]{"1e"}));
        assertEquals("Invalid argument.", e.getMessage());
    }

    @Test
    public void testInputOnlyChar() throws InvalidInputException {
        Userinterface ui = new Userinterface();
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> ui.startProgram(new String[]{"e"}));
        assertEquals("Invalid argument.", e.getMessage());
    }

    @Test
    public void testTwoInputs() throws InvalidInputException {
        Userinterface ui = new Userinterface();
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> ui.startProgram(new String[]{"01","02"}));
        assertEquals("Too many arguments. Accepted format: java TopSecret [argument]", e.getMessage());
    }

    @Test
    public void testTwoInputsWithChar() throws InvalidInputException{
        Userinterface ui = new Userinterface();
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> ui.startProgram(new String[]{"ec","01"}));
        assertEquals("Too many arguments. Accepted format: java TopSecret [argument]", e.getMessage());
    }

    @Test
    public void testTwoInputsOnlyChar() throws InvalidInputException{
        Userinterface ui = new Userinterface();
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> ui.startProgram(new String[]{"ed","edg"}));
        assertEquals("Too many arguments. Accepted format: java TopSecret [argument]", e.getMessage());
    }


}
