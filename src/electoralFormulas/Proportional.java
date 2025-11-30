package electoralFormulas;

import java.util.ArrayList;

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
		ArrayList<Integer> aux = getGreatestsFromMatrix(matrix, this.totalSeats - this.participants.size());
		ArrayList<Integer> result = new ArrayList<>();

		for (int i = 0; i < this.participants.size(); i++) {
			result.add(1 + aux.get(i));
		}
		return result;
	}

	private ArrayList<Integer> getGreatestsFromMatrix(double[][] matrix, int limit) {
		ArrayList<Integer> result = new ArrayList<>();
		int n = matrix.length;
		int m = matrix[0].length;
		boolean[][] auxMatrix = new boolean[n][m];
		double aux = 0;
		int count = 0;

		while (getTrueValues(auxMatrix) < limit) {
			aux = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (!auxMatrix[i][j] && aux < matrix[i][j]) {
						aux = matrix[i][j];
						auxMatrix[i][j] = true;
					}
				}
			}
		}

		for (int i = 0; i < n; i++) {
			count = 0;
			for (int j = 0; j < m; j++) {
				if (auxMatrix[i][j]) {
					count++;
				}
			}
			result.add(count);
		}

		return result;
	}

}