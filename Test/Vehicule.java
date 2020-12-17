/**
	Classe Vehicule
	* @author 	: -
	* @version 	: 1.0
*/

import java.util.ArrayList;
import java.util.List;

public class Vehicule
{
	class InternalTest
	{
		private int yolo;

		private void ethljg()
		{

		}
	}

	/*--------------------*/
	/* L'attribut couleur */
	/*--------------------*/
	public String couleur; // renseigne sur la couleur d'un vehicule
	private String couleur2; // renseigne sur la couleur d'un vehicule
	protected String couleur3; // renseigne sur la couleur d'un vehicule
	public static final String couleurTest = "e";
	private static String couleurTest1 = "e";
	protected static final String couleurTest2 = "e";
	private int test;

	private Voiture[] testTab;
	private List<Voiture> testList;
	private ArrayList<Voiture> testArrayList;

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
