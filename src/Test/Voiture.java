import java.io.Serializable;

import java.util.List;

public class Voiture extends Vehicule implements Roulant, Volant, Serializable
{

    private List<Vehicule> vehicules;
    private List<Voiture> voitures;
    private List<Voiture> voituress;

    public Voiture()
    {
        super("bleu");
    }

}