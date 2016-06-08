/**
 * 
 */
package model;

/**
 * @author ScattLabs
 *
 */
public class PartFile {
	private int partId;
	private byte[] buffer = new byte[1000000];
	private String fileName;
	private int byteRead;

	/**
	 * 
	 */
	public PartFile() {
		// TODO Auto-generated constructor stub
	}

	public PartFile(int partId, byte[] buffer, String fileName, int byteRead) {
		super();
		this.partId = partId;
		this.buffer = buffer;
		this.fileName = fileName;
		this.byteRead = byteRead;
	}

	public int getPartId() {
		return partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getByteRead() {
		return byteRead;
	}

	public void setByteRead(int byteRead) {
		this.byteRead = byteRead;
	}

}
