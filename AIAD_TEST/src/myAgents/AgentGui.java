/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myAgents;

import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.HashMap;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Agenda.Event;
import ExcelManager.ExcelReader;

import org.joda.time.*;

/**
 *
 * @author barbo_000
 */
public class AgentGUI extends javax.swing.JFrame {

    /**
	 * 
	 */
    // Variables declaration - do not modify                     
   
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenu2Item1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JButton jButton1;
  	private JTable jTable2;
	private JScrollPane jScrollPane2;
	private JLabel jLabel5;
	private JFileChooser jFileChoose;

	Vector<Vector<String>> mySchedule = new Vector<Vector<String>>();
	DFAgentDescription[] myAgents; 
	Person localAgent;
	ExcelReader excelReader;
	Vector<String> hours = new Vector<String>();
	Vector<String> hoursEnd = new Vector<String>();
	Vector<String> hoursDuration = new Vector<String>(); 
 	private JComboBox<String> jComboBox3;
	
	private static final long serialVersionUID = 1L;

	/**
     * Creates new form GUI_NAME
     */
    public AgentGUI(Person  localAgent) {
        
    	this.getHours();
    	this.localAgent =  localAgent;
    	
        initComponents();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
    	
    	setTitle(this.localAgent.getLocalName());
   
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2Item1  =  new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Object[][] obj =null;
        
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            obj ,
            null
        ));
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
     
        //Adicionar as colunas � tabela principal
        	model.addColumn("Hora");
        	model.addColumn("Nome do Evento" );
            model.addColumn("Estado");          
            model.addColumn("Dura��o" );
            model.addColumn("Participantees");   
            model.addColumn("Assistentes");
            
        for(int i= 0; i < mySchedule.size(); i++){
        	model.addRow(mySchedule.get(i));  
        }
       
        
        
        jScrollPane1.setViewportView(jTable1);

        jMenu1.setText("File");

        jMenuItem1.setText("Importar Agenda");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1Item1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Exportar Agenda para '.xls'");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Gerir Eventos");

       
        jMenu2Item1.setText("Criar evento");
        jMenu2Item1.addActionListener(
        		new java.awt.event.ActionListener()
        		{
        			public void actionPerformed(java.awt.event.ActionEvent evt)
        			{
        				jMenuItem1ActionPerformed(evt);
        			}
        		}
        		
        		);
        jMenu2.add(jMenu2Item1);

        jMenuItem3.setText("Alterar evento");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Remover evento");
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 785, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {                                           
   
    
    
    }
    
    private void jMenu1Item1ActionPerformed(java.awt.event.ActionEvent evt)
    {
    	 //JTextField filename = new JTextField(), dir = new JTextField();
    	jFileChoose =  new JFileChooser();
    	int rVal = jFileChoose.showOpenDialog(this);
    	if (rVal == JFileChooser.APPROVE_OPTION) {
           String filename = jFileChoose.getSelectedFile().getName();
            //dir.setText(jFileChoose.getCurrentDirectory().toString());
           excelReader =  new ExcelReader(filename);
   		   excelReader.readSheetWithFormula(filename);
           this.mySchedule = excelReader.getMyExcelCells();
          
           try{
           this.localAgent.setEventVector(mySchedule);
           
           DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
           
           //Adicionar as colunas � tabela principal
 
               
           for(int i= 0; i < mySchedule.size(); i++){
           	model.addRow(mySchedule.get(i));  
           }
           }
           catch(IllegalArgumentException e)
           {
           JOptionPane.showMessageDialog(jDialog1, "O seu excel est� mal preenchido", "Erro na leitura" , JOptionPane.ERROR_MESSAGE);
          
           }
             
    	
    	
    	
    	
    	}
          
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
    	
    		this.eventCreateDialog();
    }                                          

    /**
     * @param args the command line arguments
     */
    public void renderGUI() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AgentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgentGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        setVisible(true);
        /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgentGUI().setVisible(true);
            }
        });*/
    
    }
    
    public void setMyAgents(DFAgentDescription[] myAgents) {
		this.myAgents = myAgents;
	}



                    

	public void setLocalAgent(Person localAgent) {
		this.localAgent = localAgent;
		
	}
	
	
	public void eventCreateDialog()
	{ 
		  jDialog1 = new javax.swing.JDialog();
	      jLabel1 = new javax.swing.JLabel();
	      jTextField1 = new javax.swing.JTextField();
	      jLabel2 = new javax.swing.JLabel();
	      jComboBox1 = new javax.swing.JComboBox<String>();
	      jComboBox2 = new javax.swing.JComboBox<String>();
	      jLabel3 = new javax.swing.JLabel();
	      jLabel4 = new javax.swing.JLabel();
	      jButton1 = new javax.swing.JButton();
	      jComboBox3 = new javax.swing.JComboBox<String>();
	      jScrollPane2 =  new JScrollPane();
	      jLabel5 =  new javax.swing.JLabel();	      
	      
	      jTextField1.setText("Nome do Evento");

	        jLabel2.setText("Nome do Evento");

	        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<String>(hours));

	        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<String>(hoursEnd));
	        
	        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<String>(hoursDuration));

	        jLabel3.setText("Hora de Fim");

	        jLabel4.setText("Hora de Inicio");

	        jButton1.setText("Enviar");
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	
	            	try 
	            	{
	            	jButton1ActionPerformed(evt);
	            	
	            	}
	            	catch(IllegalArgumentException e)
	            	{
	            		JOptionPane.showMessageDialog(jDialog1, "Impossivel Enviar o Agente. Argumentos Invalidos", "Erro no Envio" , JOptionPane.ERROR_MESSAGE);
	            	}
	            
	            }
	            
	            
	        });
	        
	        this.myAgents =  this.localAgent.getNetworkAgents();
	        
	       Object[][] data = null ;
	        
	
	       
	       	        
	        String[] colNames =  {
	                "Membros", "Mandat�rio"
	            };
	        
	     
	        jTable2 = new javax.swing.JTable();
	        
	        jTable2.setModel(new javax.swing.table.DefaultTableModel(data , colNames)); 
	        
	        javax.swing.table.DefaultTableModel tableModel = (DefaultTableModel) jTable2.getModel();
	      
	        jTable2.setRowSelectionAllowed(true);
	               
	        for(int i=0;  i < myAgents.length ;i++)
	        {
	        	if(!myAgents[i].getName().getLocalName().equals(this.localAgent.getLocalName())) 
	        	{
	        		Vector<Object> temp =  new Vector<Object>();
 	        		temp.add(myAgents[i].getName().getLocalName());
 	        		//System.out.println(i);
 	        		//System.out.println(myAgents[i].getName().getLocalName());
 	        		temp.add(new Boolean(false));
	        		tableModel.addRow(temp);
	        	}
	        	
	        	
	        }
	        jScrollPane2.setViewportView(jTable2);

	      
	        jLabel1.setText("Escolha os participantes");


	        jLabel5.setText("Dura��o");

	        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
	        jDialog1.getContentPane().setLayout(jDialog1Layout);
	        jDialog1Layout.setHorizontalGroup(
	            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jDialog1Layout.createSequentialGroup()
	                .addGap(120, 120, 120)
	                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	            .addGroup(jDialog1Layout.createSequentialGroup()
	                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(jDialog1Layout.createSequentialGroup()
	                        .addGap(18, 18, 18)
	                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(jTextField1)
	                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addGroup(jDialog1Layout.createSequentialGroup()
	                                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                    .addGroup(jDialog1Layout.createSequentialGroup()
	                                        .addGap(41, 41, 41)
	                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
	                                    .addGroup(jDialog1Layout.createSequentialGroup()
	                                        .addGap(32, 32, 32)
	                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
	                                    .addGroup(jDialog1Layout.createSequentialGroup()
	                                        .addGap(50, 50, 50)
	                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
	                                .addGap(10, 10, 10)))
	                        .addGap(39, 39, 39))
	                    .addGroup(jDialog1Layout.createSequentialGroup()
	                        .addGap(63, 63, 63)
	                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(116, 116, 116)))
	                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(jDialog1Layout.createSequentialGroup()
	                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
	                        .addComponent(jLabel1)
	                        .addGap(54, 54, 54))))
	        );
	        jDialog1Layout.setVerticalGroup(
	            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
	                .addGap(20, 20, 20)
	                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(jDialog1Layout.createSequentialGroup()
	                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(13, 13, 13)
	                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jLabel5)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                        .addComponent(jButton1))
	                    .addGroup(jDialog1Layout.createSequentialGroup()
	                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
	                        .addGap(0, 0, Short.MAX_VALUE)))
	                .addContainerGap())
	        );

	      
    jDialog1.setSize(new Dimension(550, 400));
    jDialog1.setVisible(true);
    }
	
	private void getHours()
	{	
		
		ExcelManager.Agenda myAgenda = new ExcelManager.Agenda(
											new ExcelManager.AgendaTime(8,0), 
											new ExcelManager.AgendaTime(20,0));
		for(int i= 0;  i <	myAgenda.getSchedule().getHoursParcels().size(); i++)
			{
			 hours.add( myAgenda.getSchedule().getHoursParcels().get(i).getBegin().toString());
			 hoursEnd.add(myAgenda.getSchedule().getHoursParcels().get(i).getEnd().toString());
			}
		
		
		hoursDuration.add("00:30");
		hoursDuration.add("01:00");
		hoursDuration.add("01:30");
		hoursDuration.add("02:00");
		hoursDuration.add("02:30");
		hoursDuration.add("03:00");
		hoursDuration.add("03:30");
		hoursDuration.add("04:00");
		hoursDuration.add("04:30");
		hoursDuration.add("05:00");
		
	}
	
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
			
		String beginHour  = jComboBox1.getSelectedItem().toString();
		if(beginHour.equals(null))
		{
		
		throw new IllegalArgumentException("Evento n�o enviado");
			
		}
		String endHour =  jComboBox2.getSelectedItem().toString();
		if(endHour.equals(null))
		{
			throw new IllegalArgumentException("Evento n�o enviado");
		}
		
		if(endHour.equals(beginHour))
		{
		throw new IllegalArgumentException("Evento n�o enviado");
		}
		String name = jTextField1.getText();
		if(name.equals(null))
		{
			throw new IllegalArgumentException("Evento n�o enviado");
		}
		
		String duration = jComboBox3.getSelectedItem().toString();
		if(duration.equals(null))
		{
			throw new IllegalArgumentException("Evento n�o enviado");
		}
		
		DateTime hourEnd = stringToDateTime(endHour);
		DateTime hourBegin = stringToDateTime(beginHour);
		Duration durationTime = stringToDuration(duration);
		Duration hourDuration =  new Duration(hourBegin, hourEnd);
		
		
		if(hourEnd.isBefore(hourBegin) || hourBegin.isAfter(hourEnd) || hourDuration.isShorterThan(durationTime))
		{
				throw new IllegalArgumentException("Evento n�o enviado");
		}
		
		
		int[]  selectedTableIndices = jTable2.getSelectedRows();
		
		if(selectedTableIndices.length ==0)
		{
			throw new IllegalArgumentException("Evento n�o enviado");
		}
		
		Map<String, Boolean> eventAgents = new HashMap<String, Boolean>();
		
		
		for(int i =0;  i < selectedTableIndices.length; i++)
		{
			//nao esta a ter em conta se os agentaes s�o mandatorios ou nao... ter em aten�ao!!
			//if(jTable2.getValueAt(selectedTableIndices[i], 1).equals("true"))
			if(jTable2.getValueAt(selectedTableIndices[i], 0).toString().equals("true"))
				eventAgents.put(jTable2.getValueAt(selectedTableIndices[i], 0).toString(),true);
			else
				eventAgents.put(jTable2.getValueAt(selectedTableIndices[i], 0).toString(),false);
			
		}
	
		//alterar
		//Event event =  new Event(hourBegin, hourEnd, hourDuration, eventAgents,name, this.localAgent.getLocalName());
		//this.localAgent.sendMsg(event);
		
		
	jDialog1.setVisible(false);
		
    }   
	
	public DateTime stringToDateTime(String strDate)
	{
		int int0 = Character.getNumericValue(strDate.charAt(0));
		int int1= Character.getNumericValue(strDate.charAt(1));
		int int2 = Character.getNumericValue(strDate.charAt(3));
		int int3 =  Character.getNumericValue(strDate.charAt(4));
	
		DateTime dateTime =  new DateTime(1993, 10, 8, (int0*10 + int1), (int2*10 + int3));
		
		return  dateTime;
	
		
	}
	
	public Duration stringToDuration(String strDate)
	{
		
		int int0 = Character.getNumericValue(strDate.charAt(0));
		int int1= Character.getNumericValue(strDate.charAt(1));
		int int2 = Character.getNumericValue(strDate.charAt(3));
		int int3 =  Character.getNumericValue(strDate.charAt(4));
		

		DateTime startTime =  new DateTime(1993, 10, 8, 0, 0);
		DateTime endTime =  new DateTime(1993, 10, 8, (int0*10 + int1), (int2*10 + int3));
		Duration myDuration =  new Duration(startTime, endTime);
		
		return myDuration;
		
	}
	
	
	
}
