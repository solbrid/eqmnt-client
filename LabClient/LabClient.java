/**
 * 
 */
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * @author kshong
 *
 */
public class LabClient extends JFrame {
	
	JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV EXCEL ", "csv", "xlsx", "xls");
    // 인자 ( 사용자가 보여지는 텍스트, 사용 가능한 텍스트... )
	
	
	
	public LabClient() {
		setTitle("Equipment Client");
		
		setUI();
		
		setSize(700, 300);
		setVisible(true);
	}
	
	// 파일폴더 경로 찾는 Action Listener
	
	
	public void setUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridLayout gl = new GridLayout(5, 3);
		gl.setVgap(3);
		
		Container ct = getContentPane();
		ct.setLayout(gl);
		

		// 버튼
		JButton button  = new JButton("폴더경로");

		// 라벨
		JLabel label = new JLabel("파일 없음");
		
		// 서버 경로
		ct.add(new JLabel("1. Server IP : Port ")); 		
		ct.add(new JLabel("")); 		
		ct.add(new JLabel("")); 		

		ct.add(new JLabel("")); 		
		ct.add(new JTextField("")); 
		ct.add(new JTextField(""));
		
		ct.add(new JLabel("2. Local File Folder Path ")); 		
		ct.add(new JLabel("")); 		
		ct.add(new JLabel("")); 
		
		ct.add(new JLabel("")); 		
        ct.add(new JTextField(""));  
		ct.add(button);
		
		ct.add(new JLabel("폴더 경로 : ")); 		
		ct.add(label);
		ct.add(new JLabel("")); 		
		
		button.addActionListener(
				(ActionListener) new ActionListener() {
		   
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(filter); // 파일 필터 추가
                int returnVal = fileChooser.showOpenDialog(getParent());
                // 창 열기 정상 수행시 0 반환, 취소시 1 반환

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                //    label.setText(fileChooser.getSelectedFile().getPath());
                try {
                	label.setText(fileChooser.getSelectedFile().getParent() + " ::: "+fileChooser.getSelectedFile().getName());
                    // 레이블에 파일 경로 넣기
                }catch(Exception ex) {
                	ex.printStackTrace();
                }
                
                }
            }
		});	
	   }
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LabClient labClient = new LabClient();

	}

}
