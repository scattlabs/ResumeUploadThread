/**
 * 
 */
package com.view;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.config.DataHolder;
import com.controller.UploadThread;
import com.dao.UploadDao2;
import com.model.Upload2;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;

/**
 * @author ScattLabs
 *
 */
public class IDM implements PropertyChangeListener {

	private File file;
	JLabel lblOutputFileName;
	JLabel lblOutputFileSize;
	JLabel lblOutputFileDownload;
	JProgressBar progressBar;
	JLabel lblSizeUpload;

	private JFrame frame;
	private Upload2 upload2;

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
					IDM window = new IDM();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IDM() {
		initialize();
	}

	public Upload2 getUpload2() {
		return upload2;
	}

	public void setUpload2(Upload2 upload2) {
		this.upload2 = upload2;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 355);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 414, 165);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnSelectedFileActionPerformed(evt);
			}
		});
		btnSelectFile.setBounds(10, 11, 89, 23);
		panel.add(btnSelectFile);

		lblOutputFileName = new JLabel("File Name");
		lblOutputFileName.setBounds(10, 45, 394, 14);
		panel.add(lblOutputFileName);

		JLabel lblFilesize = new JLabel("File Size");
		lblFilesize.setBounds(10, 70, 46, 14);
		panel.add(lblFilesize);

		lblOutputFileSize = new JLabel("filesize");
		lblOutputFileSize.setBounds(119, 70, 285, 14);
		panel.add(lblOutputFileSize);

		JLabel lblDownload = new JLabel("Upload");
		lblDownload.setBounds(10, 95, 46, 14);
		panel.add(lblDownload);

		lblOutputFileDownload = new JLabel("download");
		lblOutputFileDownload.setBounds(202, 95, 202, 14);
		panel.add(lblOutputFileDownload);

		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonUploadActionPerformed(e);
			}
		});
		btnUpload.setBounds(10, 131, 89, 23);
		panel.add(btnUpload);

		lblSizeUpload = new JLabel("size upload");
		lblSizeUpload.setBounds(119, 95, 73, 14);
		panel.add(lblSizeUpload);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 187, 414, 14);
		frame.getContentPane().add(progressBar);

		JProgressBar progressBar1 = new JProgressBar();
		progressBar1.setBounds(40, 212, 384, 14);
		frame.getContentPane().add(progressBar1);

		JProgressBar progressBar2 = new JProgressBar();
		progressBar2.setBounds(40, 237, 384, 14);
		frame.getContentPane().add(progressBar2);

		JProgressBar progressBar3 = new JProgressBar();
		progressBar3.setBounds(40, 262, 384, 14);
		frame.getContentPane().add(progressBar3);

		JProgressBar progressBar4 = new JProgressBar();
		progressBar4.setBounds(40, 287, 384, 14);
		frame.getContentPane().add(progressBar4);
	}

	public JLabel getLblSizeUpload() {
		return lblSizeUpload;
	}

	private void btnSelectedFileActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser fc = new JFileChooser();
		int res = fc.showOpenDialog(null);
		try {
			if (res == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				lblOutputFileName.setText(file.getAbsolutePath());
				lblOutputFileSize.setText((file.length()) + " Byte");
			} else {
				JOptionPane.showMessageDialog(null, "Pilih Salah Satu File", "Warning...", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception iOException) {
			System.out.println(iOException.getMessage());
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			lblOutputFileDownload.setText(progress + " %");
		}

	}

	private void buttonUploadActionPerformed(ActionEvent event) {
		progressBar.setValue(0);
		UploadThread task = new UploadThread("172.18.2.75", 21, "updown", "admin", "", file, this);
		task.addPropertyChangeListener(this);
		task.execute();
	}

	public void splitFile() {
		if (file.length() < 4000000000000L) {
		}
		String FILE_NAME = "TextFile.txt";
		byte PART_SIZE = 4;
		FileInputStream inputStream;
		String newFileName;
		FileOutputStream filePart;
		int fileSize = (int) file.length();
		int nChunks = 0, read = 0, readLength = PART_SIZE;
		byte[] byteChunkPart;
		try {
			inputStream = new FileInputStream(file);
			while (fileSize > 0) {
				if (fileSize <= 5) {
					readLength = fileSize;
				}
				byteChunkPart = new byte[readLength];
				read = inputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);
				nChunks++;
				newFileName = FILE_NAME + ".part" + Integer.toString(nChunks - 1);
				filePart = new FileOutputStream(new File(newFileName));
				filePart.write(byteChunkPart);
				filePart.flush();
				filePart.close();
				byteChunkPart = null;
				filePart = null;
			}
			inputStream.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
