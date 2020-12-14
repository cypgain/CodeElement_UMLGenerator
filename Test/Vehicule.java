/**
	Classe Vehicule
	* @author 	: -
	* @version 	: 1.0
*/

import java.util.List;

public class Vehicule
{
	/*--------------------*/
	/* L'attribut couleur */
	/*--------------------*/
	public String couleur; // renseigne sur la couleur d'un vehicule
	private String couleur2; // renseigne sur la couleur d'un vehicule
	protected String couleur3; // renseigne sur la couleur d'un vehicule
	public static final String couleurTest = "e";
	private static final String couleurTest1 = "e";
	protected static final String couleurTest2 = "e";

	private List<String> testList;

	/**
		* Constructeur Vehicule
		* @param couleur : la couleur du véhicule.
	*/
	public Vehicule(String couleure)
	{
		this.couleur = couleure;
	}
	
	/**
		* @return Retourne la couleur du véhicule.
	*/
	public String getCouleur()
	{
		return this.couleur;
	}
	
	
	public void testa()
	{

	}
	
	private void testb()
	{

	}
	
	protected void testc()
	{

	}
	
	public static void test()
	{

	}
	
	private static void test1()
	{

	}
	
	protected static void test2()
	{

	}
}
