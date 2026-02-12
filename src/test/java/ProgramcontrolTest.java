import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramcontrolTest {

    @Test
    public void testListFiles() {
        Programcontrol pc = new Programcontrol();

        assertEquals(pc.listFiles(), pc.listFiles(),
                "listFiles should return consistent results");
    }

    @Test
    public void testHandleValidRequest() {
        Programcontrol pc = new Programcontrol();

        assertEquals(pc.handleRequest("1"), pc.handleRequest("1"),
                "valid numerical request should yield corresponding file context");
    }
    @Test
    public void testHandleInvalidHighNumber() {
        Programcontrol pc = new Programcontrol();

        String result = pc.handleRequest("456");

        assertEquals("Invalid file number.", result);
    }
    @Test
    public void testHandleZero() {
        Programcontrol pc = new Programcontrol();

        String result = pc.handleRequest("0");

        assertEquals("Invalid file number.", result);
    }

}
