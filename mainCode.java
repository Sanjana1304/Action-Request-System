import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;  

//mysql library
import java.sql.*;

public class mainCode {
    public static void main(String[] args) {
        final String dbUrl = "jdbc:mysql://localhost/ActionReqSystem?serverTimezone=UTC";
        final String user = "root";
        final String pwdd = "sanju1304";

        JFrame f=new JFrame("Action Request System");//creating instance of JFrame

        JLabel heading,userid,pwd,noAcc;
        heading=new JLabel("Login");
        heading.setBounds(130,50, 100,30);  

        userid=new JLabel("User id: ");  
        userid.setBounds(50,120, 100,30);  

        JTextField useridEntry;  
        useridEntry=new JTextField();  
        useridEntry.setBounds(130,120, 200,30);

        pwd=new JLabel("Password: ");  
        pwd.setBounds(50,170, 100,30); 

        JPasswordField pwdentry = new JPasswordField();
        pwdentry.setBounds(130,170,200,30);  
        
        JButton loginBtn=new JButton("Login");//creating instance of JButton  
        loginBtn.setBounds(135,230,100, 40);   
        

        noAcc=new JLabel("Don't have an account? ");  
        noAcc.setBounds(170,320, 200,30);  
        JButton regBtn=new JButton("Register");//creating instance of JButton  
        regBtn.setBounds(180,350,100, 40);   

        final JLabel validityLabel = new JLabel();            
        validityLabel.setBounds(20,290, 200,50); 
        
        f.add(userid); f.add(pwd);  f.add(heading); f.add(noAcc);f.add(loginBtn);f.add(regBtn);f.add(useridEntry); f.add(pwdentry);
        
         
        
        loginBtn.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {

                Connection conn1 = null;
                Statement stmt = null;

                try{
                    conn1 = DriverManager.getConnection(dbUrl,user,pwdd);
                    stmt = conn1.createStatement();

                    String givenpwd = new String(pwdentry.getPassword());

                    String query = "select sno,name,userid,pwd,usertype,dept from Users where userid='" + useridEntry.getText() + "'";
                    ResultSet loginDeets=stmt.executeQuery(query);

                    while(loginDeets.next())
                        
                        //Checking if the login details are correct and proceeding to next window
                        if(loginDeets.getString("userid").equals(useridEntry.getText()) && loginDeets.getString("pwd").equals(givenpwd)){
                            String utype = loginDeets.getString("usertype");
                            Integer sno = loginDeets.getInt("sno");

                            //IF ITS A CUSTOMER
                            if(utype.equals("Customer")){
                                JFrame cust_frame=new JFrame("Customer Page");

                                final JLabel welcomelabel = new JLabel();            
                                welcomelabel.setBounds(15,20, 400,50);

                                String username = loginDeets.getString("name");
                                String welcomeText = "Welcome "+username;
                                welcomelabel.setText(welcomeText);

                                JLabel inpReq = new JLabel("Input Your Request:");
                                inpReq.setBounds(30,60,400,50);

                                JTextArea inpReq_textArea=new JTextArea();  
                                inpReq_textArea.setBounds(50,100, 350,300); 
                                
                                JButton submitBtn=new JButton("Submit");
                                submitBtn.setBounds(50,400,100, 20);

                                JButton viewProposolBtn=new JButton("View Proposals");
                                viewProposolBtn.setBounds(30,450,110, 40);

                                JButton closeTcktBtn=new JButton("Close a ticket");
                                closeTcktBtn.setBounds(150,450,110, 40);
                                
                                JButton urTcktsBtn=new JButton("All Tickets");
                                urTcktsBtn.setBounds(30,500,110, 40);


                                submitBtn.addActionListener(new ActionListener(){
                                    public void actionPerformed(ActionEvent e){
                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String reqValue = inpReq_textArea.getText();
                                            
                                            String query2 = "insert into userreq(sno,request,status)"+ "values(" + sno + " , '" + reqValue + "','active')" ;
                                            stmt.executeUpdate(query2);

                                            JOptionPane.showMessageDialog(cust_frame, "Your ticket has been created. Issue will be solved soon", "Alert", JOptionPane.WARNING_MESSAGE);
                                            inpReq_textArea.setText("");

                                        }
                                        catch(Exception x){
                                            System.out.println(x);
                                        }
                                    }
                                });

                                urTcktsBtn.addActionListener(new ActionListener(){
                                    public void actionPerformed(ActionEvent e){
                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String query = "SELECT * FROM userreq where sno='" + sno + "'";

                                            ResultSet tckts = stmt.executeQuery(query);

                                            String arr[] =new String[0];
                                            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));

                                            while (tckts.next()){
                                                String sts = tckts.getString("status");
                                                int tid = tckts.getInt("tcktid");

                                                String eachVal = "Ticket id #"+tid+" - "+sts;
                                                arrayList.add(eachVal);

                                            }
                                            arr = arrayList.toArray(arr);

                                            JFrame tcktsFrame = new JFrame("Your Tickets");
                                            JPanel panel = new JPanel(new BorderLayout());
                                            List<String> myList = new ArrayList<String>(Arrays.asList(arr));

                                            final JList<String> list = new JList<String>(myList.toArray(new String[myList.size()]));
                                            JScrollPane scrollPane = new JScrollPane();
                                            scrollPane.setViewportView(list);
                                            list.setLayoutOrientation(JList.VERTICAL);
                                            
                                            panel.add(scrollPane);

                                            tcktsFrame.add(panel);

                                            tcktsFrame.setSize(250, 250);
                                            tcktsFrame.setLocationRelativeTo(null);
                                            tcktsFrame.setVisible(true);


                                        }
                                        catch(Exception y){
                                            System.out.println(y);
                                        }
                                    }
                                });

                                viewProposolBtn.addActionListener(new ActionListener(){
                                    public void actionPerformed(ActionEvent e){
                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String query = "SELECT tcktid FROM userreq where status='assigned' and proposal_status!='Accepted'";

                                            ResultSet tckts = stmt.executeQuery(query);

                                            String arr[] =new String[0];
                                            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));

                                            

                                            while (tckts.next()){
                                                int tid = tckts.getInt("tcktid");
                                               

                                                String eachVal = "#"+tid;
                                                arrayList.add(eachVal);

                                            }
                                            arr = arrayList.toArray(arr);

                                            JFrame prop_frame = new JFrame("Given Proposals");

                                            JLabel tcktEntryJLabel = new JLabel("Choose the ticket ID:");
                                            tcktEntryJLabel.setBounds(20,10,400,50);

                                            final JComboBox cb=new JComboBox(arr);    
                                            cb.setBounds(20, 60,90,20);                                 

                                            JButton viewButton=new JButton("View Proposal");
                                            viewButton.setBounds(120,60,150, 40);

                                            JLabel solnLabelhead = new JLabel("Proposed Solution:");
                                            solnLabelhead.setBounds(20,120,400,50);

                                            JLabel solnLabel = new JLabel();
                                            solnLabel.setBounds(80,170,400,150);

                                            
                                            JButton acceptButton=new JButton("Accept Proposal");
                                            acceptButton.setBounds(40,350,150, 40);

                                            JButton reviseButton=new JButton("Revise Proposal");
                                            reviseButton.setBounds(200,350,150, 40);

                                            acceptButton.addActionListener(new ActionListener(){
                                                public void actionPerformed(ActionEvent e){
                                                    try{
                                                        Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                                        Statement stmt = null;
                                                        stmt = conn.createStatement();

                                                        String data = " "   + cb.getItemAt(cb.getSelectedIndex());  

                                                        String tcktVal =  data.substring(2);

                                                        int tcktValint = Integer.parseInt(tcktVal);

                                                        String query = "update userreq set proposal_status='Accepted' where tcktid='" + tcktValint + "'" ;
                                                        stmt.executeUpdate(query);

                                                        JOptionPane.showMessageDialog(cust_frame, "You've accepted the proposal. Issue will be solved soon", "Alert", JOptionPane.WARNING_MESSAGE);
                                                        

                                                    }
                                                    catch(Exception z){
                                                        System.out.println(z);
                                                    }
                                                }
                                            });

                                            reviseButton.addActionListener(new ActionListener(){
                                                public void actionPerformed(ActionEvent e){
                                                    try{
                                                        Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                                        Statement stmt = null;
                                                        stmt = conn.createStatement();

                                                        String data = " "   + cb.getItemAt(cb.getSelectedIndex());  

                                                        String tcktVal =  data.substring(2);

                                                        //String tcktVal = tckTextField.getText();

                                                        int tcktValint = Integer.parseInt(tcktVal);

                                                        String query = "update userreq set proposal_status='Revise' where tcktid='" + tcktValint + "'" ;
                                                        stmt.executeUpdate(query);

                                                        JOptionPane.showMessageDialog(cust_frame, "You've asked to revise the proposal. A revised proposal will be updated soon", "Alert", JOptionPane.WARNING_MESSAGE);
                                                        

                                                    }
                                                    catch(Exception z){
                                                        System.out.println(z);
                                                    }
                                                }
                                            });

                                            viewButton.addActionListener(new ActionListener(){
                                                public void actionPerformed(ActionEvent e){
                                                    try{
                                                        Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                                        Statement stmt = null;
                                                        stmt = conn.createStatement();

                                                        String data = " "   + cb.getItemAt(cb.getSelectedIndex());  

                                                        String tcktVal =  data.substring(2);

                                                        int tcktValint = Integer.parseInt(tcktVal);

                                                        String query = "select proposal from userreq where tcktid = '" + tcktValint + "'" ;
                                                        ResultSet propset=stmt.executeQuery(query);

                                                        while(propset.next())
                                                            solnLabel.setText(propset.getString("proposal"));
                                                        
                                                    }
                                                    catch(Exception z){
                                                        System.out.println(z);
                                                    }
                                                }
                                            });

                                            

                                            prop_frame.add(tcktEntryJLabel);prop_frame.add(cb);prop_frame.add(viewButton);prop_frame.add(solnLabelhead);prop_frame.add(solnLabel);prop_frame.add(acceptButton);prop_frame.add(reviseButton);

                                            prop_frame.setSize(400,450); 
                                            prop_frame.setLayout(null);//using no layout managers  
                                            prop_frame.setVisible(true);            

                                        }
                                        catch(Exception n){
                                            System.out.println(n);
                                        }
                                                   
                                    }
                                });
                                
                                closeTcktBtn.addActionListener(new ActionListener(){
                                    public void actionPerformed(ActionEvent e){
                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String query = "SELECT tcktid FROM userreq where status='assigned' and proposal_status='Accepted'";

                                            ResultSet tckts = stmt.executeQuery(query);

                                            String arr[] =new String[0];
                                            ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));

                                            

                                            while (tckts.next()){
                                                int tid = tckts.getInt("tcktid");
                                               

                                                String eachVal = "#"+tid;
                                                arrayList.add(eachVal);

                                            }
                                            arr = arrayList.toArray(arr);

                                            JFrame close_frame = new JFrame("Closing a ticket");

                                            JLabel tcktEntryLabel = new JLabel("Choose the ticket ID:");
                                            tcktEntryLabel.setBounds(20,10,400,50);

                                            final JComboBox cb=new JComboBox(arr);    
                                            cb.setBounds(20, 50,90,20);

                                            JButton checkSolvedorNotButton=new JButton("Check");
                                            checkSolvedorNotButton.setBounds(120,50,70, 40);

                                            JLabel solvedOrnotLabel = new JLabel();
                                            solvedOrnotLabel.setBounds(80,100,400,150);

                                            checkSolvedorNotButton.addActionListener(new ActionListener(){
                                                public void actionPerformed(ActionEvent e){
                                                    try{
                                                        Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                                        Statement stmt = null;
                                                        stmt = conn.createStatement();
                                                        

                                                        String data = " " + cb.getItemAt(cb.getSelectedIndex());  

                                                        String tcktVal =  data.substring(2);

                                                        int tcktValint = Integer.parseInt(tcktVal);

                                                        String query = "select tcktclose from userreq where tcktid = '" + tcktValint + "'" ;
                                                        ResultSet propset=stmt.executeQuery(query);

                                                        String res="";

                                                        while(propset.next()){
                                                            res = new String(propset.getString("tcktclose"));
                                                            solvedOrnotLabel.setText(propset.getString("tcktclose"));
                                                        }
                                                        if(res.equals("Your issue has been solved")){
                                                            JButton closeIssueBtn=new JButton("Close Ticket");
                                                            closeIssueBtn.setBounds(120,170,150, 40);

                                                            closeIssueBtn.addActionListener(new ActionListener(){
                                                                public void actionPerformed(ActionEvent e){
                                                                    try{
                                                                        Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                                                        Statement stmt = null;
                                                                        stmt = conn.createStatement();
    
                                                                        String queryB = "update userreq set status='Solved' where tcktid=' "+tcktValint+"'";
                                                                        stmt.executeUpdate(queryB);
    
                                                                        JOptionPane.showMessageDialog(close_frame, "Ticket has been closed", "Alert", JOptionPane.WARNING_MESSAGE);
                                      
                                                                    }
                                                                    catch(Exception v){
                                                                        System.out.println(v);
                                                                    }
                                                                                    
                                                                }
                                                            });

                                                            close_frame.add(closeIssueBtn);
                                                        }
                                                            
                                                        
                                                    
                                                    }
                                                    catch(Exception b){
                                                        System.out.println(b);
                                                    }
                                                    

                                                }
                                            });

                                            close_frame.add(tcktEntryLabel);close_frame.add(cb);close_frame.add(solvedOrnotLabel);close_frame.add(checkSolvedorNotButton);
                                            
                                            close_frame.setSize(400,300); 
                                            close_frame.setLayout(null);//using no layout managers  
                                            close_frame.setVisible(true);

                                        }
                                        catch(Exception a){
                                            System.out.println(a);
                                        }
                                    }
                                });
                                
                                
                                cust_frame.add(welcomelabel);cust_frame.add(inpReq);cust_frame.add(inpReq_textArea);cust_frame.add(viewProposolBtn);cust_frame.add(urTcktsBtn);cust_frame.add(submitBtn);cust_frame.add(closeTcktBtn);

                                cust_frame.setSize(500,600); 
                                cust_frame.setLayout(null);//using no layout managers  
                                cust_frame.setVisible(true);
                            }

                            //IF ITS AN ADMIN
                            if(utype.equals("Admin")){
                                JFrame adm_frame=new JFrame("Admin Page");

                                final JLabel welcomelabel = new JLabel(); 
                                welcomelabel.setBounds(15,20, 400,50);

                                String username = loginDeets.getString("name");
                                String welcomeText = "Welcome "+username;
                                welcomelabel.setText(welcomeText);

                                final JLabel viewReqLbl = new JLabel("View Active Requests: ");            
                                viewReqLbl.setBounds(15,70, 300,50);

                                stmt = conn1.createStatement();

                                String queryA = "SELECT tcktid FROM userreq where status='active'";

                                ResultSet activetckts = stmt.executeQuery(queryA);

                                String arr[] =new String[0];
                                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));

                                
                                while (activetckts.next()){
                                    String sts = activetckts.getString("tcktid");
                                    String eachVal = "Ticket #"+sts;       
                                    arrayList.add(eachVal);

                                }
                                
                                arr = arrayList.toArray(arr);
                                List<String> myList = new ArrayList<String>(Arrays.asList(arr));
                                final JList<String> list = new JList<String>(myList.toArray(new String[myList.size()]));
                                
                                list.setBounds(20,120, 350,250);

                                JButton deetsBtn=new JButton("View Details");
                                deetsBtn.setBounds(20,370,100, 20);

                                deetsBtn.addActionListener(new ActionListener() {  
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame activeTcktFrame = new JFrame("Ticket details");

                                        String tcktname = "";  
                                        if (list.getSelectedIndex() != -1) {                       
                                          tcktname = list.getSelectedValue();
                                        }

                                        JLabel tcktLbl = new JLabel(tcktname);        
                                        tcktLbl.setBounds(20,10, 100,40);

                                        String tcktSubString = tcktname.substring(8);
                                        int tcktSubInteger = Integer.parseInt(tcktSubString);

                                        JLabel issueHeadLbl = new JLabel("Issue:");           
                                        issueHeadLbl.setBounds(20,50, 100,50);

                                        JLabel issueLbl = new JLabel();        
                                        issueLbl.setBounds(30,100, 400,200);

                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String queryB = "SELECT request FROM userreq where tcktid=' "+tcktSubInteger+"'";

                                            ResultSet reqResultSet = stmt.executeQuery(queryB);

                                            while (reqResultSet.next()){
                                                String sts = reqResultSet.getString("request");

                                                issueLbl.setText(sts);
            
                                            }
                                        }
                                        catch(Exception z){
                                            System.out.println(z);
                                        }
                                        
                                        JLabel assignreqLbl = new JLabel("Assign Request To:");        
                                        assignreqLbl.setBounds(20,280, 200,40);

                                        JRadioButton dept1,dept2,dept3;
                                        dept1=new JRadioButton("Support Ninjas");    
                                        dept1.setBounds(15,320,150,30);      
                                        dept2=new JRadioButton("Service Experts"); 
                                        dept2.setBounds(130,320,150,30);   
                                        dept3=new JRadioButton("Customer care"); 
                                        dept3.setBounds(250,320,150,30);

                                        ButtonGroup bg=new ButtonGroup();    
                                        bg.add(dept1);bg.add(dept2); bg.add(dept3);
                                        
                                        JButton assignBtn=new JButton("Assign");
                                        assignBtn.setBounds(170,360,100, 30);

                                        assignBtn.addActionListener(new ActionListener(){
                                            public void actionPerformed(ActionEvent e){
                                                try{
                                                    Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                                    Statement stmt = null;
                                                    stmt = conn.createStatement();
                                                    if(dept1.isSelected()){                                                                

                                                        String query = "update userreq set dev_assigned='Support Ninjas',status='assigned' where tcktid=' "+tcktSubInteger+"'";
                                                        stmt.executeUpdate(query);
                                                        
                                                    }    
                                                    else if(dept2.isSelected()){    
                                                        String query = "update userreq set dev_assigned='Service Experts',status='assigned' where tcktid=' "+tcktSubInteger+"'";
                                                        stmt.executeUpdate(query);
                                                    }
                                                    else if(dept3.isSelected()){    
                                                        String query = "update userreq set dev_assigned='Customer Care',status='assigned' where tcktid=' "+tcktSubInteger+"'";
                                                        stmt.executeUpdate(query);
                                                    }
                                                    JOptionPane.showMessageDialog(activeTcktFrame, "This ticket/request has been Assigned to the respective department", "Alert", JOptionPane.WARNING_MESSAGE);
                                                }
                                                catch(Exception q){
                                                    System.out.println(q);
                                                }
                                                
                                            } 
                                        });

                                        activeTcktFrame.add(tcktLbl);activeTcktFrame.add(issueHeadLbl);activeTcktFrame.add(issueLbl);activeTcktFrame.add(dept1);activeTcktFrame.add(dept2);activeTcktFrame.add(dept3);activeTcktFrame.add(assignreqLbl);activeTcktFrame.add(assignBtn);

                                        activeTcktFrame.setSize(400,450); 
                                        activeTcktFrame.setLayout(null);
                                        activeTcktFrame.setVisible(true);
                                         
                                    }
                                });

                                JLabel chkstsLbl = new JLabel("Check Status of any Ticket");
                                chkstsLbl.setBounds(20, 450, 300, 40);

                                JTextField tcktidEntry=new JTextField();  
                                tcktidEntry.setBounds(20,480, 200,30);

                                JButton chkStsBtn=new JButton("Submit");
                                chkStsBtn.setBounds(220,480,100, 30);

                                JLabel stsValLbl = new JLabel();
                                stsValLbl.setBounds(330, 480, 300, 30);
                                
                                chkStsBtn.addActionListener(new ActionListener(){  
                                    public void actionPerformed(ActionEvent e){
                                        String tcktValString = tcktidEntry.getText();
                                        int tcktValint = Integer.parseInt(tcktValString);

                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String queryB = "SELECT status FROM userreq where tcktid=' "+tcktValint+"'";

                                            ResultSet reqResultSet = stmt.executeQuery(queryB);

                                            while (reqResultSet.next()){
                                                String sts = reqResultSet.getString("status");
                                                String resultant = "Status : "+sts;
                                                stsValLbl.setText(resultant);
        
                                        }
                                        }
                                        catch(Exception a){
                                            System.out.println(a);
                                        }

                                    }
                                });



                                adm_frame.add(welcomelabel); adm_frame.add(list); adm_frame.add(viewReqLbl);adm_frame.add(deetsBtn); adm_frame.add(chkStsBtn);adm_frame.add(chkstsLbl);adm_frame.add(tcktidEntry);adm_frame.add(stsValLbl);

                                adm_frame.setSize(500,600); 
                                adm_frame.setLayout(null);//using no layout managers  
                                adm_frame.setVisible(true);
                            }
                            
                            //IF ITS A DEVELOPER
                            if(utype.equals("Developer")){
                                JFrame dev_frame=new JFrame("Developer Page");

                                final JLabel welcomelabel = new JLabel();         
                                welcomelabel.setBounds(15,20, 400,50);

                                String username = loginDeets.getString("name");
                                String welcomeText = "Welcome "+username;
                                welcomelabel.setText(welcomeText);

                                final JLabel viewReqLbl = new JLabel("Assigned Requests: ");            
                                viewReqLbl.setBounds(15,70, 300,50);

                                String dept = loginDeets.getString("dept");

                                Connection conn2 = null;
                                Statement stmt2 = null;

                                String arr[] = new String[0];
                                ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));

                                try{
                                    conn2 = DriverManager.getConnection(dbUrl,user,pwdd);
                                    stmt2 = conn2.createStatement();

                                    String query1 = "select tcktid from Userreq where status='assigned' and proposal_status!='Accepted' and dev_assigned='" + dept+ "'";
                                    ResultSet assignedSet=stmt2.executeQuery(query1);

                                    while (assignedSet.next()){
                                        String sts = assignedSet.getString("tcktid");
                                        String eachVal = "Ticket #"+sts;
                                        arrayList.add(eachVal);
                                    }
                                       
                                }
                                catch(Exception a){
                                    System.out.println(a);
                                }
                                arr = arrayList.toArray(arr);
                                List<String> myList = new ArrayList<String>(Arrays.asList(arr));
                                final JList<String> devlist = new JList<String>(myList.toArray(new String[myList.size()]));
                                devlist.setBounds(20,120, 350,250);
                                    
                                   
                                JButton deetsBtn=new JButton("View Details");
                                deetsBtn.setBounds(20,370,100, 20);

                                JLabel issueLbl = new JLabel();        
                                issueLbl.setBounds(20,390, 400,30);

                                deetsBtn.addActionListener(new ActionListener() {  
                                    public void actionPerformed(ActionEvent e) {   
                                        String tcktname = "";  
                                        if (devlist.getSelectedIndex() != -1) {                       
                                          tcktname = devlist.getSelectedValue();
                                        }

                                        String tcktSubString = tcktname.substring(8);
                                        int tcktSubInteger = Integer.parseInt(tcktSubString);

                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String queryB = "SELECT request FROM userreq where tcktid=' "+tcktSubInteger+"'";

                                            ResultSet reqResultSet = stmt.executeQuery(queryB);

                                            while (reqResultSet.next()){
                                                String sts = reqResultSet.getString("request");

                                                issueLbl.setText(sts);
            
                                            }
                                        }
                                        catch(Exception z){
                                            System.out.println(z);
                                        }
                        
                                         
                                         
                                    }  
                                });

                                JTextField SolnEntry=new JTextField();  
                                SolnEntry.setBounds(20,570, 200,30);

                                JButton propSolBtn=new JButton("Propose this Soln");
                                propSolBtn.setBounds(220,570,150, 30);

                                JLabel orLbl = new JLabel("or");
                                orLbl.setBounds(150, 620, 300, 40);

                                JButton rejectBtn=new JButton("Reject Tckt");
                                rejectBtn.setBounds(120,670,150, 30);

                                JButton acceptedPropsBtn=new JButton("Accepted Proposals");
                                acceptedPropsBtn.setBounds(30,720,150, 40);

                                propSolBtn.addActionListener(new ActionListener() {  
                                    public void actionPerformed(ActionEvent e) {
                                        String solnvalue = SolnEntry.getText();

                                        String tcktname = "";  
                                        if (devlist.getSelectedIndex() != -1) {                       
                                          tcktname = devlist.getSelectedValue();
                                        }

                                        String tcktSubString = tcktname.substring(8);
                                        int tcktSubInteger = Integer.parseInt(tcktSubString);

                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String queryB = "update userreq set proposal='"+solnvalue+"' where tcktid=' "+tcktSubInteger+"'";
                                            stmt.executeUpdate(queryB);

                                            JOptionPane.showMessageDialog(dev_frame, "Your proposal has been sent to the customer", "Alert", JOptionPane.WARNING_MESSAGE);
                                            SolnEntry.setText("");

                                            
                                        }
                                        catch(Exception z){
                                            System.out.println(z);
                                        }

                    
                                    }
                                });

                                rejectBtn.addActionListener(new ActionListener() {  
                                    public void actionPerformed(ActionEvent e) {

                                        String tcktname = "";  
                                        if (devlist.getSelectedIndex() != -1) {                       
                                          tcktname = devlist.getSelectedValue();
                                        }

                                        String tcktSubString = tcktname.substring(8);
                                        int tcktSubInteger = Integer.parseInt(tcktSubString);

                                        try{
                                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                            Statement stmt = null;
                                            stmt = conn.createStatement();

                                            String queryC = "update userreq set status='Rejected', proposal_status='NULL' where tcktid=' "+tcktSubInteger+"'";
                                            stmt.executeUpdate(queryC);

                                            JOptionPane.showMessageDialog(dev_frame, "Your rejection has been sent to the customer", "Alert", JOptionPane.WARNING_MESSAGE);

                                            
                                        }
                                        catch(Exception z){
                                            System.out.println(z);
                                        }

                    
                                    }
                                    });


                                acceptedPropsBtn.addActionListener(new ActionListener() {  
                                    public void actionPerformed(ActionEvent e) {
                                        JFrame propaccFrame=new JFrame("Accepted Proposals");

                                        JLabel propPageheadLabel=new JLabel("Ticketnames of Accepted Proposals ");  
                                        propPageheadLabel.setBounds(50,20, 300,30); 

                                        Connection conn2 = null;
                                        Statement stmt2 = null;
        
                                        String arr[] = new String[0];
                                        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(arr));
        
                                        try{
                                            conn2 = DriverManager.getConnection(dbUrl,user,pwdd);
                                            stmt2 = conn2.createStatement();
        
                                            String query1 = "select tcktid from Userreq where proposal_status='Accepted' and status!='Solved' and dev_assigned='" + dept+ "'";
                                            ResultSet acceptedSet=stmt2.executeQuery(query1);
        
                                            while (acceptedSet.next()){
                                                String sts = acceptedSet.getString("tcktid");
                                                String eachVal = "Ticket #"+sts;
                                                arrayList.add(eachVal);
                                            }
                                               
                                        }
                                        catch(Exception a){
                                            System.out.println(a);
                                        }
                                        arr = arrayList.toArray(arr);
                                        List<String> myList = new ArrayList<String>(Arrays.asList(arr));
                                        final JList<String> devlist = new JList<String>(myList.toArray(new String[myList.size()]));
                                        devlist.setBounds(20,80, 350,250);

                                        JButton solveBtn=new JButton("Solve");
                                        solveBtn.setBounds(130,350,100, 40);

                                        solveBtn.addActionListener(new ActionListener() {  
                                            public void actionPerformed(ActionEvent e) {
                                                String tcktname = "";  
                                                if (devlist.getSelectedIndex() != -1) {                       
                                                tcktname = devlist.getSelectedValue();
                                                }

                                                String tcktSubString = tcktname.substring(8);
                                                int tcktSubInteger = Integer.parseInt(tcktSubString);

                                                try{
                                                    Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                                                    Statement stmt = null;
                                                    stmt = conn.createStatement();

                                                    String queryB = "update userreq set tcktclose='Your issue has been solved' where tcktid=' "+tcktSubInteger+"'";
                                                    stmt.executeUpdate(queryB);

                                                    JOptionPane.showMessageDialog(dev_frame, "Issue has been solved. User will be notified", "Alert", JOptionPane.WARNING_MESSAGE);
                                                    SolnEntry.setText("");

                                                }
                                                catch(Exception z){
                                                    System.out.println(z);
                                                }



                                            }
                                        });

                                        propaccFrame.add(devlist);propaccFrame.add(propPageheadLabel);propaccFrame.add(solveBtn);
                                            
                                        propaccFrame.setSize(450,450);
                                        propaccFrame.setLayout(null);//using no layout managers  
                                        propaccFrame.setVisible(true);
                                    }
                                });
                                dev_frame.add(welcomelabel);dev_frame.add(viewReqLbl);dev_frame.add(deetsBtn);dev_frame.add(issueLbl);dev_frame.add(propSolBtn);dev_frame.add(SolnEntry);dev_frame.add(rejectBtn);dev_frame.add(orLbl); dev_frame.add(devlist);dev_frame.add(acceptedPropsBtn);

                                dev_frame.setSize(500,800);
                                dev_frame.setLayout(null);//using no layout managers  
                                dev_frame.setVisible(true);
                            }
                            
                        }
                            
                        else{
                            validityLabel.setText("Login failed !");
                            
                        }
                    
                }

                catch(Exception en){ 
                    System.out.println(en);
                }
                  
            }
        });
        f.add(validityLabel);
        

        regBtn.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {
                JFrame r = new JFrame("Registration Form");

                //Labels
                JLabel name,usertype,deptt,setPwd,devOnly;
                name=new JLabel("Name: ");  
                name.setBounds(50,70, 100,30);  

                usertype=new JLabel("User type: ");  
                usertype.setBounds(50,120, 100,30);  

                deptt=new JLabel("Dept: ");  
                deptt.setBounds(50,170, 200,30); 

                devOnly=new JLabel("(Only if you are a developer) ");  
                devOnly.setBounds(50,195, 200,30); 
                
                setPwd=new JLabel("Set password: ");  
                setPwd.setBounds(50,220, 100,30); 

                //Entries
                JTextField nameEntry,usertypeEntry,deptEntry; 

                nameEntry=new JTextField();  
                nameEntry.setBounds(150,70, 200,30);

                usertypeEntry = new JTextField();  
                usertypeEntry.setBounds(150,120, 200,30);

                deptEntry = new JTextField();  
                deptEntry.setBounds(150,170, 200,30);
                
                JPasswordField setPwdEntry = new JPasswordField();
                setPwdEntry.setBounds(150,220,200,30); 

                //Submit button
                JButton subBtn=new JButton("Submit");
                subBtn.setBounds(130,280,100, 40);

                subBtn.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        String nameval = nameEntry.getText();
                        String usertypeval = usertypeEntry.getText();
                        String deptval = deptEntry.getText();
                        String pwdval = new String(setPwdEntry.getPassword());
                        String useridval = nameval.substring(0, 3) + "." + usertypeval.substring(0,3)+"@thcse.com";

                        try{
                            Connection conn = DriverManager.getConnection(dbUrl,user,pwdd);
                            Statement stmt = null;
                            stmt = conn.createStatement();
                            
                            String query2 = "insert into users(userid,name,pwd,usertype,dept)"+ "values('" + useridval + "' , '" + nameval + "','" + pwdval + "','" + usertypeval + "','" + deptval + "')" ;
                            stmt.executeUpdate(query2);

                            String msg = "Registered Successfully. Your user id is "+useridval;

                            JOptionPane.showMessageDialog(r, msg, "Alert", JOptionPane.WARNING_MESSAGE);
                            
                        }
                        catch(Exception z){
                            System.out.println(z);
                        }
                    }
                });

                r.add(name); r.add(usertype); r.add(deptt); r.add(setPwd); r.add(subBtn); r.add(nameEntry);r.add(usertypeEntry); r.add(deptEntry); r.add(setPwdEntry);r.add(devOnly);

                r.setSize(400,400); 
                r.setLayout(null); 
                r.setVisible(true);
            }

        });

        f.setSize(400,500); 
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
