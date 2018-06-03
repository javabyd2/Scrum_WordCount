import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import BusinessLogic.IWebWordCount;
import BusinessLogic.WebWordCount;
import Constants.MessagesAndConfigs;

public class WebPageWordCountUnitTest {
	
	private ByteArrayInputStream _input;
	private final ByteArrayOutputStream _output = new ByteArrayOutputStream();
	private IWebWordCount _testClass;
	
	@Rule
	public ExpectedException exception;
	
	@Before
	public void setUp() {
		exception = ExpectedException.none();
		_testClass = Mockito.spy(new WebWordCount()); // use spy to ignore method call getting input from user when exception occurs
		// mock user input and console output
		_input = new ByteArrayInputStream("WordC WordA WordB WordB WordA".getBytes());
		System.setIn(_input);  
		System.setOut(new PrintStream(_output));
	}
	
	@Test
	public void CheckIfMalformedURLExceptionIsHandled() {	    
		// Arrange
	    doNothing().when(_testClass).GetWebPageFromUser(); // ignore getting input from user again after error
	    
		// Act
	    _testClass.GetWordCountOfPage("123wronglyFormattedUrl");
	    
		// Assert  
	    assertEquals(MessagesAndConfigs.MalformedURLExceptionMessage, _output.toString().trim());
	}
	
	@Test
	public void CheckIfUnknownHostExceptionIsHandled() {	    
	    doNothing().when(_testClass).GetWebPageFromUser(); // ignore getting input from user again after error
	    
	    _testClass.GetWordCountOfPage("http://unknownHost123dhsauodgsadgsd");
	    
	    assertEquals(MessagesAndConfigs.UnknownHostExceptionMessage, _output.toString().trim());
	}

	@Test
	public void CountWordsTest() {
		try (Scanner reader = new Scanner(System.in)){
			Map<String,Integer> wordCount = _testClass.CountWords(reader);
			
			// check if words were properly counted
			Assert.assertEquals(2, (int) wordCount.get("WordA"));
			Assert.assertEquals(2, (int) wordCount.get("WordB"));
			Assert.assertEquals(1, (int) wordCount.get("WordC"));
			
			// check if words were properly sorted
			Assert.assertEquals("WordA", wordCount.keySet().toArray()[0]); 
			Assert.assertEquals("WordB", wordCount.keySet().toArray()[1]); 
			Assert.assertEquals("WordC", wordCount.keySet().toArray()[2]); 
		}
	}
	
	@After
	public void tearDown() throws Exception{
		_input.close();
		_output.close();
	}
}
