package br.com.rural.graficos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Painel extends JPanel {
	
	JTextArea area = new JTextArea();
	JButton botao = new JButton();
	
	public Painel() {
		
		botao.setText("Buscar");
		area.setPreferredSize(new Dimension(400,30));
	
		setSize(300, 200);
		setLayout(new FlowLayout(FlowLayout.LEADING,30,200));
		add(area);
		add(botao);
		
		
	}

}
