import BusinessLogic.IWebWordCount;
import BusinessLogic.WebWordCount;

/**
* Program counts words on the given web page and saves results in alphabetical order to the file, along with showing results in console.
*/
public class Program {	
	public static void main(String[] args){
		IWebWordCount webWordCount = new WebWordCount();
		webWordCount.GetWebPageFromUser();
	}
}