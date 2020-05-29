package com.sineshore.p24;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class SimpleButton extends JButton {

    private static final long serialVersionUID = 1L;

    private Runnable execute;

    public SimpleButton() {
	this("");
    }

    public SimpleButton(String text) {
	super(text);
	setBackground(Color.WHITE);
	setFocusPainted(false);
	setBorder(new LineBorder(Color.BLACK, 1));

	addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
		if (execute != null)
		    execute.run();
	    }
	});
    }

    public void setExecute(Runnable execute) {
	this.execute = execute;
    }

}
