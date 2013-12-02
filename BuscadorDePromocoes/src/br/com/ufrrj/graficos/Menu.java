package br.com.ufrrj.graficos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar {
	
	JMenu   menu = new JMenu();
	JMenuItem twitter = new JMenuItem();
	JMenuItem rss = new JMenuItem();
	
	
	public Menu(){
		
		twitter.setText("Twitter");
		rss.setText("Rss");
		twitter.setIcon(new ImageIcon("twitter.png"));
		rss.setIcon(new ImageIcon("rss.png"));
		
		addEvents();
		menu.setText("Conectar");
		menu.add(twitter);
		menu.add(rss);
		
		add(menu);
		
		
		
	}
	
	
	private void addEvents(){
		
		twitter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				//configura a conexao para o twitter
				
				
			}
		});
		
		rss.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//configura a conexao para o rss
			
				
			}
		});
	}

}
