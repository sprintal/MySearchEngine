import java.io.*;
import java.util.*;
import java.util.regex.*;
public class Tokenizer
{
    public Tokenizer()
    {
    }
    
    public ArrayList<String> fileToWords(String fileContent)
    {
        fileContent = fileContent.trim();
        ArrayList<String> words = new ArrayList<String>();
        while (fileContent.length() != 0)
        {
            String crossLine = "[A-Za-z0-9]+\\-\\r\\n";
            String word = "";
            String plural = "[A-Za-z]+'s";
            String hyphened = "[A-Za-z0-9]+\\-[A-Za-z0-9]+";
            String email = "[A-Za-z0-9\\._]+@[A-Za-z0-9\\.]+";
            String url = "http[s]{0,1}://[A-Za-z0-9\\.]+";
            String ip = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}";
            String singleQuated = "'.+-(',[\\s]*')'";
            String uppercaseSet = "[A-Z][A-Za-z0-9]*(\\s[A-Z][A-Za-z0-9]*)+";
            String abbr = "[A-Z](\\.[A-Z])+";
            String abbr2 = "[A-Z]+";
            String doubleNumber = "[0-9]+\\.[0-9]+";
            Pattern pattern = Pattern.compile("^(" + crossLine + "|" + plural + "|" + hyphened + "|" + email + "|" + url + "|" + ip + "|" + 
                                              singleQuated + "|" + uppercaseSet + "|" + abbr + "|" +abbr2 + "|" + doubleNumber + ")");
            Matcher matcher = pattern.matcher(fileContent);
            Pattern skip = Pattern.compile("^[\\r\\n\\.\\s{,:;\"'()?!}]+");
            Matcher skipper = skip.matcher(fileContent);
            Pattern normal = Pattern.compile("^[A-Za-z0-9]+");
            Matcher normaler = normal.matcher(fileContent);
            if (matcher.lookingAt())
            {
                word = matcher.group(0);
                fileContent = fileContent.substring(word.length());
                if (word.matches(crossLine))
                {
                    word = word.replaceAll("-\\r\\n", " ");
                    fileContent = word + fileContent;
                }
                else if (word.matches(plural))
                {
                    word = word.substring(0, word.length() -1).toLowerCase();
                    words.add(word);
                }
                else if (word.matches(singleQuated))
                {
                    word = word.substring(1, word.length() - 1).toLowerCase();
                    words.add(word);
                }
                else if (word.matches(hyphened + "|" + email + "|" + url + "|" + uppercaseSet))
                {
                    word = word.toLowerCase();
                    words.add(word);
                }
                else if (word.matches(abbr) || word.matches(abbr2))
                {
                    word = word.replaceAll("\\.", "").toUpperCase();
                    words.add(word);
                }
                else
                    words.add(word);
            }
            else if (skipper.lookingAt())
            {
                word = skipper.group(0);
                fileContent = fileContent.substring(word.length());
            }
            else if (normaler.lookingAt())
            {
                word = normaler.group(0).toLowerCase();
                fileContent = fileContent.substring(word.length());
                words.add(word);
            }
            if (word.equals(""))
                fileContent = fileContent.substring(1);
        }
        return words;
    }
    
    public Hashtable<String, Integer> stopWords(String filePath)
    {
        Hashtable<String, Integer> stopWords = new Hashtable<String, Integer>(); 
        try
        {
            FileReader reader = new FileReader(filePath);
            try
            {
                Scanner parser = new Scanner(reader);
                while (parser.hasNextLine()) 
                {
                    String word = parser.nextLine();
                    stopWords.put(word, 1);
                }
            }
            catch(Exception exception)
            {
                System.out.println(filePath + " content illegal.Please check file content!");
            }
            finally
            {
                reader.close();
            }
        }
        catch(FileNotFoundException exception)
        {
            System.out.println(filePath + " not found");
        }
        catch(IOException exception)
        {
            System.out.println("Unexpected I/O exception occurs.Please use another file");
        }
        return stopWords;
    }
}