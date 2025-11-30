package electoralFormulas;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Proportional {

	private int totalSeats;
	private ArrayList<String> participants;
	private ArrayList<Integer> votes;
	private EnumFormulas formula;

	public Proportional(int n, ArrayList<String> list, ArrayList<Integer> votes, EnumFormulas form) throws Exception {

		if (votes.size() != list.size()) {
			throw new Exception("The number of participants does not match with their votes.");
		}

		this.totalSeats = n;
		this.participants = list;
		this.votes = votes;
		this.formula = form;
	}

	public Proportional() {
		this.totalSeats = 0;
		this.participants = new ArrayList<String>();
		this.votes = new ArrayList<Integer>();
		this.formula = null;
	}

	public void resetLists(JLabel information, JLabel result) {
		this.participants = new ArrayList<String>();
		this.votes = new ArrayList<Integer>();
		information.setText("There are " + Integer.toString(this.participants.size()) + " registers.");
		result.setText("");
	}

	public void importCsv(JFrame frame, JLabel information) {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(frame);
		String str = null;
		String[] vector = null;
		String participant = null;
		int vote = 0;
		int k = 0;

		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				BufferedReader reader = Files.newBufferedReader(selectedFile.toPath(), StandardCharsets.UTF_8);
				while((str = reader.readLine()) != null) {
					vector = str.split(",");
					participant = vector[0].trim();
					vote = Integer.parseInt(vector[1]);
					this.addRegister(participant, vote);
					k++;
				}
				reader.close();
				information.setText("There are " + Integer.toString(this.participants.size()) + " registers.");
				JOptionPane.showMessageDialog(null, Integer.toString(k) + " registers have been imported.", "Import CSV", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There is an error in the file.", "Input error", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must select a file.", "Input error", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void buttonCompute(JComboBox<String> comboBox, JLabel result, String num) {
		try {
			String str = comboBox.getSelectedItem().toString();
			try {
				int n = Integer.parseInt(num);
				this.formula = EnumFormulas.valueOf(str);
				this.totalSeats = n;
				result.setText(this.getText());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "The total of seats must be a number.", "Input error",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The formula must not be empty.", "Input error",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	private String getText() {
		ArrayList<Integer> result = null;
		int n = participants.size();

		switch (this.formula) {
		case HUNTINGTON_HILL:
			result = computeHuntington();
			break;
		case WEBSTER:
			result = computeWebster();
			break;
		case DHONDT:
			result = computeDhondt();
			break;
		}

		StringBuilder sb = new StringBuilder("<html><table><tr><th>participant</th><th>votes</th><th>seats</th></tr>");

		for (int i = 0; i < n; i++) {
			sb.append("<tr><td>");
			sb.append(this.participants.get(i));
			sb.append("</td><td>");
			sb.append(Integer.toString(this.votes.get(i)));
			sb.append("</td><td>");
			sb.append(Integer.toString(result.get(i)));
			sb.append("</td></tr>");
		}

		sb.append("</table></html>");
		return sb.toString();
	}

	public void addRegister(String str, int n) {
		this.participants.add(str);
		this.votes.add(n);
	}

	public void addRegisterButton(JLabel label, String str, String num) {
		try {
			int n = Integer.parseInt(num);
			addRegister(str, n);
			label.setText("There are " + Integer.toString(this.participants.size()) + " registers.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The number of votes must be a number.", "Input error",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	public int getTotalSeats() {
		return this.totalSeats;
	}

	public ArrayList<String> getParticipants() {
		return this.participants;
	}

	public ArrayList<Integer> getVotes() {
		return this.votes;
	}

	public EnumFormulas getFormula() {
		return this.formula;
	}

	public void setTotalSeats(int n) {
		this.totalSeats = n;
	}

	public void setParticipants(ArrayList<String> list) {
		this.participants = list;
	}

	public void setVotess(ArrayList<Integer> list) {
		this.votes = list;
	}

	public void setFormula(EnumFormulas form) {
		this.formula = form;
	}

	public String toString() {
		ArrayList<Integer> result = null;
		int n = participants.size();

		switch (this.formula) {
		case HUNTINGTON_HILL:
			result = computeHuntington();
			break;
		case WEBSTER:
			result = computeWebster();
			break;
		case DHONDT:
			result = computeDhondt();
			break;
		}

		StringBuilder sb = new StringBuilder("TOTAL SEATS: ");
		sb.append(Integer.toString(this.totalSeats));
		sb.append("\nPARTICIPANTS:\n");

		for (int i = 0; i < n; i++) {
			sb.append("\t");
			sb.append(this.participants.get(i));
			sb.append(" (");
			sb.append(Integer.toString(this.votes.get(i)));
			sb.append(", ");
			sb.append(Integer.toString(result.get(i)));
			sb.append(")\n");
		}

		sb.append("FORMULA: ");
		sb.append(this.formula);
		return sb.toString();
	}

	private double[][] createHuntingtonMatrix() {
		double[][] matrix = new double[this.participants.size()][this.totalSeats - 1];

		for (int i = 0; i < this.participants.size(); i++) {
			for (int j = 0; j < this.totalSeats - 1; j++) {
				matrix[i][j] = this.votes.get(i) / Math.sqrt((j + 1) * (j + 2));
			}
		}

		return matrix;
	}

	private double[][] createWebsterMatrix() {
		double[][] matrix = new double[this.participants.size()][this.totalSeats];

		for (int i = 0; i < this.participants.size(); i++) {
			for (int j = 0; j < this.totalSeats; j++) {
				matrix[i][j] = this.votes.get(i) / (2 * j + 1);
			}
		}

		return matrix;
	}

	private double[][] createDhondtMatrix() {
		double[][] matrix = new double[this.participants.size()][this.totalSeats];

		for (int i = 0; i < this.participants.size(); i++) {
			for (int j = 0; j < this.totalSeats; j++) {
				matrix[i][j] = this.votes.get(i) / (j + 1);
			}
		}

		return matrix;
	}

	private static int getTrueValues(boolean matrix[][]) {
		int n = matrix.length;
		int m = matrix[0].length;
		int k = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (matrix[i][j]) {
					k++;
				}
			}
		}
		return k;
	}

	private ArrayList<Integer> computeHuntington() {
		double[][] matrix = createHuntingtonMatrix();
		ArrayList<Integer> aux = getGreatestsFromMatrix(matrix,
				Math.max(0, this.totalSeats - this.participants.size()));
		ArrayList<Integer> result = new ArrayList<>();

		for (int i = 0; i < this.participants.size(); i++) {
			result.add(1 + aux.get(i));
		}
		return result;
	}

	private ArrayList<Integer> computeWebster() {
		double[][] matrix = createWebsterMatrix();
		return getGreatestsFromMatrix(matrix, this.totalSeats);
	}

	private ArrayList<Integer> computeDhondt() {
		double[][] matrix = createDhondtMatrix();
		return getGreatestsFromMatrix(matrix, this.totalSeats);
	}

	private ArrayList<Integer> getGreatestsFromMatrix(double[][] matrix, int limit) {
		ArrayList<Integer> result = new ArrayList<>();
		int n = matrix.length;
		int m = matrix[0].length;
		boolean[][] auxMatrix = new boolean[n][m];
		int auxI = 0;
		int auxJ = 0;
		double aux = 0;
		int k = 0;

		if (limit > n * m) {
			limit = n * m;
		}

		while (getTrueValues(auxMatrix) < limit) {
			aux = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (!auxMatrix[i][j] && aux < matrix[i][j]) {
						auxI = i;
						auxJ = j;
						aux = matrix[i][j];
					}
				}
			}
			auxMatrix[auxI][auxJ] = true;
		}

		for (int i = 0; i < n; i++) {
			k = 0;
			for (int j = 0; j < m; j++) {
				if (auxMatrix[i][j]) {
					k++;
				}
			}
			result.add(k);
		}

		return result;
	}

}
