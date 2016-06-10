/**
 * 
 */
package learn;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		ftpClient.upload("172.18.2.75", "updown", "admin", "F:\\repo.txt", "repo.txt");

	}

	public void upload(String hostName, String userName, String password, String fileName, String remoteFileName) {
		try {
			client.connect(hostName);
			client.login(userName, password);
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			FileInputStream inputStream;
			InputStream inputStreamSend;
			File file = new File(fileName);
			try {
				inputStream = new FileInputStream(file);
				byte[] bytesIn = new byte[1];
				int read = 0;

				while ((read = inputStream.read(bytesIn)) != -1) {
					System.out.println(new String(bytesIn));
					inputStreamSend = new ByteArrayInputStream(bytesIn);
					client.appendFile(remoteFileName, inputStreamSend);
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
