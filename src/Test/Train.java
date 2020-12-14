import java.util.Iterator;

/**
	Classe CodeElement
	* @author 	: -
	* @version 	: 1.0
*/

public class Train
{
	/*---------------*/
	/* Les attributs */
	/*---------------*/
	private int 		nbVehicule; 	// renseigne sur le nombre de véhicule
	private Vehicule[] 	tabVehicule; 	// renseigne sur le tableau de Vehicule (wagon)
	private Bateau[]	tabBateau;
	
	/**
		* Constructeur Train
	*/
	public Train()
	{
		this.nbVehicule = 100;
		this.tabVehicule = new Vehicule[nbVehicule];
	}
	
	/**
		* @return Retourne true si l'ajout du vehicule s'est réalisé et false si l'ajout du vehicule ne s'est pas réalisé.
		* @param v : Le véhicule à ajouter.
	*/
	public boolean ajouterVehicule(Vehicule v)
	{
		return true;
	}

	/**
		* @return Le nombre de Vehicule d'une couleur particulière.
	*/
	public int nbCouleurVehicule(String couleur)
	{
		return -1;
	}
	
	public String toString()
	{
		return new String();
	}
	
	public static void main(String[] args)
	{
		Train t1 = new Train();
		
		Vehicule v1 = new Vehicule("ROUGE");
		Vehicule v2 = new Vehicule("VERT");
		Vehicule v3 = new Vehicule("BLEU");
		
		// Ajout des véhicules
		t1.ajouterVehicule( v1 );
		t1.ajouterVehicule( v2 );
		t1.ajouterVehicule( v3 );
		t1.ajouterVehicule( v1 );
		t1.ajouterVehicule( v1 );

		System.out.println("Affichage du train vide");
		System.out.println(t1.toString());
		
	}
}
