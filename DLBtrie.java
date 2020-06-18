import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class DLBtrie{
    private DLBnode root;
    //private DLBnode userHistory;
    private DLBnode currNode;
    public final char terminate = '$';
    int setRootCounter;
    
    public DLBtrie(){
        root = new DLBnode(); // DLB trie for original dictionary
        //userHistory = new DLBnode(); // DLB trie for user history
    }

    public void insert(String word){
        
        currNode = root;

        for(int i = 0; i < word.length(); i++)
        {
            setRootCounter++;
            char letter = word.charAt(i);
            if(setRootCounter == 1)
                setRoot(letter);
            if(letter == currNode.value)
            {
                boolean hasChild = childExists(currNode.child); //returns true is currNode has a child
                if(hasChild == true) //if it has existing child
                {
                    currNode = currNode.child;
                }
                else // if it has no child
                {
                    currNode = addChild(letter);
                } 
            }
            else if(letter != currNode.value)
            {
                boolean hasSibling = siblingExists(currNode.rightSib);
                boolean hasMatchingSib = false;
                if(hasSibling == true)
                {
                    //check for sibling that contain char
                    while(currNode.rightSib != null)
                    {
                        currNode = currNode.rightSib;
                        if(currNode.value == letter)
                        { 
                            //checks to see if last node obtained has the value we are looking for
                            hasMatchingSib = true;
                            currNode = currNode.child;
                            break;
                        }
                    }

                }
                //no siblings contained char, create sibling
                if(!hasMatchingSib)
                {
                    currNode = addSibling(letter);
                    for(int j = i + 1; j< word.length(); j++)
                        {
                        char childLetter = word.charAt(j);
                        currNode = addChild(childLetter);
                        i = word.length();
                        }
                }
            }
            // if node did not previously exist it will be added
        }

    }
    public void setRoot(char letter)
    {
        root.value = letter;
        
    }

    public void setInitialWord(String word)
    {
        DLBnode tempNode = root;
        for(int i=1; i<word.length(); i++)
        {
            tempNode.child = new DLBnode(word.charAt(i));
            tempNode = tempNode.child;   
        }
    }
    public void initializeTrie(String file) throws IOException
    {
       
        // wrapped BufferedReader around FileReader for efficency purposes
       try(BufferedReader fileInput = new BufferedReader(new FileReader(file)))
       {
            String word;
            int count = 0;
            while((word = fileInput.readLine())!= null)
            {
                
                word += terminate;
                if(count == 0)
                {
                    setRoot(word.charAt(0));
                    setInitialWord(word);
                }
                else if(file.equals("dictionary.txt"))
                    insert(word);

                count++;
                //else
                    //insert(word, userHistory);     
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("No File Found");
        }

    }
    public boolean childExists(DLBnode hasChild)
    {
        if(hasChild != null)
            return true;
        else
            return false;
    }
    public boolean siblingExists(DLBnode hasSibling)
    {
        if(hasSibling != null)
            return true;
        else
            return false;
    }
    public DLBnode addSibling(char letter)
    {
        return currNode.rightSib = new DLBnode(letter);
    }
    public DLBnode addChild(char letter)
    {
        return currNode.child = new DLBnode(letter);
    }
    
    public boolean findNode(String word)
    { 
        currNode = root;
        for(int i = 0; i < word.length(); i++)
        {
            char searchLetter = word.charAt(i);
            currNode = findSibling(searchLetter);
            if(currNode == null)
                return false;
            currNode = currNode.child;
            
        }
        return true;
    }
    public DLBnode findSibling(char searchLetter)
    {
        if(currNode.value == searchLetter)
            return currNode;
        else if(siblingExists(currNode) == false)
            return null;
        else
        {
            while(siblingExists(currNode))
            {
                currNode = currNode.rightSib;
                if(currNode.value == searchLetter)
                    return currNode;
            }
        }
        //System.out.println("Find sibling not working");
        return null;
    }
    public ArrayList<String> getWords()
    {
        ArrayList<String> words = new ArrayList<String>();
        String suffix = "";
        findWords(words, suffix, currNode);
        return words;

    }
    public void findWords(ArrayList<String> words, String suffix, DLBnode search)
    {
        
        if(search == null || words.size() == 5)
            return;
        else if(search.value == terminate)
            words.add(suffix);
        
        findWords(words, suffix + search.value, search.child);
        //Need another recusirve call for siblings, this is in the case that we find a termination character
        findWords(words, suffix, search.rightSib);
    }

    private class DLBnode{
        public char value;
        public DLBnode rightSib;
        public DLBnode child;
       // public int frequencyCount would have been used for User History trie;
    
        public DLBnode()
        {

        }
        public DLBnode(char value)
        {
            this(value, null, null);
        }
        public DLBnode(char value, DLBnode rightSib, DLBnode child)
        {
            this.value = value;
            this.rightSib = rightSib;
            this.child = child;
        }
    }

}
