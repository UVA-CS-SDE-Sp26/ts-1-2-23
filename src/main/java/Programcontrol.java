public class Programcontrol {

    private Filehandler filehandler;

    public Programcontrol() {
        this.filehandler = new Filehandler();
    }

    public String listFiles() {
        try {
            String[] files = filehandler.listFiles();

            StringBuilder result = new StringBuilder();

            for (int i = 0; i < files.length; i++) {
                result.append(String.format("%02d %s%n", i + 1, files[i]));
            }

            return result.toString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String handleRequest(String request) {
        try {
            String[] files = filehandler.listFiles();

            int index = Integer.parseInt(request) - 1;

            if (index < 0 || index >= files.length) {
                return "Invalid file number.";
            }

            String fileName = files[index];

            return filehandler.readFile(fileName);

        } catch (Exception e) {
            return e.getMessage();
        }
    }
}