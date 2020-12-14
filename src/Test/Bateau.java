/**
	Classe Bateau
	* @author 	: CodeElement
	* @version 	: 05/11 - 1.0
*/

public class Bateau
{
	/*--------------------*/
	/* L'attribut couleur */
	/*--------------------*/
	private String couleur; // renseigne sur la couleur d'un vehicule
	private Vehicule v;

	/**
		* Constructeur Bateau
		* @param couleur : la couleur du bateau.
	*/
	public Bateau(String couleure)
	{
		this.couleur = couleure;
	}
	
	public Vehicule getVehicule()
	{
		return this.v;
	}

	public void setVehicule(Vehicule v)
	{
		this.v = v;
	}

	/**
		* @return Retourne la couleur du véhicule.
	*/
	public String getCouleur()
	{
		return this.couleur;
	}
}
