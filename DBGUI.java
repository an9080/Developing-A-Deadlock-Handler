import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.Box;

public class DBGUI extends JFrame {
	
	private JPanel contentPane;
	public JTextField textField;
	private JTextField textField_1;
	public String str1; 
	public String str;
	public int num;
	public int time;
	public int timeRollback = 0;
	public String printtxt;
	int LinesNum =0; 
	int TransactionsNum =0; 
	int countlock =0;
	String locks[]; 
	int timeStamp[];
   int comm[]; 
	private JTextField textField_2;
	private JLabel lblNewLabel_2;
	public JButton btnNewButton;
	public JButton btnNewButton_1;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DBGUI frame = new DBGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public DBGUI() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//m.main(null);
		
		JLabel lblNewLabel = new JLabel("Choose working tecnique, 1.wait-Die, 2. Wound-Wait");
		lblNewLabel.setBounds(30, 70, 413, 58);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Enter Directory of the file:");
		lblNewLabel_1.setBounds(30, 29, 356, 20);
		contentPane.add(lblNewLabel_1);
		
		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(81, 278, 307, 76);
		contentPane.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(30, 54, 356, 26);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(30, 116, 115, 26);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		btnNewButton = new JButton("Go");
		btnNewButton.setBounds(173, 238, 115, 29);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("Enter time to detect:");
		lblNewLabel_3.setBounds(30, 156, 235, 20);
		contentPane.add(lblNewLabel_3);
		
		textField_2 = new JTextField();
		textField_2.setBounds(30, 178, 115, 26);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		
		btnNewButton.addActionListener(new ActionListener(){
				
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 str1 = textField.getText(); 
			     str =  fileread(str1);
				 num = Integer.parseInt(textField_1.getText());
				 time = Integer.parseInt(textField_2.getText());
				 try {
					 readFile(str,num);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				 lblNewLabel_2.setText(printtxt);
			}
				}); 
	}
	
	

	public void readFile(String filepath, int num) throws Exception {
		
		File file = new File(filepath);
		BufferedReader in = new BufferedReader(new FileReader(file));
		String str; 
		  while ((str = in.readLine()) != null) 
			  LinesNum++; 
		 String[] fileLines = new String[LinesNum];	  
		  int[] arr2 = new int[LinesNum]; 
		 in.close();

		 BufferedReader in2 = new BufferedReader(new FileReader(file));
		 int i=0; 
		 while ((str = in2.readLine()) != null) {
			 fileLines[i] = str; 
			 arr2[i] = Integer.parseInt(str.substring(str.length() -1));
			 i++; }
		 in2.close();
		 
		 //in HashSet duplicate values are not allowed
		 HashSet<Integer> hash = new HashSet<Integer>(); 
	        for(int j = 0; j < LinesNum; j++) 
	            hash.add(arr2[j]); 
	        TransactionsNum = hash.size(); 
	   
	        
	   for (int j =0 ;j< LinesNum; j++) {
	 		   if(fileLines[j].charAt(0) == 'l')
	 			  countlock++; 
	 	   }
  
	   locks = new String[countlock];
	   int k =0; 
	   for (int j =0 ;j< LinesNum; j++) {
		   if(fileLines[j].charAt(0) == 'l')
			   locks[k++] = fileLines[j]; 
	   }
	   comm = new int[TransactionsNum]; 
	   timeStamp = new int[TransactionsNum]; 
	     for (int j=0; j< LinesNum; j++) {
		   if(fileLines[j].charAt(0) == 'b')	   
			   timeStamp[Integer.parseInt(fileLines[j].substring(fileLines[j].length() -1))-1] = j; 
		   if(fileLines[j].charAt(0) == 'c')
			   comm[Integer.parseInt(fileLines[j].substring(fileLines[j].length() -1))-1]=j;
	   
	   }
		   
	    check(fileLines,timeStamp,locks,num);
	    
	    if (time > LinesNum) 
	    	   printtxt = "Time entered is out of the schedule interval";
	    else {
	    	if ( time < timeRollback )
	    		printtxt = "until time "+ time + " there is no deadlock"; 
            
	    
	    	if (timeRollback == 0)
	    		printtxt = "there is no deadlock"; } 
}	
	
	public String fileread(String str) {

		for (int i= 0; i<str.length() ; i++) {
			if (str.charAt(i) == '\"' )
			str = str.substring(0,i) + '\"' + str.substring(i+1,str.length());
		}
		
		return str; }
	
		public void check (String[] fileLines, int[] timestamps, String[] locksArray, int userEnteredtechnique){
		boolean b = false;
		for (int i=0 ; i< locksArray.length-1; i++)
			for (int j=i+1; j< locksArray.length; j++) {
				if (locksArray[j].charAt(5) == 'X' ||  locksArray[i].charAt(5) == 'X')
						if (locksArray[j].charAt(7) == locksArray[i].charAt(7))
						{ b = checkcommit(i , j , fileLines , comm , locksArray);
							if (userEnteredtechnique == 1 && b ){
								waitDie(fileLines, timestamps, locksArray, i,j); 
								}
							else if(userEnteredtechnique == 2 && b )
								woundWait(fileLines, timestamps, locksArray, i,j);
							else printtxt = "No Deadlock"; }}
	}
   
   public boolean checkcommit(int pos1 , int pos2, String[] fileLines, int[] comm , String[] locksArray){
		boolean b = false;
		int time = 0 ; 
		int trans1= Integer.parseInt(locksArray[pos1].substring(locksArray[pos1].length() -1)); 
		int trans2 = Integer.parseInt(locksArray[pos2].substring(locksArray[pos2].length() -1));
		for(int h=0 ; h < fileLines.length; h++) {
			if(fileLines[h].equals(locksArray[pos2]))
				time = h;
		}
		
		if (comm[trans1-1] < time){
      System.out.print(comm[trans1-1] + "  " + trans1 + " " + time);
      System.out.println("b in com " + b);
			return false;}
		
			else if (comm[trans1-1] > time ) return true;
		
		
		return false;

	}

	public void waitDie (String[] fileLines, int[] timestamps, String[] locksArray, int pos1, int pos2){
		
	int trans1= Integer.parseInt(locksArray[pos1].substring(locksArray[pos1].length() -1)); 
	int trans2 = Integer.parseInt(locksArray[pos2].substring(locksArray[pos2].length() -1));
	if (timestamps[trans1-1] > timestamps[trans2-1]) {
		//wait 	
	}
	else 
		{for (int i=0; i< fileLines.length; i++)
			if (fileLines[i] == locksArray[pos2])
			timeRollback =i;
		
		printtxt = "transaction " + trans2 + " is rolled back at time " + timeRollback;
		}	
	}
	
	public void woundWait(String[] fileLines , int[] timestamps, String[] locksArray, int pos1, int pos2) {
			int trans1= Integer.parseInt(locksArray[pos1].substring(locksArray[pos1].length() -1)); 
			int trans2 = Integer.parseInt(locksArray[pos2].substring(locksArray[pos2].length() -1));
			
			if (timestamps[trans1-1] < timestamps[trans2-1]) {
				//wait 
			}
			else 
				{for (int i=0; i< fileLines.length; i++)
				{	if (fileLines[i] == locksArray[pos2])
					timeRollback =i;
				//delete(trans1);
				}
				printtxt = "transaction " + trans1 + " is rolled back at time " + timeRollback;
				//delete(trans1);
				}
		
	}
}

