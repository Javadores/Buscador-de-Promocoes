package br.com.ufrrj.graficos;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;


//sera refatorado em breve
public class PainelPrincipal extends JFrame {
	
	JMenuBar barra = new JMenuBar();
	JMenu   menu = new JMenu();
	JMenuItem item = new JMenuItem();
	JMenuItem item2 = new JMenuItem();
	Painel painel = new Painel();
	
	public PainelPrincipal() {
		
		
	
		
		item.setText("Twitter");
		item2.setText("Rss");
		item.setIcon(new ImageIcon("twitter.png"));
		item2.setIcon(new ImageIcon("rss.png"));
	
		menu.setText("Conectar");
		menu.add(item);
		menu.add(item2);
		
		barra.add(menu);
	    setTitle("Buscador de Promoções");
		setLayout(new BorderLayout());
		add(painel,BorderLayout.CENTER);
		setJMenuBar(barra);
		setResizable(false);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		
		
		
	}

}
