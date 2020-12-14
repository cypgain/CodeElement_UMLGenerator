import java.io.Serializable;

public class Voiture extends Vehicule implements Roulant, Volant, Serializable
{

    public Voiture()
    {
        super("bleu");
    }

}