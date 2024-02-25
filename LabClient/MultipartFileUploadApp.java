


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class MultipartFileUploadApp {
    
    public static void main(String[] args) {
        
        MultipartFileUploadFrame f = new MultipartFileUploadFrame();
        f.setTitle("::: 장비연동 클라이언트 :::");
        f.pack();
        f.addWindowListener(
                new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        }
        );
        f.setVisible(true);
    }
    
    public static class MultipartFileUploadFrame extends JFrame {
        
        private File targetFile;
        private JTextArea taTextResponse;
        private DefaultComboBoxModel cmbURLModel;
        
        public MultipartFileUploadFrame() {
            
            // 파일을 전송할 서버 URL      
        	String targetURL = "http://localhost:8080/httpclienttest/fileupload" ;
        	
        	
        	// URL입력 TextField
        	final JTextField tfdTargetUrl = new JTextField(30);
        	tfdTargetUrl.setText(targetURL);
        	tfdTargetUrl.setEditable(true);
        	
            JLabel lblTargetFile = new JLabel("장비로그파일:");
            
            final JTextField tfdTargetFile = new JTextField(30);
            tfdTargetFile.setEditable(false);
            
            final JCheckBox cbxExpectHeader = new JCheckBox(
                    "Use Expect header");
            cbxExpectHeader.setSelected(false);

            
            final JButton btnClientStart = new JButton("실행");
            final JButton btnClientStop = new JButton("종료");
            
            btnClientStart.addActionListener(
                    new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileHidingEnabled(false);
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    chooser.setMultiSelectionEnabled(false);
                    chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                    chooser.setDialogTitle("파일선택");
                    
                    if (chooser.showOpenDialog(MultipartFileUploadFrame.this) == JFileChooser.APPROVE_OPTION) {
                        targetFile = chooser.getSelectedFile();
                        tfdTargetFile.setText(targetFile.toString());
                        btnClientStart.setEnabled(false);                        
                        btnClientStop.setEnabled(true); 
                    }
                }
            }
            );


            btnClientStop.addActionListener(
                    new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileHidingEnabled(false);
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    chooser.setMultiSelectionEnabled(false);
                    chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                    chooser.setDialogTitle("파일선택");
                    
                    if (chooser.showOpenDialog(MultipartFileUploadFrame.this) == JFileChooser.APPROVE_OPTION) {
                        targetFile = chooser.getSelectedFile();
                        tfdTargetFile.setText(targetFile.toString());
                        btnClientStart.setEnabled(true);                        
                        btnClientStop.setEnabled(false);                        
                    }
                }
            }
            );

            
            final JButton btnDoUpload = new JButton("Upload");
            btnDoUpload.setEnabled(false);
            
            final JButton btnSelectFile = new JButton("파일찾기");
            btnSelectFile.addActionListener(
                    new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileHidingEnabled(false);
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    chooser.setMultiSelectionEnabled(false);
                    chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                    chooser.setDialogTitle("파일선택");
                    
                    if (chooser.showOpenDialog(MultipartFileUploadFrame.this) == JFileChooser.APPROVE_OPTION) {
                        targetFile = chooser.getSelectedFile();
                        tfdTargetFile.setText(targetFile.toString());
                        btnDoUpload.setEnabled(true);                        
                    }
                }
            }
            );
            
            taTextResponse = new JTextArea(10, 40);
            taTextResponse.setEditable(false);
            
            final JLabel lblURL = new JLabel("서버 URL:");
            
            btnDoUpload.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    // add the URL to the combo model 
                    // if it's not already there
                    PostMethod filePost = new PostMethod(targetURL);
                    
                    filePost.getParams().setBooleanParameter(
                            HttpMethodParams.USE_EXPECT_CONTINUE,
                            cbxExpectHeader.isSelected());
                    
                    try {
                        
                        appendMessage("Uploading " + targetFile.getName() + 
                                " to " + targetURL);
                        
                        Part[] parts = {
                            new FilePart(targetFile.getName(), targetFile)
                        };
                        
                        filePost.setRequestEntity(
                                new MultipartRequestEntity(parts, 
                                filePost.getParams())
                                );
                        
                        HttpClient client = new HttpClient();
                        client.getHttpConnectionManager().
                                getParams().setConnectionTimeout(5000);
                        
                        int status = client.executeMethod(filePost);
                        
                        if (status == HttpStatus.SC_OK) {
                            appendMessage(
                                    "Upload complete, response=" + 
                                    filePost.getResponseBodyAsString()
                                    );
                        } else {
                            appendMessage(
                                    "Upload failed, response=" + 
                                    HttpStatus.getStatusText(status)
                                    );
                        }
                    } catch (Exception ex) {
                        appendMessage("Error: " + ex.getMessage());
                        ex.printStackTrace();
                    } finally {
                        filePost.releaseConnection();
                    }
                    
                }
            });
            
            getContentPane().setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.gridheight = 1;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(5, 5, 5, 0);
            c.weightx = 1;
            c.weighty = 1;
            getContentPane().add(lblURL, c);
            
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 2;
            c.gridx = 1;
            c.insets = new Insets(5, 5, 5, 10);
            getContentPane().add(tfdTargetUrl, c);
            
            c.anchor = GridBagConstraints.EAST;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(10, 5, 5, 0);
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 1;
            getContentPane().add(lblTargetFile, c);
            
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(5, 5, 5, 5);
            c.gridwidth = 1;
            c.gridx = 1;
            getContentPane().add(tfdTargetFile, c);
            
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(5, 5, 5, 10);
            c.gridwidth = 1;
            c.gridx = 2;
            getContentPane().add(btnSelectFile, c);
            
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(10, 10, 10, 10);
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 2;
            getContentPane().add(cbxExpectHeader, c);
            
            
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.NONE;
            c.insets = new Insets(10, 10, 10, 10);
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 3;
            getContentPane().add(btnDoUpload, c);
            
            c.anchor = GridBagConstraints.CENTER;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 10, 10, 10);
            c.gridwidth = 3;
            c.gridheight = 3;
            c.weighty = 3;
            c.gridx = 0;
            c.gridy = 4;
            getContentPane().add(new JScrollPane(taTextResponse), c);
        }
        
        private void appendMessage(String m) {
            taTextResponse.append(m + "\n");
        }
        
        
        // 입력정보 파일로 저장
        
        
        // 저장된 정보로 서버로 Socket연결 테스트 
        
        
        // 클라이언트 작동 시작 Thread
        
        
        // 클라이언트 작동 종료
        
        
        // 주기생성
        
        
        
    }
}


