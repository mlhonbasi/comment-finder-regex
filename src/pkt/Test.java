/**
*
* @author Melih Onbaşı - melih.onbasi@ogr.sakarya.edu.tr
* @since 15 Nisan 2023 Cumartesi
* <p>
* Bu sınıf main olarak görev yapmaktadır.
* Regex ifadelerinin tanımlanması, döngüler ile istenilen ifadelerin saptanması ve istenilen işlemlerin yapılmasını sağlar.
* Sayma işlemi ile ilgili ek 2 fonksiyon içermektedir.
* </p>
*/

package pkt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args)
	{
		String fileArgs = args[0]; 
		FileOperations fr = new FileOperations(); 
		String javaText = fr.getFileContent(fileArgs); //Java dosyası içeriğini string olarak alma işlemi.
		
		String classRegex ="(public|private|default|protected)*\\s+class\\s+(\\w*)"; //Sınıf tanımlamaları bulan regex ifadesi.
		Pattern classPattern = Pattern.compile(classRegex);
		Matcher classMatcher = classPattern.matcher(javaText);
		
		fr.clearFileContents(); //Metin belgeleri içeriklerini temizler.
		
		while(classMatcher.find()) //Bir sınıf bulduğu sürece dönen döngü.
		{
			
			String className = classMatcher.group(2);
			fr.writeToFile(className);
			System.out.print("\nSınıf: " + className);
			
			int classStart = classMatcher.start(2); //Sınıf başlangıç indeksi.
			int classEnd = braceMatcher(javaText, classStart); //Sınıf bitiş indeksi.
			
			String classContent = javaText.substring(classStart, classEnd); //Sınıf içeriğini ayrı biçimde elde eder.
			
			String functionRegex = "(/\\*\\*([^\\*]|\\*(?!/))*?@?\\w*.*?\\*/\\s*)*(public|private|protected)*\\s+(((\\w+\\s+)?\\w+)\\s*\\(.*?\\))\\s*";
			Pattern functionPattern = Pattern.compile(functionRegex);
			Matcher functionMatcher = functionPattern.matcher(classContent);
			
			//Yorumları bulmak için regex ifadeleri.
			String singleLineRegex = "//.*";
			String multiLineRegex = "/\\*[^\\*].*?\\*/";
			String javadocRegex = "/\\*\\*([^\\*]|\\*(?!/))*?@?\\w*.*?\\*/"; 
							
			while(functionMatcher.find()) //Bir fonksiyon bulduğu sürece dönen döngü.
			{
				String functionName = functionMatcher.group(4);
				String jdocnFunction = functionMatcher.group();
				
				if(functionName.contains(" ")) //Fonksiyon isminden return tipini ayıklar.
				{
					String ext ="\\w+\\s";
					String extFuncName = "";
					extFuncName = functionName.replaceAll(ext,  extFuncName);
					functionName = extFuncName;
				}
				System.out.print("\n\tFonksiyon adı: " + functionName);
				
				int functionStart = functionMatcher.start(4); //Fonksiyon başlangıç indeksi.
				int functionEnd = braceMatcher(classContent, functionStart); //Fonksiyon bitiş indeksi.
				
				String functionContent = classContent.substring(functionStart,functionEnd); //Fonksiyon içeriğini(gövdesi) ayrı olarak elde eder.
			
				int singleLineComments = countMatcher(functionContent, singleLineRegex); //Yorum sayıları
				int multiLineComments = countMatcher(functionContent, multiLineRegex);
				int javadocComments = countMatcher(jdocnFunction + functionContent, javadocRegex);
				
				fr.writeToFile(singleLineRegex, functionContent, functionName);	//Txt dosyalarına yazdırma işlemi
				fr.writeToFile(multiLineRegex, functionContent, functionName);			
				fr.writeToFile(javadocRegex, jdocnFunction + functionContent, functionName);
				
				printNumberOfComments(singleLineComments, multiLineComments, javadocComments); //Konsol ekranına yazdırma işlemi
			}
			System.out.print("\n\no~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o~o\n\n"); //
			fr.separateClasses(); //Txt dosyalarında sınıflar arasına işaret çizme(yukarıdaki o~o~ ifadesi ile aynı).
		}
		
	}
	
	private static int braceMatcher(String content, int startIndex) // Metindeki iki ayraç gövdesinin bitiş indeksini döndürür.
	{
		int braceCount = 0;
		
		for(int i=startIndex; i < content.length(); i++)
		{
			char c = content.charAt(i);
			
			if(c == '{') {
				braceCount++;
			}
			else if(c=='}') {
				braceCount--;
				if(braceCount==0) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private static int countMatcher(String str, String regex) //İstenilen regex ifadesinin kaç adet geçtiğini döndürür
	{
		Pattern pattern;
		
		if(regex == "//.*") 	
			pattern = Pattern.compile(regex);
		else 
			 pattern = Pattern.compile(regex, Pattern.DOTALL);
		
		Matcher matcher = pattern.matcher(str);
		int count = 0;
		
		while(matcher.find()) {
			count++;
		}
		return count;
	}
	
	private static void printNumberOfComments(int slc, int mlc, int jdc) //Konsola yazdırma işlemi
	{
		System.out.print("\n\t\tTek Satır Yorum Sayısı:   " + slc);
		System.out.print("\n\t\tÇok Satırlı Yorum Sayısı: " + mlc);
		System.out.print("\n\t\tJavadoc Yorum Sayısı:     " + jdc);
		System.out.print("\n-------------------------------------------");
	}

}
