package com.sineshore.p24;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Main {

	private static final int WIDTH = 450;
	private static final int HEIGHT = 800;

	public static final Color[][] COLOR_MAP = new Color[24][5];
	public static final String[] PAIRS = { "EURUSD", "EURAUD", "EURNZD", "EURCAD", "EURCHF", "EURJPY", "GBPUSD", "GBPAUD", "GBPNZD", "GBPCAD",
			"GBPCHF", "GBPJPY", "AUDUSD", "AUDCHF", "AUDJPY", "AUDCAD", "CADJPY", "CADCHF", "NZDUSD", "NZDJPY", "NZDCAD", "USDCAD", "USDCHF",
			"USDJPY" };
	public static ColorDialog colorDialog;
	public static DirectionDialog directionDialog;
	public static JTable table;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Project 24");
		JPanel content = new JPanel(new BorderLayout(), true);
		frame.setContentPane(content);
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			frame.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/icon.png")));
		} catch (IOException ee) {
			ee.printStackTrace();
		}

		colorDialog = new ColorDialog(frame);
		directionDialog = new DirectionDialog(frame);

		table = new JTable(new DefaultTableModel(
				new String[] { "Trade Pair", "Long Term Bias", "Intermediate Term Bias", "Next Trade Direction", "Clear" }, 24)) {

			private static final long serialVersionUID = 1L;

			public int getRowHeight() {
				return getHeight() / getRowCount();
			}

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				component.setBackground(COLOR_MAP[row][column] != null ? COLOR_MAP[row][column] : Color.WHITE);
				return component;
			}

			@Override
			public void doLayout() {
				super.doLayout();
				setPreferredSize(new Dimension(getParent().getWidth(), getParent().getHeight() - getTableHeader().getHeight()));
			}

		};

		table.getColumnModel().getColumn(1).setCellEditor(new ClientsTableRenderer());
		table.getColumnModel().getColumn(2).setCellEditor(new ClientsTableRenderer());
		table.getColumnModel().getColumn(3).setCellEditor(new ClientsTableRenderer());
		table.getColumnModel().getColumn(4).setCellEditor(new ClientsTableRenderer());

		table.getColumnModel().getColumn(0).setCellRenderer(new ClientsTableButtonRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new ClientsTableButtonRenderer());
		table.getColumnModel().getColumn(2).setCellRenderer(new ClientsTableButtonRenderer());
		table.getColumnModel().getColumn(3).setCellRenderer(new ClientsTableButtonRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new ClientsTableButtonRenderer());

		for (int i = 0; i < 24; i++) {
			table.setValueAt("×", i, 4);
			table.setValueAt(PAIRS[i], i, 0);
		}

		try {
			File file = getFile();
			if (file.exists()) {
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					for (int i = 0; i < 24; i++) {
						for (int j = 0; j < 5; j++) {
							COLOR_MAP[i][j] = new Color(Integer.parseInt(reader.readLine()));
						}
					}
					for (int i = 0; i < 24; i++) {
						table.setValueAt(reader.readLine(), i, 3);
					}
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("Failed to decode path.");
			e.printStackTrace();
		}

		JScrollPane view = new JScrollPane(table);
		view.setBorder(new EmptyBorder(0, 0, 0, 0));
		view.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		view.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		content.add(view, BorderLayout.CENTER);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent event) {
				try {
					File file = getFile();
					try {
						if (!file.exists()) {
							file.createNewFile();
						}
						BufferedWriter writer = new BufferedWriter(new FileWriter(file));
						for (int i = 0; i < 24; i++) {
							for (int j = 0; j < 5; j++) {
								Color color = COLOR_MAP[i][j];
								writer.write(String.valueOf(color == null ? 0xFFFFFF : color.getRGB()));
								writer.write('\n');
							}
						}

						for (int i = 0; i < 24; i++) {
							String val = (String) table.getValueAt(i, 3);
							writer.write(val == null ? "" : String.valueOf(val));
							writer.write('\n');
						}

						writer.close();
						System.out.println("Saved!");
					} catch (IOException e) {
						System.out.println("Failed to save file to: " + file.getPath());
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e1) {
					System.out.println("Failed to decode path.");
					e1.printStackTrace();
				}
			}
		});
	}

	private static File getFile() throws UnsupportedEncodingException {
		String encoded = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		String decoded = URLDecoder.decode(encoded, "UTF-8");
		File file = new File(decoded);
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		return new File(file.getAbsolutePath() + "/" + "24-save-file.txt");
	}

}

class ClientsTableRenderer extends DefaultCellEditor {

	private static final long serialVersionUID = -8057075596231530745L;

	private JButton button;
	private String label;
	private boolean clicked;
	private int row, col;
	private JTable table;

	public ClientsTableRenderer() {
		super(new JCheckBox());
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.table = table;
		this.row = row;
		this.col = column;

		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		clicked = true;
		return button;
	}

	public Object getCellEditorValue() {
		if (clicked) {
			if (col == 1 || col == 2) {
				Main.colorDialog.show(row, col);
			} else if (col == 3) {
				Main.directionDialog.show(row, col);
			} else if (col == 4) {
				table.setValueAt("", row, 3);
				Main.COLOR_MAP[row][1] = Color.WHITE;
				Main.COLOR_MAP[row][2] = Color.WHITE;
				table.repaint();
			}
		}
		clicked = false;
		return new String(label);
	}

	public boolean stopCellEditing() {
		clicked = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}

}

class ClientsTableButtonRenderer extends JButton implements TableCellRenderer {

	private static final long serialVersionUID = 3954100802854971943L;

	public ClientsTableButtonRenderer() {
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setText((value == null) ? "" : value.toString());
		setFocusPainted(false);
		return this;
	}

}