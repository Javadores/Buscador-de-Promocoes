package view;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;


//sera refatorado em breve
public class PainelPrincipal extends JFrame {
	
	Menu barra = new Menu();
	Painel painel = new Painel();
	JProgressBar barradeprogresso = new JProgressBar();
	
	public PainelPrincipal() {
		
	
	    
	    setTitle("Buscador de Promoções");
		setLayout(new BorderLayout());
		add(painel,BorderLayout.CENTER);
		barradeprogresso.setValue(50);
		
		add(barradeprogresso,BorderLayout.SOUTH);
		setJMenuBar(barra);
		setResizable(false);
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		
		
		
	}

}
