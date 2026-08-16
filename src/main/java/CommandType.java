public enum CommandType {
    // Gemini was used to learn about enums & enhanced enums
    LIST("list"),
    MARK("mark"),
    UNMARK("unmark"),
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    DELETE("delete"),
    BYE("bye");

    private final String keyword;

    CommandType(String keyword) {
        this.keyword = keyword;
    }

    public static CommandType fromInput(String input) throws EchoException {
        for (CommandType c : CommandType.values()) {
            if (input.equals(c.keyword) || input.startsWith(c.keyword + " ")) {
                return c;
            }
        }
        throw new UnknownCommandException();
    }

    public String getKeyword() {
        return keyword;
    }
}
