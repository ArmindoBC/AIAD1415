package myAgents;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AgentGui extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JPanel myPanel ; 
	JLabel myLabel; 
	JButton myButton;
	JTextField myTextField;
	JTextArea myTextArea;
  	JScrollPane scrollPane; 
	
	public AgentGui() {
	
	this.myPanel  = new JPanel();
   	this.myButton =  new JButton("OK MANO!");
   	this.myLabel  =  new JLabel();
   	this.myTextField =  new JTextField(20);
   	this.myTextArea =  new JTextArea();
   	   	
   	myPanel.add(myButton);
   	myPanel.add(myLabel);
   	myPanel.add(myTextField);
   	myPanel.add(myTextArea);
    myTextArea.setLineWrap(true); 
    myTextArea.setWrapStyleWord(true);
   
   	
   	this.setTitle("yeeeahh");
  	this.setSize(300,200);
  	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
  	this.setVisible(true);
  	 
  	//text area 
  	scrollPane = new JScrollPane(myTextArea); 
   	myTextArea.setEditable(false);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
   	
   	
   	
  	myButton.addActionListener(new ActionListener()
  	{
  	  public void actionPerformed(ActionEvent e)
  	  {
  	    // display/center the jdialog when the button is pressed
  	    String myMessage = myTextField.getText();
  	    myTextField.setText("");
  	    myTextArea.append(myMessage + "\n");
  	  
  	  }

	
  	});  
  	
  	
  	this.add(myPanel);
	}
	
	
}
