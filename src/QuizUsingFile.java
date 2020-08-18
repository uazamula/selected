//p. 722 (Game Zone 2)
/*
 In Chapter 8, you created a game named Quiz in which the user could respond to
multiple-choice questions. Modify the game so that it stores the player’s highest
score from any previous game in a file and displays the high score at the start of
each new game. (The first time you play the game, the high score is 0.) Save the
game as QuizUsingFile.java.
 */
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class QuizUsingFile {

	public static void main (String args[]){
// ****************  Read  **********************************
		String sFile = "highestScore.txt";
		Path file = Paths.get(sFile);
		int highestScore=0;
		String highestScoreS="";
		try {
			highestScoreS = Files.readAllLines(file).get(0);
			highestScore = Integer.parseInt(highestScoreS);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("The highest score is " + highestScoreS);
// ********************   Quiz   *********************
		String s1 = "Enter a letter A, B, C, or D: ";
		String correct = "Congratulations! Your answer is correct";
		String incorrect = "Incorrect";
		int count=0;
		int countQuestions = 0;
		char ch=0;
		Scanner input = new Scanner(System.in);

		String[] questions = {"What is the capital of Great Britain?",
		"How many legs does a dog have?", "How many legs does a bird have?", 
		"How many legs does a spider have?", "What country is the smallest?"};

		String[] choices = {"A - Paris\nB - New York\nC - London\nD - Liverpool",
				"A - four\nB - three\nC - two\nD - one", 
				"A - four\nB - three\nC - two\nD - one",
	    			"A - two\nB - four\nC - six\nD - eight", 
				"A - Argentina\nB - Andorra\nC - Algeria\nD - Afghanistan"};
		char[] answers ={'C','A','C','D','B'};
		final int NUM_OF_QUESTIONS = questions.length;
		int countCorrectAnswers=0;

		for (int i = 0; i < NUM_OF_QUESTIONS; i++){
			System.out.println("Question" + (i+1));
			System.out.println(questions[i]);
			System.out.println(choices[i]);
			try{
				System.out.print(s1);
				ch = input.nextLine().toUpperCase().charAt(0);
				if ( ch == answers[i]) {
					System.out.println(correct);
					countCorrectAnswers++;
				}
				else
					System.out.println(incorrect);
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
			countQuestions++;
			if (countQuestions < NUM_OF_QUESTIONS) {
				System.out.print("Press Enter to go to the next question");
				input.nextLine();
			}
			System.out.println("\n");
		}
//****************  Write  ************************************
		if (highestScore<countCorrectAnswers){
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(sFile));
				writer.write(""+countCorrectAnswers);//String value
				writer.close();
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
}