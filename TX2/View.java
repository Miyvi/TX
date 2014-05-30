import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;


public class View extends JFrame{
	//model
	private Langage lang;
	
	private JMenuBar bar = new JMenuBar();
	private JMenu menu = new JMenu("Fichier");
	private JMenuItem menuitem = new JMenuItem("Ouvrir fichier crypté");
	private JMenuItem menuitem3 = new JMenuItem("Ouvrir fichier non-crypté");
	private JMenu menu2 = new JMenu("Choix langue");
	private JMenuItem menuitem_lng[];
	private boolean cherche_cle=false;
	private JFileChooser choisir = new JFileChooser();
	
	String texte;
	
	// partie superieure
	JPanel psup = new JPanel();
	private JTextArea area=new JTextArea();
	private JScrollPane scrollarea=new JScrollPane(area);
	private JTextField key = new JTextField();
	private JPanel keypane = new JPanel();
	private JButton crypt = new JButton("Crypter");
	File fichier;
	
	// aprtir inf
	private JTextArea area2=new JTextArea();
	
	String[] elements = new String[]{"A", "B", "C", "D", "E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	 
	private JComboBox<String> liste1 = new JComboBox(elements);
	final DefaultListModel modelist = new DefaultListModel();
	private JList<Character> myList = new JList(modelist);
	protected JScrollPane listecont;
	private JPanel pinf = new JPanel();
	private Histogramme hist = new Histogramme();
	// digrammes
	final DefaultListModel modelist_dig1 = new DefaultListModel();
	private JList<Character> myList_dig1 = new JList(modelist_dig1);
	final DefaultListModel modelist_dig2 = new DefaultListModel();
	private JList<Character> myList_dig2 = new JList(modelist_dig2);
	
	

	private JSplitPane splitglobal = new JSplitPane(JSplitPane.VERTICAL_SPLIT,psup,pinf);
	
	// affiche les digrammes dans les deux listes
	public void maj_digrammes()
	{
		modelist_dig1.clear();	
		modelist_dig2.clear();
		String[] lng=lang.ordre_proba_dig(lang.get_langue_actu().getTexte());
		String[] txt=lang.ordre_proba_dig(lang.getText_crypt());
		for(int i=0;i<lng.length;i++)modelist_dig1.addElement(lng[i]);
		for(int i=0;i<txt.length;i++)modelist_dig2.addElement(txt[i]);
		cherche_cle=true;
		key.setText("__________________________");
		key.enable(false);
		crypt.enable(false);
		crypt.setVisible(false);
		
	}
	
	public View(Langage l)
	{
		lang=l;
		area.disable();
		System.out.println("t3");
		this.setVisible(true);
		this.setSize(600, 800);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		// partie inf
		pinf.setLayout(new GridBagLayout());
	
		GridBagConstraints c = new GridBagConstraints();
		
		// colone de gauche vide
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.05;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_START; 
		//c.insets = new Insets(10,0,0,0);  //top padding
		c.gridx = 0;       //aligned with button 2
		c.gridy=0;
		pinf.add(new JLabel(""),c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		c.insets = new Insets(40,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridy=0;
		pinf.add(liste1,c);
		
		liste1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
			if(crypt.getText()=="Décrypter")
			{
				modelist.clear();
				int[] res=lang.ordre_proba(Character.getNumericValue(liste1.getSelectedItem().toString().charAt(0))-10);
				for(int i=0;i<res.length;i++) 
					{
					modelist.addElement((char)(65+res[i]));
					}
				
			
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Attention : Vous devez déjà avoir un texte crypté ouvert, pour cela, utilisez le menu et ouvrez un fichier crypté ou alors créez en un grâce à l'étape 1");
			}
				
			//System.out.println(lang.getText_crypt());
			}
			
		});
		
	
		
		// Code permettant d'afficher une liste de char
		listecont=new JScrollPane(myList);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		c.insets = new Insets(70,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridy=0;
		pinf.add(hist,c);
		hist.setPreferredSize( new Dimension(300,120));
		
		
		myList.addListSelectionListener(new ListSelectionListener()
		{

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(myList.getSelectedValue()!=null)
				{
				char lettre2 = myList.getSelectedValue();
				char lettre1 = ((String) liste1.getSelectedItem().toString()).charAt(0);
				//System.out.println(lettre1+"  "+lettre2);
				lang.onechar_uncrypt(lettre1,Character.toLowerCase(lettre2));
				area.setText(lang.getText_uncryption());
				
				String key_actu=key.getText();
				int pos=myList.getSelectedValue()-65;
				String new_key="";
				for(int i=0;i<key_actu.length();i++) 
					if(i!=pos)new_key+=key_actu.charAt(i);
					else new_key+=liste1.getSelectedItem();
				key.setText(new_key);
				}
				
			}
			
		});
		myList_dig1.enable(false);
		myList_dig2.enable(false);
		
		
		JPanel videgauche=new JPanel();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		//c.insets = new Insets(70,0,0,0);  //top padding
		c.gridx = 0;       //aligned with button 2
		c.gridy=0;
		pinf.add(videgauche,c);
		
		JLabel etape2 = new JLabel("Etape 2 : Décrypter un texte sans clé : Recherche de la taille de la clé :");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_START; 
		c.insets = new Insets(10,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridy=0;
		pinf.add(etape2,c);
	
		
		JButton aideetape2 = new JButton("?");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		//c.weightx = 0.1;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_START; 
		//c.insets = new Insets(10,0,0,0);  //top padding
		c.gridx = 2;       //aligned with button 2
		c.gridy=0;
		pinf.add(aideetape2,c);
		
		// digrmames
		JLabel dig_lbl=new JLabel("Digrammes les plus communs :");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		c.insets = new Insets(220,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridy=0;
		pinf.add(dig_lbl,c);
		
		// digrmames conteneur des listes
		JPanel dig_pane=new JPanel();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		c.insets = new Insets(250,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridy=0;
		pinf.add(dig_pane,c);
		dig_pane.setLayout(new GridBagLayout());
		
		JLabel lbl_lst_dig1=new JLabel("Digrammes communs en : " + lang.get_langue_actu().get_name());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		//c.insets = new Insets(70,0,0,0);  //top padding
		c.gridx = 3;       //aligned with button 2
		c.gridy=0;
		dig_pane.add(lbl_lst_dig1,c);
		
		JLabel lbl_lst_dig2=new JLabel("Digrammes communs du texte crypté :");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		//c.insets = new Insets(70,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridy=0;
		dig_pane.add(lbl_lst_dig2,c);
		
		JButton helpdig=new JButton("?");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		c.insets = new Insets(220,0,0,0);  //top padding
		c.gridx = 2;       //aligned with button 2
		c.gridy=0;
		pinf.add(helpdig,c);
		
		JScrollPane listecont_dig1=new JScrollPane(myList_dig1);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		//c.insets = new Insets(70,0,0,0);  //top padding
		c.gridx = 3;       //aligned with button 2
		c.gridy=1;
		dig_pane.add(listecont_dig1,c);
		
		JLabel centerdig=new JLabel("==>");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		//c.anchor = GridBagConstraints.NORTH; 
		//c.insets = new Insets(70,0,0,0);  //top padding
		c.gridx = 2;       //aligned with button 2
		c.gridy=1;
		dig_pane.add(centerdig,c);
		
		JScrollPane listecont_dig2=new JScrollPane(myList_dig2);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.weightx = 0.5;   //request any extra vertical space
		c.anchor = GridBagConstraints.NORTH; 
		//c.insets = new Insets(70,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridy=1;
		dig_pane.add(listecont_dig2,c);
		
		
		// message d'aide
		aideetape2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"Utilisez la liste pour selectionner une lettre, une liste de lettre apparait, celles-ci sont les lettres ayant de plus de chances d'être la lettre dans le texte décrypté."
						+ "\nSelectionner une de ces lettres permet de la remplacer dans le texte de base et de vérifier si votre choix était bon."
						+ "\n Attention : Vous devez déjà avoir un texte crypté ouvert, pour cela, utilisez le menu et ouvrez un fichier crypté ou alors créez en un grâce à l'étape 1");
				
			}
			
			
		});
		
		// message d'aide pour les digrammes
				helpdig.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						JOptionPane.showMessageDialog(null,"Les digrammes sont des ensembles de deux lettres, chaque digramme possède un taux d'apparition qui lui est plus ou moins fixe en fonction de la langue du texte étudié."
								+ "\nAfin de décripter un texte cripté en monoalphabéthique il est donc possible de chercher dans le texte la liste des digramme apparaissant le plus souvent et de les comparer avec les digrammes "
								+ "\nles plus connus de la langue du texte de base" +"\n\nNote : Cette méthode est souvent plus efficace que la précédente (il est aussi interessant de les combiner)");
						
						
					}
					
				});
				

		psup.setLayout(new BorderLayout());
		JLabel etape1 = new JLabel("Etape 1 : Charger un fichier non-crypté afin de le crypter");
		psup.add(etape1,BorderLayout.NORTH);
		menu.add(menuitem3);
		menu.add(menuitem);
		menuitem_lng = new JMenuItem[lang.get_langues().length];
		
		for(int i=0;i<lang.get_langues().length;i++)
		{
			menuitem_lng[i]=new JMenuItem((lang.get_langues()[i].get_name()));
			menu2.add(menuitem_lng[i]);
		}
		
		bar.add(menu);
		bar.add(menu2);
		splitglobal.setResizeWeight(0.3);
		
		this.getContentPane().add(bar, BorderLayout.NORTH);
		this.getContentPane().add(splitglobal, BorderLayout.CENTER);
		
		
		//Haut
		area.setEditable(false);
		
		// menu key
		keypane.setLayout(new BorderLayout());
		keypane.add(key,BorderLayout.CENTER);
		keypane.add(crypt,BorderLayout.EAST);
		
		psup.add(scrollarea,BorderLayout.CENTER);
		psup.add(keypane,BorderLayout.SOUTH);

		
// action du bouton crypt		
crypt.addActionListener(new ActionListener(){

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(area.isEnabled())
		{
			lang.setKey(key.getText());
			lang.crypt();
			area.setText(lang.getText_crypt());
			//maj_digrammes();
			double[]  order=lang.compare_key_size(lang.getText_crypt());
			hist.setcourbe(order);
			hist.paint(hist.getGraphics());
			//for(int i=0;i<order.length;i++) System.out.println(order[i]);
			//lang.compare_key_size(lang.getText_crypt());
		}
		else JOptionPane.showMessageDialog(null,"Vous n'avez pas de texte à traiter");
		
		
		
	}
	
});
		
menuitem.addActionListener(new ActionListener() {
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				choisir.setAcceptAllFileFilterUsed(false);
				FileFilter ff=new FileFilter() {

					@Override
					public String getDescription() {
						return "text crypté";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".txt");
					}
				};
				
				choisir.addChoosableFileFilter(ff);
				choisir.setFileFilter(ff);
				
				
				
				
				int returnVal = choisir.showOpenDialog(choisir);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File fichier = choisir.getSelectedFile();
		            
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
			  			  area.setText(texte);
			  			  area.enable();
			  			  lang.setText_crypt(texte);
			  			  crypt.setText("Décrypter");
			  			  //maj_digrammes();
			  			  
			  		}
			  		catch(IOException ie)
			  		{
			  			ie.printStackTrace();
			  		}
		   
		        }
			}
		} );
		

// action listener sur le choix de la langue
		for(int i=0;i<lang.get_langues().length;i++)
			{
			final int pos=i;
		menuitem_lng[i].addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			lang.setNum_lang(pos);
				
			}
		
		     } );
			}
		
		
		
		
menuitem3.addActionListener(new ActionListener() {
			
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				choisir.setAcceptAllFileFilterUsed(false);
				FileFilter ff=new FileFilter() {

					@Override
					public String getDescription() {
						return "text non-crypté";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".txt");
					}
				};
				
				choisir.addChoosableFileFilter(ff);
				choisir.setFileFilter(ff);
				
				
				int returnVal = choisir.showOpenDialog(choisir);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					 File fichier = choisir.getSelectedFile();
			            
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
			  			  area.setText(texte);
			  			  area.enable();
			  			  lang.setText_uncrypt(texte);
			  			  crypt.setText("Crypter");
			  			  
			  		}
			  		catch(IOException ie)
			  		{
			  			ie.printStackTrace();
			  		}
			   
			        }
		   
		        }
			
		} );
		
		
	}
	
	
}
