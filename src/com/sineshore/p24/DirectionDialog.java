package com.sineshore.p24;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DirectionDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int row, col;
	private int buttonSize = 60;

	public DirectionDialog(JFrame frame) {
		super(frame, "Choose a Direction");
		SimpleButton longTrade = new SimpleButton("Long");
		longTrade.setBackground(Color.WHITE);
		longTrade.setPreferredSize(new Dimension(buttonSize, buttonSize));
		longTrade.setBorder(new EmptyBorder(0, 0, 0, 0));

		longTrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.table.setValueAt("Long", row, col);
				DirectionDialog.this.dispose();
			}
		});

		SimpleButton shortTrade = new SimpleButton("Short");
		shortTrade.setBackground(Color.WHITE);
		shortTrade.setPreferredSize(new Dimension(buttonSize, buttonSize));
		shortTrade.setBorder(new EmptyBorder(0, 0, 0, 0));

		shortTrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.table.setValueAt("Short", row, col);
				DirectionDialog.this.dispose();
			}
		});

		SimpleButton clear = new SimpleButton("Clear");
		clear.setBackground(Color.WHITE);
		clear.setPreferredSize(new Dimension(buttonSize, buttonSize));
		clear.setBorder(new EmptyBorder(0, 0, 0, 0));

		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.table.setValueAt("", row, col);
				DirectionDialog.this.dispose();
			}
		});

		JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setContentPane(content);

		content.add(longTrade);
		content.add(shortTrade);
		content.add(clear);

		setResizable(false);
		pack();
	}

	public void show(int row, int col) {
		this.row = row;
		this.col = col;
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		setLocation(mouse.x - getWidth() / 2, mouse.y - getHeight() / 2);
		setVisible(true);
	}

}
