package electoralFormulas;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Main {

	public static void main(String[] args) throws Exception {
		Proportional proportional = new Proportional();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("Electoral formulas");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(screenSize.width / 2, screenSize.height / 2);
		frame.setLocation(screenSize.width / 4, screenSize.height / 4);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.setLayout(new GridLayout(13, 1));
		JLabel labelName = new JLabel("Name of the participant");
		panel.add(labelName);
		JTextField name = new JTextField(20);
		panel.add(name);
		JLabel labelVote = new JLabel("Vote of the participant");
		panel.add(labelVote);
		JTextField vote = new JTextField(20);
		panel.add(vote);
		JLabel information = new JLabel();
		panel.add(information);
		JButton button = new JButton("Add register");
		panel.add(button);
		button.addActionListener(e -> proportional.addRegisterButton(information, name.getText(), vote.getText()));
		JButton buttonFile = new JButton("Import CSV");
		buttonFile.addActionListener(e -> proportional.importCsv(frame, information));
		panel.add(buttonFile);

		frame.add(panel);
		JLabel labelSeats = new JLabel("Total number of seats");
		panel.add(labelSeats);
		JTextField totalSeats = new JTextField(20);
		panel.add(totalSeats);

		Object[] formulas = EnumFormulas.class.getEnumConstants();
		String[] options = new String[formulas.length];

		for (int i = 0; i < formulas.length; i++) {
			options[i] = formulas[i].toString();
		}

		JComboBox<String> comboBox = new JComboBox<>(options);
		panel.add(comboBox);
		JButton buttonCompute = new JButton("Compute");
		JLabel result = new JLabel();
		buttonCompute.addActionListener(e -> proportional.buttonCompute(comboBox, result, totalSeats.getText()));
		JScrollPane scrollPane = new JScrollPane(result);
		panel.add(scrollPane);
		panel.add(buttonCompute);
		JButton reset = new JButton("Reset");
		panel.add(reset);
		reset.addActionListener(e -> proportional.resetLists(information, result));

		frame.setVisible(true);
	}
}
