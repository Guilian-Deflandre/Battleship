
public class HTMLErrorGenerator{

	private StringBuilder HTMLPage;

	public HTMLErrorGenerator(ConstantsConversion.CODE error) {
		HTMLPage = new StringBuilder(1000);

		HTMLPage.append("<!DOCTYPE html>\n");
		HTMLPage.append("<html>\n");
		HTMLPage.append("<head>\n");
		HTMLPage.append("<style>\n");
		HTMLPage.append("	body{background: gray;}\n");
		HTMLPage.append("	h1{font-size: 110px;color: white;font-family: Impact, serif;margin-top: 100px;" +
						"margin-bottom: 5px;margin-left: auto;margin-right: auto;height: 150px;width: 900px;" +
				 "border: 1px solid black;border-radius: 5px;text-align: center;background-color: black;" +
																				"background-size: cover;}\n");
		HTMLPage.append("div{\n");
		HTMLPage.append("font-size: 40px;\n");
		HTMLPage.append("text-align: center;\n");
		HTMLPage.append("}\n");
		HTMLPage.append("</style>\n");
		HTMLPage.append("	<title>Error</title>\n");
		HTMLPage.append("	<meta charset=\"utf-8\">\n");
		HTMLPage.append("</head>\n");
		HTMLPage.append("<body>\n");
		HTMLPage.append("<div>\n");
		HTMLPage.append("<h1>\n");
		HTMLPage.append(error.toString());
		HTMLPage.append("\n</h1>\n");
		HTMLPage.append("</div>\n");
		HTMLPage.append("</body>\n");
		HTMLPage.append("</html>\n");
	}

	public String HTMLPage() {
		return HTMLPage.toString();
	}

}
