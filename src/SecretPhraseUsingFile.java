//p.722 (Game Zone 1)
/*
  In several Game Zone assignments earlier in this book, you created games
similar to Hangman in which the user guesses a secret phrase by selecting a
series of letters. These versions had limited appeal because each contained only a
few possible phrases to guess; after playing the games a few times, the user would
have memorized all the phrases. Now create a version in which any number of
secret phrases can be saved to a file before the game is played. Use a text editor
such as Notepad to type any number of phrases into a file, one per line. Save the
file as Phrases.txt. Then, create a game that randomly selects a phrase from
the file and allows a user to guess the phrase letter by letter. Save the game as
SecretPhraseUsingFile.java.
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SecretPhraseUsingFile {
    public static void main(String[] args)
    {
//********************   PART I   ************************************
        Path file = Paths.get("SecretPhrase.txt");
        int MAX_ROW = 100;
        String[] s = new String[MAX_ROW];
        int count = 0;
        try{
            InputStream input = new
                    BufferedInputStream(Files.newInputStream(file));
            BufferedReader reader = new
                    BufferedReader(new InputStreamReader(input));
            s[count] = reader.readLine();
            do{
                count++;
                s[count] = reader.readLine();
            }while(s[count] != null);
            reader.close();
        }
        catch(Exception e){
            System.out.println("Message: " + e.getMessage());
        }
//********************   PART II   ************************************

        Scanner input = new Scanner(System.in);
        int[] lengthOfStrings = new int[count];
        char chToAsterisk = '*';
        for(int i = 0; i < count; i++)
            lengthOfStrings[i] = s[i].length();
        int randIndex = (int)(Math.random()*1000) % count;
        String str = s[randIndex];
        int countAttempts=0;
//to asterisks
        StringBuilder strToAsterisk = new StringBuilder(str);
        for (int i = 0; i < lengthOfStrings[randIndex]; i++)
            if (Character.isLetter(str.charAt(i)))
                strToAsterisk.setCharAt(i,chToAsterisk);
        StringBuilder sb = new StringBuilder(strToAsterisk);
        char ch;
        System.out.println("Guess a phrase \"" + sb +"\"");
        boolean isLoop;
//guess
        do{
            System.out.print("Guess a letter: ");
            ch = input.nextLine().charAt(0);
            for (int i = 0; i < lengthOfStrings[randIndex]; ++i){
                if (Character.isLetter(ch)){
                    if (Character.toLowerCase(ch) == str.toLowerCase().charAt(i))
                        if (Character.isUpperCase(str.charAt(i)))
                            sb.setCharAt(i,Character.toUpperCase(ch));
                        else
                            sb.setCharAt(i,Character.toLowerCase(ch));
                }
                else if (ch == str.charAt(i))
                    sb.setCharAt(i,ch);

            }
            isLoop = !(str.equals(sb.toString()));
            System.out.println(sb);
            countAttempts++;
        } while(isLoop);
        System.out.println("Congratulation! The phrase is \"" + sb +"\"");
        System.out.println("Number of attempts is " + countAttempts);

    }
}
