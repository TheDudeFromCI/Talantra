package talantra.game;

import java.util.Scanner;

public class CommandListener implements Runnable{
	public CommandListener(){
		Thread t = new Thread(this);
		t.setName("Command Listener");
		t.setDaemon(true);
		t.start();
	}
	public void run(){
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		while(true){
			try{
				processCommand(scan.nextLine());
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
	}
	private void processCommand(@SuppressWarnings("unused") String line){}
}
