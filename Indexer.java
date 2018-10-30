import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
public class Indexer
{
    public Indexer()
    {
    }
    
    public ArrayList<ArrayList<String>> fileToTokens(String dirPath, String stopWordsPath) throws Exception
    {
        Tokenizer tokenizer = new Tokenizer();
        FileScanner fileScan = new FileScanner();
        Scanner scan = new Scanner(System.in);
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        Hashtable<String, Integer> stopWords = tokenizer.stopWords(stopWordsPath);
        String[] files = fileScan.readDir(dirPath);
        System.out.println("There are " + files.length + " files in directory " + dirPath + ".");
        for (int index = 0; index < files.length; index++)
        {
            long startTime=System.currentTimeMillis();
            System.out.println("Tokenizing " + dirPath + "\\" + files[index] + "...");
            String file = fileScan.readFile(dirPath + "\\" + files[index]);
            ArrayList<String> words = tokenizer.fileToWords(file);
            ArrayList<String> tokens = new ArrayList<String>();
            for (int index2 = 0; index2 < words.size(); index2++)
            {
                if(stopWords.containsKey(words.get(index2)))
                    continue;
                else
                {
                    Stemmer stem = new Stemmer();
                    stem.add(words.get(index2).toCharArray(), words.get(index2).length());
                    stem.stem();
                    tokens.add(stem.toString());
                }
            }
            long endTime=System.currentTimeMillis();
            double excTime = (double)(endTime - startTime) / 1000;
            System.out.println("Completed in " + excTime + "s");
            result.add(tokens);
        }
        return result;
    }
    
    public String[] queryToStemmed(ArrayList<String> keywordArray)
    {
        String[] keywordList = new String[keywordArray.size()];
        for (int index = 0; index < keywordArray.size(); index++)
        {
            Stemmer stem = new Stemmer();
            String keyword = keywordArray.get(index);
            stem.add(keyword.toCharArray(), keyword.length());
            stem.stem();
            keywordList[index] = stem.toString();
        }
        return keywordList;
    }
    
    public ArrayList<String> indexing(ArrayList<ArrayList<String>> result, String dirPath, String targetPath)
    {
        FileScanner fileScan = new FileScanner();
        String[] fileList = fileScan.readDir(dirPath);
        Hashtable<String, Record> indexTable = new Hashtable<String, Record>();
        System.out.println("Indexing terms.");
        for (int index = 0; index < result.size(); index ++)
        {
            ArrayList<String> fileTokens = result.get(index);
            for (int index2 = 0; index2 < fileTokens.size(); index2 ++)
            {
                String token = fileTokens.get(index2);
                Record record = new Record();
                if (!indexTable.containsKey(token))
                {
                    record.setTerm(token);
                    record.setDf(record.getDf() + 1);
                    record.addDocIdTf(fileList[index], 1);
                }
                else if (indexTable.containsKey(token))
                {
                    record = indexTable.get(token);
                    if (!record.getDocIdTfList().containsKey(fileList[index]))
                    {
                        record.setDf(record.getDf() + 1);
                        record.addDocIdTf(fileList[index], 1);
                    }
                    else if (record.getDocIdTfList().containsKey(fileList[index]))
                    {
                        record.addTfByOne(fileList[index]);
                    }
                }
                indexTable.put(token, record);
            }
        }
        System.out.println("There are " + indexTable.size() + " terms.");
        return indexToString(indexTable, fileList.length, targetPath);
    }
    
    public ArrayList<String> indexToString(Hashtable<String, Record> indexTable, int numberOfFiles, String targetPath)
    {
        Enumeration<String> tokens;
        ArrayList<String> hashStrings = new ArrayList<String>();
        tokens = indexTable.keys();
        while (tokens.hasMoreElements())
        {
            String token = tokens.nextElement();
            String hashString = token + ",";
            Record record = indexTable.get(token);
            Enumeration<String> docIdTfs = record.getDocIdTfList().keys();
            while (docIdTfs.hasMoreElements())
            {
                String docId = docIdTfs.nextElement().toString();
                int tf = record.getDocIdTfList().get(docId).intValue();
                hashString = hashString + docId.replaceAll(",", "") + "," + tf + ",";
            }
            hashString = hashString.substring(0, hashString.length() - 1);
            double idf = record.caculateIdf(numberOfFiles);
            DecimalFormat decimalFormat=new DecimalFormat();
            decimalFormat.applyPattern("#0.000");
            String idfString = decimalFormat.format(idf);
            hashString = hashString + "," + idfString;
            hashStrings.add(hashString);
            
        }
        try
            {
                PrintWriter outputFile = new PrintWriter(targetPath + "\\index.txt"); 
                //outputFile.println("test");
                for (int index = 0; index < hashStrings.size(); index++)
                {
                    outputFile.println(hashStrings.get(index));
                }
                outputFile.close();
            }
            catch(IOException exception)
            {
                System.out.println("I/O error happend when write file");
            }
            return hashStrings;
    }
}