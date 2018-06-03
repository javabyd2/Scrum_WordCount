package BusinessLogic;
import java.util.Map;
import java.util.Scanner;

public interface IWebWordCount {
	void GetWebPageFromUser();
	void GetWordCountOfPage(String webPage);
	Map<String,Integer> CountWords(Scanner input);
}
