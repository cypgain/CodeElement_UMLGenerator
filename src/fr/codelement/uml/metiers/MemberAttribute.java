package fr.codelement.uml.metiers;

public class MemberAttribute extends Member
{

    public MemberAttribute(String name, String type, int visibility)
    {
        super(name, type, visibility);
    }

    @Override
    protected boolean isStatic(int visibility)
    {
        return (visibility / 24) >= 1;
    }

    @Override
    protected MemberVisibility getMemberVisibility(int visibility)
    {
        visibility %= 24;
        return visibility == 1 ? MemberVisibility.PUBLIC : visibility == 2 ? MemberVisibility.PRIVATE : MemberVisibility.PROTECTED;
    }



}
