public class Echo {
    private static final String name = "Echo";
    private static final String separator = "============================================================";
    private static void greet() {
        String banner = " _____     _           \n"
                      + "| ____|___| |__   ___  \n"
                      + "|  _| / __| '_ \\ / _ \\ \n"
                      + "| |__| (__| | | | (_) |\n"
                      + "|_____\\___|_| |_|\\___/ \n";
        String salutation = "Hello! I'm " + name + ".\n" +
                          "How can I help?";
        System.out.println(separator);
        System.out.println(banner);
        System.out.println(salutation);
        System.out.println(separator);
    }

    private static void farewell() {
        String valediction = "Bye!";
        System.out.println(valediction);
        System.out.println(separator);
    }
    public static void main(String[] args) {
        Echo.greet();
        Echo.farewell();
    }
}
