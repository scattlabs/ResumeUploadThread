/**
 * 
 */
package com.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.config.DataHolder;
import com.controller.UploadController;

import javax.swing.JProgressBar;

/**
 * @author ScattLabs
 *
 */
public class UploadForm implements PropertyChangeListener {

	private JFrame frmUploadFile;
	private JLabel lblOutputFileName;
	private JButton btnUpload;
	private File file;
	private JTextField textNchunks;
	private JLabel lblBB;
	private JLabel lblOutputFilesize;
	private JProgressBar progressBar;
	private JTextField textMergeFile;
	private JTextField textJumlahPart;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DataHolder.getInstance().execute();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String lookAndFeel = javax.swing.UIManager.getSystemLookAndFeelClassName();
				try {
					javax.swing.UIManager.setLookAndFeel(lookAndFeel);
					UploadForm window = new UploadForm();
					window.frmUploadFile.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UploadForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUploadFile = new JFrame();
		frmUploadFile.setResizable(false);
		frmUploadFile.setTitle("Upload File");
		frmUploadFile.setBounds(100, 100, 450, 300);
		frmUploadFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUploadFile.getContentPane().setLayout(null);

		JButton btnSelectedFile = new JButton("Select File");
		btnSelectedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectedFileActionPerformed(e);
			}
		});
		btnSelectedFile.setBounds(10, 11, 117, 23);
		frmUploadFile.getContentPane().add(btnSelectedFile);

		lblOutputFileName = new JLabel("");
		lblOutputFileName.setBounds(137, 15, 297, 23);
		frmUploadFile.getContentPane().add(lblOutputFileName);

		btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UploadController.getInstance().splitFile(file, "");
			}
		});
		btnUpload.setBounds(10, 111, 117, 23);
		frmUploadFile.getContentPane().add(btnUpload);

		textNchunks = new JTextField();
		textNchunks.setBounds(137, 83, 86, 20);
		frmUploadFile.getContentPane().add(textNchunks);
		textNchunks.setColumns(10);

		JLabel lblNchunks = new JLabel("nChunks");
		lblNchunks.setBounds(81, 86, 46, 14);
		frmUploadFile.getContentPane().add(lblNchunks);

		lblBB = new JLabel("b      1000 b = 1 kb ; 1000 kb = 1 mb ");
		lblBB.setBounds(233, 86, 187, 14);
		frmUploadFile.getContentPane().add(lblBB);

		lblOutputFilesize = new JLabel("");
		lblOutputFilesize.setBounds(137, 49, 86, 23);
		frmUploadFile.getContentPane().add(lblOutputFilesize);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 158, 424, 14);
		frmUploadFile.getContentPane().add(progressBar);

		JLabel lblPathFile = new JLabel("Path File");
		lblPathFile.setBounds(10, 186, 46, 14);
		frmUploadFile.getContentPane().add(lblPathFile);

		textMergeFile = new JTextField();
		textMergeFile.setBounds(113, 183, 321, 20);
		frmUploadFile.getContentPane().add(textMergeFile);
		textMergeFile.setColumns(10);
		textMergeFile.setText("D:/FTP/");

		JLabel lblJumlahPart = new JLabel("Jumlah Part");
		lblJumlahPart.setBounds(10, 211, 86, 14);
		frmUploadFile.getContentPane().add(lblJumlahPart);

		textJumlahPart = new JTextField();
		textJumlahPart.setBounds(113, 208, 86, 20);
		frmUploadFile.getContentPane().add(textJumlahPart);
		textJumlahPart.setColumns(10);

		JButton btnMerge = new JButton("Merge");
		btnMerge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UploadController.getInstance().mergeFile(textMergeFile.getText(),
						Integer.parseInt(textJumlahPart.getText()));
			}
		});
		btnMerge.setBounds(10, 236, 117, 23);
		frmUploadFile.getContentPane().add(btnMerge);
	}

	public JTextField getTextMergeFile() {
		return textMergeFile;
	}

	public void setTextMergeFile(JTextField textMergeFile) {
		this.textMergeFile = textMergeFile;
	}

	public JTextField getTextJumlahPart() {
		return textJumlahPart;
	}

	public void setTextJumlahPart(JTextField textJumlahPart) {
		this.textJumlahPart = textJumlahPart;
	}

	private void btnSelectedFileActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser fc = new JFileChooser();
		int res = fc.showOpenDialog(null);
		try {
			if (res == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				lblOutputFileName.setText(file.getAbsolutePath());
				lblOutputFilesize.setText("" + file.length() + " byte");
			} else {
				JOptionPane.showMessageDialog(null, "Pilih Salah Satu File", "Warning...", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception iOException) {
			System.out.println(iOException.getMessage());
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			System.out.println(progress);
			progressBar.setValue(progress);
		}
	}

	public JFrame getFrmUploadFile() {
		return frmUploadFile;
	}

	public void setFrmUploadFile(JFrame frmUploadFile) {
		this.frmUploadFile = frmUploadFile;
	}

	public JLabel getLblOutputFileName() {
		return lblOutputFileName;
	}

	public void setLblOutputFileName(JLabel lblOutputFileName) {
		this.lblOutputFileName = lblOutputFileName;
	}

	public JButton getBtnUpload() {
		return btnUpload;
	}

	public void setBtnUpload(JButton btnUpload) {
		this.btnUpload = btnUpload;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public JTextField getTextNchunks() {
		return textNchunks;
	}

	public void setTextNchunks(JTextField textNchunks) {
		this.textNchunks = textNchunks;
	}

	public JLabel getLblBB() {
		return lblBB;
	}

	public void setLblBB(JLabel lblBB) {
		this.lblBB = lblBB;
	}

	public JLabel getLblOutputFilesize() {
		return lblOutputFilesize;
	}

	public void setLblOutputFilesize(JLabel lblOutputFilesize) {
		this.lblOutputFilesize = lblOutputFilesize;
	}
}
