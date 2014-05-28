import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;




public class Langage {
	String key, text_crypt, text_uncrypt, text_uncryption="";
	double[] prob_text=new double[26];
	double[] prob_lang=new double[26];
	double indice_coincidence;
	double[][] prob_dig_lang= new double[26][26];
	double[][] prob_dig_text= new double[26][26];
	
	langue[] lng=new langue[2];
	private int num_lang=0;
	private int taille_cle_max=15;
	
	

	public Langage() throws IOException
	{
		lng[0]=new langue("Français",0.0746,"fr.txt");
		lng[1]=new langue("Englais",0.0667,"eng.txt");
		
					
	}
	
	public void choix_langue(String name)
	{
		for(int i=0;i<lng.length;i++)
			if(lng[i].get_name()==name)
			{
			num_lang=i;
			}
	}
	
	public langue get_langue_actu()
	{
		return lng[num_lang];
	}
	
	public langue[] get_langues()
	{
		System.out.println("t2");
		return lng;
	}
	
	 public static String replaceCharAt(String s, int pos, char c) {
	        return s.substring(0,pos) + c + s.substring(pos+1);
	    }
	 
	// for the step 2 // replace 1 char in the text
	public void onechar_uncrypt(char letter1,char letter2)
	{
		if(text_uncryption=="") text_uncryption=text_crypt.toUpperCase();
		for(int i=0;i<text_crypt.length();i++)
		{
			if(text_crypt.charAt(i)==letter1) text_uncryption=replaceCharAt(text_uncryption,i,letter2);
		}
	}
	
	//prepare the text to be crypt/uncrypt by suppressing down character and special character. and UPPER them
	public String prepare(String texte){
		String txt=texte.replaceAll("\\W","").toUpperCase();
		txt = txt.replaceAll("[\\d]", ""); 
		txt = txt.replaceAll("_", ""); 
		return txt;
	}

	//return the most probable language
	public String choose_language(){
		String language="Français";
		return language;
	}
	
	//max the number of occurence of each letter in the text.
	public int[] make_occur(String texte){
		texte=prepare(texte);
	int[] dia =new int[26];
		int i=0;
		int pos=0;
		for(i=0;i<26;i++)
		{
			dia[i]=0;
		}
		for(i=0;i<texte.length();i++)
		{
			pos = Character.getNumericValue(texte.charAt(i)) -10;
			dia[pos]++;
		}
		return dia;
	}
	
	// make percent for each char for the table
	public double[] make_percent(String texte)
	{	int[] dia = make_occur(texte);
		int taille=0;
		double[] res = new double[26];
		for(int i=0;i<26;i++)
		{
			taille+=dia[i];
		}
		for(int i=0;i<26;i++)
		{
			res[i]=(((double)dia[i])*100)/taille;
		}
		return res;
		
	}
	
	// order the result of make_percent
	public double[] make_percent_order(String texte)
	{
		double []res = make_percent(texte);
		
		for(int i=0;i<26;i++)
			for(int j=0;j<26;j++)
				if(res[i]<res[j])
				{
					double tmp=res[i];
					res[i]=res[j];
					res[j]=tmp;
				}
		return res;
	}
	
	// battacharya allow to compare 2 histogram
	public double batta(double[] h1,double[] h2)
	{
		double res=0;
		for(int i=0;i<26;i++)
		{	
			res+=Math.sqrt(h1[i]*h2[i]);
		}
		return res;
		
	}
	
	
	// compare with the actual langage for every key size
	public double[] compare_key_size(String texte)
	{
		
		double langue[] = make_percent_order(lng[num_lang].get_texte());
		double res[] = new double[taille_cle_max];
		
		
		for(int i=0;i<taille_cle_max;i++)
		{
			// find the actual text
			String text_actu="";
			int size=i+1;
			for(int j=0;j<texte.length();j++)
			{
				if((j+1)%size==0)text_actu+=texte.charAt(j);
			}
			double txt[] = make_percent_order(text_actu);
			res[i]=batta(txt,langue);
		}
		return res;
	}
	
	public double make_indence(int[] tab)
	{
		double res=0;
		int n=0;
		for(int i =0;i<tab.length;i++)
		{
			n+=tab[i];
		}
		for(int i=0;i<tab.length;i++)
		{
			res=res+(((double)(tab[i])*(double)((tab[i]-1))))/(((double)n*((double)(n-1))));
		}
		
		return res;
	}
	
	// return the most probable letters in a language for a letter  
	public int[] ordre_proba(int lettre)
	{
	
		prob_text=make_percent(text_crypt);
		prob_lang=make_percent(lng[num_lang].get_texte());
		double[] distance=new double[26];
		for(int i=0;i<26;i++)
		{
			distance[i]= prob_text[lettre]-prob_lang[i];
			if(distance[i]<0)distance[i]=-distance[i];
		}
		int[] res=new int[5];
		for(int i=0;i<5;i++)
		{
			double max=10000;
			for(int j=0;j<26;j++)
			{
				if(max>distance[j])
				{
					max=distance[j];
					res[i]=j;
				}
			}
			distance[res[i]]=10000;
		}
		return res;
	}
	
	//make a list of the most present digrame to make a diagramme.
	
	public double[][] make_percent_digramme(String texte){
		double[][] dig = new double[26][26];
		int pos1,pos2;
		
		for(int i=0;i<26;i++)
			for(int j=0;j<26;j++)
				dig[i][j]=0;
		
		// get a table of digrame with their occurences
		for(int k=0;k<texte.length()-1;k++)
		{
			pos1=(Character.getNumericValue(texte.charAt(k))-10);
			pos2=(Character.getNumericValue(texte.charAt(k+1))-10);
			dig[pos1][pos2]++;
		}
		
		for(int i=0;i<26;i++)
			for(int j=0;j<26;j++)
				dig[i][j]=dig[i][j]*100/(texte.length()-1);
		
		
		return dig;
		
		
		
	}
	
	
	
	
	// return the most probable digrame in a text 
		public String[] ordre_proba_dig(String txt)
		{
			double[][] tb = make_percent_digramme(txt);
			int maxi=0,maxj=0;
			double max=0;
			String[] res=new String[5];
			
			
			for(int k=0;k<5;k++)
			{
				max=0;
				maxi=0;
				maxj=0;
				for(int i=0;i<26;i++)
					for(int j=0;j<26;j++)
					{
						if(max<tb[i][j])
						{
							max=tb[i][j];
							maxi=i;
							maxj=j;
						}
					}
				char a= (char) (maxi+65);
				char b= (char) (maxj+65);
				
				res[k]=Character.toString(a)+Character.toString(b);
				tb[maxi][maxj]=0;
			}
			return res;
			
		}
		
	
	//change the older char by a new char
	public void change_char(char older, char newer){
	
	}
	
	//Make a String for one letter which represent the most probable character.
	public String make_list_char(char letter){
		String c="ghjgj";
		return c;
	}
	
	// this function will return the uncrypt char from a crypted char
	public char uncrypt_char(char ch)
	{
		String alpha="abcdefghijklmnopqrstuvwxyz";
		int i;
		char trouve='a';
		String cle=key.toLowerCase();
		ch=Character.toLowerCase(ch);
		for(i=0;i<26;i++)
		{
			if(cle.charAt(i)==ch) {trouve=alpha.charAt(i);}
		}
		return trouve;
	}
	
	//Crypt or uncrypt the text thanks to the key.
	//crypt_uncrypt take 0 for uncryption and 1 for crypt.
	public void crypt(){
		char[] acrypt = new char[text_uncrypt.length()];
		int ch;
			for(int i=0;i<text_uncrypt.length();i++)
			{
				
				ch = Character.getNumericValue(text_uncrypt.charAt(i)) -10;
				acrypt[i]=(char) ((((int)key.charAt(i%key.length()))-(int)'A'+ch)%26+(int)'A');
			}
			text_crypt=new String(acrypt);
			text_crypt=text_crypt.toUpperCase();
			
		
	}
	
	
	// accesseurs
	

	public String getText_uncryption() {
		return text_uncryption;
	}

	public void setText_uncryption(String text_uncryption) {
		this.text_uncryption = text_uncryption;
	}

	public String getKey() {
		return key;
	}

	public boolean setKey(String key) {
		if(key.length()==0 || key.length()>taille_cle_max) 
			{
			JOptionPane.showMessageDialog(null,"Attention la clé doit faire 26 charactères");
			return false;
			}
		this.key = prepare(key);
		return true;
	}

	public String getText_crypt() {
		return text_crypt;
	}

	public void setText_crypt(String text_crypt) {
		this.text_crypt = prepare(text_crypt);
	}

	public String getText_uncrypt() {
		return text_uncrypt;
	}

	public void setText_uncrypt(String text_uncrypt) {
		this.text_uncrypt = prepare(text_uncrypt);
	}

	public int getNum_lang() {
		return num_lang;
	}

	public void setNum_lang(int num_lang) {
		this.num_lang = num_lang;
	}
	
}
