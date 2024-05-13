/**
*
* @author Melih Onbaşı - melih.onbasi@ogr.sakarya.edu.tr
* @since 15 Nisan 2023 Cumartesi
* <p>
* Bu sınıfta dosya işlemleri tanımlanmıştır.
* Dosyaya yazma ve okuma metodları bulunmaktadır.
* </p>
*/

package pkt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;

public class FileOperations {
	
	String getFileContent(String filePath) // .java dosyasındaki içeriği string olarak alır ve döndürür.
	{
		Path path = Paths.get(filePath);
		String content ="";
		
	    try {
	    	content = Files.readString(path);
	    } 
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return content;
	}
	
	void writeToFile(String regex, String functionContent, String functionName) //Txt dosyalarına yazdırma işlemi yapar
	{
		Pattern pattern;
		
		if(regex == "//.*") 	
			pattern = Pattern.compile(regex);
		else 
			pattern = Pattern.compile(regex, Pattern.DOTALL);
		
		Matcher matcher = pattern.matcher(functionContent);
		
		switch(regex)
		{
			case "//.*": //Tek satır yorumları dosyaya yazar.
				try 
				{
					FileWriter singleLineWriter = new FileWriter("../teksatir.txt", true);
					singleLineWriter.write("Fonksiyon: " + functionName + "\n\n");
						
					while(matcher.find())
					{
						singleLineWriter.write(matcher.group() + "\n\n");
					}
					singleLineWriter.write("\n--------------------------\n\n");
					singleLineWriter.close();			
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
				
			case "/\\*[^\\*].*?\\*/": //Çok satırlı yorumları dosyaya yazar.
				try 
				{
					FileWriter multiLineWriter = new FileWriter("../coksatir.txt", true);
					multiLineWriter.write("Fonksiyon: " + functionName + "\n\n");
						
					while(matcher.find())
					{
						multiLineWriter.write(matcher.group() + "\n\n");
					}
					multiLineWriter.write("\n--------------------------\n\n");
					multiLineWriter.close();			
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;
				
			case "/\\*\\*([^\\*]|\\*(?!/))*?@?\\w*.*?\\*/": //Javadoc yorum satırlarını dosyaya yazar.
				try 
				{
					FileWriter javadocWriter = new FileWriter("../javadoc.txt", true);
					javadocWriter.write("Fonksiyon: " + functionName + "\n\n");
						
					while(matcher.find())
					{
						javadocWriter.write(matcher.group() + "\n\n");
					}
					javadocWriter.write("\n--------------------------\n\n");
					javadocWriter.close();			
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				break;	
		}
		
	}
	void writeToFile(String className) { //En başa sınıf ismi yazar.
		try {
			FileWriter slw = new FileWriter("../teksatir.txt", true);
			slw.write("\nSınıf: " + className + "\n\n");
			slw.close();
			
			FileWriter jdw= new FileWriter("../javadoc.txt", true);
			jdw.write("\nSınıf: " + className + "\n\n");
			jdw.close();
			
			FileWriter mlw= new FileWriter("../coksatir.txt", true);
			mlw.write("\nSınıf: " + className + "\n\n");
			mlw.close();
				
			}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	void separateClasses() //Txt dosyalarında sınıfların arasına işaret çeker
	{
		try {
			FileWriter slw = new FileWriter("../teksatir.txt", true);
			slw.write("\n\no~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o\n\n");
			slw.close();
			
			FileWriter mlw= new FileWriter("../coksatir.txt", true);
			mlw.write("\n\no~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o\n\n");
			mlw.close();
			
			FileWriter jdw= new FileWriter("../javadoc.txt", true);
			jdw.write("\n\no~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o\n\n");
			jdw.close();
			}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	void clearFileContents() //Programı çalıştırmadan önce metin belgelerinin içeriğini temizler.
	{
		try {
			FileWriter slCleaner = new FileWriter("../teksatir.txt", false);
			slCleaner.write("");
			slCleaner.close();
			
			FileWriter mlCleaner = new FileWriter("../coksatir.txt", false);
			mlCleaner.write("");
			mlCleaner.close();
			
			FileWriter jdCleaner = new FileWriter("../javadoc.txt", false);
			jdCleaner.write("");
			jdCleaner.close();
			}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
