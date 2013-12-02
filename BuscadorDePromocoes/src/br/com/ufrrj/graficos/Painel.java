package br.com.ufrrj.graficos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.omg.CORBA.portable.InputStream;

public class Painel extends JPanel {
	
    private	JTextArea area = new JTextArea();
	private JButton botao = new JButton();
	
	public Painel() {
		
		botao.setText("Buscar");
		area.setPreferredSize(new Dimension(400,40));
		area.setBackground(Color.DARK_GRAY);
	
		area.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,30));
		area.setForeground(Color.WHITE);
	    addEvent();
		setSize(300, 200);
		setLayout(new FlowLayout(FlowLayout.LEADING,80,230));
	
		add(area);
		add(botao);
		
		
	}
	
	
	private void addEvent(){
		
		botao.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//faz a requisição para o facade 
				//area.getText();
				
			}
		});
		
	}
	
	
	 protected void paintComponent(Graphics g)  
	    {          
	    super.paintComponent(g); 
	      BufferedImage background =null;
		try {
			background = ImageIO.read(new File("fundo.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        Graphics2D g2d = (Graphics2D)g.create();  
	        g2d.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);  
	        g2d.dispose();          
	    }      
	
   
   
}
