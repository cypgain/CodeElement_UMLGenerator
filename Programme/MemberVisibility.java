public enum MemberVisibility
{
    PUBLIC('+'),
    PRIVATE('-'),
    PROTECTED('#');

    private char symbol;

    MemberVisibility(char symbol)
    {
        this.symbol = symbol;
    }

    public char getSymbol()
    {
        return this.symbol;
    }
}
