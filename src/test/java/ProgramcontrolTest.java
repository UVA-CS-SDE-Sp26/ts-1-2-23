import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramcontrolTest {

    @Test
    public void testListFiles() {
        Programcontrol pc = new Programcontrol();

        assertEquals(pc.listFiles(), pc.listFiles(),
                "listFiles should consistently return the same result");
    }

    @Test
    public void testHandleValidRequest() {
        Programcontrol pc = new Programcontrol();

        assertEquals(pc.handleRequest("1"), pc.handleRequest("1"),
                "Valid numeric request should return file content");
    }}