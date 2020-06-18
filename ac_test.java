import java.util.*;
import java.io.*;
import java.text.*;

public class ac_test{

    static DecimalFormat formatNums = new DecimalFormat("#0.000000");

    public static void main(String[] args) throws IOException
    {
        DLBtrie trie = new DLBtrie();
        Scanner scanner = new Scanner(System.in);
        String userWord = "";
        char c;
        long start = 0;
        long time = 0;
        long totalTime = 0;
        int letterCount = 0; // tracks how many total letters we find word suggestion for
        final double diviser = 1000000000.0;
        //boolean wordStarted = false;

        trie.initializeTrie("dictionary.txt");
        
        ArrayList<String> userInput = new ArrayList<String>();
        int j = 0;
        System.out.print("Enter your first character: ");
        do
        {
            String tempWord = userWord;
            userWord += scanner.nextLine();
            c = userWord.charAt(j);
            if(c != '!')
            {
                if(c == '1' || c == '2' || c == '3' || c == '4' || c == '5')
                {
                    trie.findNode(tempWord);
                    trie.getWords();
                }
                else
                {
                    trie.findNode(userWord);
                    userInput = trie.getWords();
                }

                if((int)(c) < 54 && (int)(c) > 48 && letterCount > 0)
                {
                    System.out.print("\nWORD COMPLETED: "+ tempWord + userInput.get((int)(c) - 49) + "\nEnter first character of next word: ");
                    j = 0;
                    userWord = "";

                }
                else
                {
                    start = System.nanoTime();
                    if(!userInput.isEmpty())
                    {
                        j++;

                        try
                        {
                            userInput = trie.getWords();
                        }
                        catch(NullPointerException x)
                        {
                            System.out.println("Null Pointer Exception");
                        }
                        time = System.nanoTime() - start;
                        totalTime += time;
                        System.out.println("\n(" + formatNums.format(time /diviser) + " seconds)" );
                        System.out.println("Predictions:");

                        for(int i = 0; i < userInput.size(); i++)
                        {
                            System.out.print("("+(i+1)+ ") " + userWord + userInput.get(i) + "   ");
                        }

                    }
                    else
                        System.out.println("\n No predictions generated");
                
                    System.out.print("\nEnter next character: ");
                    letterCount++;
                }
            }
        }while(c != '!');

            System.out.println("\nAverage Time: " + formatNums.format((totalTime/letterCount)/diviser) + " seconds");
            System.out.println("Bye!");
    }
}