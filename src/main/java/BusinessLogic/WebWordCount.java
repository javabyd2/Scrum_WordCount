package BusinessLogic;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import Constants.MessagesAndConfigs;

/**
* Program counts words on the given web page and saves results in alphabetical order to the file, along with showing results in console.
*/
public class WebWordCount implements IWebWordCount {
	
	/**
	* Gets web page address from user input.
	*/
	public void GetWebPageFromUser(){
    	System.out.println(MessagesAndConfigs.UserInstruction);
    	
    	try (Scanner reader = new Scanner(System.in)){
        	String webPage = reader.next(); // get web page address from the user         
            GetWordCountOfPage(webPage);
    	}
	}
	
	/**
	* Connects to the given webpage, downloads content and performs Word-Count algorithm. Saves results to the file.
	*/
	public void GetWordCountOfPage(String webPage){
		Scanner in = null;
		PrintStream writer = null;
		
		try {
			// check if webpage address begins with http or https protocol
			if(webPage.matches("^(https?)://.*$") == false){
				throw new MalformedURLException();
			}
			
			// create connection to the web and get text
	        URL site = new URL(webPage);
	        URLConnection urlConnection = site.openConnection();
	        in = new Scanner(new InputStreamReader(urlConnection.getInputStream()));
	        
	        // count words
	        Map<String, Integer> wordCount = CountWords(in);
	        
	        // print results to file and output stream
	        LocalDate localDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MessagesAndConfigs.DatePattern);
	        String dateFormatted = formatter.format(localDate);
	        formatter = DateTimeFormatter.ofPattern(MessagesAndConfigs.DatePattern.replaceAll(MessagesAndConfigs.OnlyAlphaNumericRegex, ""));
	        
	        String webPageForFile = webPage.replaceAll(MessagesAndConfigs.OnlyAlphaNumericRegex,""); // remove all non alpha-numeric characters from webpage URL to allow putting it in filename without problems
	        
	        writer = new PrintStream(new File(formatter.format(localDate) + "_" + webPageForFile));
	         
	        System.out.println(dateFormatted + " " + webPage);
	        writer.println(dateFormatted + " " + webPage);
	        
	        for(String word : wordCount.keySet()){
	            System.out.println(word + " " + wordCount.get(word));
	        	writer.println(word + " " + wordCount.get(word));
	        }
		} catch(MalformedURLException e){
        	System.out.println(MessagesAndConfigs.MalformedURLExceptionMessage);
        	GetWebPageFromUser();
        } catch(UnknownHostException e){
        	System.out.println(MessagesAndConfigs.UnknownHostExceptionMessage);
        	GetWebPageFromUser();
        } catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			// always free memory (even in case of exception) to avoid memory leaks
			if (in != null){
				in.close();
			}
			if (writer != null){
				writer.close();
			}
		}
	}
	
	/**
	* Performs Word-Count algorithm on the input from Scanner.
	*/
	public Map<String,Integer> CountWords(Scanner input)
	{
		input.useDelimiter("[^a-zA-ZøüÊÒÛ≥ÍπúØè∆•å £”—']+");
        
        Map<String, Integer> wordCount = new TreeMap<String, Integer>(); // use TreeMap, as it can be ordered. If sorting by keys/words would not be required, we would use HashMap here.
        
        while (input.hasNext()) {
        	String word = input.next();
            if(!wordCount.containsKey(word)){
                wordCount.put(word, 1);
            }
            else{
                wordCount.put(word, wordCount.get(word) + 1);
            }
        }
        
        return wordCount;
	}
}