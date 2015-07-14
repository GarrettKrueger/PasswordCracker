

import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		String toCrack = "";
		Scanner in = new Scanner(System.in);
		while (!toCrack.equals("end")){
		System.out.println("Name of the hex file to crack(without extention)? or end to quit");
		toCrack = in.nextLine();
		if (!toCrack.equals("end")){
			Crack.hxCrack(toCrack);
			}
		}
		in.close();
	}
	
}
