package com.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.util.FTPException;

/**
 * @author ScattLabs
 *
 */
public class FTPUtility2 {

	private String host;
	private int port;
	private String username;
	private String password;

	private FTPClient ftpClient = new FTPClient();
	private int replyCode;

	private OutputStream outputStream;

	public FTPUtility2(String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.username = user;
		this.password = pass;
	}

	/**
	 * Connect and login to the server.
	 *
	 * @throws FTPException
	 */
	public void connect() throws FTPException {
		try {
			ftpClient.connect(host, port);
			replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FTPException("FTP serve refused connection.");
			}

			boolean logged = ftpClient.login(username, password);
			if (!logged) {
				// failed to login
				ftpClient.disconnect();
				throw new FTPException("Could not login to the server.");
			}

			ftpClient.enterLocalPassiveMode();

		} catch (IOException ex) {
			throw new FTPException("I/O error: " + ex.getMessage());
		}
	}

	/**
	 * Start uploading a file to the server
	 * 
	 * @param uploadFile
	 *            the file to be uploaded
	 * @param destDir
	 *            destination directory on the server where the file is stored
	 * @throws FTPException
	 *             if client-server communication error occurred
	 */
	public void uploadFile(File uploadFile, String destDir) throws FTPException {
		try {
			boolean success = ftpClient.changeWorkingDirectory(destDir);
			if (!success) {
				throw new FTPException(
						"Could not change working directory to " + destDir + ". The directory may not exist.");
			}

			success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (!success) {
				throw new FTPException("Could not set binary file type.");
			}

			outputStream = ftpClient.storeFileStream(uploadFile.getName());

		} catch (IOException ex) {
			throw new FTPException("Error uploading file: " + ex.getMessage());
		}
	}

	/**
	 * Write an array of bytes to the output stream.
	 */
	public void writeFileBytes(byte[] bytes, int offset, int length) throws IOException {
		outputStream.write(bytes, offset, length);
	}

	public void appendFile(String remote, InputStream local) throws IOException {
		ftpClient.appendFile(remote, local);
	}

	/**
	 * Complete the upload operation.
	 */
	public void finish() throws IOException {
		outputStream.close();
		ftpClient.completePendingCommand();
	}

	/**
	 * Log out and disconnect from the server
	 */
	public void disconnect() throws FTPException {
		if (ftpClient.isConnected()) {
			try {
				if (!ftpClient.logout()) {
					throw new FTPException("Could not log out from the server");
				}
				ftpClient.disconnect();
			} catch (IOException ex) {
				throw new FTPException("Error disconnect from the server: " + ex.getMessage());
			}
		}
	}
}