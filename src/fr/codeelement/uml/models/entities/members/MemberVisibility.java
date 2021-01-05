package fr.codeelement.uml.models.entities.members;

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

    /**
     * Recupere le symbole de la visibilité
     * @return
     */
    public char getSymbol()
    {
        return this.symbol;
    }
}
