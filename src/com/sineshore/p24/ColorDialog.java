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

public class ColorDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int row, col;
	private int buttonSize = 60;

	public ColorDialog(JFrame frame) {
		super(frame, "Choose a Color");
		SimpleButton white = new SimpleButton();
		white.setBackground(Color.WHITE);
		white.setPreferredSize(new Dimension(buttonSize, buttonSize));
		white.setBorder(new EmptyBorder(0, 0, 0, 0));

		white.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.COLOR_MAP[row][col] = Color.WHITE;
				ColorDialog.this.dispose();
			}
		});

		SimpleButton green = new SimpleButton();
		green.setBackground(Color.GREEN);
		green.setPreferredSize(new Dimension(buttonSize, buttonSize));
		green.setBorder(new EmptyBorder(0, 0, 0, 0));

		green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.COLOR_MAP[row][col] = Color.GREEN;
				ColorDialog.this.dispose();
			}
		});

		SimpleButton red = new SimpleButton();
		red.setBackground(Color.RED);
		red.setPreferredSize(new Dimension(buttonSize, buttonSize));
		red.setBorder(new EmptyBorder(0, 0, 0, 0));

		red.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Main.COLOR_MAP[row][col] = Color.RED;
				ColorDialog.this.dispose();
			}
		});

		JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setContentPane(content);

		content.add(white);
		content.add(green);
		content.add(red);

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
