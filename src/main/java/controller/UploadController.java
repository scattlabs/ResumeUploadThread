/**
 * 
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import model.PartFile;

/**
 * @author ScattLabs
 *
 */
public class UploadController {

	public static Logger logger = Logger.getLogger(UploadController.class.getName());

	static UploadController uc;
	private HashMap<Integer, PartFile> mapPartFile = new HashMap<Integer, PartFile>();
	private int lastKategori = 1; // kategori 1 belum terkirim
	private int jumlahKategori = 0; // total kategori keseluruhan
	private int jumlahFileKategori = 4; // jumlah file dalam 1 kategori
	private int jumlahFileUploadcurrent = 0; // jumlah yang file yang sedang
												// dikirim
	private String destdir = "";

	private HashMap<Integer, Integer> listFileYangSedangDikirim = new HashMap<Integer, Integer>();

	public static UploadController getInstance() {
		if (uc == null) {
			uc = new UploadController();
		}
		return uc;
	}

	public void mergeFile(String fileName, int nChunks) {
		File ofile = new File(fileName);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		List<File> list = new ArrayList<File>();
		for (int i = 0; i < nChunks; i++) {
			list.add(new File(fileName + ".part" + i + ""));
		}
		try {
			fos = new FileOutputStream(ofile, true);
			for (File file : list) {
				fis = new FileInputStream(file);
				fileBytes = new byte[(int) file.length()];
				bytesRead = fis.read(fileBytes, 0, (int) file.length());
				assert (bytesRead == fileBytes.length);
				fos.write(fileBytes);
				fos.flush();
				fileBytes = null;
				fis.close();
				fis = null;
			}
			for (File file : list) {
				file.delete();
			}
			fos.close();
			fos = null;
			JOptionPane.showMessageDialog(null, "Merge File Berhasil");
		} catch (Exception exception) {
			System.out.println("ex : " + exception.getMessage());
			exception.printStackTrace();
		}
	}

	public void splitFile(File fileUpload, String destDir) {
		logger.info("splitFile");
		this.destdir = destDir;
		FileInputStream inputStream;
		byte[] buffer; // 1MB
		int bytesRead = -1;
		long totalBytesRead = 0;
		long fileSize = fileUpload.length();
		int read = 1000000; // IMB
		int index = 1;
		try {
			inputStream = new FileInputStream(fileUpload);

			mapPartFile = new HashMap<Integer, PartFile>();
			PartFile partFile;

			while (totalBytesRead < fileSize) {
				buffer = new byte[1000000];
				// sisa file terakhir bila < read
				if (totalBytesRead + read > fileSize) {
					read = (int) (fileSize - totalBytesRead);
				}
				bytesRead = inputStream.read(buffer, 0, read);
				partFile = new PartFile();
				partFile.setPartId(index);
				partFile.setBuffer(buffer);
				partFile.setByteRead(bytesRead);
				partFile.setFileName(destDir + "/" + fileUpload.getName() + ".part" + Integer.toString(index));
				mapPartFile.put(partFile.getPartId(), partFile);
				totalBytesRead += bytesRead;
				assert (bytesRead == buffer.length);
				index++;
			}
			jumlahKategori = (int) Math.round((double) mapPartFile.size() / (double) jumlahFileKategori);
			sendPartKategori();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendPartKategori() {
		logger.info("");
		listFileYangSedangDikirim = new HashMap<Integer, Integer>();
		int start = 0;
		int end = 0;
		if (lastKategori == 1) {
			start = 1;
			end = 1 * jumlahFileKategori;
		} else if (lastKategori == jumlahKategori) {
			start = ((lastKategori - 1) * jumlahFileKategori) + 1;
			end = mapPartFile.size();
		} else {
			start = ((lastKategori - 1) * jumlahFileKategori) + 1;
			end = lastKategori * jumlahFileKategori;
		}
		jumlahFileUploadcurrent = (end - start) + 1;

		int i = start;
		while (i <= end) {
			listFileYangSedangDikirim.put(i, i);
			i++;
		}

		while (start <= end) {
			UploadTask task = new UploadTask(mapPartFile.get(start), destdir);
			task.execute();
			start++;
		}
	}

	public void setProgress(int progress) {
		if (progress != 0) {
			System.out.println("file beres : " + progress);
			System.out.println("jumlah file yang sedang sikirim : " + listFileYangSedangDikirim.size());
			listFileYangSedangDikirim.remove(progress);
			if (listFileYangSedangDikirim.size() == 0) {
				lastKategori += 1;
				System.out.println("LAST KATEGORI : " + lastKategori);
				System.out.println("JUMLAH KATEGORI : " + jumlahKategori);
				if (lastKategori <= jumlahKategori) {
					sendPartKategori();
				} else {
					JOptionPane.showMessageDialog(null, "upload successful");
					uc = new UploadController();
				}
			}
		}
	}

	public int getLastKategori() {
		return lastKategori;
	}

	public void setLastKategori(int lastKategori) {
		this.lastKategori = lastKategori;
	}

	public int getJumlahKategori() {
		return jumlahKategori;
	}

	public void setJumlahKategori(int jumlahKategori) {
		this.jumlahKategori = jumlahKategori;
	}

	public int getJumlahFileUploadcurrent() {
		return jumlahFileUploadcurrent;
	}

	public void setJumlahFileUploadcurrent(int jumlahFileUploadcurrent) {
		this.jumlahFileUploadcurrent = jumlahFileUploadcurrent;
	}

	public String getDestdir() {
		return destdir;
	}

	public void setDestdir(String destdir) {
		this.destdir = destdir;
	}
}
