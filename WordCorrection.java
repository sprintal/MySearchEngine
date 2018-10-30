import java.util.*;
public class WordCorrection
{
    public WordCorrection()
    { 
    }
    
    private Hashtable<String, Integer> wordList()
    {
        Hashtable<String, Integer> wordSet = new Hashtable<String, Integer>();
        String dirPath = "doc";
        // Scanner scan = new Scanner(System.in);
        FileScanner fileScan = new FileScanner();
        //System.out.print("Please input standard doc set path: ");
        //dirPath = scan.nextLine().trim();
        // while (!fileScan.isDir(dirPath))
        // {
            // System.out.println("Error, the inputted path is not a valid directory.");
            // System.out.print("Please try again to enter a directory path: ");
            // dirPath = scan.nextLine().trim();
        // }
        //ArrayList<String> wordSet = new ArrayList<String>();
        String[] files = fileScan.readDir(dirPath);
        for (int index = 0; index < files.length; index++)
        {
            String fileContent = fileScan.readFile(dirPath + "\\" + files[index]);
            fileContent = fileContent.replaceAll("[\\r\\n{}()'\"]", " ");
            String[] fileContentList = fileContent.split(" ");
            for (int index2 = 0; index2 < fileContentList.length; index2++)
            {
                if (!wordSet.containsKey(fileContentList[index2]))
                    wordSet.put(fileContentList[index2].toLowerCase(), 1);
            }
        }
        return wordSet;
    }
    
    public void correction(String inputtedString)
    {
        inputtedString = inputtedString.trim();
        Hashtable<String, Integer> wordSet = wordList();
        String[] inputtedWordList = inputtedString.split(" ");
        char[] alphabat = alphabat();
        int correct = 0;
        Hashtable<String, ArrayList<String>> correctedTable = new Hashtable<String, ArrayList<String>>();
        for (int index = 0; index < inputtedWordList.length; index++)
        {
            ArrayList<String> corrected = new ArrayList<String>();
            String word = inputtedWordList[index].toLowerCase();
            if (wordSet.containsKey(word))
                correct++;
            else
            {
                ArrayList<String> added = add(word);
                ArrayList<String> removed = remove(word);
                ArrayList<String> replaced = replace(word);
                ArrayList<String> changed = change(word);
                for (String correctedWord : added)
                {
                    if (wordSet.containsKey(correctedWord))
                        corrected.add(correctedWord);
                }
                for (String correctedWord : removed)
                {
                    if (wordSet.containsKey(correctedWord))
                        corrected.add(correctedWord);
                }
                for (String correctedWord : replaced)
                {
                    if (wordSet.containsKey(correctedWord))
                        corrected.add(correctedWord);
                }
                for (String correctedWord : changed)
                {
                    if (wordSet.containsKey(correctedWord))
                        corrected.add(correctedWord);
                }
                correctedTable.put(word, corrected);
            }   
        }
        if (correct == inputtedWordList.length)
            System.out.println("The inputted String is correct.");
        else if (correctedTable.size() != 0)
            {
                Enumeration<String> wrongWords = correctedTable.keys();
                while (wrongWords.hasMoreElements())
                {
                    String wrongWord = wrongWords.nextElement();
                    ArrayList<String> corrected = correctedTable.get(wrongWord);
                    if (corrected.size() != 0)
                    {
                        System.out.println("The inputted word " + wrongWord + " may be wrong, do you mean the following?");
                        for (String correctedWord : corrected)
                        {
                            System.out.print(correctedWord + " ");
                        }
                        System.out.println();
                    }
                    else
                        System.out.println("The inputted word " + wrongWord + " may contain undetectable errors.");
                }
            }
    }
    
    private char[] alphabat()
    {
        char[] alphabat = new char[26];
        for (int index = 0; index < alphabat.length; index++)
        {
            alphabat[index] = (char)('a' + index);
        }
        return alphabat;
    }
    
    private ArrayList<String> add(String word)
    {
        char[] alphabat = alphabat();
        ArrayList<String> result = new ArrayList<String>();
        for (int index = 0; index < word.length() + 1; index++)
        {
            for(int index2 = 0; index2 < alphabat.length; index2++)
            {
                String wordAltered = word.substring(0, index) + alphabat[index2] + word.substring(index, word.length());
                result.add(wordAltered);
            }
        }
        return result;
    }
    
    private ArrayList<String> remove(String word)
    {
        ArrayList<String> result = new ArrayList<String>();
        for (int index = 0; index < word.length(); index++)
        {
            String wordAltered = word.substring(0, index) + word.substring(index + 1, word.length());
            result.add(wordAltered);
        }
        return result;
    }
    
    public ArrayList<String> replace(String word)
    {
        char[] alphabat = alphabat();
        ArrayList<String> result = new ArrayList<String>();
        for (int index = 0; index < word.length(); index++)
        {
            for (int index2 = 0; index2 < alphabat.length; index2++)
            {
                String wordAltered = word.substring(0, index) + alphabat[index2] + word.substring(index + 1);
                result.add(wordAltered);
            }
        }
        return result;
    }
    
    private ArrayList<String> change(String word)
    {
        ArrayList<String> result = new ArrayList<String>();
        for (int index = 0; index < word.length() - 1; index++)
        {
            String wordAltered = word.substring(0, index) + word.charAt(index + 1) + word.charAt(index) + word.substring(index + 2, word.length());
            result.add(wordAltered);
        }
        return result;
    }
}