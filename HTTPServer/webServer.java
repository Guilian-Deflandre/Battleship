import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.*;


public class webServer {
	/*
	 * USE OF THE CLASS:
	 * 	Generate a java server in order to generate and update the web page of the implement of the Battle-
	 *  ship game from the project 1.
	 */
	private static ServerSocket socketServer;
	private static ExecutorService pool;
	private static int maxThreads;
	private static int clientID = 0;
	public static URL BASE_URL;
	public static ArrayList<Grid> actifGame = new ArrayList<Grid>();
	public static ArrayList<Fame> hallOfFame = new ArrayList<Fame>();
	
	public static void main(String[] args){
		if(args.length != 1){
			System.err.println("Incorrect input! Please respect usage: \n 'java WebServer maxThreads':\n "
							  + "\t -maxThreads = number of of requests that can be treated simultaneously.");
			return;
		}else{
			try{
				if(Integer.parseInt(args[0]) < 1){
					System.err.println("Please enter a valid value for maximum number of threads.");
					return;
				}else if(Integer.parseInt(args[0]) > ConstantsConversion.MAX_THREADS){
					System.err.println("Number of threads too high, it has been set at 20 for security.");
					maxThreads = ConstantsConversion.MAX_THREADS;
				}else{
					maxThreads = Integer.parseInt(args[0]);
				}
			}catch(NumberFormatException  e1){
				System.err.println("Please enter a valid value for maximum number of threads.");
				return;
			}
		}
		
		try{
			socketServer = new ServerSocket(ConstantsConversion.PORT);
			pool = Executors.newFixedThreadPool(maxThreads);
			BASE_URL = new URL(ConstantsConversion.PROTOCOL_URL + "://" + ConstantsConversion.HOST 
															      	          + ConstantsConversion.PORT);
			
			System.out.println("Listening on port " + ConstantsConversion.PORT);
			while(true){
				Socket socket = socketServer.accept();
				pool.execute(new Battleship(socket));
				System.out.println("Connection of client");
			}

		}catch(IOException e){
			e.printStackTrace();
			pool.shutdownNow();
			try{
				socketServer.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	
	public static void hallOfFameManager(Fame finisher){
		/*
		 * USE OF THE METHOD:
		 *  Add the finisher score and name (declare in Fame class) in the hall of fame if its result are 
		 *  good enought
		 * Arguments:
		 * 	-finisher			A Fame object containing game statistics of a COOKIE client
		 * Returns:
		 *  	/
		 */
		if(hallOfFame.size() < 10){
			hallOfFame.add(finisher);
		}else{
			int worstScore = hallOfFame.get(0).getScore();
			int indexFameToRemove = 0;
			for(int i=1; i<hallOfFame.size(); i++){
				if(hallOfFame.get(i).getScore() > worstScore){
					worstScore = hallOfFame.get(i).getScore();
					indexFameToRemove = i;
				}
			}
			if(finisher.getScore() < hallOfFame.get(indexFameToRemove).getScore()){
				hallOfFame.set(indexFameToRemove, finisher);
			}
		}
	}
	
	public static int getClientID(){
		clientID++;
		return clientID;
	}
	
}


