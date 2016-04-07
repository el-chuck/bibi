package bibi;

import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Element;

public class Bibi {
	private final static String SEARCH_TEXT = "Bibi";

	private final static int START_PAGE_NUMBER = 1;
	private final static int END_PAGE_NUMBER = 10000;

	private final static int PAGE_LOAD_WAIT_TIME = 5000;

	private final static String START_URL = "https://steamcommunity.com/search/users/#page=" + START_PAGE_NUMBER
			+ "&filter=users&text=" + SEARCH_TEXT;

	private final static String OUT_FILE = "bibis.txt";

	private final static String PATTERN = "^d.*bibi.*$";

	public static void main(String[] args) throws IOException {
		BrowserEngine browser = BrowserFactory.getWebKit();

		FileWriter writer = new FileWriter(OUT_FILE);

		Pattern pattern = Pattern.compile(PATTERN);

		Page page = browser.navigate(START_URL);
		page.show();

		for (int i = START_PAGE_NUMBER; i <= END_PAGE_NUMBER; i++) {

			writer.write("############ PAGE" + i + System.lineSeparator());

			try {
				Thread.sleep(PAGE_LOAD_WAIT_TIME);
			} catch (InterruptedException e) {
			}

			for (Element e : page.getDocument().queryAll(".searchPersonaName")) {
				String name = e.getText().orElse("");
				Matcher m = pattern.matcher(name.toLowerCase());
				if (m.matches()) {
					writer.write(name + System.lineSeparator());
				}
			}

			page.executeScript("CommunitySearch.NextPage()");

			writer.flush();
		}

		writer.close();
		page.close();
		browser.shutdown();
		System.out.println("done");
	}
}
