import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;


public class langue
{
	String name;
	double coincidence;
	String texte="";
	public langue(String nom,double d,String fichier) throws IOException{
		
		
		try {
			   
			  BufferedInputStream in = new BufferedInputStream(new FileInputStream(fichier));
			  StringWriter out= new StringWriter();
			  int b;
			  while((b=in.read()) != -1)
			  {
				  out.write(b);
			  }
			  out.flush();
			  out.close();
			  in.close();
			  texte=out.toString();
			  texte=prepare(texte);
			 
			  
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
		name=nom;
		coincidence=d;
		}
	
	public String getTexte() {
		return texte;
	}
	
	//prepare the text to be crypt/uncrypt by suppressing down character and special character. and UPPER them
		public String prepare(String texte){
			String txt=texte.replaceAll("\\W","").toUpperCase();
			txt = txt.replaceAll("[\\d]", ""); 
			txt = txt.replaceAll("_", ""); 
			return txt;
		}


	public void setTexte(String texte) {
		this.texte = texte;
	}

	String get_texte()
	{
		return texte;
	}
	
	String get_name()
	{
		return name;
	}
	
	double get_coincidence()
	{
		return coincidence;
	}
}

