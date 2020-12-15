public enum Actions
{
    RIGHT('e'),
    LEFT('f'),
    UP('g'),
    DOWN('d');

    private char test;

    Actions(char test)
    {
        this.test = test;
    }

    public char getTest()
    {
        return test;
    }
}
