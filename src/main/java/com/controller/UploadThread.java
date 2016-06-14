/**
 * 
 */
package com.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTPClient;

import com.dao.UploadDao2;
import com.model.Upload2;
import com.view.IDM;

/**
 * @author ScattLabs
 *
 */
public class UploadThread extends SwingWorker<Integer, Void> {

	private static final int BUFFER_SIZE = 1409600;// 4096

	private String host;
	private int port;
	private String username;
	private String password;

	private String destDir;
	private File fileUpload;
	private IDM idm;
	private Upload2 upload2;
	private UploadDao2 uploadDao2;

	public UploadThread(String host, int port, String username, String password, String destDir, File fileUpload,
			IDM idm) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.destDir = destDir;
		this.fileUpload = fileUpload;
		this.idm = idm;
		/*
		 * this.upload2 = upload2; this.uploadDao2 = uploadDao2;
		 */
	}

	public IDM getIdm() {
		return idm;
	}

	public void setIdm(IDM idm) {
		this.idm = idm;
	}

	public Upload2 getUpload2() {
		return upload2;
	}

	public void setUpload2(Upload2 upload2) {
		this.upload2 = upload2;
	}

	/**
	 * Executed in background thread
	 */
	@Override
	protected Integer doInBackground() throws Exception {
		FTPClient client = new FTPClient();
		try {
			client.connect(host);
			client.login(username, password);
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			FileInputStream inputStream;
			InputStream inputStreamSend;
			try {
				inputStream = new FileInputStream(fileUpload);
				byte[] bytesIn = new byte[1];
				int read = 0;
				int upTotal = 0;
				while ((read = inputStream.read(bytesIn)) != -1) {
					if (upTotal >= 4) {
						inputStreamSend = new ByteArrayInputStream(bytesIn);
						client.appendFile(fileUpload.getName(), inputStreamSend);
					}
					upTotal += read;
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * Executed in Swing's event dispatching thread
	 */
	@Override
	protected void done() {
		if (!isCancelled()) {
			JOptionPane.showMessageDialog(null, "File has been uploaded successfully!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
