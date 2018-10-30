import java.io.*;
import java.util.*;
public class FileScanner
{
    public FileScanner()
    {
    }
    
    public String readFile(String filePath)
    {
        String encoding = "UTF-8";
        File file = new File(filePath);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try 
        {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        try
        {
            return new String(fileContent, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
    
    public String[] readDir(String dirPath)
    {
        File file = new File(dirPath);
        String[] fileList = file.list();
        return fileList;
    }
    
    public boolean isDir(String dirPath)
    {
        try
        {
            File file = new File(dirPath);
            if (file.isDirectory())
            {
                return true;
            }
            else 
            {
                return false;
            }
        }
        catch(Exception e)
        {
            System.out.println("readDir() Exception: " + e.getMessage());
        }
        return false;
    }
    
    public boolean isFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            if (file.isFile())
            {
                return true;
            }
            else 
            {
                return false;
            }
        }
        catch(Exception e)
        {
            System.out.println("readDir() Exception: " + e.getMessage());
        }
        return false;
    }
}