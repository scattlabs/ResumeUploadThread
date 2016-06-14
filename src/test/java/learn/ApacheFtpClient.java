/** 
 * 
 */
package learn;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author ScattLabs
 *
 */
public class ApacheFtpClient {
	FTPClient client;
	static ApacheFtpClient ftpClient;

	public ApacheFtpClient() {
		client = new FTPClient();
	}

	public static void main(String[] args) {
		ftpClient = new ApacheFtpClient();
		ftpClient.upload("172.18.2.75", "updown", "admin", "F:\\TestAppend.txt", "repo.txt");

	}

	public void upload(String hostName, String userName, String password, String fileName, String remoteFileName) {
		try {
			client.connect(hostName);
			client.login(userName, password);
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();

			// code to connect and login....

			/*
			 * String filePath = "Flow Upload.png"; FTPFile file =
			 * client.mlistFile(filePath); long size = file.getSize();
			 * System.out.println("File size = " + size);
			 * System.out.println(file.getName());
			 */

			File file2 = new File(fileName);
			InputStream inputStream = new FileInputStream(file2);
			byte[] bytesIn = new byte[20];
			inputStream.read(bytesIn);
			System.out.println(new String(bytesIn));
			InputStream inputStreamSend = new ByteArrayInputStream(bytesIn);
			client.appendFile(remoteFileName, inputStreamSend);

			bytesIn = new byte[24];
			inputStream.read(bytesIn);
			System.out.println(new String(bytesIn));
			inputStreamSend = new ByteArrayInputStream(bytesIn);
			client.appendFile(remoteFileName, inputStreamSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
