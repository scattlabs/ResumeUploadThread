/**
 * 
 */
package com.controller;

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

import org.springframework.beans.factory.annotation.Autowired;

import com.config.DataHolder;
import com.dao.UploadDao;
import com.model.PartFile;
import com.model.Upload;

/**
 * @author ScattLabs
 *
 */
public class UploadController {

	public static Logger logger = Logger.getLogger(UploadController.class.getName());

	static UploadController uc;
	private HashMap<Integer, PartFile> mapPartFile = new HashMap<Integer, PartFile>();
	private int lastGroup = 1; // Group 1 belum terkirim
	private int groupAmount = 0; // total Group keseluruhan
	private int groupSize = 4; // jumlah file dalam 1 Group
	private int uploadAmountFilescurrent = 0; // jumlah yang file yang sedang
												// dikirim
	private String destdir = "";
	private Upload uploadCurrent;
	@Autowired
	private UploadDao uploadDao;

	private HashMap<Integer, Integer> listFilesSent = new HashMap<Integer, Integer>();

	public static UploadController getInstance() {
		if (uc == null) {
			uc = new UploadController();
			uc.setUploadDao(DataHolder.getInstance().getUploadDao());
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

	// check file upload sudah ada didatabase belum
	public Upload checkFile(File file) {
		try {
			for (Upload upload : uploadDao.list("")) {
				if (file.getName().equals(upload.getFileName()) && file.length() == upload.getFileSize()
						&& upload.getUploadStatus() == 0) {
					lastGroup = upload.getLastGroup();
					System.out.println("MELANJUTKAN");
					return upload;
				}
			}
		} catch (Exception e) {
			logger.warning(e.getMessage());// TODO: handle exception
		}

		System.out.println("NEW UPLOAD");
		return null;
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

			setUploadCurrent(checkFile(fileUpload));

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
			System.out.println("jumlah part : " + mapPartFile.size());
			groupAmount = mapPartFile.size() / groupSize;
			if (mapPartFile.size() % groupSize > 0) {
				groupAmount += 1;
			}
			System.out.println("jumlah Group : " + groupAmount);
			setUploadCurrent(checkFile(fileUpload));
			if (getUploadCurrent() == null) {
				setUploadCurrent(new Upload(0, fileUpload.getName(), (int) fileUpload.length(), read,
						mapPartFile.size(), groupSize, groupAmount, 0, 0));
				uploadDao.save(getUploadCurrent());
			}
			sendPartGroup();
		} catch (FileNotFoundException e1) {
			logger.warning(e1.getMessage());
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warning(e.getMessage());
		}
	}

	public void sendPartGroup() {
		logger.info("");
		listFilesSent = new HashMap<Integer, Integer>();
		int start = 0;
		int end = 0;
		if (lastGroup == 1) {
			start = 1;
			end = 1 * groupSize;
		} else if (lastGroup == groupAmount) {
			start = ((lastGroup - 1) * groupSize) + 1;
			end = mapPartFile.size();
		} else {
			start = ((lastGroup - 1) * groupSize) + 1;
			end = lastGroup * groupSize;
		}
		uploadAmountFilescurrent = (end - start) + 1;

		int i = start;
		while (i <= end) {
			listFilesSent.put(i, i);
			i++;
		}

		while (start <= end) {
			UploadTask task = new UploadTask(mapPartFile.get(start), destdir);
			task.execute();
			start++;
		}
	}

	public void setProgress(int progress) {
		try {
			if (progress != 0) {
				System.out.println("file beres : " + progress);
				System.out.println("jumlah file yang sedang sikirim : " + listFilesSent.size());
				listFilesSent.remove(progress);
				if (listFilesSent.size() == 0) {
					lastGroup += 1;
					System.out.println("LAST Group : " + lastGroup);
					System.out.println("JUMLAH Group : " + groupAmount);
					if (lastGroup <= groupAmount) {
						if (getUploadCurrent() != null) {
							getUploadCurrent().setLastGroup(lastGroup);
							uploadDao.save(getUploadCurrent());
							sendPartGroup();
						}
					} else {
						getUploadCurrent().setUploadStatus(1);
						uploadDao.save(getUploadCurrent());
						JOptionPane.showMessageDialog(null, "upload successful");
						uc = new UploadController();
						uc.setUploadDao(DataHolder.getInstance().getUploadDao());
					}
				}
			}
		} catch (Exception e) {
			logger.warning(e.getMessage());// TODO: handle exception
		}

	}

	public HashMap<Integer, PartFile> getMapPartFile() {
		return mapPartFile;
	}

	public void setMapPartFile(HashMap<Integer, PartFile> mapPartFile) {
		this.mapPartFile = mapPartFile;
	}

	public int getLastGroup() {
		return lastGroup;
	}

	public void setLastGroup(int lastGroup) {
		this.lastGroup = lastGroup;
	}

	public int getGroupAmount() {
		return groupAmount;
	}

	public void setGroupAmount(int groupAmount) {
		this.groupAmount = groupAmount;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	public int getUploadAmountFilescurrent() {
		return uploadAmountFilescurrent;
	}

	public void setUploadAmountFilescurrent(int uploadAmountFilescurrent) {
		this.uploadAmountFilescurrent = uploadAmountFilescurrent;
	}

	public String getDestdir() {
		return destdir;
	}

	public void setDestdir(String destdir) {
		this.destdir = destdir;
	}

	public Upload getUploadCurrent() {
		return uploadCurrent;
	}

	public void setUploadCurrent(Upload uploadCurrent) {
		this.uploadCurrent = uploadCurrent;
	}

	public UploadDao getUploadDao() {
		return uploadDao;
	}

	public void setUploadDao(UploadDao uploadDao) {
		this.uploadDao = uploadDao;
	}

	public HashMap<Integer, Integer> getListFilesSent() {
		return listFilesSent;
	}

	public void setListFilesSent(HashMap<Integer, Integer> listFilesSent) {
		this.listFilesSent = listFilesSent;
	}

}
