import java.util.*;
public class Searcher
{
    public Searcher()
    {
    }
    
    public Hashtable<String, Record> fileToIndex(String indexDir)
    {
        FileScanner fileScan = new FileScanner();
        Hashtable<String, Record> indexTable = new Hashtable<String, Record>();
        String[] indexList = fileScan.readFile(indexDir + "\\index.txt").split("\\r\\n");
        for (int index = 0; index < indexList.length; index++)
        {
            Record record = new Record();
            String[] recordString = indexList[index].split(",");
            String term = recordString[0];
            int length = recordString.length;
            String df = recordString[length - 1];
            record.setTerm(term);
            record.setIdf(Double.parseDouble(df));
            for (int index2 = 1; index2 < length - 1; index2 += 2)
            {
                record.addDocIdTf(recordString[index2], Integer.parseInt(recordString[index2 + 1]));
            }
            indexTable.put(term, record);
        }
        return indexTable;
    }
    
    public Hashtable<String, Double> generateDocVector(Hashtable<String, Record> indexTable, String[] keywordList)
    {
        Hashtable<Integer, String> vocabulary = new Hashtable<Integer, String>();
        Hashtable<Integer, String> indexToDoc = new Hashtable<Integer, String>();
        Hashtable<String, Integer> docList = new Hashtable<String, Integer>();
        Hashtable<String, Integer> queryTable = new Hashtable<String, Integer>();
        Enumeration<String> terms = indexTable.keys();
        int indexForTerm = 0;
        int indexForDoc = 0;
        while (terms.hasMoreElements())
        {
            String term = terms.nextElement();
            vocabulary.put(indexForTerm, term);
            Record record = indexTable.get(term);
            Enumeration<String> docs = record.getDocIdTfList().keys();
            while (docs.hasMoreElements())
            {
                String docId = docs.nextElement();
                if (docList.containsKey(docId))
                    continue;
                else if (!docList.containsKey(docId))
                {
                    docList.put(docId, indexForDoc);
                    indexToDoc.put(indexForDoc, docId);
                    indexForDoc++;
                }
            }
            indexForTerm++;
        }
        
        double docVectorMatrix[][] = new double[indexForDoc + 1][indexForTerm];
        System.out.println("docs: " + (docVectorMatrix.length - 1) + ", terms: " + docVectorMatrix[0].length);
        
        for (indexForTerm = 0; indexForTerm < docVectorMatrix[0].length; indexForTerm++)
        {
            String term = vocabulary.get(indexForTerm);
            Record record = indexTable.get(term);
            Hashtable<String, Integer> docIdTfList = record.getDocIdTfList();
            Enumeration<String> docIds = docIdTfList.keys();
            while (docIds.hasMoreElements())
            {
                String docId = docIds.nextElement();
                indexForDoc = docList.get(docId).intValue();
                docVectorMatrix[indexForDoc][indexForTerm] = record.getIdf() * docIdTfList.get(docId).intValue();
            }
        }
        for (int index = 0; index < keywordList.length; index++)
        {
            String term = keywordList[index];
            if (queryTable.containsKey(term))
            {
                int count = queryTable.get(term).intValue();
                count++;
                queryTable.put(term, count);
            }
            else if (!queryTable.containsKey(term))
            {
                queryTable.put(term, 1);
            }
        }
        Enumeration<String> keywords = queryTable.keys();
        while (keywords.hasMoreElements())
        {
            String keyword = keywords.nextElement();
            for (int index = 0; index < docVectorMatrix[0].length; index++)
            {
                String term = vocabulary.get(index);
                if (queryTable.containsKey(term))
                {
                    int tf = queryTable.get(term).intValue();
                    Record record = indexTable.get(term);
                    double idf = record.getIdf();
                    docVectorMatrix[docVectorMatrix.length - 1][index] = tf * idf;
                }
            }
        }
        //ArrayList simularityList = new ArrayList<>();
        Hashtable<String, Double> docToSimu = new Hashtable<String, Double>();
        for (int index = 0; index < docVectorMatrix.length - 1; index++)
        {
            double dq = 0.0;
            double d2 = 0.0;
            double q2 = 0.0;
            for (int index2 = 0; index2 < docVectorMatrix[0].length; index2++)
            {
                dq += docVectorMatrix[index][index2] * docVectorMatrix[docVectorMatrix.length - 1][index2];
                d2 += docVectorMatrix[index][index2] * docVectorMatrix[index][index2];
                q2 += docVectorMatrix[docVectorMatrix.length - 1][index2] * docVectorMatrix[docVectorMatrix.length - 1][index2];
            }
            double d = Math.sqrt(d2);
            double q = Math.sqrt(q2);
            Double simularity = dq / (d * q);
            String docId = indexToDoc.get(index);
            docToSimu.put(docId, simularity);
        }
        return docToSimu;
    }
    
    public List<String> sortResult(Hashtable<String, Double> result)
    {
        List<String> resultList = new ArrayList<String>(result.keySet());
        Collections.sort(resultList, new Comparator<Object>(){
            public int compare(Object arg0, Object arg1)
            {
                double re = result.get(arg1) - result.get(arg0);
    		return re > 0 ? 1 : -1;
            }
        });
        return resultList;
    }  
}