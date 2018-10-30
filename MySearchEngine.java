import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
public class MySearchEngine
{
    public MySearchEngine()
    {
    }
    
     public static void main(String args[]) throws Exception
    {
        List<String> resultList = new ArrayList<String>();
        Scanner scan = new Scanner(System.in);
        ArrayList<String> hashStrings = new ArrayList<String>();
        FileScanner fileScan = new FileScanner();
        System.out.println("Welcome to Kang's Search Engine!");
        String instruct = args[0];
        if (instruct.equals("index"))
        {
            String dirPath = args[1];
            String targetPath = args[2];
            String stopWordsPath = args[3];
            while (!fileScan.isDir(dirPath))
            {
                System.out.println("Error, the inputted path is not a valid diractory.");
                System.out.print("Please try again to enter the directory to be indexed: ");
                dirPath = scan.nextLine().trim();
            }
            while (!fileScan.isFile(stopWordsPath))
            {
                System.out.println("Error, the inputted stop words list path is not a valid file.");
                System.out.print("Please try again to enter the stop words list file path: ");
                stopWordsPath = scan.nextLine().trim();
            }
            if (!fileScan.isDir(targetPath))
            {
                File target = new File(targetPath);
                target.mkdir();
            }
            System.out.println(" Indexing " + dirPath + " to " + targetPath + 
                               " using stop words from " + stopWordsPath);
            long startTime=System.currentTimeMillis();
            Indexer inde = new Indexer();
            hashStrings = inde.indexing(inde.fileToTokens(dirPath, stopWordsPath), dirPath, targetPath);
            long endTime=System.currentTimeMillis();
            double excTime = (double)(endTime - startTime) / 1000;
            System.out.println("Index completed. Whole session completed in " + excTime + "s.");
        }
        else if (instruct.equals("search"))
        {
            Searcher search = new Searcher();
            String indexPath = args[1];
            String numberOfDocsString = args[2];
            Indexer inde = new Indexer();
            String[] keywordList = Arrays.copyOfRange(args, 3, args.length);
            while (!fileScan.isDir(indexPath))
            {
                System.out.println("Error, the inputted path is not a valid diractory.");
                System.out.print("Please try again to enter the directory to be searched: ");
                indexPath = scan.nextLine().trim();
            }
            while (!numberOfDocsString.matches("[0-9]+"))
            {
                System.out.println("Error, the inputted information is not a valid itteger number.");
                System.out.print("Please try again to enter the number of files to be shown: ");
                numberOfDocsString = scan.nextLine().trim();
            }
            int numberOfDocs = Integer.parseInt(numberOfDocsString);
            String keywordString = new String();
            for (String keyword : keywordList)
            {
                keywordString = keywordString + "," + keyword;
            }
            keywordString = keywordString.substring(1);
            System.out.println("Searching " + indexPath + " for keywords " + keywordString + " and showing top " + numberOfDocsString + " results.");
            ArrayList<String> keywordArray = new ArrayList<String>();
            Tokenizer toke = new Tokenizer();
            keywordArray = toke.fileToWords(keywordString);
            keywordList = inde.queryToStemmed(keywordArray);
            Hashtable<String, Double> result = search.generateDocVector(search.fileToIndex(indexPath), keywordList);
            resultList = search.sortResult(result);
            DecimalFormat decimalFormat=new DecimalFormat();
            decimalFormat.applyPattern("#0.000");
            for (int index = 0; index < Math.min(resultList.size(), numberOfDocs); index++)
            {
                System.out.println(resultList.get(index) + " " + decimalFormat.format(result.get(resultList.get(index))));
            }
        }
        else
        {
            System.out.println("Error, please enter a valid instruction.");
        }
    }
}