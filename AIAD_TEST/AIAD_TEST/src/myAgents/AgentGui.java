package myAgents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class AgentGui extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	MyHelloWorldAgent myAgent =  null;
	
		
	JTextArea textarea = null; 
	JTextField textfield = null; 
	JScrollPane scrollpane = null; 
	JFrame myFrame = null;
	public AgentGui(MyHelloWorldAgent myAgent, JFrame myFrame) {
		
			this.myAgent  =  myAgent;	
	        this.myFrame =  myFrame;
			
			setBorder(BorderFactory.createEtchedBorder()); 
	        setLayout(new BorderLayout()); 
	       

	        // Create the area in which the messages will be seen after a RETURN is hit. 
	        // Using the JTextArea class to construct the GUI for this purpose. 
	        textarea = new JTextArea(); 
	        textarea.setBackground(Color.cyan); 
	        textarea.setLineWrap(true); 
	        textarea.setWrapStyleWord(true); 
	        textarea.setEditable(false); 
	        scrollpane = new JScrollPane(textarea); 
	        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

	        // Create the area in which the user can key in the text message to be displayed. 
	        // Using the JTextField class to construct the GUI for this purpose. 
	        textfield = new JTextField(); 
	        textfield.setBackground(Color.yellow); 
	        textfield.setDragEnabled(true); 
	        textfield.requestFocus(true);

	        add("Center", scrollpane); 
	        add("South", textfield);
	        
	        textfield.addActionListener(this);
	 
	}
	public void actionPerformed(ActionEvent evt) { 
        String text = textfield.getText(); 
        // So you want to quit? 
		if ("quit".equalsIgnoreCase(text)) { 
		    // Let's be nice 
			
		    System.exit(1); 
		}
		// pesquisa DF por agentes "ping"
		         DFAgentDescription template = new DFAgentDescription();
		         ServiceDescription sd1 = new ServiceDescription();
		         sd1.setType("Agente Ocupado");
		         template.addServices(sd1);
		         try {
		        	 
		        	 //procura todos os agentes; 
		            DFAgentDescription[] result = DFService.search(myAgent, template);
		            // envia mensagem "pong" inicial a todos os agentes "ping"
		            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		            for(int i=0; i<result.length; ++i)
		            msg.addReceiver(result[i].getName());
		            msg.setContent(text);
		            myAgent.send(msg);
		         } 
		         
		         catch(FIPAException e) 
		         { e.printStackTrace(); 
		         JOptionPane.showMessageDialog(myFrame, "Some errors in the program");
		         }
		     
		// OK, let's just send the message then 
        
		
		// And clear the TextField 
		textfield.setText("");
		 }


	
	
}
