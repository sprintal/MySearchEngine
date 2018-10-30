import java.util.*;
public class Record
{
    private String term;
    private Hashtable<String, Integer> docIdTfList;
    private int df;
    private double idf;
    
    public Record()
    {
        term = "";
        docIdTfList = new Hashtable<String, Integer>();
        df = 0;
        idf = 0.0;
    }
    
    public Record(String newTerm, Hashtable<String, Integer> newDocIdTfList, int newDf, double newIdf)
    {
        term = newTerm;
        docIdTfList = newDocIdTfList;
        df = newDf;
        idf = newIdf;
    }
    
    public String getTerm()
    {
        return term;
    }
    
    public void setTerm(String newTerm)
    {
        term = newTerm;
    }
    
    public Hashtable<String, Integer> getDocIdTfList()
    {
        return docIdTfList;
    }
    
    public void setDocIdTfList(Hashtable<String, Integer> newDocIdTfList)
    {
        docIdTfList = newDocIdTfList;
    }
    
    public int getTf(String docId)
    {
        int tf = docIdTfList.get(docId).intValue();
        return tf;
    }
    
    public void addTfByOne(String docId)
    {
        int tf = docIdTfList.get(docId).intValue();
        docIdTfList.put(docId, tf + 1);
    }
    
    private Integer[] setDocIdTf(int newDocId, int newTf)
    {
        Integer[] docIdTf = new Integer[2];
        docIdTf[0] = newDocId;
        docIdTf[1] = newTf;
        return docIdTf;
    }
    
    public void addDocIdTf(String newDocId, int newTf)
    {
        docIdTfList.put(newDocId, newTf);
    }
    
    public int getDf()
    {
        return df;
    }
    
    public void setDf(int newDf)
    {
        df = newDf;
    }
    
    public double getIdf()
    {
        return idf;
    }
    
    public void setIdf(double newIdf)
    {
        idf = newIdf;
    }
    
    public double caculateIdf(int N)
    {
        idf = Math.log((double)N / ((double)df + 1));
        return idf;
    }
}