package controller;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.PartFile;
import util.FTPException;

/**
 * @author ScattLabs
 *
 */
public class UploadTask extends SwingWorker<Void, Void> {

	private String host = "172.18.2.75";
	private int port = 21;
	private String username = "updown";
	private String password = "admin";

	private PartFile partFile;

	public UploadTask(PartFile partFile) {
		this.setPartFile(partFile);
	}

	/**
	 * Executed in background thread
	 */
	@Override
	protected Void doInBackground() throws Exception {
		FTPUtility util = new FTPUtility(host, port, username, password);
		try {
			util.connect();
			util.uploadFile(partFile.getFileName(), "");
			util.writeFileBytes(partFile.getBuffer(), 0, partFile.getByteRead());

		} catch (FTPException ex) {
			JOptionPane.showMessageDialog(null, "Error uploading file: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			UploadController.getInstance().setProgress(partFile.getPartId());
			cancel(true);
		} finally {
			util.disconnect();
		}
		return null;
	}

	@Override
	protected void done() {
		if (!isCancelled()) {
			if (partFile != null) {
				UploadController.getInstance().setProgress(partFile.getPartId());
			}
		}
	}

	public PartFile getPartFile() {
		return partFile;
	}

	public void setPartFile(PartFile partFile) {
		this.partFile = partFile;
	}
}