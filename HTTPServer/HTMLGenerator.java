import java.net.URL;

public class HTMLGenerator{
	/*
	 * USE OF THE CLASS:
	 * 	Generate the requested HTML page from client in function of the requested url. This object also ne-
	 *  ed to know the state of the current client game.
	 */

	private StringBuilder HTMLPage;

	/* --------------------------------------------------------------------------------------------------- *
	 *                                            CONSTRUCTOR                                              *
	 * --------------------------------------------------------------------------------------------------- */
	public HTMLGenerator(Grid game, URL url){
		HTMLPage = new StringBuilder(1000);
		// Generate the play.html HTML page
		if(url.getPath().equals("/play.html")){
			HTMLPage.append("<!DOCTYPE html>\n");
			HTMLPage.append("<head>\n");
			HTMLPage.append("<style>\n");
			HTMLPage.append("	#grid{margin-left: auto;margin-right: auto;border-collapse:collapse;1px solid "
																								+ "black;}\n");
			HTMLPage.append("	.unknowCell{width: 40px;height: 40px;background: none;border: 2px solid black;}\n");
			HTMLPage.append("	.border{color: white;background-color: black;}\n");
			HTMLPage.append("	.ocean{background-color: blue;}\n");
			HTMLPage.append("	button{width: 40px;height: 40px;background: none;}\n");
			HTMLPage.append("	body{background: gray;}\n");
			HTMLPage.append("	h1{font-size: 110px;color: white;font-family: Impact, serif;margin-top: 0px;" +
					    "margin-bottom: 5px;margin-left: auto;margin-right: auto;height: 150px;width: 900px;" +
					 "border: 1px solid black;border-radius: 5px;text-align: center;background-color: black;" +
																				  "background-size: cover;}\n");
			HTMLPage.append("	.missedCell{border-radius: 50%;background-color: white;background-position: "
																	 + "center;background-size: 30px 30px;}\n");
			HTMLPage.append("	.touchedCell{border-radius: 50%;background-color: red;background-position: center;"
					 												 + "background-size: 30px 30px;}\n");
			HTMLPage.append("</style>\n");
			HTMLPage.append("	<title>Battleship</title>\n");
			HTMLPage.append("	<meta charset=\"utf-8\">\n");
			HTMLPage.append("</head>\n");
			HTMLPage.append("<body>\n");
			HTMLPage.append("<h1>Battleship game</h1>\n");
			HTMLPage.append("<table id=\"grid\">\n");
			HTMLPage.append("	<thead>\n");
			HTMLPage.append("		<th class=\"border\"></th>\n");
			HTMLPage.append("		<th class=\"border\">1</th>\n");
			HTMLPage.append("		<th class=\"border\">2</th>\n");
			HTMLPage.append("		<th class=\"border\">3</th>\n");
			HTMLPage.append("		<th class=\"border\">4</th>\n");
			HTMLPage.append("		<th class=\"border\">5</th>\n");
			HTMLPage.append("		<th class=\"border\">6</th>\n");
			HTMLPage.append("		<th class=\"border\">7</th>\n");
			HTMLPage.append("		<th class=\"border\">8</th>\n");
			HTMLPage.append("		<th class=\"border\">9</th>\n");
			HTMLPage.append("		<th class=\"border\">10</th>\n");
			HTMLPage.append("	</thead>\n");
			HTMLPage.append("	<tbody class=\"ocean\">\n");
			HTMLPage.append(game.toHTML());
			HTMLPage.append("	</tbody>\n");
			HTMLPage.append("</table>\n");
			HTMLPage.append("<script type=\"text/javascript\">\n");
			HTMLPage.append("var grid = document.getElementById(\"grid\"), x, y;\n");
			HTMLPage.append("	for(var i=0;i<grid.rows.length;i++){ \n");
			HTMLPage.append("		for(var j=0;j<grid.rows[i].cells.length;j++){\n");
			HTMLPage.append("			grid.rows[i].cells[j].addEventListener('click', function(){\n");
			HTMLPage.append("				y = this.parentElement.rowIndex;\n");
			HTMLPage.append("				x = this.cellIndex;\n");
			HTMLPage.append("				var request = new XMLHttpRequest();\n");
			HTMLPage.append("				request.open(\"GET\", \"http://localhost:8028/play.html?\" + (x-1) + \"&\" + (y-1), true);\n");
			HTMLPage.append("				request.onreadystatechange = function(){\n");
			HTMLPage.append("				console.log('Click');"				);
			HTMLPage.append("				console.log('this :',this);\n");
			HTMLPage.append("				console.log('this.readyState :',this.readyState);\n");
			HTMLPage.append("				console.log('this.status :',this.status);\n");
			HTMLPage.append("				console.log('this.responseText :',this.responseText);\n");
			HTMLPage.append("					if(this.readyState == 4 && this.status == 200){\n");
			HTMLPage.append("	 					var result = parseInt(this.responseText,10);\n");
			HTMLPage.append("						console.log(result);	\n");
			HTMLPage.append("						if(result == 0){\n");
			HTMLPage.append("	 						document.getElementById(\"grid\").rows[y].cells[x].className=\"missedCell\";\n");
			HTMLPage.append("	 					}else if((result>0) && (result<6)){\n");
			HTMLPage.append("	 						document.getElementById(\"grid\").rows[y].cells[x].className=\"touchedCell\";\n");
			HTMLPage.append("	 					}else if(result == 10){\n");
			HTMLPage.append("	 						alert(\"You win this game! You will be redirected towards the Hall Of Fame.\");\n");
			HTMLPage.append("	 						window.location.replace(\"http://localhost:8028/halloffame.html\");\n");
			HTMLPage.append("	 					}else if(result == 11){\n");
			HTMLPage.append("	 						alert(\"You lose this game! You will be redirected towards a new one.\");\n");
			HTMLPage.append("	 						window.location.replace(\"http://localhost:8028/halloffame.html\");\n");
			HTMLPage.append("	 					}\n");
			HTMLPage.append("					}\n");
			HTMLPage.append("				request.addEventListener(\"error\", function () {\n");
			HTMLPage.append("					console.error(\"Erreur réseau\");\n");
			HTMLPage.append("				});\n");
			HTMLPage.append("			 request.send();\n");
			HTMLPage.append("			});\n");
			HTMLPage.append("		}\n");
			HTMLPage.append("	}");
			HTMLPage.append("</script>\n");
			HTMLPage.append("<noscript>\n");
			HTMLPage.append("	<form action=\"http://localhost:8028/play.html\" method=\"POST\">\n");
			HTMLPage.append("	<p>JavaScript is disable in your browser. Enter your guess via this form:</p>\n");
			HTMLPage.append("	<select name=\"y\">\n");
			HTMLPage.append("		<option value=\"0\">A</option>\n");
			HTMLPage.append("		<option value=\"1\">B</option>\n");
			HTMLPage.append("		<option value=\"2\">C</option>\n");
			HTMLPage.append("		<option value=\"3\">D</option>\n");
			HTMLPage.append("		<option value=\"4\">E</option>\n");
			HTMLPage.append("		<option value=\"5\">F</option>\n");
			HTMLPage.append("		<option value=\"6\">G</option>\n");
			HTMLPage.append("		<option value=\"7\">H</option>\n");
			HTMLPage.append("		<option value=\"8\">I</option>\n");
			HTMLPage.append("		<option value=\"9\">J</option>\n");
			HTMLPage.append("	</select>");
			HTMLPage.append("	<select name=\"x\">\n");
			HTMLPage.append("		<option value=\"0\">1</option>\n");
			HTMLPage.append("		<option value=\"1\">2</option>\n");
			HTMLPage.append("		<option value=\"2\">3</option>\n");
			HTMLPage.append("		<option value=\"3\">4</option>\n");
			HTMLPage.append("		<option value=\"4\">5</option>\n");
			HTMLPage.append("		<option value=\"5\">6</option>\n");
			HTMLPage.append("		<option value=\"6\">7</option>\n");
			HTMLPage.append("		<option value=\"7\">8</option>\n");
			HTMLPage.append("		<option value=\"8\">9</option>\n");
			HTMLPage.append("		<option value=\"9\">10</option>\n");
			HTMLPage.append("	</select>\n");
			HTMLPage.append("	<input type=\"submit\" value=\"Submit\"/>\n");
			HTMLPage.append("	</form>\n");
			HTMLPage.append("</noscript>\n");
			HTMLPage.append("</body>");
			HTMLPage.append("</html>");
		// Generate the halloffame.html HTML page
		}else if(url.getPath().equals("/halloffame.html")){
			HTMLPage.append("<!DOCTYPE html>\n");
			HTMLPage.append("<html>\n");
			HTMLPage.append("<head>\n");
			HTMLPage.append("<style>\n");
			HTMLPage.append("	body{background: gray;}\n");
			HTMLPage.append("	h1{font-size: 70px;color: white;font-family: Impact, serif;margin-top: 0px;" +
					    "margin-bottom: 5px;margin-left: auto;margin-right: auto;height: 200px;width: 900px;" +
					 "border: 1px solid black;border-radius: 5px;text-align: center;background-color: black;" +
								"background-size: cover;}ul{font-size: 15px;font-family: Impact;}\n");
			HTMLPage.append("</style>\n");
			HTMLPage.append("	<title>Battleship Hall of fame</title>\n");
			HTMLPage.append("	<meta charset=\"utf-8\">\n");
			HTMLPage.append("</head>\n");
			HTMLPage.append("<body>\n");
			HTMLPage.append("	<h1>Wall Of Fame of the Battleship game</h1>\n");
			HTMLPage.append("	<ul>\n");
			for(int i=0; i<webServer.hallOfFame.size(); i++){
				HTMLPage.append("		<li>ID: "+ webServer.hallOfFame.get(i).getName() + "<br>Score: " +
										Integer.toString(webServer.hallOfFame.get(i).getScore()) +"</li>\n");
			}
			HTMLPage.append("	</ul>\n");
			HTMLPage.append("	</body>\n");
			HTMLPage.append("</html>\n");
		}
	}


	public String HTMLPage() {
		/*
		 * USE OF THE METHOD:
		 *  Convert the initialized HTML page in a String (@override of toString()).
		 * Arguments:
		 * 	/
		 * Returns:
		 *  HTMLPage.toString():  String containing the requested HTML piece of code.
		 */
		return HTMLPage.toString();
	}

}
