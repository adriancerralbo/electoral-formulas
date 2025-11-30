package electoralFormulas;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {

		Scanner scanner = new Scanner(System.in);
		ArrayList<String> list = new ArrayList<>();
		ArrayList<Integer> votes = new ArrayList<>();
		boolean follow = true;
		String line = null;
		String[] data = null;

		System.out.println("##################################");
		System.out.println("THIS IS A PROGRAM TO COMPUTE SEATS");
		System.out.println("##################################");
		System.out.println();
		System.out.println("Type the participants and their votes in the following format:");
		System.out.println("NAME VOTES");
		System.out.println("Intro an empty line to finish.");
		System.out.println();

		while (follow) {
			line = scanner.nextLine();
			if (line.isEmpty()) {
				follow = false;
			} else {
				try {
					data = line.split(" ");
					list.add(data[0]);
					votes.add(Integer.parseInt(data[1]));
				} catch (Exception e) {
					System.err.println("This data is not valid.");
				}
			}
		}

		System.out.println("Type the number of total seats:");
		follow = true;
		int n = 0;

		while (follow) {
			try {
				n = scanner.nextInt();
				follow = false;
			} catch (Exception e) {
				System.err.println("This data is not valid");
			}
		}

		System.out.println("Select the formula:");
		Object[] formulas = EnumFormulas.class.getEnumConstants();
		follow = true;
		int k = 0;

		for (int i = 0; i < formulas.length; i++) {
			System.out.println(Integer.toString(i + 1) + " " + formulas[i]);
		}

		while (follow) {
			try {
				k = scanner.nextInt();
				if (1 < k || formulas.length < k) {
					throw new Exception();
				}
				follow = false;
			} catch (Exception e) {
				System.err.println("This data is not valid");
			}
		}

		Proportional prop = new Proportional(n, list, votes, EnumFormulas.valueOf(formulas[k - 1].toString()));
		System.out.println(prop.toString());
	}
}
